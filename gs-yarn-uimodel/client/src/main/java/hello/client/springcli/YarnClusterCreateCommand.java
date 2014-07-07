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
import java.util.Properties;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.springframework.boot.cli.util.Log;
import org.springframework.util.Assert;
import org.springframework.yarn.boot.app.YarnContainerClusterApplication;

public class YarnClusterCreateCommand extends AbstractApplicationCommand {

	public YarnClusterCreateCommand() {
		super("clustercreate", "Create Cluster", new ClusterCreateOptionHandler());
	}

	private static final class ClusterCreateOptionHandler extends ApplicationOptionHandler {

		private OptionSpec<String> applicationIdOption;

		private OptionSpec<String> clusterIdOption;

		private OptionSpec<String> projectionTypeOption;

		private OptionSpec<String> projectionOption;

		@Override
		protected final void options() {
			this.applicationIdOption = option(CliSystemConstants.OPTIONS_APPLICATION_ID, CliSystemConstants.DESC_APPLICATION_ID).withRequiredArg();
			this.clusterIdOption = option(CliSystemConstants.OPTIONS_CLUSTER_ID, CliSystemConstants.DESC_CLUSTER_ID).withRequiredArg();
			this.projectionTypeOption = option("projection-type", "Specify Projection Type").withRequiredArg();
			this.projectionOption = option("projection-any", "Specify Any Count").withRequiredArg();
		}

		@Override
		protected void runApplication(OptionSet options) throws Exception {
			List<?> nonOptionArguments = new ArrayList<Object>(options.nonOptionArguments());
			Assert.isTrue(nonOptionArguments.size() == 4, "Cluster Id and Application Id must be defined");
			YarnContainerClusterApplication app = new YarnContainerClusterApplication();
			Properties appProperties = new Properties();
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.operation", "CLUSTERCREATE");
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.applicationId",
					options.valueOf(applicationIdOption));
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.clusterId",
					options.valueOf(clusterIdOption));
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.projectionType",
					options.valueOf(projectionTypeOption));
			appProperties.setProperty("spring.yarn.internal.ContainerClusterApplication.projectionDataAny",
					options.valueOf(projectionOption));
			app.appProperties(appProperties);
			String info = app.run(new String[0]);
			Log.info(info);
		}

	}

}
