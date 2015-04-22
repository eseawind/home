package net.casnw.xinanjiang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

/**
 * 新安江模型
 * 
 * @author 雪庭
 * @sine 2015年4月21日
 */
public class XAJModel {

	// 模型要运行的步长
	public static final int NNN = 2557;
	// FORCING 驱动数据
	// 降水数据
	private double[] m_pP;
	// 水面蒸发数据
	private double[] m_pEm;
	// 模拟径流数据
	public double[] runoff = new double[NNN];
	// 观测径流数据
	public double[] m_wall;
	// 模型输出
	private double[][] results;

	// OUTPUT 输出数据

	private double m_U; // for 24h. U=A(km^2)/3.6/delta_t
	// SOIL 土壤数据

	// EVAPORATION 蒸发

	// PARAMETER 模型参数

	public static final int SA_MAX = 1; /* 1: 最大化问题,0: 最小化问题 */
	public static final int SA_DIM = 10;
	public static final int SA_L = 5; /* 局部搜索的次数，即内循环次数 */
	public static final int SA_N = 3000; /* 最大迭代次数 */
	public static final int SA_NN = 20; /* 收敛检验 */
	public static final int SA_COUNT = 1000; /* 自适应均匀分布发生函数参数 */
	public static final int T0 = 1000; /* 初始温度 */
	public static final int Tn = 1; /* 终止温度 */
	public static final int GEN_TYPE = 2; /* 1:高斯,2:均匀 */
	public static final int TEMP_TYPE = 1; /*
											 * Option for Nine temperature
											 * schedule function
											 */
	public static final double PI = 3.14159265358979323846;

	private java.util.Random m_r;

	/**
	 * 构造函数
	 */
	public XAJModel() {
		// 驱动数据
		this.m_pP = new double[NNN];
		this.m_pEm = new double[NNN];
		// 观测径流数据
		this.m_wall = new double[NNN];
	}

	/**
	 * 取得文件读取缓冲
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件读取缓冲
	 * @throws FileNotFoundException
	 *             文件不存在异常
	 */
	private BufferedReader getReader(String fileName)
			throws FileNotFoundException {
		File file;
		URL url = ClassLoader.getSystemResource(fileName);
		// 如果xml文件不存在
		if (url == null) {
			throw new IllegalStateException(
					String.format("文件不存在: %s", fileName));
		}
		// 取得文件绝对路径
		String filePath = ClassLoader.getSystemResource(fileName).getPath();
		file = new File(filePath);
		if (!file.exists() || file.isDirectory()) {
			throw new FileNotFoundException();
		}
		BufferedReader in = new BufferedReader(new FileReader(file));
		return in;
	}

	/**
	 * 模型初始化
	 * 
	 * @param forcingFileName
	 * @param runoffAllFileName
	 */
	public void init(String forcingFileName, String runoffAllFileName) {
		initForcing(forcingFileName);
		initRunoffAll(runoffAllFileName);
	}

	/**
	 * 初始化参数
	 * 
	 * @param fileName
	 */
	private void initForcing(String fileName) {
		int i;

		this.m_U = 1.0; // this.m_Area/(3.6*this.m_DeltaT);

		try {
			BufferedReader in = getReader(fileName);
			String line; // 一行数据

			for (i = 0; i < NNN; i++) // 读取驱动数据
			{
				line = in.readLine();
				if (line != null) {
					String[] temp = line.split("\\t");
					this.m_pP[i] = Double.parseDouble(temp[0]);
					this.m_pEm[i] = Double.parseDouble(temp[1]);
				}
			}
			in.close();
		} catch (Exception e) {
			System.out.println("找不到指定的驱动数据！" + e.getMessage());
			return;
		}
	}

	/**
	 * 初始化参数
	 * 
	 * @param fileName
	 */
	private void initRunoffAll(String fileName) {
		try {
			BufferedReader in = getReader(fileName);
			String line; // 一行数据

			for (int i = 0; i < NNN; i++) // 读取驱动数据
			{
				line = in.readLine();
				if (line != null) {
					m_wall[i] = Double.parseDouble(line);
				}
			}
			in.close();
		} catch (Exception e) {
			System.out.println("找不到指定的驱动数据！" + e.getMessage());
			return;
		}
	}

	public void runModle() {
		double[] lowerX = { 0.0001, 0.0001, 0.0001, 5, 50, 50, 0.0001, 0.0001,
				0.0001, 1 };
		double[] upperX = { 1, 0.5, 1, 30, 100, 200, 0.3, 50, 0.99, 6 };
		double[] deltaX;
		double[] xi;
		double[] xj;

		double z;
		double t;

		double obj_i = 0;
		double obj_j = 0;
		double deltaE;

		int k;

		int count;

		long time0;
		long time1;
		double times;

		int n; /* 外循环控制变量 */
		int l; /* 内循环控制变量 */
		int d;

		/* 开辟数据存储空间 */
		results = new double[SA_N * SA_L][];
		for (k = 0; k < SA_N * SA_L; k++) {
			results[k] = new double[SA_DIM + 4];
		}

		deltaX = new double[SA_DIM];
		xi = new double[SA_DIM];
		xj = new double[SA_DIM];

		/* 随机初始化搜索起点 */
		for (d = 0; d < SA_DIM; d++) {
			z = rndu();
			xi[d] = lowerX[d] + z * (upperX[d] - lowerX[d]);
		}
		obj_i = nse(m_wall, runoff, xi);
		k = 0;
		t = T0;
		/* 模拟退火的主算法 */
		// 得到从程序启动到此次函数调用时累计的毫秒数。
		time0 = System.currentTimeMillis(); // 毫秒

		for (n = 0; n < SA_N; n++) {
			l = 0;
			count = 0;

			while (l < SA_L) {
				/* 应用发生函数,产生扰动 */
				do {
					/* 高斯分布产生扰动 */
					if (GEN_TYPE == 1) {
						GaussianGenerate(deltaX, t, lowerX, upperX);
					}
					/* 均匀分布产生扰动 */
					if (GEN_TYPE == 2) {
						UniformGenerate(deltaX, t, lowerX, upperX, count);
					}

					Add(xi, xj, deltaX);
				} while (TestNewX(xj, lowerX, upperX) == 0);

				/* 计算新点的目标函数值 */
				obj_j = nse(m_wall, runoff, xj);

				if (SA_MAX == 0) /* 目标函数最小化问题 */
				{
					/* 转化为最大化问题 */
					obj_i *= -1.0;
					obj_j *= -1.0;
				} /* 目标函数最大化问题 */
				if (obj_j > obj_i) /* 接受Xj,复制Xj->Xi */
				{
					Copy(xj, xi);
					obj_i = obj_j;
				} else /* Metropolis准则 */
				{
					z = rndu();
					deltaE = obj_j - obj_i;

					if (Math.exp(deltaE / t) > z) /* 接受Xj,复制Xj->Xi */
					{
						Copy(xj, xi);
						obj_i = obj_j;
						if (GEN_TYPE == 2)
							count = 0;
					} else {
						if (GEN_TYPE == 2)
							count++;
						continue;
					}
				}
				// time1=clock();
				time1 = System.currentTimeMillis(); // 毫秒
				// times=(double)(time1-time0)/CLOCKS_PER_SEC; //得到秒数
				// CLOCKS_PER_SEC=1000
				times = (double) (time1 - time0) / 1000;
				/* printf("#\n"); */
				/* 将新接受的点保存起来 */
				results[k][0] = k + 1;
				results[k][1] = t;
				for (d = 2; d < SA_DIM + 2; d++) {
					results[k][d] = xi[d - 2];
				}
				results[k][SA_DIM + 2] = obj_i;
				results[k][SA_DIM + 3] = times;
				/* printf("-------------------------------\n"); */
				System.out.println("第 " + k + " 次优化结果显示开始：");
				System.out.println("序号： "
						+ String.format("%1$f", results[k][0]));
				System.out.println("T： "
						+ String.format("%1$16.3f", results[k][1]));
				System.out.println("参数： ");
				for (d = 2; d < SA_DIM + 1; d++) {
					System.out
							.println(String.format("%1$16.3f", results[k][d]));
				}
				System.out.println("目标函数值： "
						+ String.format("%1$16.3f", results[k][12]));
				System.out.println("时间： "
						+ String.format("%1$f", results[k][13]));
				System.out.println("第 " + k + " 次优化结果显示结束.");

				l++;
				k++;
			}

			/* 退火降温 */
			switch (TEMP_TYPE) {
			case 1:
				t = AS1(n);
				break;

			case 2:
				t = AS2(n);
				break;

			case 3:
				t = AS3(n);
				break;

			case 4:
				t = AS4(n);
				break;

			case 5:
				t = AS5(n);
				break;

			case 6:
				t = AS6(n);
				break;

			case 7:
				t = AS7(n);
				break;

			case 8:
				t = AS8(n);
				break;

			case 9:
				t = AS9(n);
				break;
			}

		}
	}

	/* 产生0-1之间均匀分布的随机变量 */
	double rndu() {
		if (m_r == null) {
			m_r = new java.util.Random();
		}
		return m_r.nextDouble();
	}

	double nse(double[] r, double[] runoff, double[] params) {
		int i;
		double tmp = 0;
		double ave = 0;
		double tmp2 = 0;
		double y;

		runNse(params, this.m_U);

		for (i = 365; i < NNN; i++) {
			tmp += r[i];
		}
		ave = tmp / (NNN - 365.0);
		tmp = 0;

		for (i = 365; i < NNN; i++) {
			tmp += (runoff[i] - r[i]) * (runoff[i] - r[i]);
			tmp2 += (r[i] - ave) * (r[i] - ave);
		}
		y = 1.0 - tmp / tmp2;
		return (1000.0 * y);
	}

	private void runNse(double[] params, double m_U) {
		int i;

		double m_K = params[0]; // (1) 流域蒸散发能力与实测水面蒸发之比
		double m_IMP = params[1]; // (2) 流域不透水面积占全流域面积之比
		double m_B = params[2]; // (3) 蓄水容量曲线的方次
		double m_Wum = params[3]; // (4) 上层蓄水容量
		double m_Wlm = params[4]; // (5) 下层蓄水容量
		double m_Wdm = params[5]; // (6) 深层蓄水容量
		double m_C = params[6]; // (7) 深层蒸散发系数
		double m_FC = params[7]; // (8) 稳定入渗率（毫米／小时）
		double m_KKG = params[8]; // (9) 地下径流消退系数
		double m_Kstor = params[9]; // (10)汇流计算参数
		
		// 模型输出，蒸散发项
		double[] m_pE = new double[NNN];
		double[] m_pEd = new double[NNN];
		double[] m_pEl = new double[NNN];
		double[] m_pEu = new double[NNN];
		// 模型输出，产流项
		double[] m_pR = new double[NNN];
		double[] m_pRg = new double[NNN];
		double[] m_pRs = new double[NNN];
		// 模型状态量，土壤湿度
		double[] m_pW = new double[NNN];
		double[] m_pWd = new double[NNN];
		double[] m_pWl = new double[NNN];
		double[] m_pWu = new double[NNN];		

		double m_WM = m_Wum + m_Wlm + m_Wdm;
		double m_WMM = m_WM * (1.0 + m_B) / (1.0 - m_IMP);
		
		// 模型的状态变量
		double PE; // 大于零时为净雨量，小于零时为蒸发不足量，单位（毫米）

		double R; // 产流深度，包括地表径流深度和地下径流深度两部分（毫米）
		double RG; // 地下径流深度，单位（毫米）
		double RS; // 地表径流深度，单位（毫米）

		double A; // 当流域内的土壤湿度为W是,土壤含水量折算成的径流深度,单位（毫米）

		double E = 0.0; // 蒸散发
		double EU = 0.0; // 上层土壤蒸散发量（毫米）
		double EL = 0.0; // 下层土壤蒸散发量（毫米）
		double ED = 0.0; // 深层土壤蒸散发量（毫米）

		// 假设流域经历了长时间降水，各层土壤含水量均为该层土壤的蓄水能力
		double W = m_WM; // 流域内土壤湿度
		double WU = m_Wum; // 流域内上层土壤湿度
		double WL = m_Wlm; // 流域内下层土壤适度
		double WD = m_Wdm; // 流域内深层土壤湿度

		for (i = 0; i < NNN; i++) {
			PE = m_pP[i] - m_K * m_pEm[i];
			// 如果降水量小于足蒸发需求
			if (PE < 0) {
				R = 0.0; // 产流总量为零
				RG = 0.0; // 地下径流量为零
				RS = 0.0; // 地表径流量为零

				// 如果上层土壤含水量大于蒸发不足量
				if ((WU + PE) > 0.0) {
					// 上层土壤为流域蒸散发提供水量
					EU = m_K * m_pEm[i];
					// 没有降水量用于增加土壤湿度
					EL = 0.0; /* 降水用来增加土壤湿度的部分 */
					//
					ED = 0.0;
					// 更新上层土壤含水量
					WU = WU + PE;
				}
				// 上层土壤含水量小于蒸发不足量
				else {
					EU = WU + m_pP[i]; // 上层土壤蒸发,降水全部用于蒸发
					WU = 0.0; // 上层含水量为0，全部水分被蒸发
					// 如果下层含水量大于下层土壤的蒸散发潜力
					if (WL > (m_C * m_Wlm)) {
						EL = (m_K * m_pEm[i] - EU) * (WL / m_Wlm);
						WL = WL - EL;
						ED = 0;
					}
					// 如果下层土壤含水量小于下层土壤的蒸散发潜力
					else {
						// 如果下层土壤的含水量蒸发之后还有剩余
						if (WL > m_C * (m_K * m_pEm[i] - EU)) {
							EL = m_C * (m_K * m_pEm[i] - EU);
							WL = WL - EL; // ///////////////////////////////
							ED = 0.0;
						}
						// 如果下层土壤含水量全部蒸发之后尚不能满足蒸发需求
						else {
							EL = WL; /* 下层土壤含水量全部用于蒸散发 */
							WL = 0.0; /* 下层土剩余壤含水量为0 */
							ED = m_C * (m_K * m_pEm[i] - EU) - EL; /* 深层土壤含水量参与蒸发 */
							WD = WD - ED; /* 深层土壤含水量更新 */
						}
					}
				}
			}
			// 如果降水量大于或者等于蒸散发需求,即降水满足蒸发后还有剩余
			else {
				/*************** 以下代码负责径流划分计算 **************/
				// 初始化变量
				R = 0.0; // 产流总量为零
				RG = 0.0; // 地下径流产流深度为零
				RS = 0.0; // 地表径流产流深度为零

				// 计算流域当天土壤含水量折算成的径流深度Ａ
				// m_WM:流域平均蓄水容量(一个参数),
				// m_W:流域内土壤湿度(一个状态变量)
				// m_B:蓄水容量曲线的方次(一个参数)
				A = m_WMM * (1 - Math.pow((1.0 - W / m_WM), 1.0 / (1 + m_B)));
				// 土壤湿度折算净雨量加上降水后蒸发剩余雨量小于流域内最大含水容量
				if ((A + PE) < m_WMM) {
					// 流域内的产流深度计算
					R = PE /* 降水蒸发后的剩余量(PE=P-E:状态变量) */
							+ W /* 流域内土壤湿度 (W:状态变量) */
							+ m_WM
							* Math.pow((1 - (PE + A) / m_WMM), (1 + m_B))
							- m_WM; /* 减去流域平均蓄水容量（m_WM:参数） */
				}
				// 土壤湿度折算净雨量加上降水后蒸发剩余雨量大于流域内最大含水容量
				else {
					// 流域内的产流深度计算
					R = PE /* 降水蒸发后的剩余量(PE=P-E:状态变量) */
							+ W /* 流域内土壤湿度 (W:状态变量) */
							- m_WM; /* 减去流域平均蓄水容量（m_WM:参数） */
				}
				// 如果降水经过蒸散发后的剩余量大于等于土壤稳定入渗率//
				if (PE > m_FC) {
					// 计算地下径流的产流深度
					RG = (R - m_IMP * PE) * (m_FC / PE);
					// 计算地表径流的产流深度
					RS = R - RG;
				}
				// 如果降水蒸发后的剩余量小于土壤的稳定入渗率(m_FC:参数)
				// 除了不透水面积上的地表产流外，全部下渗，形成地下径流
				else {
					// 计算地下径流的产流深度
					RG = R - /* 总产流深度 */
					m_IMP * PE; /* 不透水面积上的产流深度，m_IMP:参数 */
					// 计算地表径流的产流深度
					RS = R - RG;
				}
				/*************** 径流划分计算结束 **************/

				// 计算上层土壤蒸散发量
				EU = m_K * /* 流域蒸散发能力与实测蒸散发值的比 */
				m_pEm[i]; /* 当前时段的水面蒸发 */
				ED = 0.0;
				EL = 0.0; /* 降水用来增加土壤湿度的部分 */

				/*************** 以下代码负责土壤含水量的更新计算 **************/
				// 如果上层土壤含水量与降水蒸散发剩余量之和减去产流量之后
				// 大于上层土壤的蓄水能力
				if ((WU + PE - R) >= m_Wum) {
					// 上层含水量+下层含水量+降水剩余量-产流量-上层土壤蓄水需求
					// 后的水量大于下层土壤蓄水需求，多余水量分配到深层土壤
					if ((WU + WL + PE - R - m_Wum) > m_Wlm) {
						WU = m_Wum; /* 上层土壤含水量=上层土壤蓄水容量 */
						WL = m_Wlm; /* 下层土壤含水量=下层土壤蓄水容量 */
						WD = W + PE - R - WU - WL; /* 绝对降水剩余量补充到深层土壤中 */
					}
					// 上层含水量+下层含水量+降水剩余量-产流量-上层土壤蓄水需求
					// 后的水量小于下层土壤蓄水需求，剩余水量补充到下层土壤中
					else {
						WL = WU + WL + PE - R - m_Wum; /* 下层土壤含水量 */
						WU = m_Wum; /* 上层土壤蓄水容量得到满足 */
					}
				}
				// 如果上层土壤含水量与降水蒸散发剩余量之和减去产流量之后
				// 小于上层土壤的蓄水能力
				else {
					WU = WU + PE - R;
					// WU 可能小于零，应该加一些控制代码..........
				}
				/*************** 土壤含水量的更新计算结束 **************/
			}

			E = EU + EL + ED;
			W = WU + WL + WD;
			/* 以下部分是状态量：总蒸发量、上、下和深层土壤的蒸发的保存 */
			/* 1 */
			m_pE[i] = E;
			// 当前步长的蒸发 （模型重要输出）
			/* 2 */
			m_pEu[i] = EU;
			// 当前步长上层土壤蒸发
			/* 3 */
			m_pEl[i] = EL;
			// 当前步长下层土壤蒸发
			/* 4 */
			m_pEd[i] = ED;
			// 当前步长深层土壤蒸发
			/* 5 */
			m_pW[i] = W;
			// 当前步长流域平均土壤含水量
			/* 6 */
			m_pWu[i] = WU;
			// 当前步长流域上层土壤含水量
			/* 7 */
			m_pWl[i] = WL;
			// 当前步长流域下层土壤含水量
			/* 8 */
			m_pWd[i] = WD;
			// 当前步长流域深层土壤含水量
			/* 9 */
			m_pRg[i] = RG;
			// 当前步长流域基流径流深度
			/* 10 */
			m_pRs[i] = RS;
			// 当前步长流域地表径流径流深度
			/* 11 */
			m_pR[i] = R;
			// 当前步长的总产流径流深度
		}
		routing(m_Kstor, m_KKG, m_pRs, m_pRg, m_U);
	}

	private void routing(double m_Kstor, double m_KKG, double[] m_pRs, double[] m_pRg, double m_U) {
	    double[] UH = new double[100]; // 单位线,假定最长的汇流时间为100天
	    int N ;			// 汇流天数 
	    N = 0;
	    double K;			// 汇流参数
	    double sum;
	    int i, j;
	    
		// 模型输出，出流项，经过汇流的产流
		double[] m_pQrg = new double[NNN];
		double[] m_pQrs = new double[NNN];
		double[] m_pQ = new double[NNN];	    

	    K = m_Kstor;
	    // 单位线推导
	    for (i = 0; i < 100; i++)
	      {
	        UH[i] = (1.0 / K) * Math.exp((-1.0 * i) / K);
	      }
	   UH[0]=(UH[1]+UH[2])*0.5;
	    sum = 0.0;
	    for (i = 0; i < 100; i++)
	      {
	        sum += UH[i];
	        if (sum > 1.0)
	          {
	            UH[i] = 1.0 - (sum - UH[i]);
	            N = i;
	            break;
	          }
	      }
	    // 单位线汇流计算
	    for (i = 0; i < NNN; i++)
	      {
	        m_pQrs[i] = 0.0;
	        for (j = 0; j <= N; j++)
	          {
	            if ((i - j) < 0)
	              {
	                continue;
	              }
	            m_pQrs[i] += m_pRs[i - j] * UH[j] * m_U;
	          }
	      }
	    //地下水汇流计算
	    m_pQrg[0] = 0.0;
	    for (i = 1; i < NNN; i++)
	      {
	        m_pQrg[i] = m_pQrg[i - 1] * m_KKG +
	          m_pRg[i] * (1.0 - m_KKG) * m_U;
	      }
	    for (i = 0; i < NNN; i++)
	      {
	        m_pQ[i] = m_pQrs[i] + m_pQrg[i];
	      }
	    
	    for (i = 0; i < (NNN-1); i++)
	      {
	        runoff[i] = m_pQ[i];	
	      }	    
	}

	// 生成均值为0，标准差为T高斯分布扰动向量
	void GaussianGenerate(double[] DeltaX, double T, double[] LowerX,
			double[] UpperX) {
		/* 这个发生函数的弊病很大，修改了 */
		int d;
		double stdv;
		for (d = 0; d < SA_DIM; d++) {
			stdv = (1.0 * T - 1.0 * Tn) / (1.0 * T0 - 1.0 * Tn)
					* (UpperX[d] - LowerX[d]);
			DeltaX[d] = GasDev(0, stdv);
		}
	}

	/* 生成均匀分布的扰动 */
	void UniformGenerate(double[] DeltaX, double T, double[] LowerX,
			double[] UpperX, int count) {
		int d;
		double z;
		double c;

		if (count >= SA_COUNT) {
			count = SA_COUNT - 1;
		}
		c = (1.0 * SA_COUNT - 1.0 * count) / (1.0 * SA_COUNT);

		for (d = 0; d < SA_DIM; d++) {
			z = rndu();
			DeltaX[d] = 2.0 * (z - 0.5) * (UpperX[d] - LowerX[d]) * c;
		}
	}

	/* 产生高斯分布的随机变量 */
	double GasDev(double mean, double stdv) {
		int idum0;
		int iset = 0;
		double gset = 0;
		double fac, rsq, v1, v2;
		double tmp;
		double r1;
		double r2;
		long time = System.currentTimeMillis() / 1000; // 毫秒
		idum0 = (int) ((rndu() - 0.5) * (int) (System.currentTimeMillis() / 1000));
		if (idum0 < 0) {
			iset = 0;
		}
		if (iset == 0) {
			do {
				r1 = rndu();
				r2 = rndu();
				v1 = 2.0 * r1 - 1.0;
				v2 = 2.0 * r2 - 1.0;
				rsq = v1 * v1 + v2 * v2;
			} while ((rsq >= 1.0) || (rsq == 0.0));
			fac = Math.sqrt(-2.0 * Math.log(rsq) / rsq);
			gset = v1 * fac;
			iset = 1;
			tmp = v2 * fac;
		} else {
			iset = 0;
			tmp = gset;
		}
		return (stdv * tmp + mean);
	}

	void Add(double[] Xi, double[] Xj, double[] DeltaX) {
		int d;
		for (d = 0; d < SA_DIM; d++) {
			Xj[d] = Xi[d] + DeltaX[d];
		}
	}

	/* 检验新的变量是否在可行的范围内 */
	int TestNewX(double[] NewX, double[] LowerX, double[] UpperX) {
		int status = 1;
		int d;
		for (d = 0; d < SA_DIM; d++) {
			if ((NewX[d] < LowerX[d]) || (NewX[d] > UpperX[d])) {
				status = 0;
				break;
			}
		}
		return (status);
	}

	void Copy(double[] From, double[] To) {
		int d;
		for (d = 0; d < SA_DIM; d++) {
			To[d] = From[d];
		}
	}

	/* 退火时间表函数， Annealing Schedule */
	double AS1(int k) {
		double T;
		T = T0 - k * (T0 - Tn) / SA_N;
		return (T);
	}

	double AS2(int k) {
		double T;
		T = T0 * Math.pow((1.0 * Tn / T0), (1.0 * k) / SA_N);
		return (T);
	}

	double AS3(int k) {
		double T;
		double A;
		A = Math.log(1.0 * (T0 - Tn)) / Math.log(1.0 * SA_N);
		T = T0 - Math.pow(1.0 * k, A);
		return (T);
	}

	double AS4(int k) {
		double T;
		T = 1.0 * Tn + (1.0 * T0 - 1.0 * Tn)
				/ (1 + Math.exp(0.3 * (1.0 * k - SA_N / 2)));
		return (T);
	}

	double AS5(int k) {
		double T;
		T = 0.5 * (T0 - Tn) * (1 + Math.cos(k * PI / SA_N)) + Tn;
		return (T);
	}

	double AS6(int k) {
		double T;
		T = 0.5 * (T0 - Tn) * (1 - Math.tanh(10.0 * k / SA_N - 5)) + Tn;
		return (T);
	}

	double AS7(int k) {
		double T;
		T = (T0 - Tn) / Math.cosh(10.0 * k / SA_N) + Tn;
		return (T);
	}

	double AS8(int k) {
		double T;
		double A;
		A = (1.0 / SA_N) * Math.log(1.0 * T0 / Tn);
		T = T0 * Math.exp(-A * k);
		return (T);
	}

	double AS9(int k) {
		double T;
		double A;
		A = (1.0 / (SA_N * SA_N)) * Math.log(1.0 * T0 / Tn);
		T = T0 * Math.exp(-A * k * k);
		return (T);
	}
}
