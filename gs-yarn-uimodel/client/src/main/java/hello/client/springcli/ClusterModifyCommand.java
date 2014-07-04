package hello.client.springcli;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.springframework.boot.cli.util.Log;
import org.springframework.util.Assert;
import org.springframework.yarn.boot.app.YarnContainerClusterApplication;

public class ClusterModifyCommand extends AbstractApplicationCommand {

	public ClusterModifyCommand() {
		super("clusterstart", "Start Cluster", new ClusterModifyOptionHandler());
	}

	private static final class ClusterModifyOptionHandler extends ApplicationOptionHandler {

		private OptionSpec<String> applicationIdOption;

		private OptionSpec<String> clusterIdOption;

		private OptionSpec<String> projectionOption;

		@Override
		protected final void options() {
			this.applicationIdOption = option("application-id", "Specify Yarn Application Id").withRequiredArg();
			this.clusterIdOption = option("cluster-id", "Specify Cluster Id").withRequiredArg();
			this.projectionOption = option("projection-any", "Specify Any Count").withRequiredArg();
		}

		@Override
		protected void runApplication(OptionSet options) throws Exception {
			List<?> nonOptionArguments = new ArrayList<Object>(options.nonOptionArguments());
			Assert.isTrue(nonOptionArguments.size() == 2, "Cluster Id and Application Id must be defined");
			YarnContainerClusterApplication app = new YarnContainerClusterApplication();
			Properties appProperties = new Properties();
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERMODIFY");
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
					options.valueOf(applicationIdOption));
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
					options.valueOf(clusterIdOption));
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.projectionDataAny",
					options.valueOf(projectionOption));
			app.appProperties(appProperties);
			String info = app.run(new String[0]);
			Log.info(info);
		}

	}

}
