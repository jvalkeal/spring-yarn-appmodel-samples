package hello.client.springcli;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.cli.command.Command;
import org.springframework.boot.cli.command.CommandRunner;
import org.springframework.boot.cli.command.core.HelpCommand;
import org.springframework.boot.cli.command.core.VersionCommand;
import org.springframework.boot.loader.tools.LogbackInitializer;

public abstract class AbstractCli {

	private final List<Command> commands = new ArrayList<Command>();

	protected void registerCommand(Command command) {
		commands.add(command);
	}

	protected void doMain(String[] args) {
		System.setProperty("java.awt.headless", Boolean.toString(true));
		LogbackInitializer.initialize();

		CommandRunner runner = new CommandRunner("spring");
		runner.addCommand(new HelpCommand(runner));

		for (Command command : commands) {
			runner.addCommand(command);
		}
		runner.setOptionCommands(HelpCommand.class, VersionCommand.class);

		int exitCode = runner.runAndHandleErrors(args);
		if (exitCode != 0) {
			// If successful, leave it to run in case it's a server app
			System.exit(exitCode);
		}
	}

}
