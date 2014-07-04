package hello.client.command;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;

public abstract class SubCommandLineRouter {

	@Option(name = "-h", aliases = { "--help" }, usage = "Print this help")
	public boolean help;

	protected void doMain(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
		CmdLineException e = null;
		try {
			parser.parseArgument(args);
		} catch (CmdLineException ee) {
			e = ee;
		}

		YarnCommand yarnCommand = getYarnCommand();
		System.out.println("yarnCommand=" + yarnCommand);
		if (help) {
			parser.printUsage(new OutputStreamWriter(System.out),null,OptionHandlerFilter.ALL);
			return;
		} else if (e != null) {
			System.err.println("Error parsing arguments: " + e.getMessage());
			parser.printUsage(System.err);
			System.exit(1);
		}

		if (yarnCommand != null) {
			if (yarnCommand.isHelp()) {
				CmdLineParser parser2 = new CmdLineParser(getYarnCommand());
				try {
					parser2.parseArgument(args);
				} catch (CmdLineException ee) {
					parser2.printUsage(System.out);
				}
			} else {
				// we're expecting first argument at this point to be a sub-command
				// so zap it away from boot args
				List<String> appArgs = new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(args, Math.min(1, args.length),
						args.length)));
				System.out.println(yarnCommand.execute(appArgs));
			}
		} else {
			System.err.println("No defined command");
		}

	}

	protected abstract YarnCommand getYarnCommand();

}
