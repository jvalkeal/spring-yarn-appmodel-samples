package hello.client.command;

import java.util.List;
import java.util.Properties;

import org.kohsuke.args4j.Option;
import org.springframework.yarn.boot.app.YarnContainerClusterApplication;

public class ClusterCreateCommand extends AbstractYarnCommand {

	public final static String CMD = "CLUSTERCREATE";

	@Option(name = "-a", aliases = { "--application-id" }, usage = "Yarn Application id")
	protected String applicationId;

	@Option(name = "-c", aliases = { "--cluster-id" }, usage = "Cluster id")
	private String clusterId;

	@Option(name = "-t", aliases = { "--projection-type" }, usage = "Cluster projection type")
	private String projectionType;

	@Option(name = "-d", aliases = { "--projection-any" }, usage = "Cluster projection any data")
	private String projectionAny;

	@Override
	public String execute(List<String> appArgs) {
		YarnContainerClusterApplication app = new YarnContainerClusterApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERCREATE");
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
				applicationId);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
				clusterId);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.projectionType",
				projectionType);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.projectionDataAny",
				projectionAny);
		app.appProperties(appProperties);
		return app.run(appArgs.toArray(new String[0]));
	}

}
