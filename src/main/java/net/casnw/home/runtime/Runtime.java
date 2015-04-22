//@DECLARE@
package net.casnw.home.runtime;

import net.casnw.home.io.ModelDescription;
import net.casnw.home.io.XMLParse;
import net.casnw.home.model.Model;
import net.casnw.home.model.Modelable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 运行环境实现
 *
 * @author zxr@lzb.ac.cn
 * @since 2013-04-12
 * @version 1.0
 *
 */
public class Runtime implements Runtimeable {

    protected final Log _logger = LogFactory.getLog(getClass());
    private Model _homeModel = null;//模型
    private String _propertiesFile;//属性文件
    //   public ModelLoader modelLoader;    
    private String _workspaceDir = "";
    private int isValid = 1;
    private ModelDescription _modelDescription = null;
    Modelable model = new Model();


    public Runtime(String propertiesFile) throws Exception {
        if (propertiesFile.contains(":")) {
            propertiesFile = propertiesFile.replaceAll("\\\\", "/");
            _propertiesFile = propertiesFile;

            if (_propertiesFile.contains("/")) {
                _workspaceDir = _propertiesFile.substring(0, _propertiesFile.lastIndexOf("/"));
            }

        } else {
            propertiesFile = propertiesFile.replaceAll("\\\\", "/");
            _propertiesFile = propertiesFile;

            if (_propertiesFile.contains("/")) {
                _workspaceDir = _propertiesFile.substring(0, _propertiesFile.lastIndexOf("/"));
            } else {
                _workspaceDir = "";
            }
            _workspaceDir = System.getProperty("user.dir") + "/" + _workspaceDir;
            _propertiesFile = System.getProperty("user.dir") + "/" + _propertiesFile;

        }
        readPropertise();
    }
    //读取模型信息

    private void readPropertise() throws Exception {

        XMLParse xmlp = new XMLParse(_propertiesFile);
        this._modelDescription = xmlp.getModelDescription();
    }
//加载模型

    @Override
    public void loadModel() throws Exception {

        _logger.info("loadModel begin");
        if (isValid == 0) {
            return;
        }

        ModelLoader modelLoader = new ModelLoader(this);

        this._homeModel = modelLoader.loadModel(_modelDescription);

        if (_homeModel != null) {
            _homeModel.init();
        }

        _logger.info("loadModel end");
    }

    @Override
    public void runModel() {
        _logger.info("runModel begin");
        if (isValid == 0) {
            return;
        }
        try {
            if (_homeModel != null) {
                _homeModel.run();
            }
        } catch (Exception e) {

            _logger.info("error:" + e.getMessage());
        }

        _logger.info("runModel end");

    }

    @Override
    public void clearModel() {
        _logger.info("clearModel begin");
        if (isValid == 0) {
            return;
        }
        try {
            if (_homeModel != null) {
                _homeModel.clear();
            }
        } catch (Exception e) {

            _logger.info("error:" + e.getMessage());
        }
        _logger.info("clearModel end");
    }

    public Model getModel() {
        return _homeModel;
    }
}
