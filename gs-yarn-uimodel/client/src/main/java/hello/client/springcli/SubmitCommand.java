package hello.client.springcli;

import java.util.ArrayList;
import java.util.List;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.springframework.boot.cli.util.Log;
import org.springframework.util.Assert;
import org.springframework.yarn.boot.app.YarnSubmitApplication;

public class SubmitCommand extends AbstractApplicationCommand {

	public SubmitCommand() {
		super("submit", "Submit Application", new SubmitOptionHandler());
	}

	private static final class SubmitOptionHandler extends ApplicationOptionHandler {

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
			YarnSubmitApplication app = new YarnSubmitApplication();
			app.applicationVersion(appVersion);
			ApplicationId applicationId = app.run(new String[0]);
			Log.info("New instance submitted with id " + applicationId);
		}

	}

}
