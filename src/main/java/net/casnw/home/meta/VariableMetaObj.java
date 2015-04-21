//@DECLARE@
package net.casnw.home.meta;

import java.util.Date;
import net.casnw.home.meta.UnitEnum;

/**
 * 变量元数据对象
 *
 * @author 敏玉芳
 * @since 2013-12-01
 * @version 1.0
 */
public class VariableMetaObj {

    private String name;
    private String dataType;
    private String description;
    private UnitEnum unit;
    private String range;
    private String value;
    private int size;
    private String context;
    private Date date;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the unit
     */
    /* public UnitEnum getUnit() {
     return unit;
     }*/
    public String getUnit() {
        return unit.getString();
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(UnitEnum unit) {
        this.unit = unit;
    }

    public void setUnit(String unit) {
        switch (unit) {
            case "mg":
                this.unit = UnitEnum.mg;
                break;
            case "g":
                this.unit = UnitEnum.g;
                break;
            case "C":
                this.unit = UnitEnum.C;
                break;
            case "T":
                this.unit = UnitEnum.T;
                break;
            case "Minite":
                this.unit = UnitEnum.mg;
                break;

            default:
                // this.unit = UnitEnum.NULL;
                this.unit = null;
                break;
        }
    }

    /**
     * @return the range
     */
    public String getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(String range) {
        this.range = range;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the context
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
