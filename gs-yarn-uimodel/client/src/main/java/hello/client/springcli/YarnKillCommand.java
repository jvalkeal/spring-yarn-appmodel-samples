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
import org.springframework.yarn.boot.app.YarnKillApplication;

public class YarnKillCommand extends AbstractApplicationCommand {

	public YarnKillCommand() {
		super("kill", "Kill Application", new KillOptionHandler());
	}

	private static final class KillOptionHandler extends ApplicationOptionHandler {

		private OptionSpec<String> applicationIdOption;

		@Override
		protected final void options() {
			this.applicationIdOption = option(CliSystemConstants.OPTIONS_APPLICATION_ID, CliSystemConstants.DESC_APPLICATION_ID).withRequiredArg();
		}

		@Override
		protected void runApplication(OptionSet options) throws Exception {
			List<?> nonOptionArguments = new ArrayList<Object>(options.nonOptionArguments());
			Assert.isTrue(nonOptionArguments.size() == 1, "Application Id must be defined");
			YarnKillApplication app = new YarnKillApplication();
			Properties appProperties = new Properties();
			appProperties.setProperty("spring.yarn.internal.YarnKillApplication.applicationId", options.valueOf(applicationIdOption));
			app.appProperties(appProperties);
			String info = app.run(new String[0]);
			Log.info(info);
		}

	}

}
