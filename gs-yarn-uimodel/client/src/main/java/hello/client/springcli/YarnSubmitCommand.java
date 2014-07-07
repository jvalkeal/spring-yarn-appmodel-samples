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

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.springframework.boot.cli.util.Log;
import org.springframework.util.Assert;
import org.springframework.yarn.boot.app.YarnSubmitApplication;

public class YarnSubmitCommand extends AbstractApplicationCommand {

	public YarnSubmitCommand() {
		super("submit", "Submit Application", new SubmitOptionHandler());
	}

	private static final class SubmitOptionHandler extends ApplicationOptionHandler {

		private OptionSpec<String> applicationVersionOption;

		@Override
		protected final void options() {
			this.applicationVersionOption = option(CliSystemConstants.OPTIONS_APPLICATION_VERSION, CliSystemConstants.DESC_APPLICATION_VERSION).withRequiredArg();
		}

		@Override
		protected void runApplication(OptionSet options) throws Exception {
			List<?> nonOptionArguments = new ArrayList<Object>(options.nonOptionArguments());
			Assert.isTrue(nonOptionArguments.size() == 1, "Application Version must be defined");
			String appVersion = options.valueOf(applicationVersionOption);
			YarnSubmitApplication app = new YarnSubmitApplication();
			app.applicationVersion(appVersion);
			ApplicationId applicationId = app.run(new String[0]);
			Log.info("New instance submitted with id " + applicationId);
		}

	}

}
