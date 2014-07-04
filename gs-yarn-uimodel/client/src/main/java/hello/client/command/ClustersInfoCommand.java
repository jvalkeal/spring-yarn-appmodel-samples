package hello.client.command;

import java.util.List;
import java.util.Properties;

import org.kohsuke.args4j.Option;
import org.springframework.yarn.boot.app.YarnContainerClusterApplication;

public class ClustersInfoCommand extends AbstractYarnCommand {

	public final static String CMD = "CLUSTERSINFO";

	@Option(name = "-a", aliases = { "--application-id" }, usage = "Yarn Application id")
	protected String applicationId;

	@Override
	public String execute(List<String> appArgs) {
		YarnContainerClusterApplication app = new YarnContainerClusterApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERSINFO");
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
				applicationId);
		app.appProperties(appProperties);
		return app.run(appArgs.toArray(new String[0]));
	}

}
