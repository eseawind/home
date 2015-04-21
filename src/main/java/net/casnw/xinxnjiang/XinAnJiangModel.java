package net.casnw.xinxnjiang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import net.casnw.home.io.DataWriter;

/**
 *
 * @author Huo Jiuyuan
 * @since 2014/08/21
 * @version 0.1
 * @Desc From Wang Shugong C++ Program, two-source Xinanjiang model
 * 
 */
public class XinAnJiangModel {
  // FORCING 驱动数据
  private double[] m_pP;  	// 降水数据
  private double[] m_pEm;	// 水面蒸发数据
  //
  private int m_nSteps;	// 模型要运行的步长
  
  // OUTPUT  输出数据
  private double[] m_pR;		// 流域内每一步长的产流量(径流深度) 
  private double[] m_pRg;		// 每一步长的地表径流深(毫米) 
  private double[] m_pRs;		// 每一步长的基流径流深(毫米) 
  private double[] m_pE;		// 每一步长的蒸发(毫米) 
  private double[] m_pQrs;	// 流域出口地表径流量
  private double[] m_pQrg;	// 流域出口地下径流量
  private double[] m_pQ;		// 流域出口的总流量
  // 
  private double m_U;		// for 24h. U=A(km^2)/3.6/delta_t
  // SOIL 土壤数据
  private double[] m_pW;		// 流域内土壤湿度
  private double[] m_pWu;		// 流域内上层土壤湿度
  private double[] m_pWl;		// 流域内下层土壤湿度
  private double[] m_pWd;		// 流域内深层土壤湿度

  private double m_Wum;		// 流域内上层土壤蓄水容量，植被良好的流域，约为20mm,差的流域,2~10mm
  private double m_Wlm;		// 流域内下层土壤蓄水容量，可取60~90mm
  private double m_Wdm;		// 流域内深层土壤蓄水容量，WDM=WM-WUM-WLM 

  // EVAPORATION 蒸发
  private double[] m_pEu;		// 上层土壤蒸发量（毫米）
  private double[] m_pEl;		// 下层土壤蒸发量（毫米）
  private double[] m_pEd;		// 深层土壤蒸发量（毫米）
  
  // PARAMETER 模型参数
  private double m_K;		// 流域蒸散发能力与实测蒸散发值的比
  private double m_IMP;		// 不透水面积占全流域面积之比
  private double m_B;		// 蓄水容量曲线的方次，小流域（几平方公里）B为0.1左右，
  // 中等面积（300平方公里以内）0.2~0.3，较大面积0.3~0.4   
  private double m_WM;		// 流域平均蓄水容量（毫米）(WM=WUM+WLM+WDM)

  private double m_C;		// 流域内深层土壤蒸发系数，江南湿润地区：0.15-0.2，华北半湿润地区：0.09-0.12
  private double m_FC;		// 稳定入渗率，毫米/小时
  private double m_KKG;		// 地下径流消退系数
  //double m_UH;  // 单元流域上地面径流的单位线 
  private double m_Kstor;	// 脉冲汇流计算的参数,Liang
  private double m_WMM;		// 流域内最大蓄水容量  
  private double m_Area;	// 流域面积
  private int m_DeltaT;		// 每一步长的小时数
  //double m_U;     // 

  public void XinanjiangModel ()
  {
    this.m_pE = null;
    this.m_pP = null;

    this.m_pE = null;
    this.m_pEd = null;
    this.m_pEl = null;
    this.m_pEu = null;

    this.m_pQrg = null;
    this.m_pQrs = null;

    this.m_pR = null;
    this.m_pRg = null;
    this.m_pRs = null; 
  };
 
  // 模型析构函数
  protected void finalize()  
  {  
    this.m_pP = null;
    this.m_pEm = null;

    this.m_pE = null;
    this.m_pEd = null;
    this.m_pEl = null;
    this.m_pEu = null;

    this.m_pQrg = null;
    this.m_pQrs = null;
    this.m_pQ = null;

    this.m_pR = null;
    this.m_pRg = null;
    this.m_pRs = null;

    this.m_pW = null;
    this.m_pWd = null;
    this.m_pWl = null;
    this.m_pWu = null;  
  };
  
  
  // 通过文件初始化模型设置，包括步长，步数，驱动数据
  public void XinanjiangModel (File FileName)
  {
      
  };
  
  // 初始化模型
  public void InitModel (int nSteps, double Area, int DeltaT, File ForcingFile) 
  {
    File fp;
    int i;
    this.m_nSteps = nSteps;
    // 驱动数据
    this.m_pP = new double[this.m_nSteps];
    this.m_pEm = new double[this.m_nSteps];
    // 模型输出，蒸散发项
    this.m_pE = new double[this.m_nSteps];
    this.m_pEd = new double[this.m_nSteps];
    this.m_pEl = new double[this.m_nSteps];
    this.m_pEu = new double[this.m_nSteps];
    // 模型输出，出流项，经过汇流的产流
    this.m_pQrg = new double[this.m_nSteps];
    this.m_pQrs = new double[this.m_nSteps];
    this.m_pQ = new double[this.m_nSteps];
    // 模型输出，产流项
    this.m_pR = new double[this.m_nSteps];
    this.m_pRg = new double[this.m_nSteps];
    this.m_pRs = new double[this.m_nSteps];
    // 模型状态量，土壤湿度
    this.m_pW = new double[this.m_nSteps];
    this.m_pWd = new double[this.m_nSteps];
    this.m_pWl = new double[this.m_nSteps];
    this.m_pWu = new double[this.m_nSteps];

    this.m_Area = Area;
    this.m_DeltaT = DeltaT;
    this.m_U = 1.0;		//this.m_Area/(3.6*this.m_DeltaT);
    // Forcing文件的格式：第一列：降水（单位毫米）空格第二列水面蒸发（毫米）
    fp = ForcingFile;
    
    try
    {
        if (!fp.exists()||fp.isDirectory())
        throw new FileNotFoundException();
        BufferedReader in = new BufferedReader(new FileReader(fp));         
        String line;  //一行数据
//        int row=0;
            //逐行读取，并将每个数组放入到数组中
//            while((line = in.readLine()) != null){
//                String[] temp = line.split("t");
//                
//                for(int j=0;j<temp.length;j++){
//                    arr2[row][j] = Double.parseDouble(temp[j]);
//                    this.m_pP[i] = 
//                    this.m_pEm[i]=
//                }
//                row++;
//            }
         for (i = 0; i < this.m_nSteps; i++)  //读取驱动数据
         {
//            fscanf (fp, "%lf%lf", &(this.m_pP[i]), &(this.m_pEm[i]));
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
    
  };
  
   // 初始化模型
  public void InitModel (int nSteps, double Area, int DeltaT, double[][] forcingData) 
  {
    File fp;
    int i;
    this.m_nSteps = nSteps;
    // 驱动数据
    this.m_pP = new double[this.m_nSteps];
    this.m_pEm = new double[this.m_nSteps];
    // 模型输出，蒸散发项
    this.m_pE = new double[this.m_nSteps];
    this.m_pEd = new double[this.m_nSteps];
    this.m_pEl = new double[this.m_nSteps];
    this.m_pEu = new double[this.m_nSteps];
    // 模型输出，出流项，经过汇流的产流
    this.m_pQrg = new double[this.m_nSteps];
    this.m_pQrs = new double[this.m_nSteps];
    this.m_pQ = new double[this.m_nSteps];
    // 模型输出，产流项
    this.m_pR = new double[this.m_nSteps];
    this.m_pRg = new double[this.m_nSteps];
    this.m_pRs = new double[this.m_nSteps];
    // 模型状态量，土壤湿度
    this.m_pW = new double[this.m_nSteps];
    this.m_pWd = new double[this.m_nSteps];
    this.m_pWl = new double[this.m_nSteps];
    this.m_pWu = new double[this.m_nSteps];

    this.m_Area = Area;
    this.m_DeltaT = DeltaT;
    this.m_U = 1.0;		//this.m_Area/(3.6*this.m_DeltaT);
    // Forcing数据格式：第一维：降水（单位毫米）；第二维水面蒸发（毫米）
    
    for (i = 0; i < this.m_nSteps; i++)  //读取驱动数据
    {
//     fscanf (fp, "%lf%lf", &(this.m_pP[i]), &(this.m_pEm[i]));
       this.m_pP[i] = forcingData[i][0];
       this.m_pEm[i]= forcingData[i][1];
     }
 
};
  
  // 设置模型参数
  public void SetParameters (double[] Params)
  {
    this.m_K = Params[0];	// (1) 流域蒸散发能力与实测水面蒸发之比
    this.m_IMP = Params[1];	// (2) 流域不透水面积占全流域面积之比
    this.m_B = Params[2];	// (3) 蓄水容量曲线的方次
    this.m_Wum = Params[3];	// (4) 上层蓄水容量
    this.m_Wlm = Params[4];	// (5) 下层蓄水容量
    this.m_Wdm = Params[5];	// (6) 深层蓄水容量
    this.m_C = Params[6];	// (7) 深层蒸散发系数
    this.m_FC = Params[7];	// (8) 稳定入渗率（毫米／小时）
    this.m_KKG = Params[8];	// (9) 地下径流消退系数
    this.m_Kstor = Params[9];	// (10)汇流计算参数

    this.m_WM = this.m_Wum + this.m_Wlm + this.m_Wdm;
    this.m_WMM = this.m_WM * (1.0 + this.m_B) / (1.0 - this.m_IMP);
  };
  // 运行新安江模型
  public void RunModel ()
  {
    int i;
 
    // 模型的状态变量
    double PE;			// 大于零时为净雨量，小于零时为蒸发不足量，单位（毫米）

    double R;			// 产流深度，包括地表径流深度和地下径流深度两部分（毫米）
    double RG;			// 地下径流深度，单位（毫米）
    double RS;			// 地表径流深度，单位（毫米）

    double A;			// 当流域内的土壤湿度为W是,土壤含水量折算成的径流深度,单位（毫米）

    double E = 0.0;		// 蒸散发
    double EU = 0.0;		// 上层土壤蒸散发量（毫米）
    double EL = 0.0;		// 下层土壤蒸散发量（毫米）
    double ED = 0.0;		// 深层土壤蒸散发量（毫米）

    // 假设流域经历了长时间降水，各层土壤含水量均为该层土壤的蓄水能力 
    double W = this.m_WM;	// 流域内土壤湿度
    double WU = this.m_Wum;	// 流域内上层土壤湿度
    double WL = this.m_Wlm;	// 流域内下层土壤适度
    double WD = this.m_Wdm;	// 流域内深层土壤湿度

    for (i = 0; i < this.m_nSteps; i++)
      {
        PE = m_pP[i] - m_K * m_pEm[i];
        // 如果降水量小于足蒸发需求
        if (PE < 0)
          {
            R = 0.0;		// 产流总量为零
            RG = 0.0;		// 地下径流量为零
            RS = 0.0;		// 地表径流量为零

            // 如果上层土壤含水量大于蒸发不足量
            if ((WU + PE) > 0.0)
              {
                // 上层土壤为流域蒸散发提供水量
                EU = m_K * m_pEm[i];
                // 没有降水量用于增加土壤湿度
                EL = 0.0;		/* 降水用来增加土壤湿度的部分 */
                // 
                ED = 0.0;
                // 更新上层土壤含水量
                WU = WU + PE;
              }
            // 上层土壤含水量小于蒸发不足量
            else
              {
                EU = WU + m_pP[i];	// 上层土壤蒸发,降水全部用于蒸发
                WU = 0.0;		// 上层含水量为0，全部水分被蒸发
                // 如果下层含水量大于下层土壤的蒸散发潜力
                if (WL > (m_C * m_Wlm))
                  {
                    EL = (m_K * m_pEm[i] - EU) * (WL / m_Wlm);
                    WL = WL - EL;
                    ED = 0;
                  }
                // 如果下层土壤含水量小于下层土壤的蒸散发潜力
                else
                  {
                    // 如果下层土壤的含水量蒸发之后还有剩余
                    if (WL > m_C * (m_K * m_pEm[i] - EU))
                      {
                        EL = m_C * (m_K * m_pEm[i] - EU);
                        WL = WL - EL;	/////////////////////////////////
                        ED = 0.0;
                      }
                    // 如果下层土壤含水量全部蒸发之后尚不能满足蒸发需求
                    else
                      {
                        EL = WL;	/* 下层土壤含水量全部用于蒸散发 */
                        WL = 0.0;	/* 下层土剩余壤含水量为0        */
                        ED = m_C * (m_K * m_pEm[i] - EU) - EL;	/* 深层土壤含水量参与蒸发 */
                        WD = WD - ED;	/* 深层土壤含水量更新 */
                      }
                  }
              }
          }
        // 如果降水量大于或者等于蒸散发需求,即降水满足蒸发后还有剩余
       else
           {
                  /*************** 以下代码负责径流划分计算 **************/
                  // 初始化变量
                  R = 0.0;		// 产流总量为零
                  RG = 0.0;		// 地下径流产流深度为零
                  RS = 0.0;		// 地表径流产流深度为零

                  // 计算流域当天土壤含水量折算成的径流深度Ａ
                  // m_WM:流域平均蓄水容量(一个参数),
                  // m_W:流域内土壤湿度(一个状态变量)
                  // m_B:蓄水容量曲线的方次(一个参数)                     
                  A = m_WMM * (1 - Math.pow((1.0 - W / m_WM), 1.0 / (1 + m_B)));
                  // 土壤湿度折算净雨量加上降水后蒸发剩余雨量小于流域内最大含水容量
                  if ((A + PE) < this.m_WMM)
                  {
                          // 流域内的产流深度计算
                          R = PE		/* 降水蒸发后的剩余量(PE=P-E:状态变量) */
                          + W		/* 流域内土壤湿度 (W:状态变量)         */
                          + m_WM * Math.pow ((1 - (PE + A) / m_WMM), (1 + m_B)) - m_WM;	/* 减去流域平均蓄水容量（m_WM:参数）   */
                  }
                  // 土壤湿度折算净雨量加上降水后蒸发剩余雨量大于流域内最大含水容量
                  else
                  {
                          // 流域内的产流深度计算
                          R = PE		/* 降水蒸发后的剩余量(PE=P-E:状态变量) */
                          + W		/* 流域内土壤湿度 (W:状态变量)         */
                          - m_WM;		/* 减去流域平均蓄水容量（m_WM:参数）   */
                  }
                  // 如果降水经过蒸散发后的剩余量大于等于土壤稳定入渗率//
                  if (PE > m_FC)
                          {
                          // 计算地下径流的产流深度
                          RG = (R - this.m_IMP * PE) * (m_FC/ PE);
                          // 计算地表径流的产流深度 
                          RS = R - RG;
                          }
                  // 如果降水蒸发后的剩余量小于土壤的稳定入渗率(m_FC:参数)
                  //除了不透水面积上的地表产流外，全部下渗，形成地下径流
                  else
                          {
                          // 计算地下径流的产流深度
                          RG = R -		  /* 总产流深度                         */
                               m_IMP * PE;  /* 不透水面积上的产流深度，m_IMP:参数 */
                          // 计算地表径流的产流深度 
                          RS = R - RG;
                          }
                                  /***************      径流划分计算结束      **************/

                  // 计算上层土壤蒸散发量
                  EU = m_K *		/* 流域蒸散发能力与实测蒸散发值的比 */
                          m_pEm[i];		/* 当前时段的水面蒸发               */
                  ED = 0.0;
                  EL = 0.0;		/* 降水用来增加土壤湿度的部分 */

                                  /*************** 以下代码负责土壤含水量的更新计算 **************/
                  // 如果上层土壤含水量与降水蒸散发剩余量之和减去产流量之后
                  // 大于上层土壤的蓄水能力
                  if ((WU + PE - R) >= m_Wum)
                          {
                          // 上层含水量+下层含水量+降水剩余量-产流量-上层土壤蓄水需求
                          // 后的水量大于下层土壤蓄水需求，多余水量分配到深层土壤
                          if ((WU + WL + PE - R - m_Wum) > m_Wlm)
                          {
                          WU = m_Wum;	/* 上层土壤含水量=上层土壤蓄水容量 */
                          WL = m_Wlm;	/* 下层土壤含水量=下层土壤蓄水容量 */
                          WD = W + PE - R - WU - WL;	/* 绝对降水剩余量补充到深层土壤中  */
                          }
                          // 上层含水量+下层含水量+降水剩余量-产流量-上层土壤蓄水需求
                          // 后的水量小于下层土壤蓄水需求，剩余水量补充到下层土壤中
                          else
                          {
                          WL = WU + WL + PE - R - m_Wum;	/* 下层土壤含水量           */
                          WU = m_Wum;	/* 上层土壤蓄水容量得到满足 */
                          }
                          }
                  // 如果上层土壤含水量与降水蒸散发剩余量之和减去产流量之后
                  // 小于上层土壤的蓄水能力
                  else
                          {
                          WU = WU + PE - R;
                          // WU 可能小于零，应该加一些控制代码..........
                          }
                                  /*************** 土壤含水量的更新计算结束 **************/
                  }

        E = EU + EL + ED;
        W = WU + WL + WD;
        /* 以下部分是状态量：总蒸发量、上、下和深层土壤的蒸发的保存 */
                                          /* 1 */ this.m_pE[i] = E;
                                          // 当前步长的蒸发        （模型重要输出）
                                          /* 2 */ this.m_pEu[i] = EU;
                                          // 当前步长上层土壤蒸发
                                          /* 3 */ this.m_pEl[i] = EL;
                                          // 当前步长下层土壤蒸发
                                          /* 4 */ this.m_pEd[i] = ED;
                                          // 当前步长深层土壤蒸发
                                          /* 5 */ this.m_pW[i] = W;
                                          // 当前步长流域平均土壤含水量
                                          /* 6 */ this.m_pWu[i] = WU;
                                          // 当前步长流域上层土壤含水量
                                          /* 7 */ this.m_pWl[i] = WL;
                                          // 当前步长流域下层土壤含水量
                                          /* 8 */ this.m_pWd[i] = WD;
                                          // 当前步长流域深层土壤含水量
                                          /* 9 */ this.m_pRg[i] = RG;
                                          // 当前步长流域基流径流深度
                                          /* 10*/ this.m_pRs[i] = RS;
                                          // 当前步长流域地表径流径流深度
                                          /* 11*/ this.m_pR[i] = R;
                                          // 当前步长的总产流径流深度
      }
    this.Routing ();  
  };
  
  // 保存模拟结果到文件
  public void SaveResults (String FileName)
  {
    DataWriter outWriter;
   
    int i;
    try
    {
//        if(!FileName.exists())
//             FileName.createNewFile();

        //      fprintf (fp,"       E(mm)      EU(mm)      EL(mm)      ED(mm)       W(mm)      WU(mm)      WL(mm)      WD(mm)       R(mm)      RG(mm)      RS(mm)     Q(m3/d)    QS(m3/d)    QG(m3/d)\n");
        outWriter = new DataWriter(FileName);
        System.out.println("模型运算结果输出至文件开始");
        outWriter.writeLine("       E(mm)      EU(mm)      EL(mm)      ED(mm)       W(mm)      WU(mm)      WL(mm)      WD(mm)       R(mm)      RG(mm)      RS(mm)     Q(m3/d)    QS(m3/d)    QG(m3/d)");
        
        for (i = 0; i < this.m_nSteps; i++)
        {
//            fprintf (fp," %11.5lf %11.5lf %11.5lf %11.5lf %11.5lf %11.5lf %11.5lf %11.5lf %11.5lf %11.5lf %11.5lf %11.5lf %11.5lf %11.5lf\n",
//                 this.m_pE[i], this.m_pEu[i], this.m_pEl[i], this.m_pEd[i],
//                 this.m_pW[i], this.m_pWu[i], this.m_pWl[i], this.m_pWd[i],
//                 this.m_pR[i], this.m_pRs[i], this.m_pRg[i], this.m_pQ[i],
//                 this.m_pQrs[i], this.m_pQrg[i]);
            outWriter.writeLine(String.format("%1$16.3f  %2$16.3f  %3$16.3f  %4$16.3f  %5$16.3f  %6$16.3f  %7$16.3f  %8$16.3f  %9$16.3f  %10$16.3f  %11$16.3f  %12$16.3f  %13$16.3f  %14$16.3f",
                this.m_pE[i], this.m_pEu[i], this.m_pEl[i], this.m_pEd[i],
                 this.m_pW[i], this.m_pWu[i], this.m_pWl[i], this.m_pWd[i],
                 this.m_pR[i], this.m_pRs[i], this.m_pRg[i], this.m_pQ[i],
                 this.m_pQrs[i], this.m_pQrg[i]));
           
         }  
        System.out.println("模型运算结果输出至文件结束");
//         out.close();
    }
    catch(Exception e)
    {
        System.out.println("无法创建模型参数输出文件！");
        return;
    }
  };
  public void Runoff (double[] runoff)
  {
    /*从1990年1月1日到1996年12月31日为模型的标定期，共有2557天，其总从
     1990年1月1日到1990年12月31日为模型运行的预热期，不参与标定      */
    int i;
    
    for (i = 0; i < 2556; i++)
      {
        runoff[i] = this.m_pQ[i];	
           // if(runoff[i]<0)
                  //{
                  ////	printf("runoff=%lf\n",this.m_pQ[i]);
                  ////	getch();
                  //	runoff[i]=0.0;
                  //}
      }
  };

  // 进行汇流计算，将径流深度转换为流域出口的流量
  private void Routing ()
  {
    double[] UH = new double[100]; // 单位线,假定最长的汇流时间为100天
    int N ;			// 汇流天数 
    N = 0;
    double K;			// 汇流参数
    double sum;
    int i, j;

    K = this.m_Kstor;
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
    for (i = 0; i < this.m_nSteps; i++)
      {
        this.m_pQrs[i] = 0.0;
        for (j = 0; j <= N; j++)
          {
            if ((i - j) < 0)
              {
                continue;
              }
            this.m_pQrs[i] += this.m_pRs[i - j] * UH[j] * this.m_U;
          }
      }
    //地下水汇流计算
    this.m_pQrg[0] = 0.0;
    for (i = 1; i < this.m_nSteps; i++)
      {
        this.m_pQrg[i] = this.m_pQrg[i - 1] * this.m_KKG +
          this.m_pRg[i] * (1.0 - this.m_KKG) * this.m_U;
      }
    for (i = 0; i < this.m_nSteps; i++)
      {
        this.m_pQ[i] = this.m_pQrs[i] + this.m_pQrg[i];
      }
  };
}