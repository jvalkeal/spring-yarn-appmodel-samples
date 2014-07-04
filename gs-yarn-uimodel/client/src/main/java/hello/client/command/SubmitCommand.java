package hello.client.command;

import java.util.List;

import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.kohsuke.args4j.Option;
import org.springframework.yarn.boot.app.YarnSubmitApplication;

public class SubmitCommand extends AbstractYarnCommand {

	public final static String CMD = "SUBMIT";

	@Option(name = "-a", aliases = { "--application-version" }, usage = "Application version of the application, defaults to 'app'")
	private String applicationVersion = "app";

	@Override
	public String execute(List<String> appArgs) {
		YarnSubmitApplication app = new YarnSubmitApplication();
		app.applicationVersion(applicationVersion);
		ApplicationId applicationId = app.run(appArgs.toArray(new String[0]));
		return "New instance submitted with id " + applicationId;
	}

}
