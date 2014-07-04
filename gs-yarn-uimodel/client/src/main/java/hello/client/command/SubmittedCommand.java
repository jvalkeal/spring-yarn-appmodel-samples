package hello.client.command;

import java.util.List;
import java.util.Properties;

import org.kohsuke.args4j.Option;
import org.springframework.yarn.boot.app.YarnInfoApplication;

public class SubmittedCommand extends AbstractYarnCommand {

	public final static String CMD = "SUBMITTED";

	@Option(name = "-v", aliases = { "--verbose" }, usage = "Verbose output")
	private boolean verbose;

	@Option(name = "-t", aliases = { "--type" }, usage = "Yarn application type")
	private String type = "GS";

	public String execute(List<String> appArgs) {
		YarnInfoApplication app = new YarnInfoApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.operation", "SUBMITTED");
		if (verbose) {
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.verbose", "true");
		}
		appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.type", getApplicationType());
		app.appProperties(appProperties);
		return app.run(appArgs.toArray(new String[0]));
	}

	protected String getApplicationType() {
		return type;
	}

}
