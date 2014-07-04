package hello.client.springcli;

import java.util.Properties;

import org.springframework.boot.cli.command.AbstractCommand;
import org.springframework.boot.cli.command.status.ExitStatus;
import org.springframework.boot.cli.util.Log;
import org.springframework.yarn.boot.app.YarnInfoApplication;

public class PushedCommand extends AbstractCommand {

	public PushedCommand() {
		super("pushed", "list running applications");
	}

	@Override
	public ExitStatus run(String... args) throws Exception {
		YarnInfoApplication app = new YarnInfoApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.operation", "PUSHED");
		app.appProperties(appProperties);
		String info = app.run(args);
		Log.info(info);
		return ExitStatus.OK;
	}

}
