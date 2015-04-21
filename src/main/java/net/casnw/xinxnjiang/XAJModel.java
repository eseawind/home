package net.casnw.xinxnjiang;

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

	// FORCING 驱动数据
	private double[] m_pP; // 降水数据
	private double[] m_pEm; // 水面蒸发数据
	//
	public static final int NNN = 2557; // 模型要运行的步长
	
	public double[]  m_wall; // 观测径流数据	

	// OUTPUT 输出数据
	private double[] m_pR; // 流域内每一步长的产流量(径流深度)
	private double[] m_pRg; // 每一步长的地表径流深(毫米)
	private double[] m_pRs; // 每一步长的基流径流深(毫米)
	private double[] m_pE; // 每一步长的蒸发(毫米)
	private double[] m_pQrs; // 流域出口地表径流量
	private double[] m_pQrg; // 流域出口地下径流量
	private double[] m_pQ; // 流域出口的总流量
	//
	private double m_U; // for 24h. U=A(km^2)/3.6/delta_t
	// SOIL 土壤数据
	private double[] m_pW; // 流域内土壤湿度
	private double[] m_pWu; // 流域内上层土壤湿度
	private double[] m_pWl; // 流域内下层土壤湿度
	private double[] m_pWd; // 流域内深层土壤湿度

	private double m_Wum; // 流域内上层土壤蓄水容量，植被良好的流域，约为20mm,差的流域,2~10mm
	private double m_Wlm; // 流域内下层土壤蓄水容量，可取60~90mm
	private double m_Wdm; // 流域内深层土壤蓄水容量，WDM=WM-WUM-WLM

	// EVAPORATION 蒸发
	private double[] m_pEu; // 上层土壤蒸发量（毫米）
	private double[] m_pEl; // 下层土壤蒸发量（毫米）
	private double[] m_pEd; // 深层土壤蒸发量（毫米）

	// PARAMETER 模型参数
	private double m_K; // 流域蒸散发能力与实测蒸散发值的比
	private double m_IMP; // 不透水面积占全流域面积之比
	private double m_B; // 蓄水容量曲线的方次，小流域（几平方公里）B为0.1左右，
	// 中等面积（300平方公里以内）0.2~0.3，较大面积0.3~0.4
	private double m_WM; // 流域平均蓄水容量（毫米）(WM=WUM+WLM+WDM)

	private double m_C; // 流域内深层土壤蒸发系数，江南湿润地区：0.15-0.2，华北半湿润地区：0.09-0.12
	private double m_FC; // 稳定入渗率，毫米/小时
	private double m_KKG; // 地下径流消退系数
	// double m_UH; // 单元流域上地面径流的单位线
	private double m_Kstor; // 脉冲汇流计算的参数,Liang
	private double m_WMM; // 流域内最大蓄水容量
	private double m_Area; // 流域面积
	private int m_DeltaT; // 每一步长的小时数

	/**
	 * 构造函数
	 */
	public XAJModel() {
	    // 驱动数据
	    this.m_pP = new double[NNN];
	    this.m_pEm = new double[NNN];
	    // 模型输出，蒸散发项
	    this.m_pE = new double[NNN];
	    this.m_pEd = new double[NNN];
	    this.m_pEl = new double[NNN];
	    this.m_pEu = new double[NNN];
	    // 模型输出，出流项，经过汇流的产流
	    this.m_pQrg = new double[NNN];
	    this.m_pQrs = new double[NNN];
	    this.m_pQ = new double[NNN];
	    // 模型输出，产流项
	    this.m_pR = new double[NNN];
	    this.m_pRg = new double[NNN];
	    this.m_pRs = new double[NNN];
	    // 模型状态量，土壤湿度
	    this.m_pW = new double[NNN];
	    this.m_pWd = new double[NNN];
	    this.m_pWl = new double[NNN];
	    this.m_pWu = new double[NNN];		
	}
	
	/**
	 * 取得文件读取缓冲
	 * @param fileName 文件名
	 * @return 文件读取缓冲
	 * @throws FileNotFoundException 文件不存在异常
	 */
	private BufferedReader getReader(String fileName) throws FileNotFoundException {
		File file;
		URL url = ClassLoader.getSystemResource(fileName);
		// 如果xml文件不存在
		if (url == null) {
			throw new IllegalStateException(String.format("文件不存在: %s", fileName));
		}
		// 取得文件绝对路径
		String filePath = ClassLoader.getSystemResource(fileName).getPath();
		file = new File(filePath);
        if (!file.exists()||file.isDirectory()) {
        	throw new FileNotFoundException();
        }
        BufferedReader in = new BufferedReader(new FileReader(file));  
        return in;
	}	

	/**
	 * 模型初始化
	 * @param forcingFileName 
	 * @param runoffAllFileName
	 */
	public void init(String forcingFileName, String runoffAllFileName) {
		initForcing(forcingFileName);
		initRunoffAll(runoffAllFileName);
	}

	/**
	 * 初始化参数
	 * @param fileName
	 */
	private void initForcing(String fileName) {
	    int i;

	    this.m_U = 1.0;		//this.m_Area/(3.6*this.m_DeltaT);

	    try
	    {
	        BufferedReader in = getReader(fileName);
	        String line;  //一行数据

	         for (i = 0; i < NNN; i++)  //读取驱动数据
	         {
	            line = in.readLine();
	            if (line != null){
	               String[] temp = line.split("\\t");
	               this.m_pP[i] = Double.parseDouble(temp[0]);
	               this.m_pEm[i]= Double.parseDouble(temp[1]);
	            }
	          }
	         in.close();
	    }
	    catch (Exception e)
	    {
	        System.out.println("找不到指定的驱动数据！"+ e.getMessage());
	        return;
	    }		
	}
	
	/**
	 * 初始化参数
	 * @param fileName
	 */
	private void initRunoffAll(String fileName) {
	    try
	    {
	        BufferedReader in = getReader(fileName);
	        String line;  //一行数据

	         for (int i = 0; i < NNN; i++)  //读取驱动数据
	         {
	            line = in.readLine();
				if (line != null) {
					m_wall[i] = Double.parseDouble(line);
				}	            
	          }
	         in.close();
	    }
	    catch (Exception e)
	    {
	        System.out.println("找不到指定的驱动数据！"+ e.getMessage());
	        return;
	    }		
	}
	
	public void runModle() {

	}
}
