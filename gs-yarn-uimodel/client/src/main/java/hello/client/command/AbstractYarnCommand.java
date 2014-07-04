package hello.client.command;

import java.util.List;

import org.kohsuke.args4j.Option;

public abstract class AbstractYarnCommand implements YarnCommand {

	@Option(name = "-h", aliases = { "--help" }, usage = "Print this help")
	protected boolean help;

	@Override
	public abstract String execute(List<String> appArgs);

	@Override
	public boolean isHelp() {
		return help;
	}

}
