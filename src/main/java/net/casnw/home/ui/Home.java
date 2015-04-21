package net.casnw.home.ui;

import net.casnw.home.runtime.Runtimeable;
import net.casnw.home.runtime.Runtime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * home命令行运行
 * @author 邵勇
 * @since 2013-03-25
 * @version 1.0
 */
public class Home {

	public static void main(String[] args) throws Exception{
		
		// 参数数目不对
		if (args.length!=0x01)
		{
			_logger.info("参数数目："+args.length);
			throw new Exception("home 参数错误，\nexample：java -jar home.jar thone/thone.properties");
		}
		
		// 新建runtime
		Runtimeable runtime = new Runtime(args[0x00]);
		runtime.loadModel();
		runtime.runModel();
	}
	
	// -------------------------------------------------------------------------------------------------------
	// log
	protected static final Log _logger = LogFactory.getLog(Home.class);
	// -------------------------------------------------------------------------------------------------------	
}
