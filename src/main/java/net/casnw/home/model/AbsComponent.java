//@DECLARE@
package net.casnw.home.model;

/**
 * 抽象模块
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-10
 * @version 1.0
 *
 */
public abstract class AbsComponent implements Componentable {

    private Contextable _context = null;
    private Modelable _model = null;
    private String _name = getClass().getName();

    @Override
    public void init() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void run() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void clear() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public Modelable getModel() {
        // TODO Auto-generated method stub

        return this._model;

    }

    @Override
    public Contextable getContext() {
        // TODO Auto-generated method stub
        return this._context;
    }

    @Override
    public void setModel(Modelable model) {
        if (model != null) {
            _model = model;
        }
    }

    @Override
    public void setContext(Contextable context) {
        if (context != null) {
            _context = context;
        }

    }

    @Override
    public void setInstanceName(String name) {
        _name = name;
    }

    @Override
    public String getInstanceName() {
        return _name;
    }
}
