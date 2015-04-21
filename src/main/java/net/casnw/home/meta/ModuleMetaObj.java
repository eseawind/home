//@DECLARE@
package net.casnw.home.meta;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


/**
 * 模块元数据对象
 *
 * @author 敏玉芳
 * @since 2013-12-01
 * @version 1.0
 */
public class ModuleMetaObj implements Cloneable {

    private String name;
    private String author;
    private String version;
    private String keyword;
    private String description;
    private String category;
    private String applicationField;
    private String theory;
    private TimescaleEnum timeScale;
    private SpacescaleEnum spaceScale;
    private SparefsysEnum spaRefSys;
    private String model;
    private String moduleClass;
    private List<VariableMetaObj> varMetaObjList;
    private Color themeColor;

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
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the applicationField
     */
    public String getApplicationField() {
        return applicationField;
    }

    /**
     * @param applicationField the applicationField to set
     */
    public void setApplicationField(String applicationField) {
        this.applicationField = applicationField;
    }

    /**
     * @return the theory
     */
    public String getTheory() {
        return theory;
    }

    /**
     * @param theory the theory to set
     */
    public void setTheory(String theory) {
        this.theory = theory;
    }

    /**
     * @return the timeScale
     */
    /*  public TimescaleEnum getTimeScale() {
     return timeScale;
     }*/
    public String getTimeScale() {
        System.out.println("ff==" + timeScale.toString());
        return timeScale.toString();
    }

    /**
     * @param timeScale the timeScale to set
     */
    public void setTimeScale(TimescaleEnum timeScale) {
        this.timeScale = timeScale;
    }

    public void setTimeScale(String timeScale) {
        switch (timeScale) {
            case "YEAR":
                this.timeScale = TimescaleEnum.YEAR;
                break;
            case "MONTH":
                this.timeScale = TimescaleEnum.MONTH;
                break;
            case "Day":
                this.timeScale = TimescaleEnum.Day;
                break;
            case "Hour":
                this.timeScale = TimescaleEnum.Hour;
                break;
            case "Minite":
                this.timeScale = TimescaleEnum.Minite;
                break;
            case "Second":
                this.timeScale = TimescaleEnum.Second;
                break;
            default:
                this.timeScale = TimescaleEnum.Day;
                break;
        }
    }

    /**
     * @return the spaceScale
     */
    /* public SpacescaleEnum getSpaceScale() {
     return spaceScale;
     }*/
    public String getSpaceScale() {
        return spaceScale.getString();
    }

    /**
     * @param spaceScale the spaceScale to set
     */
    public void setSpaceScale(SpacescaleEnum spaceScale) {
        this.spaceScale = spaceScale;
    }

    public void setSpaceScale(String spaceScale) {
        switch (spaceScale) {
            case "BASIN":
                this.spaceScale = SpacescaleEnum.BASIN;
                break;
            case "SUB_BASIN":
                this.spaceScale = SpacescaleEnum.SUB_BASIN;
                break;
            case "GRID":
                this.spaceScale = SpacescaleEnum.GRID;
                break;
            case "HRU":
                this.spaceScale = SpacescaleEnum.HRU;
                break;
            case "MIXING_GRID":
                this.spaceScale = SpacescaleEnum.MIXING_GRID;
                break;
            default:
                this.spaceScale = SpacescaleEnum.BASIN;
                break;
        }
    }

    /**
     * @return the spaRefSys
     */
    /*  public SparefsysEnum getSpaRefSys() {
     return spaRefSys;
     }*/
    public String getSpaRefSys() {
        return spaRefSys.getString();
    }

    /**
     * @param spaRefSys the spaRefSys to set
     */
    public void setSpaRefSys(SparefsysEnum spaRefSys) {
        this.spaRefSys = spaRefSys;
    }

    public void setSpaRefSys(String spaRefSys) {
        switch (spaRefSys) {
            case "BASIN":
                this.spaRefSys = SparefsysEnum.BASIN;
                break;
            case "SUB_BASIN":
                this.spaRefSys = SparefsysEnum.SUB_BASIN;
                break;
            case "GRID":
                this.spaRefSys = SparefsysEnum.GRID;
                break;
            case "HRU":
                this.spaRefSys = SparefsysEnum.HRU;
                break;
            default:
                this.spaRefSys = SparefsysEnum.BASIN;
                break;
        }

    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the varMetaObjList
     */
    public List<VariableMetaObj> getVarMetaObjList() {
        return varMetaObjList;
    }

    /**
     * @param varMetaObjList the varMetaObjList to set
     */
    public void setVarMetaObjList(List<VariableMetaObj> varMetaObjList) {
        this.varMetaObjList = varMetaObjList;
    }

    public void addVarMetaObj(VariableMetaObj vmo) {
        if (this.varMetaObjList == null) {
            this.varMetaObjList = new ArrayList<VariableMetaObj>();
        }
        this.varMetaObjList.add(vmo);
    }

    @Override
    public Object clone() {
        ModuleMetaObj mmo = null;
        try {
            mmo = (ModuleMetaObj) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return mmo;
    }

    /**
     * @return the moduleClass
     */
    public String getModuleClass() {
        return moduleClass;
    }

    /**
     * @param moduleClass the moduleClass to set
     */
    public void setModuleClass(String moduleClass) {
        this.moduleClass = moduleClass;
    }

    /**
     * @return the themeColor
     */
    public Color getThemeColor() {
        return themeColor;
    }

    /**
     * @param themeColor the themeColor to set
     */
    public void setThemeColor(Color themeColor) {
        this.themeColor = themeColor;
    }
}
