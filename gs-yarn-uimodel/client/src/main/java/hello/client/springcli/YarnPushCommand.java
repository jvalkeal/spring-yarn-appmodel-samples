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
import org.springframework.yarn.boot.app.YarnPushApplication;

public class YarnPushCommand extends AbstractApplicationCommand {

	public YarnPushCommand() {
		super("push", "Push Application", new PushOptionHandler());
	}

	private static final class PushOptionHandler extends ApplicationOptionHandler {

		private OptionSpec<String> applicationVersionOption;

		@Override
		protected final void options() {
			this.applicationVersionOption = option("application-version", "Specify application version").withRequiredArg();
		}

		@Override
		protected void runApplication(OptionSet options) throws Exception {
			List<?> nonOptionArguments = new ArrayList<Object>(options.nonOptionArguments());
			Assert.isTrue(nonOptionArguments.size() == 1, "Application Version must be defined");
			String appVersion = options.valueOf(applicationVersionOption);
			YarnPushApplication app = new YarnPushApplication();
			app.applicationVersion(appVersion);
			Properties instanceProperties = new Properties();
			instanceProperties.setProperty("spring.yarn.applicationVersion", appVersion);
			app.configFile("application.properties", instanceProperties);
			Log.info("New instance " + appVersion + " installed");
		}

	}

}
