package hello.container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.yarn.annotation.OnYarnContainerStart;
import org.springframework.yarn.annotation.YarnContainer;

@YarnContainer
public class HelloPojo {

	private static final Log log = LogFactory.getLog(HelloPojo.class);

	@Autowired
	private Configuration configuration;

	@OnYarnContainerStart
	public void publicVoidNoArgsMethod() throws Exception {
		log.info("Hello from HelloPojo.. sleeping");
		try {
			Thread.sleep(1000*60*30);
		} catch (Exception e) {
			log.info("Hello from HelloPojo.. interrupted");
		}
		log.info("Hello from HelloPojo.. sleep done");

//		log.info("XXX1 topology.script.file.name=" + configuration.get("topology.script.file.name"));
//		log.info("XXX2 net.topology.script.file.name=" + configuration.get("net.topology.script.file.name"));
//
//		Configuration cc = new Configuration(true);
//		log.info("XXX1 topology.script.file.name=" + cc.get("topology.script.file.name"));
//		log.info("XXX2 net.topology.script.file.name=" + cc.get("net.topology.script.file.name"));

//		log.info("About to list from hdfs root content");

//		@SuppressWarnings("resource")
//		FsShell shell = new FsShell(configuration);
//		for (FileStatus s : shell.ls(false, "/")) {
//			log.info(s);
//		}

//		log.info("Sleep 60 sec");
//		Thread.sleep(60000);
//		log.info("Sleep done, bye");
	}

}
