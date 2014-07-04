package hello.client.springcli;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.springframework.boot.cli.command.OptionParsingCommand;
import org.springframework.boot.cli.command.options.OptionHandler;
import org.springframework.boot.cli.command.status.ExitStatus;
import org.springframework.boot.cli.util.Log;

public class AbstractApplicationCommand extends OptionParsingCommand {

	protected AbstractApplicationCommand(String name, String description, OptionHandler handler) {
		super(name, description, handler);
	}

	protected abstract static class ApplicationOptionHandler extends OptionHandler {

		@Override
		protected final ExitStatus run(OptionSet options) throws Exception {
			try {
				runApplication(options);
			} catch (Exception e) {
				Log.error(e.getMessage());
				return ExitStatus.ERROR;
			}
			return ExitStatus.OK;
		}

		protected abstract void runApplication(OptionSet options) throws Exception;

		protected boolean isFlagOn(OptionSet options, OptionSpec<Boolean> option) {
			return options.has(option) ? options.valueOf(option) : false;
		}

	}

}
