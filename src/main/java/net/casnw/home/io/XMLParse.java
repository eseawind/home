//@DECLARE@
package net.casnw.home.io;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * HOME XML文件解析
 *
 * @author 罗立辉
 * @since 2013-04-09
 * @version 1.0
 */
public class XMLParse {

    private File _path_file;
    private String path;
    private Document document;
    private Element root;
    // 定义属性
    private static String VARIABLE_ATTRIBUTE = "attribute";
    private static String VARIABLE_CONTEXT = "context";
    private static String VARIABLE_SIZE = "size";
    private static String VARIABLE_NAME = "name";
    private static String VARIABLE_TYPE = "type";
    private static String VARIABLE_VALUE = "value";
//    private final Log _logger = LogFactory.getLog(getClass());

    /**
     * 构造函数，传递工作目录path,读取XML文件
     *
     * @param path
     */
    public XMLParse(String path) {

        this.path = path;

        try {
            //判断目录是否为空
            if (path == null) {
          //      _logger.info(path + " 目录不存在或者文件不存在！");

            } else {
                _path_file = new File(path).getAbsoluteFile();
                if (!_path_file.isFile()) {
            //        _logger.info(_path_file + " 不是文件！");
                }
            }

            SAXReader reader = new SAXReader();
            document = reader.read(_path_file);
            root = document.getRootElement();

        } catch (DocumentException ex) {
            Logger.getLogger(XMLParse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 写入instanceClass和instanceName
     *
     * @param node
     * @param module
     */
    private void parseClassAndName(Node node, ModuleDescription module) {
        String instanceClass;
        String instanceName;

        try {
            Element element = (Element) node;

            instanceClass = element.attributeValue("class");
            instanceName = element.attributeValue("name");

            module.setInstanceClass(instanceClass);
            module.setInstanceName(instanceName);
            module.setEle(element);
        } catch (Exception ex) {
            Logger.getLogger(XMLParse.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }
    
    /**
     * 通过任意Node获得其变量对象
     *
     * @return VariableDescription
     */
    private VariableDescription getVariableNode(Node node) {
        String VariableName;
        String VariableAtt;
        String VariableValue;
        String VariableType;
        String VariableContext;
        String VariableSize;
        VariableDescription variableDes = new VariableDescription();

        try {
            Element element = (Element) node;
            VariableName = element.attributeValue(VARIABLE_NAME);
            VariableAtt = element.attributeValue(VARIABLE_ATTRIBUTE);
            VariableValue = element.attributeValue(VARIABLE_VALUE);
            VariableType = element.attributeValue(VARIABLE_TYPE);
            VariableContext = element.attributeValue(VARIABLE_CONTEXT);
            VariableSize = element.attributeValue(VARIABLE_SIZE);

            variableDes = new VariableDescription(VariableAtt, VariableName, VariableValue, VariableType, VariableContext, VariableSize);
        } catch (Exception ex) {
            Logger.getLogger(XMLParse.class.getName()).log(Level.SEVERE, null, ex);
        }

        return variableDes;
    }
    
    /**
     * 获得模块节点的模块对象
     *
     * @param node
     * @return
     * @throws Exception
     */
    private ModuleDescription getModuleNode(Element node) {
        ModuleDescription module = new ModuleDescription();

        try {
            parseClassAndName(node, module);
            parseModuleChildren(node, module);
        } catch (Exception ex) {
            Logger.getLogger(XMLParse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return module;
    }
    
    /**
     * 获得容器节点的容器对象
     *
     * @param node
     * @return
     * @throws Exception
     */
    private ContextDescription getContextNode(Element node) {
        ContextDescription context = new ContextDescription();

        try {
            parseClassAndName(node, context);
            parseContextChildren(node, context);
        } catch (Exception ex) {
            Logger.getLogger(XMLParse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return context;
    }

    /**
     * 获得模型节点的容器对象
     *
     * @param node
     * @return
     * @throws Exception
     */
    private ContextDescription getModelNode(Element root) {
        ContextDescription model = new ModelDescription();
        try {
            parseClassAndName(root, model);
            parseContextChildren(root, model);
        } catch (Exception ex) {
            Logger.getLogger(XMLParse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }

    /**
     * 分析模块下的孩子节点
     *
     * @param node
     * @param module
     * @return
     * @throws Exception
     */
    private void parseModuleChildren(Element node, ModuleDescription module) {
        //@SuppressWarnings("unchecked")
        List<Element> childrenElements = node.elements();

        try {
            for (Element element : childrenElements) {
                Description description = parseNode(element);
                if ("var".equals(element.getName())) {
                    module.addVariable((VariableDescription) description);
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(XMLParse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 分析容器下的孩子节点
     *
     * @param node
     * @param context
     * @return
     * @throws Exception
     */
    private void parseContextChildren(Element node, ContextDescription context) {
        //@SuppressWarnings("unchecked")
        List<Element> childrenElements = node.elements();

        try {
            for (Element element : childrenElements) {
                
                //分析下级节点
                Description description = parseNode(element);
                
                if ("var".equals(element.getName())) {
                    context.addVariable((VariableDescription) description);
                }
                if ("module".equals(element.getName())) {
                    context.addModule((ModuleDescription) description);
                }
                if ("context".equals(element.getName())) {
                    context.addModule((ContextDescription) description);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(XMLParse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 分析所有节点
     *
     * @param node
     * @return
     * @throws Exception
     */
    private Description parseNode(Element node) {
        String nodeType = node.getName();

        if ("model".equals(nodeType)) {
            return getModelNode(node);
        }
        if ("context".equals(nodeType)) {
            return getContextNode(node);
        }
        if ("module".equals(nodeType)) {
            return getModuleNode(node);
        }
        if ("var".equals(nodeType)) {
            return getVariableNode(node);
        } else {
            return null;
        }
    }

    /**
     * 获得模型对象
     *
     * @return ModelDescription
     * @throws Exception
     */
    public ModelDescription getModelDescription() {
        
        ModelDescription modelDes = new ModelDescription();

        try {
            if (document != null) {
                //Description model = parseNode(document.getRootElement());
                Description model = parseNode(root);
                if (model instanceof ModelDescription) {
                    modelDes = (ModelDescription) model;
                    modelDes.setDoc(document);
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(XMLParse.class.getName()).log(Level.SEVERE, null, ex);
        }

        return modelDes;
    }

}
