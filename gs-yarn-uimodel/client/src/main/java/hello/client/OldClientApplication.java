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
import org.springframework.yarn.boot.app.YarnContainerClusterApplication;
import org.springframework.yarn.boot.app.YarnInfoApplication;
import org.springframework.yarn.boot.app.YarnKillApplication;
import org.springframework.yarn.boot.app.YarnPushApplication;
import org.springframework.yarn.boot.app.YarnSubmitApplication;

public class OldClientApplication {

	@Option(name = "-o", aliases = { "--operation" }, required = true)
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
		} catch (CmdLineException e) {
			// we should not get here
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
		}

		// run actual app based on given operation
		try {
			if (Operation.PUSH.equals(operation)) {
				PushOptions options = new PushOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doPush(options, appArgs);
				}
			} else if (Operation.SUBMIT.equals(operation)) {
				SubmitOptions options = new SubmitOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doSubmit(options, appArgs);
				}
			} else if (Operation.KILL.equals(operation)) {
				KillOptions options = new KillOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doKill(options, appArgs);
				}
			} else if (Operation.SUBMITTED.equals(operation)) {
				SubmittedOptions options = new SubmittedOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doListSubmitted(options, appArgs);
				}
			} else if (Operation.PUSHED.equals(operation)) {
				PushedOptions options = new PushedOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doListPushed(appArgs);
				}
			} else if (Operation.CLUSTERSINFO.equals(operation)) {
				ClustersInfoOptions options = new ClustersInfoOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doClustersInfo(options, appArgs);
				}
			} else if (Operation.CLUSTERINFO.equals(operation)) {
				ClusterInfoOptions options = new ClusterInfoOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doClusterInfo(options, appArgs);
				}
			} else if (Operation.CLUSTERCREATE.equals(operation)) {
				ClusterCreateOptions options = new ClusterCreateOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doClusterCreate(options, appArgs);
				}
			} else if (Operation.CLUSTERDESTROY.equals(operation)) {
				ClusterDestroyOptions options = new ClusterDestroyOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doClusterDestroy(options, appArgs);
				}
			} else if (Operation.CLUSTERSTART.equals(operation)) {
				ClusterStartOptions options = new ClusterStartOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doClusterStart(options, appArgs);
				}
			} else if (Operation.CLUSTERSTOP.equals(operation)) {
				ClusterStopOptions options = new ClusterStopOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doClusterStop(options, appArgs);
				}
			} else if (Operation.CLUSTERMODIFY.equals(operation)) {
				ClusterModifyOptions options = new ClusterModifyOptions();
				parser = new CmdLineParser(options);
				parser.parseArgument(appArgs);
				if (options.help) {
					parser.printUsage(System.out);
				} else {
					doClusterModify(options, appArgs);
				}
			} else {
				throw new IllegalArgumentException("Operation " + operation + " not valid");
			}
		} catch (CmdLineException e) {
			parser.printUsage(System.err);
			System.err.println();
			System.exit(1);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			if (rootCause != null && StringUtils.hasText(rootCause.getMessage())) {
//				System.err.println("Command failed: " + rootCause.getMessage());
				System.err.println("Command failed: " + ExceptionUtils.getFullStackTrace(e));
			} else {
				// something we didn't expect
				// so print out stack trace
				e.printStackTrace();
			}
			System.exit(1);
		}
	}

	private void doPush(PushOptions options, List<String> appArgs) {
		YarnPushApplication app = new YarnPushApplication();
		app.applicationVersion(options.applicationVersion);

		Properties instanceProperties = new Properties();
		instanceProperties.setProperty("spring.yarn.applicationVersion", options.applicationVersion);
		app.configFile("application.properties", instanceProperties);

		app.run(appArgs.toArray(new String[0]));
		System.out.println("New instance " + options.applicationVersion + " installed");
	}

	private void doSubmit(SubmitOptions options, List<String> appArgs) {
		YarnSubmitApplication app = new YarnSubmitApplication();
		app.applicationVersion(options.applicationVersion);
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

	private void doListPushed(List<String> appArgs) {
		YarnInfoApplication app = new YarnInfoApplication();

		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.operation", "PUSHED");
		app.appProperties(appProperties);

		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

	private void doListSubmitted(SubmittedOptions options, List<String> appArgs) {
		YarnInfoApplication app = new YarnInfoApplication();

		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.operation", "SUBMITTED");
		if (options.verbose) {
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.verbose", "true");
		}
		if (StringUtils.hasText(options.type)) {
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.type", options.type);
		} else {
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.type", "GS");
		}
		app.appProperties(appProperties);

		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

	private void doClustersInfo(ClustersInfoOptions options, List<String> appArgs) {
		YarnContainerClusterApplication app = new YarnContainerClusterApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERSINFO");
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
				options.applicationId);
		app.appProperties(appProperties);

		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

	private void doClusterInfo(ClusterInfoOptions options, List<String> appArgs) {
		YarnContainerClusterApplication app = new YarnContainerClusterApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERINFO");
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
				options.applicationId);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
				options.clusterId);
		app.appProperties(appProperties);

		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

	private void doClusterCreate(ClusterCreateOptions options, List<String> appArgs) {
		YarnContainerClusterApplication app = new YarnContainerClusterApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERCREATE");
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
				options.applicationId);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
				options.clusterId);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.projectionType",
				options.projectionType);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.projectionDataAny",
				options.projectionAny);
		app.appProperties(appProperties);
		String run = app.run(appArgs.toArray(new String[0]));
		System.out.println("Cluster created: " + run);

		// String content =
		// "{\"clusterId\":\"foo\",\"projection\":\"HOSTS\",\"projectionData\":{\"any\":1,\"hosts\":{\"host1\":11,\"host2\":22}}}";
		// String content =
		// "{\"clusterId\":\"foo\",\"projection\":\"ANY\",\"projectionData\":{\"any\":1}}";

		// --operation CLUSTERCREATE
		// --cluster-id foo
		// --projection-type ANY
		// --projection-any 2
		// --projection-host host1,host2
		// --projection-rack rack1,rack2
		// --projection-data {\"any\":1,\"hosts\":{\"host1\":11,\"host2\":22}}

	}

	private void doClusterDestroy(ClusterDestroyOptions options, List<String> appArgs) {
		YarnContainerClusterApplication app = new YarnContainerClusterApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERDESTROY");
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
				options.applicationId);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
				options.clusterId);
		app.appProperties(appProperties);
		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

	private void doClusterStart(ClusterStartOptions options, List<String> appArgs) {
		YarnContainerClusterApplication app = new YarnContainerClusterApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERSTART");
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
				options.applicationId);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
				options.clusterId);
		app.appProperties(appProperties);
		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

	private void doClusterStop(ClusterStopOptions options, List<String> appArgs) {
		YarnContainerClusterApplication app = new YarnContainerClusterApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERSTOP");
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
				options.applicationId);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
				options.clusterId);
		app.appProperties(appProperties);
		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

	private void doClusterModify(ClusterModifyOptions options, List<String> appArgs) {
		YarnContainerClusterApplication app = new YarnContainerClusterApplication();
		Properties appProperties = new Properties();
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERMODIFY");
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
				options.applicationId);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
				options.clusterId);
//		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.projectionType",
//				options.projectionType);
		appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.projectionDataAny",
				options.projectionAny);
		app.appProperties(appProperties);
		String info = app.run(appArgs.toArray(new String[0]));
		System.out.println(info);
	}

//	public static void main(String[] args) {
//		new ClientApplication().doMain(args);
//	}

	private static class GenericOptions {

		@Option(name = "-h", aliases = { "--help" }, usage = "Print this help")
		protected boolean help;
	}

	private static class PushOptions extends GenericOptions {

		@Option(name = "-a", aliases = { "--application-version" }, usage = "Application version of the application, defaults to 'app'")
		private String applicationVersion = "app";
	}

	private static class SubmitOptions extends GenericOptions {

		@Option(name = "-a", aliases = { "--application-version" }, usage = "Application version of the application, defaults to 'app'")
		private String applicationVersion = "app";
	}

	private static class KillOptions extends GenericOptions {

		@Option(name = "-a", aliases = { "--application-id" }, usage = "Yarn Application id")
		private String applicationId;
	}

	private static class SubmittedOptions extends GenericOptions {

		@Option(name = "-v", aliases = { "--verbose" }, usage = "Verbose output")
		private boolean verbose;

		@Option(name = "-t", aliases = { "--type" }, usage = "Yarn application type")
		private String type;
	}

	private static class PushedOptions extends GenericOptions {
	}

	private static class GenericClusterOptions extends GenericOptions {

		@Option(name = "-a", aliases = { "--application-id" }, usage = "Yarn Application id")
		protected String applicationId;
	}

	private static class ClustersInfoOptions extends GenericClusterOptions {
	}

	private static class ClusterInfoOptions extends GenericClusterOptions {

		@Option(name = "-c", aliases = { "--cluster-id" }, usage = "Cluster id")
		private String clusterId;
	}

	private static class ClusterCreateOptions extends GenericClusterOptions {

		@Option(name = "-c", aliases = { "--cluster-id" }, usage = "Cluster id")
		private String clusterId;

		@Option(name = "-t", aliases = { "--projection-type" }, usage = "Cluster projection type")
		private String projectionType;

		@Option(name = "-d", aliases = { "--projection-any" }, usage = "Cluster projection any data")
		private String projectionAny;
	}

	private static class ClusterDestroyOptions extends GenericClusterOptions {
		@Option(name = "-c", aliases = { "--cluster-id" }, usage = "Cluster id")
		private String clusterId;
	}

	private static class ClusterModifyOptions extends GenericClusterOptions {

		@Option(name = "-c", aliases = { "--cluster-id" }, usage = "Cluster id")
		private String clusterId;

		@Option(name = "-d", aliases = { "--projection-any" }, usage = "Cluster projection any data")
		private String projectionAny;
	}

	private static class ClusterStartOptions extends GenericClusterOptions {

		@Option(name = "-c", aliases = { "--cluster-id" }, usage = "Cluster id")
		private String clusterId;
	}

	private static class ClusterStopOptions extends GenericClusterOptions {

		@Option(name = "-c", aliases = { "--cluster-id" }, usage = "Cluster id")
		private String clusterId;
	}

	private enum Operation {
		PUSH, SUBMIT, KILL, PUSHED, SUBMITTED, CLUSTERSINFO, CLUSTERINFO, CLUSTERCREATE, CLUSTERDESTROY, CLUSTERMODIFY, CLUSTERSTART, CLUSTERSTOP
	}

}
