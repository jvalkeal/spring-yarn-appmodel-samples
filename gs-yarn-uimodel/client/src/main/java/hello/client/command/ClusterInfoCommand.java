package hello.client.command;

import java.util.List;
import java.util.Properties;

import org.kohsuke.args4j.Option;
import org.springframework.yarn.boot.app.YarnContainerClusterApplication;

public class ClusterInfoCommand extends AbstractYarnCommand {

	public final static String CMD = "CLUSTERINFO";

	@Option(name = "-a", aliases = { "--application-id" }, usage = "Yarn Application id")
	protected String applicationId;

	@Option(name = "-c", aliases = { "--cluster-id" }, usage = "Cluster id")
	private String clusterId;

	@Override
	public String execute(List<String> appArgs) {
		YarnContainerClusterApplication app = new YarnContainerClusterApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERINFO");
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
				applicationId);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
				clusterId);
		app.appProperties(appProperties);
		return app.run(appArgs.toArray(new String[0]));
	}

}
