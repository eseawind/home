//@DECLARE@
package net.casnw.home.runtime;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.casnw.home.model.AbsComponent;
/**
 * HOME框架的主入口
 *
 * @author myf@lzb.ac.cn
 * @since 2013-03-13
 * @version 1.0
 */
public class HOMERun {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //args = new String[1];
       // args[0] = "D:\\test\\HOME New\\prms4.hom";
       // args[0] = "D:\\test\\HOME New\\thornthwaite.hom";
       //  args[0] = "D:\\test\\HOME New\\fortran_test.hom";
      //  args[0] = "D:\\test\\HOME New\\gbhm_iter4.hom";
       // args[0] = "D:\\test\\HOME New\\gbhm_hourly.hom";
       // args[0] = "gbhm.hom";
        
        //     args[0] = "D:\\test\\HOME develop\\PRMS3\\prmsspatial.hom";
        String propertiesFile = "";
        if (args != null && !"".equalsIgnoreCase(args[0])) {
            propertiesFile = args[0];
        }
        Runtime rt;
        try {
            System.out.println("propertiesFile==" + propertiesFile);
            rt = new Runtime(propertiesFile);
            rt.loadModel();
            rt.runModel();
            rt.clearModel();
        } catch (Exception ex) {
            System.out.printf("main error:" + ex.getLocalizedMessage());
        }

    }
}
