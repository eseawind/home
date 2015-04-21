//@DECLARE@
package net.casnw.home.model;

import java.util.Calendar;
import net.casnw.home.meta.DatatypeEnum;
import net.casnw.home.meta.ModuleMeta;
import net.casnw.home.meta.VariableMeta;
import net.casnw.home.poolData.PoolInteger;
import net.casnw.home.poolData.PoolLong;

@ModuleMeta(name = "HRUContext",
        author = "home",
        version = "1.0",
        keyword = "spatial,hru,Context",
        description = "control hur loop")
public class HRUContext extends Context {

    @VariableMeta(name = "hruNum", dataType = DatatypeEnum.PoolInteger, description = "hru numbers")
    public PoolInteger hruNum;
    @VariableMeta(name = "currentHRUNum", dataType = DatatypeEnum.PoolInteger, description = "current hru index")
    public PoolInteger currentHRUNum;

    public int getHRUNum() {
        if (this.hruNum == null) {
            return 0;
        }
        return hruNum.getValue();
    }

    public void setHRUNum(int hruNum) {
        if (this.hruNum == null) {
            this.hruNum = new PoolInteger();
            this.currentHRUNum = new PoolInteger();
        }
        this.hruNum.setValue(hruNum);
    }

    /**
     * @return the currentTime
     */
    public int getCurrentHRUNum() {
        return currentHRUNum.getValue();
    }

    @Override
    public void run() throws Exception {
        int iteratornum = this.getHRUNum();
        if (iteratornum > 0) {
            for (int i = 0; i < iteratornum; i++) {
                _logger.info("hru num = " + (i + 1));
                super.run();
                currentHRUNum.setValue(iteratornum);
            }
        } else {
            _logger.info("hru num = 0,can not compute");
        }
    }
}
