/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.casnw.home.model;

import net.casnw.home.meta.ModuleMeta;
import net.casnw.home.poolData.PoolBoolean;

/**
 * If选择容器
 * @author zhaoxr
 * @since 2013-10-12
 * @version 1.0
 */
@ModuleMeta(name = "CaseContext",
        author = "home",
        version = "1.0",
        keyword = "CaseContext",
        description = "control if loop")
public class CaseContext extends Context{
    public PoolBoolean judge;
    
    public void run() throws Exception {
       if(judge.getValue()){
        super.run();
        
       }
       }
    /**
     * @param judje 设置判断初值
     */
    public void setJudje(Boolean judge) {
        if(judge==null){
         this.judge=new PoolBoolean();
        }        
            this.judge .setValue(judge);
          
    }
     /**
     * @param judje 取得判断初值
     */
    public PoolBoolean getJudge() {
        
           return judge;
          
    }
   
}
