package hello.client.springcli;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.springframework.boot.cli.util.Log;
import org.springframework.util.Assert;
import org.springframework.yarn.boot.app.YarnContainerClusterApplication;

public class ClustersInfoCommand extends AbstractApplicationCommand {

	public ClustersInfoCommand() {
		super("clustersinfo", "List clusters", new ClustersInfoOptionHandler());
	}

	private static final class ClustersInfoOptionHandler extends ApplicationOptionHandler {

		private OptionSpec<String> applicationIdOption;

		@Override
		protected final void options() {
			this.applicationIdOption = option("application-id", "Specify Yarn Application Id").withRequiredArg();
		}

		@Override
		protected void runApplication(OptionSet options) throws Exception {
			List<?> nonOptionArguments = new ArrayList<Object>(options.nonOptionArguments());
			Assert.isTrue(nonOptionArguments.size() == 1, "Application Id must be defined");
			YarnContainerClusterApplication app = new YarnContainerClusterApplication();
			Properties appProperties = new Properties();
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERSINFO");
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
					options.valueOf(applicationIdOption));
			app.appProperties(appProperties);
			String info = app.run(new String[0]);
			Log.info(info);
		}

	}

}
