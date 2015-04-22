/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.casnw.xinanjiang;

/**
 *
 * @author Administrator
 */
class UseTime {
     public static final int CLOCKS_PER_SEC=1000;
     private long m_StartTime;
    private long m_EndTime;
    
    // clock(void)  得到从程序启动到此次函数调用时累计的毫秒数。
    // 在程序开始执行前使用
    void Start() {
        this.m_StartTime= System.currentTimeMillis();  //毫秒
    }
    // 在程序执行完毕后使用
    void End() {
        this.m_EndTime= System.currentTimeMillis();  //毫秒
    }
    // 返回程序执行用时，单位为秒
    double TimeUsed() {
       return ((1.0*(this.m_EndTime-this.m_StartTime))/CLOCKS_PER_SEC);      
    }
}
