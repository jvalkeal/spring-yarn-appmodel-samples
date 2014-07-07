/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

		CommandRunner runner = new CommandRunner(getMainCommandName());
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

	protected String getMainCommandName() {
		return "java -jar <jar>";
	}

}
