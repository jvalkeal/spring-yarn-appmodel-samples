package hello.client.command;

import java.util.List;
import java.util.Properties;

import org.kohsuke.args4j.Option;
import org.springframework.yarn.boot.app.YarnPushApplication;

public class PushCommand extends AbstractYarnCommand {

	public final static String CMD = "PUSH";

	@Option(name = "-a", aliases = { "--application-version" }, usage = "Application version of the application, defaults to 'app'")
	private String applicationVersion = "app";

	@Override
	public String execute(List<String> appArgs) {
		YarnPushApplication app = new YarnPushApplication();
		app.applicationVersion(applicationVersion);
		Properties instanceProperties = new Properties();
		instanceProperties.setProperty("spring.yarn.applicationVersion", applicationVersion);
		app.configFile("application.properties", instanceProperties);

		app.run(appArgs.toArray(new String[0]));
		return "New instance " + applicationVersion + " installed";
	}

}
