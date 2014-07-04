package hello.client.springcli;

import java.util.Properties;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.springframework.boot.cli.util.Log;
import org.springframework.yarn.boot.app.YarnInfoApplication;

public class SubmittedCommand extends AbstractApplicationCommand {

	public SubmittedCommand() {
		super("submitted", "List submitted applications", new SubmittedOptionHandler());
	}

	@Override
	public String getUsageHelp() {
		return "[options]";
	}

	private static final class SubmittedOptionHandler extends ApplicationOptionHandler {

		private OptionSpec<String> typeOption;

		private OptionSpec<Boolean> verboseOption;

		@Override
		protected final void options() {
			typeOption = option("type", "Specify Yarn Application Type").withOptionalArg().defaultsTo("GS");
			verboseOption = option("verbose", "Verbose Output").withOptionalArg().ofType(Boolean.class)
					.defaultsTo(true);
		}

		@Override
		protected void runApplication(OptionSet options) throws Exception {
			YarnInfoApplication app = new YarnInfoApplication();
			Properties appProperties = new Properties();
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.operation", "SUBMITTED");
			if (isFlagOn(options, verboseOption)) {
				appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.verbose", "true");
			}
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.type", options.valueOf(typeOption));
			app.appProperties(appProperties);
			String info = app.run(new String[0]);
			Log.info(info);
		}

	}

}
