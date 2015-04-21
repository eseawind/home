/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.casnw.home.model;

/**
 *　While选择容器
 * @author zhaoxr
 * @since 2013-10-12
 * @version 1.0
 */

import net.casnw.home.poolData.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class WhileContext extends Context{
    
    public PoolBoolean condition;
    protected final Log _logger = LogFactory.getLog(getClass());
     
    
       @Override
    public void run() throws Exception {
       while(getCondition().getValue()){
        super.run();
        
       }
       }

    /**
     * @return the condition
     */
    public PoolBoolean getCondition() {
        _logger.info("condition=="+condition);
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(Boolean condition) {
        if (this.condition == null) {
            this.condition = new PoolBoolean();
        }
        this.condition.setValue(condition);
        
    }
   
       
}
