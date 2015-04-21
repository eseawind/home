//@DECLARE@
package net.casnw.home.io;

import org.dom4j.Document;

/**
 * 模型的描述
 *
 * @author 罗立辉
 * @since 2013-04-09
 * @version 1.0
 */
public class ModelDescription extends ContextDescription {

   // private List<ContextDescription> contextDes = new ArrayList<>();
    private Document doc;

    /**
     * @return the doc
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * @param doc the doc to set
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }

    
}
