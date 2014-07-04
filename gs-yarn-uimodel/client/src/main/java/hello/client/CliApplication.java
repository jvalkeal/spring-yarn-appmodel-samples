package hello.client;

import hello.client.springcli.AbstractCli;
import hello.client.springcli.ClusterCreateCommand;
import hello.client.springcli.ClusterDestroyCommand;
import hello.client.springcli.ClusterInfoCommand;
import hello.client.springcli.ClusterModifyCommand;
import hello.client.springcli.ClusterStartCommand;
import hello.client.springcli.ClusterStopCommand;
import hello.client.springcli.ClustersInfoCommand;
import hello.client.springcli.KillCommand;
import hello.client.springcli.PushCommand;
import hello.client.springcli.PushedCommand;
import hello.client.springcli.SubmitCommand;
import hello.client.springcli.SubmittedCommand;

public class CliApplication extends AbstractCli {

	public static void main(String... args) {
		CliApplication clientApplication = new CliApplication();
		clientApplication.registerCommand(new PushCommand());
		clientApplication.registerCommand(new PushedCommand());
		clientApplication.registerCommand(new SubmitCommand());
		clientApplication.registerCommand(new SubmittedCommand());
		clientApplication.registerCommand(new KillCommand());
		clientApplication.registerCommand(new ClustersInfoCommand());
		clientApplication.registerCommand(new ClusterInfoCommand());
		clientApplication.registerCommand(new ClusterCreateCommand());
		clientApplication.registerCommand(new ClusterStartCommand());
		clientApplication.registerCommand(new ClusterStopCommand());
		clientApplication.registerCommand(new ClusterModifyCommand());
		clientApplication.registerCommand(new ClusterDestroyCommand());
		clientApplication.doMain(args);
	}

}
