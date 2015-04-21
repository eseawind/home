 //@DECLARE@
package net.casnw.home.model;

/**
 * 容器
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-10
 * @version 1.0
 *
 */
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.casnw.home.meta.ModuleMeta;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.casnw.home.poolData.*;
@ModuleMeta(name = "Context",
        author = "home",
        version = "1.0",
        keyword = "Context",
        description = "")
public class Context extends AbsComponent implements Contextable {

    protected final Log _logger = LogFactory.getLog(getClass());
    private List<Componentable> _componentList = new ArrayList<Componentable>();
    //变量对应关系列表
    Map<String, AttributeAccess> _attributeAccessMap = new HashMap<String, AttributeAccess>();
    private List<AttributeAccess> _attributeAccessList = new ArrayList<AttributeAccess>();
    //静态变量对应关系列表
    private List<AttributeAccess> _staAttriAccessList = new ArrayList<AttributeAccess>();
    //数据池
    private Map<String, Datable> _attributeMap = null;

    @Override
    public void init() throws Exception {
        _logger.info("context:" + this.getInstanceName() + " init begin");

        initAccessors();
        //initAllComponent();
        _logger.info("context:" + this.getInstanceName() + " init end");

    }

    public void initAllComponent() throws Exception {
        _logger.info("invoke the init function of component of context:" + this.getInstanceName() + "  begin");
        Iterator<Componentable> compIterator = _componentList.iterator();
        Componentable comp;
        while (compIterator.hasNext()) {
            comp = compIterator.next();

            _logger.info(comp.getClass().getName() + " init begin");
            comp.init();

        }
        _logger.info("invoke the init function of component of context::" + this.getInstanceName() + "   end");

    }

    @Override
    public void run() throws Exception {
        _logger.info("context:" + this.getInstanceName() + "  run begin");

        Iterator<Componentable> compIterator = _componentList.iterator();

        Componentable comp;
        while (compIterator.hasNext()) {
            comp = compIterator.next();
            _logger.info(comp.getClass().getName() + " run begin");
            comp.run();
            _logger.info(comp.getClass().getName() + " run end");
        }
        _logger.info("context:" + this.getInstanceName() + "  run end");
    }

    @Override
    public void clear() throws Exception {
        _logger.info("context:" + this.getInstanceName() + "  clear begin");
        Iterator<Componentable> compIterator = _componentList.iterator();
        Componentable comp;
        while (compIterator.hasNext()) {
            comp = compIterator.next();
            _logger.info(comp.getClass().getName() + " clear begin");
            comp.clear();
            _logger.info(comp.getClass().getName() + " clear end");
        }
        _logger.info("context:" + this.getInstanceName() + "  clear end");
    }

    @Override
    public void setModel(Modelable model) {
        if (model != null) {
            super.setModel(model);
        }
    }

    @Override
    public void addComponent(Componentable component) {

        if (component != null && !_componentList.contains(component)) {
            _componentList.add(component);
            component.setContext(this);
        }
    }

    @Override
    public void addAccess(AttributeAccess attributeAccess) {
        if (attributeAccess != null) {
            //判断是否有重复项
            if (!_attributeAccessList.contains(attributeAccess)) {
                _attributeAccessList.add(attributeAccess);
                _attributeAccessMap.put(attributeAccess.getAttributeName(), attributeAccess);
            }
        }
    }
    // AttributeAccess(String attributeName, Contextable context)

    @Override
    public void addStaticAccess(AttributeAccess attributeAccess) {
        if (attributeAccess != null) {
            //判断是否有重复项
            if (!_staAttriAccessList.contains(attributeAccess)) {
                _staAttriAccessList.add(attributeAccess);
            }
        }

    }

    @Override
    public List<AttributeAccess> getAttributeAccess() {
        return _attributeAccessList;
    }

    @Override
    public void initAccessors() {

        _logger.info("context:" + this.getInstanceName() + "  initAccessors begin");

        _attributeMap = new HashMap<String, Datable>();
        Iterator<AttributeAccess> attributeIterator = _attributeAccessList.iterator();
        AttributeAccess attributeAccess = null;
        AbsComponent comp = null;
        String attributeName = "";
        String compName = "";
        String dataType = "";
        //  Class type = null;
        Field[] fields;
        Field compField;
        Datable dataObject = null;
        Contextable context;
        boolean flag = false;

        while (attributeIterator.hasNext()) {
            String size = "";
            attributeAccess = attributeIterator.next();
            fields = null;

            _logger.info("getAttributeName:" + attributeAccess.getAttributeName());
                //处理context的变量
            //context的属性，非模块属性
            if ((attributeAccess.getComponent()) instanceof Context) {
                attributeName = attributeAccess.getAttributeName();
                context = attributeAccess.getContext();
                if (context == null) {
                    context = this;
                }
                    //context 的属性
                //fields = attributeAccess.getComponent().getClass().getDeclaredField(attributeName);
                flag = false;
                fields = attributeAccess.getComponent().getClass().getDeclaredFields();
                for (int k = 0; k < fields.length; k++) {
                    if (fields[k].getName().equalsIgnoreCase(attributeName)) {
                        try {
                            dataType = fields[k].getType().getSimpleName();
                            size = attributeAccess.getSize();
                            dataObject = getDataObject(dataType, attributeName, context, size);
                            fields[k].set(attributeAccess.getComponent(), dataObject);
                            flag = true;
                            break;
                        } catch (IllegalArgumentException ex) {

                            Logger.getLogger(Context.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {

                            Logger.getLogger(Context.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                if (flag == false) {
                    dataType = attributeAccess.getDataType();
                    size = attributeAccess.getSize();
                    dataObject = getDataObject(dataType, attributeName, context, size);
                }

                if (attributeAccess.getValue() != null && !"".equalsIgnoreCase(attributeAccess.getValue())) {
                    dataObject.setValue(attributeAccess.getValue());
                }

            } else {
                comp = (AbsComponent) attributeAccess.getComponent();
                attributeName = attributeAccess.getAttributeName();
                compName = attributeAccess.getCompName();
                context = attributeAccess.getContext();
                try {
                    compField = comp.getClass().getDeclaredField(compName);
                    dataType = compField.getType().getSimpleName();
                    size = attributeAccess.getSize();
                    if (context != null) {
                        dataObject = getDataObject(dataType, attributeName, context, size);
                    } else {
                        //处理静态变量
                        context = this;
                        for (int i = 0; i < _staAttriAccessList.size(); i++) {
                            String attriName = _staAttriAccessList.get(i).getAttributeName();
                            if (attriName.equalsIgnoreCase(attributeName)) {
                                context = _staAttriAccessList.get(i).getContext();
                            }
                        }

                        dataObject = getDataObject(dataType, attributeName, context, size);
                    }
                    compField.set(comp, dataObject);
                    if (attributeAccess.getValue() != null && !"".equalsIgnoreCase(attributeAccess.getValue())) {
                        dataObject.setValue(attributeAccess.getValue());
                    }
                } catch (NoSuchFieldException ex) {

                    _logger.info(" contex initAccessors error:" + ex.getMessage());
                } catch (SecurityException ex) {

                    _logger.info(" contex initAccessors error:" + ex.getMessage());
                } catch (IllegalArgumentException ex) {

                    _logger.info(" contex initAccessors error:" + ex.getMessage());
                } catch (IllegalAccessException ex) {

                    _logger.info(" contex initAccessors error:" + ex.getMessage());
                }
            }
        }

        _logger.info("context:" + this.getInstanceName() + "  initAccessors end");

    }

    private Datable getDataObject(String type, String attributeName, Contextable context, String size) {
        Datable dataObject = null;
        Map<String, Datable> attributeMap = context.getDataPool();

        if (attributeMap.containsKey(attributeName)) {
            dataObject = attributeMap.get(attributeName);
        } else {
            switch (type) {
                case "PoolInteger":
                    dataObject = new PoolInteger();
                    break;
                case "PoolDouble":
                    dataObject = new PoolDouble();
                    break;
                case "PoolFloat":
                    dataObject = new PoolFloat();
                    break;
                case "PoolLong":
                    dataObject = new PoolLong();
                    break;
                case "PoolString":
                    dataObject = new PoolString();
                    if (size != null && !"".equalsIgnoreCase(size)) {
                        ((PoolString) dataObject).length = Integer.parseInt(size);
                    }
                    break;
                case "PoolBoolean":
                    dataObject = new PoolBoolean();
                    break;
                case "PoolObject":
                    dataObject = new PoolObject();
                    break;
                case "PoolIntegerArray":
                    dataObject = new PoolIntegerArray();
                    if (size != null && !"".equalsIgnoreCase(size)) {
                        ((PoolIntegerArray) dataObject).length = Integer.parseInt(size);
                    }
                    break;
                case "PoolDoubleArray":
                    dataObject = new PoolDoubleArray();
                    if (size != null && !"".equalsIgnoreCase(size)) {
                        ((PoolDoubleArray) dataObject).length = Integer.parseInt(size);
                    }
                    break;
                case "PoolFloatArray":
                    dataObject = new PoolFloatArray();
                    if (size != null && !"".equalsIgnoreCase(size)) {
                        ((PoolFloatArray) dataObject).length = Integer.parseInt(size);
                    }
                    break;
                case "PoolLongArray":
                    dataObject = new PoolLongArray();
                    if (size != null && !"".equalsIgnoreCase(size)) {
                        ((PoolLongArray) dataObject).length = Integer.parseInt(size);
                    }
                    break;
                case "PoolBooleanArray":
                    dataObject = new PoolBooleanArray();
                    if (size != null && !"".equalsIgnoreCase(size)) {
                        ((PoolBooleanArray) dataObject).length = Integer.parseInt(size);
                    }
                    break;
                case "PoolStringArray":
                    dataObject = new PoolStringArray();
                    if (size != null && !"".equalsIgnoreCase(size)) {
                        ((PoolStringArray) dataObject).length = Integer.parseInt(size);
                    }
                    break;
                case "PoolObjectArray":
                    dataObject = new PoolObjectArray();
                    if (size != null && !"".equalsIgnoreCase(size)) {
                        ((PoolObjectArray) dataObject).length = Integer.parseInt(size);
                    }
                    break;
                case "PoolInteger2DArray":
                    dataObject = new PoolInteger2DArray();

                    if (size != null && size.split(",").length == 2) {
                        ((PoolInteger2DArray) dataObject).rowsNum = Integer.parseInt(size.split(",")[0]);
                        ((PoolInteger2DArray) dataObject).colsNum = Integer.parseInt(size.split(",")[1]);
                    }

                    break;
                case "PoolDouble2DArray":
                    dataObject = new PoolDouble2DArray();
                    if (size != null && size.split(",").length == 2) {
                        ((PoolDouble2DArray) dataObject).rowsNum = Integer.parseInt(size.split(",")[0]);
                        ((PoolDouble2DArray) dataObject).colsNum = Integer.parseInt(size.split(",")[1]);
                    }
                    break;
                case "PoolFloat2DArray":
                    dataObject = new PoolFloat2DArray();
                    if (size != null && size.split(",").length == 2) {
                        ((PoolFloat2DArray) dataObject).rowsNum = Integer.parseInt(size.split(",")[0]);
                        ((PoolFloat2DArray) dataObject).colsNum = Integer.parseInt(size.split(",")[1]);
                    }
                    break;
                case "PoolLong2DArray":
                    dataObject = new PoolLong2DArray();
                    if (size != null && size.split(",").length == 2) {
                        ((PoolLong2DArray) dataObject).rowsNum = Integer.parseInt(size.split(",")[0]);
                        ((PoolLong2DArray) dataObject).colsNum = Integer.parseInt(size.split(",")[1]);
                    }
                    break;
                case "PoolBoolean2DArray":
                    dataObject = new PoolBoolean2DArray();
                    if (size != null && size.split(",").length == 2) {
                        ((PoolBoolean2DArray) dataObject).rowsNum = Integer.parseInt(size.split(",")[0]);
                        ((PoolBoolean2DArray) dataObject).colsNum = Integer.parseInt(size.split(",")[1]);
                    }
                    break;
                case "PoolString2DArray":
                    dataObject = new PoolString2DArray();
                    if (size != null && size.split(",").length == 2) {
                        ((PoolString2DArray) dataObject).rowsNum = Integer.parseInt(size.split(",")[0]);
                        ((PoolString2DArray) dataObject).colsNum = Integer.parseInt(size.split(",")[1]);
                    }
                    break;
                case "PoolDouble3DArray":
                    dataObject = new PoolDouble3DArray();
                    break;
                case "PoolInteger3DArray":
                    dataObject = new PoolInteger3DArray();
                    break;
                case "PoolCalendar":
                    dataObject = new PoolCalendar();
                    break;
                case "PoolDate":
                    dataObject = new PoolDate();
                    break;
            }
            attributeMap.put(attributeName, dataObject);
            // _attributeMap.put(attributeName, dataObject);
        }
        return dataObject;
    }

    @Override
    public List<Componentable> getComponents() {
        return _componentList;
    }

    @Override
    public Map<String, Datable> getDataPool() {
        return _attributeMap;
    }

    @Override
    public Map<String, AttributeAccess> getAttributeAccessMap() {
        return _attributeAccessMap;
    }

    @Override
    public void setComponents(List<Componentable> components) {
        Iterator<Componentable> i = components.iterator();
        while (i.hasNext()) {
            i.next().setContext(this);
        }
        this._componentList = components;

    }

    @Override
    public Componentable getComponent(String name) {
        for (int i = 0; i < _componentList.size(); i++) {
            if (_componentList.get(i).getInstanceName().equals(name)) {
                return _componentList.get(i);
            }
            if (_componentList.get(i) instanceof Context) {
                Componentable comp = ((Context) _componentList.get(i)).getComponent(name);
                if (comp != null) {
                    return comp;
                }
            }
        }
        return null;
    }
}
