package hello.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.springframework.util.StringUtils;
import org.springframework.yarn.boot.app.YarnInfoApplication;
import org.springframework.yarn.boot.app.YarnKillApplication;
import org.springframework.yarn.boot.app.YarnPushApplication;
import org.springframework.yarn.boot.app.YarnSubmitApplication;

public class ClientApplication {

	@Option(name = "--operation", required = true)
	private Operation operation;

	private void doMain(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);

		// we control arguments so it's safe to assume that
		// first two are --operation and xxx, rest
		// are passed to actual running application
		// and appArgs are parsed again.
		List<String> mainArgs = Arrays.asList(Arrays.copyOfRange(args, 0, Math.min(2, args.length)));
		List<String> appArgs = new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(args, Math.min(2, args.length),
				args.length)));

		// get operation to know what to do
		try {
			parser.parseArgument(mainArgs);
		}
		catch (CmdLineException e) {
			// we should not get here
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
		}

		// run actual app based on given operation
		try {
			if (Operation.INSTALL.equals(operation)) {
				InstallOptions options = new InstallOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				}
				else {
					doInstall(options, appArgs);
				}
			}
			else if (Operation.SUBMIT.equals(operation)) {
				SubmitOptions options = new SubmitOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				}
				else {
					doSubmit(options, appArgs);
				}
			}
			else if (Operation.KILL.equals(operation)) {
				KillOptions options = new KillOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				}
				else {
					doKill(options, appArgs);
				}
			}
			else if (Operation.LISTSUBMITTED.equals(operation)) {
				ListSubmittedOptions options = new ListSubmittedOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				}
				else {
					doListSubmitted(options, appArgs);
				}
			}
			else if (Operation.LISTINSTALLED.equals(operation)) {
				ListInstalledOptions options = new ListInstalledOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				}
				else {
					doListInstalled(appArgs);
				}
			}
			else {
				throw new IllegalArgumentException("Operation " + operation + " not valid");
			}
		}
		catch (CmdLineException e) {
			parser.printUsage(System.err);
			System.err.println();
			System.exit(1);
		}
		catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			if (rootCause != null && StringUtils.hasText(rootCause.getMessage())) {
				System.err.println("Command failed: " + rootCause.getMessage());
			}
			else {
				// something we didn't expect
				// so print out stack trace
				e.printStackTrace();
			}
			System.exit(1);
		}
	}

	private void doInstall(InstallOptions options, List<String> appArgs) {
		YarnPushApplication app = new YarnPushApplication();
		app.applicationVersion(options.instanceId);

		Properties instanceProperties = new Properties();
		instanceProperties.setProperty("spring.yarn.applicationId", options.instanceId);
		app.configFile("application.properties", instanceProperties);

		app.run(appArgs.toArray(new String[0]));
		System.out.println("New instance " + options.instanceId + " installed");
	}

	private void doSubmit(SubmitOptions options, List<String> appArgs) {
		YarnSubmitApplication app = new YarnSubmitApplication();
		app.applicationVersion(options.instanceId);
		ApplicationId applicationId = app.run(appArgs.toArray(new String[0]));
		System.out.println("New instance submitted with id " + applicationId);
	}

	private void doKill(KillOptions options, List<String> appArgs) {
		YarnKillApplication app = new YarnKillApplication();

		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.YarnKillApplication.applicationId", options.applicationId);
		app.appProperties(appProperties);

		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

	private void doListInstalled(List<String> appArgs) {
		YarnInfoApplication app = new YarnInfoApplication();

		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.operation", "INSTALLED");
		app.appProperties(appProperties);

		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

	private void doListSubmitted(ListSubmittedOptions options, List<String> appArgs) {
		YarnInfoApplication app = new YarnInfoApplication();

		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.operation", "SUBMITTED");
		if (options.verbose) {
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.verbose", "true");
		}
		if (StringUtils.hasText(options.type)) {
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.type", options.type);
		}
		else {
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.type", "GS");
		}
		app.appProperties(appProperties);

		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

	public static void main(String[] args) {
		new ClientApplication().doMain(args);
	}

	private static class InstallOptions {

		@Option(name = "-h", aliases = { "--help" }, usage = "Print this help")
		private boolean help;

		@Option(name = "-i", aliases = { "--instance-id" }, usage = "Instance id of the application, defaults to 'app'")
		private String instanceId = "app";
	}

	private static class SubmitOptions {

		@Option(name = "-h", aliases = { "--help" }, usage = "Print this help")
		private boolean help;

		@Option(name = "-i", aliases = { "--instance-id" }, usage = "Instance id of the application, defaults to 'app'")
		private String instanceId = "app";
	}

	private static class KillOptions {

		@Option(name = "-h", aliases = { "--help" }, usage = "Print this help")
		private boolean help;

		@Option(name = "-a", aliases = { "--application-id" }, usage = "Yarn Application id")
		private String applicationId;
	}

	private static class ListSubmittedOptions {

		@Option(name = "-h", aliases = { "--help" }, usage = "Print this help")
		private boolean help;

		@Option(name = "-v", aliases = { "--verbose" }, usage = "Verbose output")
		private boolean verbose;

		@Option(name = "-t", aliases = { "--type" }, usage = "Yarn application type")
		private String type;
	}

	private static class ListInstalledOptions {

		@Option(name = "-h", aliases = { "--help" }, usage = "Print this help")
		private boolean help;
	}

	private enum Operation {
		INSTALL,
		SUBMIT,
		KILL,
		LISTINSTALLED,
		LISTSUBMITTED
	}

}
