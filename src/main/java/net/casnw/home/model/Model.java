//@DECLARE@
package net.casnw.home.model;

import java.util.List;
import net.casnw.home.meta.ModuleMeta;
import net.casnw.home.runtime.Runtimeable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ModuleMeta(name = "Model",
        author = "home",
        version = "1.0",
        keyword = "model",
        description = "a new model")
public class Model extends Context implements Modelable {

    protected final Log _logger = LogFactory.getLog(getClass());
    private Runtimeable _runtime = null;
    //private Contextable _context = null;
    //private String name = "";

    @Override
    public Runtimeable getRuntime() {
        // TODO Auto-generated method stub
        return this._runtime;
    }

    @Override
    public void setRuntime(Runtimeable runtime) {
        this._runtime = runtime;
    }

    @Override
    public void init() throws Exception {
        _logger.info(" model init begin");
        super.init();
        _logger.info(" model init end");

    }

    @Override
    public void run() throws Exception {
        _logger.info(" model run begin");
        super.run();
        _logger.info(" model run end ");
    }

    @Override
    public void clear() throws Exception {
        _logger.info(" model clear begin");
        super.clear();
        _logger.info(" model clear end");
    }

    @Override
    public Modelable getModel() {
        return this;
    }

    /* public String getName() {      
     return this.getInstanceName();
     }
    
     public void setName(String name) {
     this.name = name;
     this.setInstanceName(name);
     }*/
}
