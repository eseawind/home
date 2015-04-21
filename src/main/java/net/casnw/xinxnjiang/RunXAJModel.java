/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.casnw.xinxnjiang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import net.casnw.home.io.DataWriter;

/**
 *
 * @author Administrator
 */
public class RunXAJModel {

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

	// public static final int NNN = 1825;
	public static final int NNN = 2557;
	public static XinAnJiangModel xm = new XinAnJiangModel();
	public static double[] r = new double[NNN]; // 观测径流数据
	public static double[] runoff = new double[NNN]; // 模拟径流数据

	private static File getFile(String fileName) {
		File fp;
		URL url = ClassLoader.getSystemResource(fileName);
		// 如果xml文件不存在
		if (url == null) {
			throw new IllegalStateException(
					String.format("文件不存在: %s", fileName));
		}
		// 取得文件绝对路径
		String filePath = ClassLoader.getSystemResource(fileName).getPath();
		File file = new File(filePath);
		// fp = new
		// File("D:\\研究学习\\0.研究方向\\参数模拟\\参数模拟程序－王书功\\DATA\\runoff_all.txt");
		fp = new File(filePath);
		return fp;
	}

	public static void main(String[] args) {

		double[] x1 = { 0.0001, 0.0001, 0.0001, 5, 50, 50, 0.0001, 0.0001,
				0.0001, 1 };
		double[] x2 = { 1, 0.5, 1, 30, 100, 200, 0.3, 50, 0.99, 6 };
		int d;
		double results[][];

		UseTime ut = new UseTime();

		File fp = getFile("runoff_all.txt");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fp));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String line = null; // 一行数据
		for (int i = 0; i < NNN; i++) {
			try {
				line = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (line != null) {
				r[i] = Double.parseDouble(line);
			}
		}
		// 径流数据读取完毕
		System.out.println("径流数据读取完毕");
		System.out.println("读取径流数据");

		System.out.printf("初始化新安江模型");
		File forceFile = getFile("forcing.txt");
		xm.InitModel(NNN, 4000, 24, forceFile);

		System.out.printf("新安江模型初始化完毕\n");

		ut.Start();
		SA sa = new SA();
		results = sa.SA_Wang("NSE", xm, r, runoff, x1, x2);
		ut.End();
		System.out.printf("模型运行时间: " + ut.TimeUsed() + "秒");
		System.out.printf("保存模型运行结果数据：");
		// File simResult = new
		// File("D:\\dev\\Netbeans\\XinAnjiangModel\\dist\\out\\XAJ_Result.txt");
		// //File simFile = getFile("XAJ_Result.txt");
		// xm.SaveResults(simResult);
		// ////xm.SaveResults("D:\\dev\\Netbeans\\XinAnjiangModel\\dist\\out\\XAJ_Result.txt");
		xm.SaveResults("XAJ_Result.txt");

		System.out.println("保存参数结果 ... ");
		String temp = " Index   Temp";

		for (d = 0; d < SA_DIM; d++) {
			temp = temp + String.format("   Variable_%1$d", d);
		}
		temp = temp + " Objective   Seconds";

		// File fpOut=new
		// File("D:\\dev\\Netbeans\\XinAnjiangModel\\dist\\out\\SA_Out.txt");
		// /*存储计算结果*/
		DataWriter outWriter = null;

		try {
			// outWriter = new
			// DataWriter("D:\\dev\\Netbeans\\XinAnjiangModel\\dist\\out\\SA_Out.txt");
			outWriter = new DataWriter("SA_Out.txt");
		} catch (Exception e) {
			System.out.println("无法创建输出文件.");
			System.out.println("系统非正常退出!");
			System.exit(1);
		}
		outWriter.writeLine(temp);

		// fprintf(fpOut,"   Objective     Seconds\n");
		int k;
		for (k = 0; k < SA_N * SA_L; k++) {
			/* fprintf(fpOut,"%6ld %10.4lf",Results[k][0],Results[k][1]); */
			temp = "";
			for (d = 0; d < SA_DIM + 4; d++) {
				temp = temp + String.format("%1$16.3f", results[k][d]) + "   ";
			}
			outWriter.writeLine(temp);
		}

		int i = 0;
		try {
			// DataWriter runoffWriter = new DataWriter(runoffOutput);
			// DataWriter runoffWriter = new
			// DataWriter("D:\\dev\\Netbeans\\XinAnjiangModel\\dist\\out\\XAJ_Runoff.txt");
			DataWriter runoffWriter = new DataWriter("XAJ_Runoff.txt");
			System.out.println("模型运算径流结果输出至文件开始");
			runoffWriter.writeLine("  runoff  ");

			for (i = 0; i < 2556; i++) {
				runoffWriter.writeLine(String.format("%1$16.3f", runoff[i]));
			}
			System.out.println("模型运算径流结果输出至文件结束");
		} catch (Exception e) {
			System.out.println("模型运算径流结果输出错误：" + e.getMessage());
		}

		// return 0;
	}
	/**
	 * public double RunXaj(double[] params) { int i; double tmp = 0; double ave
	 * = 0; double tmp2 = 0; double y; //extern double r[NNN];
	 * xm.SetParameters(params); xm.RunModel(); xm.Runoff(runoff);
	 * 
	 * for (i = 365; i < NNN; i++) { tmp += r[i]; //cout<<r[i]<<endl; } ave =
	 * tmp / (NNN - 365.0); tmp = 0;
	 * 
	 * for (i = 365; i < NNN; i++) { tmp += (runoff[i] - r[i]) * (runoff[i] -
	 * r[i]); tmp2 += (r[i] - ave) * (r[i] - ave); } //cout<<tmp2<<endl;
	 * //getch(); y = 1.0 - tmp / tmp2; //cout<<y<<endl; return y; }
	 * 
	 * // 均方根误差 public double RMSE(double[] params) { int i; double tmp = 0;
	 * 
	 * xm.SetParameters(params); xm.RunModel(); xm.Runoff(runoff);
	 * 
	 * for (i = 365; i < NNN; i++) { tmp += (runoff[i] - r[i]) * (runoff[i] -
	 * r[i]); } return (Math.sqrt(tmp / (NNN - 365.0))); }
	 * 
	 * public double RMSESQRT(double[] params) { int i; double tmp = 0;
	 * 
	 * xm.SetParameters(params); xm.RunModel(); xm.Runoff(runoff);
	 * 
	 * for (i = 365; i < NNN; i++) { if (runoff[i] < 0) {
	 * //cout<<runoff[i]<<endl; //getch(); runoff[i] = 0; } tmp +=
	 * (Math.sqrt(runoff[i]) - Math.sqrt(r[i])) (Math.sqrt(runoff[i]) -
	 * Math.sqrt(r[i]));
	 * 
	 * } return (Math.sqrt(tmp / (NNN - 365.0))); }
	 * 
	 * public double RMSELOG(double[] params) { int i; double tmp = 0; double
	 * ave = 0;
	 * 
	 * xm.SetParameters(params); xm.RunModel(); xm.Runoff(runoff);
	 * 
	 * for (i = 365; i < NNN; i++) { ave += r[i]; } ave /= (NNN - 365.0);
	 * 
	 * for (i = 365; i < NNN; i++) { if (runoff[i] < 0) {
	 * //cout<<runoff[i]<<endl; //getch(); runoff[i] = 1e-10; } tmp +=
	 * (Math.log(runoff[i]) - Math.log(r[i])) (Math.log(runoff[i]) -
	 * Math.log(r[i]));
	 * 
	 * } return (Math.sqrt(tmp / (NNN - 365.0) + ave / 10.0)); }
	 * 
	 * double BLAS(double[] params) { int i; double tmp = 0; double sum = 0.0;
	 * xm.SetParameters(params); xm.RunModel(); xm.Runoff(runoff);
	 * 
	 * for (i = 365; i < NNN; i++) { tmp += Math.abs(runoff[i] - r[i]); sum +=
	 * r[i]; } return (100 * tmp / sum); }
	 * 
	 * double NSE(double[] params) { int i; double tmp = 0; double ave = 0;
	 * double tmp2 = 0; double y;
	 * 
	 * xm.SetParameters(params); xm.RunModel(); xm.Runoff(runoff);
	 * 
	 * for (i = 365; i < NNN; i++) { tmp += r[i]; } ave = tmp / (NNN - 365.0);
	 * tmp = 0;
	 * 
	 * for (i = 365; i < NNN; i++) { tmp += (runoff[i] - r[i]) * (runoff[i] -
	 * r[i]); tmp2 += (r[i] - ave) * (r[i] - ave); } y = 1.0 - tmp / tmp2;
	 * return (1000.0 * y); }
	 * 
	 * double NSESQRT(double[] params) { int i; double tmp = 0; double ave = 0;
	 * double tmp2 = 0; double y;
	 * 
	 * xm.SetParameters(params); xm.RunModel(); xm.Runoff(runoff);
	 * 
	 * for (i = 365; i < NNN; i++) { tmp += Math.sqrt(r[i]); } ave = tmp / (NNN
	 * - 365.0); tmp = 0;
	 * 
	 * for (i = 365; i < NNN; i++) { if (runoff[i] < 0) { runoff[i] = 0; } tmp
	 * += (Math.sqrt(runoff[i]) - Math.sqrt(r[i])) * (Math.sqrt(runoff[i]) -
	 * Math.sqrt(r[i])); tmp2 += (r[i] - ave) * (r[i] - ave); } y = 1.0 - tmp /
	 * tmp2; return y; }
	 */
}
