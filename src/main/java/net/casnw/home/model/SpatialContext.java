//@DECLARE@
package net.casnw.home.model;

import net.casnw.home.meta.DatatypeEnum;
import net.casnw.home.meta.ModuleMeta;
import net.casnw.home.meta.VariableMeta;
import net.casnw.home.poolData.PoolDouble2DArray;
import net.casnw.home.poolData.PoolInteger;

/**
 * 空间容器
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-21
 * @version 1.0
 *
 */
@ModuleMeta(name = "SpatialContext",
        author = "home",
        version = "1.0",
        keyword = "spatial,grid,Context",
        description = "control grid loop")
public class SpatialContext extends Context {

    @VariableMeta(name = "rowsNum", dataType = DatatypeEnum.PoolInteger, description = "row numbers of grid")
    public PoolInteger rowsNum;
    @VariableMeta(name = "colsNum", dataType = DatatypeEnum.PoolInteger, description = "col numbers of grid")
    public PoolInteger colsNum;
    private int currentRowNum = 0;
    private int currentColNum = 0;
    //二维数组gridData是从ascII文件读取的数据
    public PoolDouble2DArray data;

    /**
     * @return the rowsNum
     */
    public int getRowsNum() {
        if (this.rowsNum == null) {
            return 0;
        }
        return rowsNum.getValue();
    }

    /**
     * @param rowsNum the rowsNum to set
     */
    public void setRowsNum(int rowsNum) {
        if (this.rowsNum == null) {
            this.rowsNum = new PoolInteger();
        }
        this.rowsNum.setValue(rowsNum);
    }

    /**
     * @return the colsNum
     */
    public int getColsNum() {
        if (this.colsNum == null) {
            return 0;
        }
        return colsNum.getValue();
    }

    /**
     * @param colsNum the colsNum to set
     */
    public void setColsNum(int colsNum) {
        if (this.colsNum == null) {
            this.colsNum = new PoolInteger();
        }
        this.colsNum.setValue(colsNum);
    }

    /**
     * @return the currentRowNum
     */
    public int getCurrentRowNum() {
        return currentRowNum;
    }

    /**
     * @param currentRowNum the currentRowNum to set
     */
    public void setCurrentRowNum(int currentRowNum) {
        this.currentRowNum = currentRowNum;
    }

    /**
     * @return the currentColsNum
     */
    public int getCurrentColNum() {
        return currentColNum;
    }

    /**
     * @param currentColsNum the currentColsNum to set
     */
    public void setCurrentColNum(int currentColNum) {
        this.currentColNum = currentColNum;
    }

    @Override
    public void init() throws Exception {
        _logger.info("spatialContext init begin");
        super.initAccessors();
        if (getData() != null) {
            if (colsNum == null) {
                colsNum = new PoolInteger();
            }
            if (rowsNum == null) {
                rowsNum = new PoolInteger();
            }
            colsNum.setValue(getData().getColsNum());
            rowsNum.setValue(getData().getRowsNum());
        }
        super.initAllComponent();

        _logger.info("spatialContext init end");
    }

    @Override
    public void run() throws Exception {
        _logger.info("spatialContext run begin");
        if (getData() != null) {
            if (this.rowsNum.getValue() > 0 && this.colsNum.getValue() > 0) {
                for (int i = 0; i < this.rowsNum.getValue(); i++) {
                    for (int j = 0; j < this.colsNum.getValue(); j++) {
                        _logger.info("currentRowNum = " + (i + 1) + ",currentColNum = " + (j + 1));
                        this.currentRowNum = i;
                        this.currentColNum = j;
                        Double cellValue = new Double((getData().getCellValue(i, j)));
                        if (!cellValue.equals(new Double(Double.NaN))) {
                            super.run();
                        } else {
                            _logger.info("currentRowNum = " + (i + 1) + ",currentColNum = " + (j + 1) + " is nodata!");
                        }
                    }
                }
            }
        } else {
            if (this.rowsNum.getValue() > 0 && this.colsNum.getValue() > 0) {
                for (int i = 0; i < this.rowsNum.getValue(); i++) {
                    for (int j = 0; j < this.colsNum.getValue(); j++) {
                        _logger.info("currentRowNum = " + (i + 1) + ",currentColNum = " + (j + 1));
                        this.currentRowNum = i;
                        this.currentColNum = j;
                        super.run();
                    }
                }
            }
        }
        _logger.info("spatialContext run end");
    }

    /**
     * @return the data
     */
    public PoolDouble2DArray getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(PoolDouble2DArray data) {
        this.data = data;
    }
}
