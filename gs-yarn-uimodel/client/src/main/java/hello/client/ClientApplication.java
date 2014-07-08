package hello.client;

import org.springframework.yarn.boot.cli.AbstractCli;
import org.springframework.yarn.boot.cli.YarnClusterCreateCommand;
import org.springframework.yarn.boot.cli.YarnClusterDestroyCommand;
import org.springframework.yarn.boot.cli.YarnClusterInfoCommand;
import org.springframework.yarn.boot.cli.YarnClusterModifyCommand;
import org.springframework.yarn.boot.cli.YarnClusterStartCommand;
import org.springframework.yarn.boot.cli.YarnClusterStopCommand;
import org.springframework.yarn.boot.cli.YarnClustersInfoCommand;
import org.springframework.yarn.boot.cli.YarnKillCommand;
import org.springframework.yarn.boot.cli.YarnPushCommand;
import org.springframework.yarn.boot.cli.YarnPushedCommand;
import org.springframework.yarn.boot.cli.YarnSubmitCommand;
import org.springframework.yarn.boot.cli.YarnSubmittedCommand;

public class ClientApplication extends AbstractCli {

	public static void main(String... args) {
		ClientApplication app = new ClientApplication();
		app.registerCommand(new YarnPushCommand());
		app.registerCommand(new YarnPushedCommand());
		app.registerCommand(new YarnSubmitCommand());
		app.registerCommand(new YarnSubmittedCommand());
		app.registerCommand(new YarnKillCommand());
		app.registerCommand(new YarnClustersInfoCommand());
		app.registerCommand(new YarnClusterInfoCommand());
		app.registerCommand(new YarnClusterCreateCommand());
		app.registerCommand(new YarnClusterStartCommand());
		app.registerCommand(new YarnClusterStopCommand());
		app.registerCommand(new YarnClusterModifyCommand());
		app.registerCommand(new YarnClusterDestroyCommand());
		app.doMain(args);
	}

}
