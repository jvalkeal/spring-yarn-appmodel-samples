package hello.client.springcli;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.springframework.boot.cli.util.Log;
import org.springframework.util.Assert;
import org.springframework.yarn.boot.app.YarnPushApplication;

public class PushCommand extends AbstractApplicationCommand {

	public PushCommand() {
		super("push", "Push Application", new PushOptionHandler());
	}

	private static final class PushOptionHandler extends ApplicationOptionHandler {

		private OptionSpec<String> applicationVersionOption;

		@Override
		protected final void options() {
			this.applicationVersionOption = option("application-version", "Specify Yarn Application Id").withRequiredArg();
		}

		@Override
		protected void runApplication(OptionSet options) throws Exception {
			List<?> nonOptionArguments = new ArrayList<Object>(options.nonOptionArguments());
			Assert.isTrue(nonOptionArguments.size() == 1, "Application Version must be defined");
			String appVersion = options.valueOf(applicationVersionOption);
			YarnPushApplication app = new YarnPushApplication();
			app.applicationVersion(appVersion);
			Properties instanceProperties = new Properties();
			instanceProperties.setProperty("spring.yarn.applicationVersion", appVersion);
			app.configFile("application.properties", instanceProperties);
			Log.info("New instance " + appVersion + " installed");
		}

	}

}
