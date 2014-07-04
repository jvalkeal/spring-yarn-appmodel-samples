package hello.client.command;

import java.util.List;
import java.util.Properties;

import org.kohsuke.args4j.Option;
import org.springframework.yarn.boot.app.YarnKillApplication;

public class KillCommand extends AbstractYarnCommand {

	public final static String CMD = "KILL";

	@Option(name = "-a", aliases = { "--application-id" }, required=true, usage = "Yarn Application id")
	private String applicationId;

	@Override
	public String execute(List<String> appArgs) {
		YarnKillApplication app = new YarnKillApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.YarnKillApplication.applicationId", applicationId);
		app.appProperties(appProperties);

		return app.run(appArgs.toArray(new String[0]));
	}

}
