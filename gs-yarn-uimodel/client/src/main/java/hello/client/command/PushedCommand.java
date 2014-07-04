package hello.client.command;

import java.util.List;
import java.util.Properties;

import org.springframework.yarn.boot.app.YarnInfoApplication;

public class PushedCommand extends AbstractYarnCommand {

	public final static String CMD = "PUSHED";

	public String execute(List<String> appArgs) {
		YarnInfoApplication app = new YarnInfoApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.operation", "PUSHED");
		app.appProperties(appProperties);
		return app.run(appArgs.toArray(new String[0]));
	}

}
