package hello.client;

import hello.client.command.ClusterCreateCommand;
import hello.client.command.ClusterDestroyCommand;
import hello.client.command.ClusterInfoCommand;
import hello.client.command.ClusterModifyCommand;
import hello.client.command.ClusterStartCommand;
import hello.client.command.ClusterStopCommand;
import hello.client.command.ClustersInfoCommand;
import hello.client.command.KillCommand;
import hello.client.command.PushCommand;
import hello.client.command.PushedCommand;
import hello.client.command.SubCommandLineRouter;
import hello.client.command.SubmitCommand;
import hello.client.command.SubmittedCommand;
import hello.client.command.YarnCommand;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.spi.SubCommand;
import org.kohsuke.args4j.spi.SubCommandHandler;
import org.kohsuke.args4j.spi.SubCommands;

public class Old2ClientApplication extends SubCommandLineRouter {

	@Argument(handler = SubCommandHandler.class, required = true, usage = "CMD one of " + PushCommand.CMD + ", "
			+ PushedCommand.CMD + ", " + SubmitCommand.CMD + ", " + SubmittedCommand.CMD + ", " + KillCommand.CMD
			+ ", " + ClustersInfoCommand.CMD + ", " + ClusterInfoCommand.CMD + ", " + ClusterCreateCommand.CMD + ", "
			+ ClusterStartCommand.CMD + ", " + ClusterStopCommand.CMD + ", " + ClusterModifyCommand.CMD + ", "
			+ ClusterDestroyCommand.CMD)
	@SubCommands({ @SubCommand(name = PushCommand.CMD, impl = PushCommand.class),
			@SubCommand(name = PushedCommand.CMD, impl = PushedCommand.class),
			@SubCommand(name = SubmitCommand.CMD, impl = SubmitCommand.class),
			@SubCommand(name = SubmittedCommand.CMD, impl = SubmittedCommand.class),
			@SubCommand(name = KillCommand.CMD, impl = KillCommand.class),
			@SubCommand(name = ClustersInfoCommand.CMD, impl = ClustersInfoCommand.class),
			@SubCommand(name = ClusterInfoCommand.CMD, impl = ClusterInfoCommand.class),
			@SubCommand(name = ClusterCreateCommand.CMD, impl = ClusterCreateCommand.class),
			@SubCommand(name = ClusterStartCommand.CMD, impl = ClusterStartCommand.class),
			@SubCommand(name = ClusterStopCommand.CMD, impl = ClusterStopCommand.class),
			@SubCommand(name = ClusterModifyCommand.CMD, impl = ClusterModifyCommand.class),
			@SubCommand(name = ClusterDestroyCommand.CMD, impl = ClusterDestroyCommand.class) })
	private YarnCommand command;

	@Override
	protected YarnCommand getYarnCommand() {
		return command;
	}

//	public static void main(String[] args) {
//		ClientApplication app = new ClientApplication();
//		app.doMain(new String[]{"SUBMITTED", "-xxx"});
//	}

}
