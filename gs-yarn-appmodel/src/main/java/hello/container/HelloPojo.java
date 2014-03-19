package hello.container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.yarn.annotation.OnYarnContainerStart;
import org.springframework.yarn.annotation.YarnContainer;

@YarnContainer
public class HelloPojo {

	private static final Log log = LogFactory.getLog(HelloPojo.class);

	@Autowired
	private Configuration configuration;

	@OnYarnContainerStart
	public void publicVoidNoArgsMethod() throws Exception {
		log.info("Hello from HelloPojo");
		log.info("About to list from hdfs root content");

		@SuppressWarnings("resource")
		FsShell shell = new FsShell(configuration);
		for (FileStatus s : shell.ls(false, "/")) {
			log.info(s);
		}

		log.info("Sleep 60 sec");
		Thread.sleep(60000);
		log.info("Sleep done, bye");
	}

}
