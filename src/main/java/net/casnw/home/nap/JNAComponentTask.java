//@DECLARE@
package net.casnw.home.nap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * @author myf@lzb.ac.cn
 * @since 2013-08-13
 * @version 1.0
 *
 */
public class JNAComponentTask extends Task {

    private List<FileSet> filesets = new ArrayList<FileSet>();
    private String destDir; //产生的java文件的相对路径
    private String dllName; //dll的名字
    protected final Log _logger = LogFactory.getLog(getClass());

    public void addFileset(FileSet fileset) {
        filesets.add(fileset);
    }

    public void setDllName(String dllName) {
        this.dllName = dllName;
    }

    /**
     * @param destDir the destDir to set
     */
    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }

    @Override
    public void execute() throws BuildException {
        _logger.info(this.getClass().getName() + " execute() is begin");
        if (filesets.size() < 1) {
            throw new BuildException("No 'fileset'(s).");
        }
        if (destDir == null) {
            throw new BuildException("No 'destdir'");
        }

        String userDir = System.getProperty("user.dir");
        Project prj = new Project();
        prj.setBasedir(userDir);
        for (FileSet fs : filesets) {

            DirectoryScanner ds = fs.getDirectoryScanner(prj);
            File baseDir = ds.getBasedir();
            userDir = userDir.replaceAll("\\\\", "/");
            String[] userDirs = userDir.split("/");
            String projectName = userDirs[userDirs.length - 1].trim();
            if ("".equalsIgnoreCase(dllName)) {
                dllName = projectName;
            }

            for (String incFile : ds.getIncludedFiles()) {
                File srcFile = new File(baseDir, incFile);
                JNAFortran ah = new JNAFortran();
                ah.setPackageName(incFile);
                ah.setSrcFile(srcFile);//fortran 源文件
                ah.setLibname(dllName);
                ah.setGenDestDir(destDir);
                try {
                    ah.execute();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(JNAComponentTask.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(JNAComponentTask.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(JNAComponentTask.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        _logger.info(this.getClass().getName() + " execute() is end");
    }
}
