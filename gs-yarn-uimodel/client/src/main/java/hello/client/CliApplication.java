package hello.client;

import hello.client.springcli.AbstractCli;
import hello.client.springcli.YarnClusterCreateCommand;
import hello.client.springcli.YarnClusterDestroyCommand;
import hello.client.springcli.YarnClusterInfoCommand;
import hello.client.springcli.YarnClusterModifyCommand;
import hello.client.springcli.YarnClusterStartCommand;
import hello.client.springcli.YarnClusterStopCommand;
import hello.client.springcli.YarnClustersInfoCommand;
import hello.client.springcli.YarnKillCommand;
import hello.client.springcli.YarnPushCommand;
import hello.client.springcli.YarnPushedCommand;
import hello.client.springcli.YarnSubmitCommand;
import hello.client.springcli.YarnSubmittedCommand;

public class CliApplication extends AbstractCli {

	public static void main(String... args) {
		CliApplication clientApplication = new CliApplication();
		clientApplication.registerCommand(new YarnPushCommand());
		clientApplication.registerCommand(new YarnPushedCommand());
		clientApplication.registerCommand(new YarnSubmitCommand());
		clientApplication.registerCommand(new YarnSubmittedCommand());
		clientApplication.registerCommand(new YarnKillCommand());
		clientApplication.registerCommand(new YarnClustersInfoCommand());
		clientApplication.registerCommand(new YarnClusterInfoCommand());
		clientApplication.registerCommand(new YarnClusterCreateCommand());
		clientApplication.registerCommand(new YarnClusterStartCommand());
		clientApplication.registerCommand(new YarnClusterStopCommand());
		clientApplication.registerCommand(new YarnClusterModifyCommand());
		clientApplication.registerCommand(new YarnClusterDestroyCommand());
		clientApplication.doMain(args);
	}

}
