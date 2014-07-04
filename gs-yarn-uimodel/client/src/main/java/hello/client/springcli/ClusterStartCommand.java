package hello.client.springcli;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.springframework.boot.cli.util.Log;
import org.springframework.util.Assert;
import org.springframework.yarn.boot.app.YarnContainerClusterApplication;

public class ClusterStartCommand extends AbstractApplicationCommand {

	public ClusterStartCommand() {
		super("clusterstart", "Start Cluster", new ClusterStartOptionHandler());
	}

	private static final class ClusterStartOptionHandler extends ApplicationOptionHandler {

		private OptionSpec<String> applicationIdOption;

		private OptionSpec<String> clusterIdOption;

		@Override
		protected final void options() {
			this.applicationIdOption = option("application-id", "Specify Yarn Application Id").withRequiredArg();
			this.clusterIdOption = option("cluster-id", "Specify Cluster Id").withRequiredArg();
		}

		@Override
		protected void runApplication(OptionSet options) throws Exception {
			List<?> nonOptionArguments = new ArrayList<Object>(options.nonOptionArguments());
			Assert.isTrue(nonOptionArguments.size() == 2, "Cluster Id and Application Id must be defined");
			YarnContainerClusterApplication app = new YarnContainerClusterApplication();
			Properties appProperties = new Properties();
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERSTART");
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
					options.valueOf(applicationIdOption));
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
					options.valueOf(clusterIdOption));
			app.appProperties(appProperties);
			String info = app.run(new String[0]);
			Log.info(info);
		}

	}

}
