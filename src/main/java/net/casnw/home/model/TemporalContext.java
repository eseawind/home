//@DECLARE@
package net.casnw.home.model;

import java.util.Calendar;
import net.casnw.home.meta.DatatypeEnum;
import net.casnw.home.meta.ModuleMeta;
import net.casnw.home.meta.VariableMeta;
import net.casnw.home.poolData.PoolInteger;
import net.casnw.home.poolData.PoolLong;
import net.casnw.home.poolData.PoolString;
import net.casnw.home.util.OftenTools;

@ModuleMeta(name = "TemporalContext",
        author = "home",
        version = "1.0",
        keyword = "Temporal,Context",
        description = "control time loop")
public class TemporalContext extends Context {

    @VariableMeta(name = "startTime", dataType = DatatypeEnum.PoolString, description = "start time")
    public PoolString startTime;
    @VariableMeta(name = "endTime", dataType = DatatypeEnum.PoolString, description = "end time")
    public PoolString endTime;
    private Calendar currentTime = null;
    @VariableMeta(name = "intervalUnit", dataType = DatatypeEnum.PoolString, description = "interval Unit,like year,"
            + "month,day,hour,minute,default value is minute")
    public PoolString intervalUnit;
    private int timeUnit = 0;
    @VariableMeta(name = "intervalCount", dataType = DatatypeEnum.PoolInteger, description = "interval count")
    public PoolInteger intervalCount;
    @VariableMeta(name = "iteratorNum", dataType = DatatypeEnum.PoolLong, description = "iterator number")
    public PoolLong iteratorNum;
    private String format = "yyyy-MM-dd";
    private Calendar sTime = null;
    private Calendar eTime = null;
    private int currentIteratorNum = 0;//当前迭代次数从1开始

    public long getIteratorNum() {
        Calendar time = null;
        if (startTime != null && endTime != null
                && !"".equalsIgnoreCase(startTime.getValue())
                && !"".equalsIgnoreCase(endTime.getValue())) {
            iteratorNum = new PoolLong();
            iteratorNum.setValue(1);
            timeUnit = timeUnit(intervalUnit.getValue());
            setStartTime(startTime.getValue());
            setEndTime(endTime.getValue());
            currentTime = (Calendar) sTime.clone();
            time = (Calendar) sTime.clone();

            while (time.before(eTime)) {
                time.add(timeUnit, intervalCount.getValue());
                iteratorNum.setValue(iteratorNum.getValue() + 1);
            }
        } else {
            if (iteratorNum != null) {
                setIteratorNum(iteratorNum.getValue());
            } else {
                setIteratorNum(1);
            }
        }
        return iteratorNum.getValue();
    }

    @Override
    public void run() throws Exception {
        long iteratornum = this.getIteratorNum();
        if (iteratornum > 0) {
            for (int i = 0; i < iteratornum; i++) {
                currentIteratorNum = i + 1;
                _logger.info("iteratornum = " + (i + 1));
                super.run();
                if (timeUnit != 0) {
                    currentTime.add(timeUnit, intervalCount.getValue());
                }
            }
        }
    }

    /**
     * @return the startTime
     */
    public Calendar getStartTime() {
        return sTime;
    }

    /**
     * @return the currentTime
     */
    public Calendar getCurrentTime() {
        return currentTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        if (this.startTime == null) {
            this.startTime = new PoolString();
        }
        this.startTime.setValue(startTime);
        sTime = OftenTools.toCanlendar(startTime, format);
    }

    /**
     * @return the endTime
     */
    public Calendar getEndTime() {
        return eTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        if (this.endTime == null) {
            this.endTime = new PoolString();
        }
        this.endTime.setValue(endTime);
        eTime = OftenTools.toCanlendar(endTime, format);
    }

    /**
     * @return the intervalUnit
     */
    public String getIntervalUnit() {
        return intervalUnit.getValue();
    }

    /**
     * @param intervalUnit the intervalUnit to set
     */
    public void setIntervalUnit(String intervalUnit) {
        if (this.intervalUnit == null) {
            this.intervalUnit = new PoolString();
        }
        this.intervalUnit.setValue(intervalUnit);
    }

    /**
     * @return the intervalCount
     */
    public int getIntervalCount() {
        return intervalCount.getValue();
    }

    /**
     * @param intervalCount the intervalCount to set
     */
    public void setIntervalCount(int intervalCount) {
        if (this.intervalCount == null) {
            this.intervalCount = new PoolInteger();
        }
        this.intervalCount.setValue(intervalCount);
    }

    //1对应年，2对应月，3是星期，5是日，11是小时，12是分钟，13是秒
    private int timeUnit(String intervalUnit) {
        int timeUnit = 0;
        switch (intervalUnit) {
            case "year":
                timeUnit = 1;
                break;
            case "month":
                timeUnit = 2;
                break;
            case "week":
                timeUnit = 3;
                break;
            case "day":
                timeUnit = 5;
                break;
            case "hour":
                timeUnit = 11;
                break;
            case "minute":
                timeUnit = 12;
                break;
            case "second":
                timeUnit = 13;
                break;
            default:
                timeUnit = 12;
        }
        return timeUnit;
    }

    /**
     * @param iterateNum the iteratorNum to set
     */
    public void setIteratorNum(long iterateNum) {
        if (this.iteratorNum != null) {
            this.iteratorNum.setValue(iterateNum);
        } else {
            this.iteratorNum = new PoolLong();
            this.iteratorNum.setValue(iterateNum);
        }
    }

    /**
     * @return the currentIteratorNum
     */
    public int getCurrentIteratorNum() {
        return currentIteratorNum;
    }

}
