/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.casnw.xinxnjiang;

import java.io.File;
import net.casnw.home.io.DataWriter;


/**
 *
 * @author Huo Jiuyuan
 * @since 2014-08-21
 * @Desc SA Optimization algorithm
 */
public class SA {
    public static final int SA_MAX = 1;                        /* 1: 最大化问题,0: 最小化问题 */
    public static final int SA_DIM = 10;
    public static final int SA_L =  5;                        /* 局部搜索的次数，即内循环次数*/
    public static final int SA_N = 3000;                     /* 最大迭代次数                */
    public static final int SA_NN = 20;                        /* 收敛检验                    */
    public static final int SA_COUNT = 1000;                 /* 自适应均匀分布发生函数参数 */
    public static final int T0 = 1000;                      /* 初始温度                    */ 
    public static final int Tn = 1;                         /* 终止温度                    */
    public static final int GEN_TYPE = 2;                   /* 1:高斯,2:均匀               */
    public static final int TEMP_TYPE = 1;                  /* Option for Nine temperature schedule function */
    public static final double PI = 3.14159265358979323846;
    
    public static final int NNN = 2557;    
//    typedef double (*pObj)(double *X);

    static long idum;	
    public static final int IA = 16807;
    public static final int IM = 2147483647;
    public static final double AM = (1.0/IM);
    public static final int IQ =127773;
    public static final int IR =2836;
    public static final int MASK= 123459876;
    int flag = 0;
    
    double NSE(XinAnJiangModel xm, double[] r, double[] runoff, double[] params)
    {
      int i;
        double tmp = 0;
        double ave = 0;
        double tmp2 = 0;
        double y;

        xm.SetParameters(params);
        xm.RunModel();
        xm.Runoff(runoff);

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
    
    /*产生0-1之间均匀分布的随机变量*/
    double Rndu ()
    {
//      int flag = 0;
//      static flag =0;
//      unsigned long rc=1;
      long rc=1;
      double ans;
      long k;

      if (flag == 0)
      {
          //  idum = (long) time (null); C++时间函数
          //  time() 系统当前时间
          //  参数说明: timer=NULL时得到机器日历时间
         long time = System.currentTimeMillis();  //毫秒
         idum = (long) (System.currentTimeMillis()/1000);
         flag = 1;
      }
      idum ^= MASK;
      k = idum / IQ;
      idum = IA * (idum - k * IQ) - IR * k;
      if (idum < 0)
        {
          idum += IM;
        }
      ans = AM * idum;
      idum ^= MASK;
      rc++;
      if(rc>=1e5)
        {
          flag=0;
          rc=1;
        }
//      return (ans);
      return (ans);
    }
    /*产生高斯分布的随机变量*/
    double GasDev(double mean, double stdv)
    {
            int idum0;
//            static int iset=0;
            int iset=0;
//            static double gset;
            double gset = 0;
            double fac,rsq,v1,v2;
            double tmp;
            double r1;
            double r2;
//            idum0=(int)((Rndu()-0.5)*(int)time(null));
            long time = System.currentTimeMillis()/1000;  //毫秒
            idum0 = (int)((Rndu()-0.5)*(int)(System.currentTimeMillis()/1000));
            if(idum0<0)
            {
                iset=0;
            }
            idum=idum0;
            if(iset==0)
            {
                    do{
                            r1=Rndu();
                            r2=Rndu();
//                            v1 = 2.0 * r1 - 1,0;
                            v1 = 2.0 * r1 - 1.0;
                            v2 = 2.0 * r2 - 1.0;
                            rsq = v1 * v1 + v2 * v2;
            /*		printf("r1=%lf r2=%lf, rsq=%lf\n",r1,r2,rsq);
                            getch();*/
                    }while((rsq>=1.0)||(rsq==0.0));
                    fac=Math.sqrt(-2.0*Math.log(rsq)/rsq);
                    gset=v1*fac;
                    iset=1;
                    tmp=v2*fac;
            }
            else
            {
                    iset=0;
                    tmp=gset;
            }
            //printf("tmp=%lf\n",-2.0*log(rsq)/rsq);
            //getch();
            return (stdv*tmp+mean);
    }
    /*生成均值为0，标准差为T高斯分布扰动向量*/
    void GaussianGenerate(double[] DeltaX, double T,double[] LowerX, double[] UpperX)
    {
            /*这个发生函数的弊病很大，修改了*/
            int d;
            double stdv;
            for(d=0; d<SA_DIM; d++)
            {
                    stdv=(1.0*T-1.0*Tn)/(1.0*T0-1.0*Tn)*(UpperX[d]-LowerX[d]);
                    DeltaX[d]=GasDev(0,stdv);
                    //printf("DeltaX[%d]=%lf \n",d,DeltaX[d]);
                    //getch();
            }
    }

    /*生成均匀分布的扰动*/
    void UniformGenerate(double[] DeltaX, double T, double[] LowerX, double[] UpperX, int count)
    {
            int d;
            double z;
            double c;

            if(count>=SA_COUNT)
            {
                    count=SA_COUNT-1;
            }
            c=(1.0*SA_COUNT-1.0*count)/(1.0*SA_COUNT);

            for(d=0; d<SA_DIM; d++)
            {
                    z=Rndu();
                    DeltaX[d]=2.0*(z-0.5)*(UpperX[d]-LowerX[d])*c;
            }
    }

    /*检验新的变量是否在可行的范围内*/
    int TestNewX(double[] NewX, double[] LowerX, double[] UpperX)
    {
            int status=1;
            int d;
            for(d=0; d<SA_DIM; d++)
            {
                    if((NewX[d] < LowerX[d]) || (NewX[d] > UpperX[d]))
                    {
                            status=0;
                            break;
                    }
            }
            return(status);
    }
    /* Boltzmann distribution,  DeltaE: energy increament, T: temperature of current iteration  */
    double Boltzmann(double DeltaE, double T)
    {
            return(Math.exp(DeltaE/T));
    }
    /*退火时间表函数， Annealing Schedule*/
    double AS1(int k)
    {
            double T;
            T=T0-k*(T0-Tn)/SA_N;
            return(T);
    }

    double AS2(int k)
    {
            double T;
            T=T0*Math.pow((1.0*Tn/T0),(1.0*k)/SA_N);
            return(T);
    }

    double AS3(int k)
    {
            double T;
            double A;
            A=Math.log(1.0*(T0-Tn))/Math.log(1.0*SA_N);
            T=T0-Math.pow(1.0*k,A);
            return(T);
    }

    double AS4(int k)
    {
            double T;
            T=1.0*Tn+(1.0*T0-1.0*Tn)/(1+Math.exp(0.3*(1.0*k-SA_N/2)));
            return(T);
    }

    double AS5(int k)
    {
            double T;
            T=0.5*(T0-Tn)*(1+Math.cos(k*PI/SA_N))+Tn;
            return(T);
    }

    double AS6(int k)
    {
            double T;
            T=0.5*(T0-Tn)*(1-Math.tanh(10.0*k/SA_N - 5))+Tn;
            return(T);
    }

    double AS7(int k)
    {
            double T;
            T = (T0-Tn)/Math.cosh(10.0*k/SA_N)+Tn;
            return (T);
    }

    double AS8(int k)
    {
            double T;
            double A;
            A=(1.0/SA_N)*Math.log(1.0*T0/Tn);
            T=T0*Math.exp(-A*k);
            return (T);
    }

    double AS9(int k)
    {
            double T;
            double A;
            A=(1.0/(SA_N*SA_N))*Math.log(1.0*T0/Tn);
            T=T0*Math.exp(-A*k*k);
            return (T);
    }

    void Add(double[] Xi, double[] Xj,double[] DeltaX)
    {
            int d;
            for(d=0;d<SA_DIM;d++)
            {
                    Xj[d] = Xi[d] + DeltaX[d];
//                 Xj[d] = Xi[d] + Math.abs(DeltaX[d]);
            }
    }

    void Copy(double[] From, double[] To)
    {
            int d;
            for(d=0; d<SA_DIM; d++)
            {
                    To[d]=From[d];
            }
    }
    //参数：目标函数名替换，如NSE；参数最小值；参数最大值
//    void SA_Wang(double pf, double[] LowerX, double[] UpperX)
//    public void SA_Wang(String obj, double[] LowerX, double[] UpperX)
    public double[][] SA_Wang(String obj, XinAnJiangModel xm, double[] r, double[] runoff, double[] LowerX, double[] UpperX)
    {
            double[] DeltaX;
            double[] Xi;
            double[] Xj;
            double[][] Results;

            double z;
            double T;

            double Obj_i = 0;
            double Obj_j = 0;
            double DeltaE;

            //double mean;
            //double stdv;
            //double tmp;

//            unsigned long k;
//            unsigned long kk;
            int k;
            int kk;

            int count;

            long time0;
            long time1;
            double times;

            int n;	    /*外循环控制变量*/
            int l;          /*内循环控制变量*/
            int d;
           
            /* 开辟数据存储空间 */

//            Results = (double **)malloc(SA_N*SA_L*sizeof(double*));
            Results = new double[SA_N*SA_L][];   
            for(k=0;k<SA_N*SA_L;k++)
            {
//                Results[k] = (double *)malloc((SA_DIM+4)*sizeof(double));
                Results[k] = new double[SA_DIM+4];
            }

//            DeltaX = (double *)malloc(SA_DIM*sizeof(double));
//            Xi     = (double *)malloc(SA_DIM*sizeof(double));
//            Xj     = (double *)malloc(SA_DIM*sizeof(double));
            DeltaX = new double[SA_DIM];
            Xi     = new double[SA_DIM];
            Xj     = new double[SA_DIM];
            
            /* 随机初始化搜索起点 */
            for(d=0;d<SA_DIM;d++)
            {
                z=Rndu();
                Xi[d]=LowerX[d]+z*(UpperX[d]-LowerX[d]);
            }
//            Obj_i=pf(Xi);
            // NSE(XinAnJiangModel xm, double[] r, double[] runoff, double[] params)
            if (obj=="NSE") 
            {
                Obj_i=NSE(xm,r,runoff,Xi);
            }
            k=0;
            T=T0;
            /* 模拟退火的主算法 */
//            clock_t clock(void)
//            得到从程序启动到此次函数调用时累计的毫秒数。
            time0=System.currentTimeMillis();  //毫秒
            //time0=clock();
            
            for(n=0;n<SA_N;n++)
            {		
                    l=0;
                    count=0;

                    while(l<SA_L)
                    {
                            /*应用发生函数,产生扰动*/
                            do{
                                    /* 高斯分布产生扰动 */
                                if(GEN_TYPE==1) 
                                {
                                    GaussianGenerate(DeltaX,T,LowerX,UpperX);
                                }
                                    /* 均匀分布产生扰动 */
                                if(GEN_TYPE==2) 
                                {    
                                    UniformGenerate(DeltaX,T,LowerX,UpperX,count);
                                }   
    
                                Add(Xi,Xj,DeltaX);
                            } while(TestNewX(Xj,LowerX,UpperX)==0);

                            /* 计算新点的目标函数值 */
//                            Obj_j=pf(Xj);
                            if (obj=="NSE") 
                            {
                                Obj_j=NSE(xm,r,runoff,Xj);
                            }

                            if(SA_MAX==0) /* 目标函数最小化问题*/
                            {
                                /* 转化为最大化问题  */
                                Obj_i*= -1.0;
                                Obj_j*= -1.0;
                            }       /*目标函数最大化问题*/
                            if(Obj_j>Obj_i)      /*接受Xj,复制Xj->Xi*/
                            {
                                    Copy(Xj,Xi);
                                    Obj_i=Obj_j;				
                            }
                            else                /*Metropolis准则*/
                            {
                                    z=Rndu();
                                    DeltaE=Obj_j-Obj_i;

                                    if( Boltzmann(DeltaE,T) > z ) /*接受Xj,复制Xj->Xi*/
                                    {
                                            Copy(Xj,Xi);
                                            Obj_i=Obj_j;	
                                        if(GEN_TYPE==2)
                                            count=0;
                                        }
                                    else
                                    {
                                        if(GEN_TYPE==2)
                                            count++;
                                        continue;
                                    }
                            }
                            //time1=clock();
                            time1=System.currentTimeMillis();  //毫秒
//                            times=(double)(time1-time0)/CLOCKS_PER_SEC; //得到秒数  CLOCKS_PER_SEC=1000
                            times=(double)(time1-time0)/1000;
                            /*printf("#\n");*/
                            /*将新接受的点保存起来*/
                            Results[k][0]=k+1;
                            Results[k][1]=T;
                            for(d=2;d<SA_DIM+2;d++)
                            {
                                    Results[k][d]=Xi[d-2];			
                            }
                            Results[k][SA_DIM+2]=Obj_i;
                            Results[k][SA_DIM+3]=times;
                            /*printf("-------------------------------\n");*/
                           System.out.println("第 " + k + " 次优化结果显示开始：");
                           System.out.println("序号： " + String.format("%1$f", Results[k][0])); 
                           System.out.println("T： " + String.format("%1$16.3f", Results[k][1])); 
                           System.out.println("参数： "); 
                           for(d=2;d<SA_DIM+1;d++)
                            {
                                System.out.println(String.format("%1$16.3f", Results[k][d]));
                            }
                           System.out.println("目标函数值： " + String.format("%1$16.3f", Results[k][12])); 
                           System.out.println("时间： " + String.format("%1$f", Results[k][13])); 
                           System.out.println("第 " + k + " 次优化结果显示结束.");
                            
                            /*getch();*/
                            l++;
                            k++;
                    }

                    /* 收敛检验，如果后SA_NN个点的目标函数值的标准差Standard Deviation小于1e-5,则认为收敛 */
                    //if(k>SA_NN)
                    //{
                    //	tmp=0;
                    //	for(kk=k-1; kk>=k-SA_NN; kk--)
                    //	{
                    //		tmp+=Results[kk][SA_DIM+2];
                    //	}
                    //	mean = tmp/SA_NN;

                    //	for(kk=k-1; kk>=k-SA_NN; kk--)
                    //	{
                    //		tmp+=(Results[kk][SA_DIM+2]-mean)*(Results[kk][SA_DIM+2]-mean);
                    //	}
                    //	stdv = sqrt(tmp/SA_NN);

                    //	if(stdv < 1e-5)
                    //	{
                    //		printf("Convergence -- STDV\n");
                    //		break;
                    //	}
                    //}
                    //printf(" Obj_i=%lf stdv=%lf T=%lf\n",Obj_i, stdv,T);
                    //printf("Number of Samples: %ld \n",k);
                                            /*退火降温*/
                    switch(TEMP_TYPE)
                    {
                    case 1:
                            T=AS1(n);
                            break;

                    case 2:
                            T=AS2(n);
                            break;

                    case 3:
                            T=AS3(n);
                            break;

                    case 4:
                            T=AS4(n);
                            break;

                    case 5:
                            T=AS5(n);
                            break;

                    case 6:
                            T=AS6(n);
                break;

                    case 7:
                            T=AS7(n);
                            break;

                    case 8:
                            T=AS8(n);
                            break;

                    case 9:
                            T=AS9(n);
                            break;
                    }

            }
            //SAVE_RESULTS:
          return Results;
                    
            /*  释放内存  */
            /* for(k=0;k<SA_N*SA_L;k++)
            { 
               free(Results[k]);
            }
            free(Results);*/
    }
}
