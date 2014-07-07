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

import java.util.Properties;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.springframework.boot.cli.util.Log;
import org.springframework.yarn.boot.app.YarnInfoApplication;

public class YarnSubmittedCommand extends AbstractApplicationCommand {

	public YarnSubmittedCommand() {
		super("submitted", "List submitted applications", new SubmittedOptionHandler());
	}

	@Override
	public String getUsageHelp() {
		return "[options]";
	}

	private static final class SubmittedOptionHandler extends ApplicationOptionHandler {

		private OptionSpec<String> typeOption;

		private OptionSpec<Boolean> verboseOption;

		@Override
		protected final void options() {
			typeOption = option(CliSystemConstants.OPTIONS_APPLICATION_TYPE, CliSystemConstants.DESC_APPLICATION_TYPE).withOptionalArg().defaultsTo("GS");
			verboseOption = option("verbose", "Verbose Output").withOptionalArg().ofType(Boolean.class)
					.defaultsTo(true);
		}

		@Override
		protected void runApplication(OptionSet options) throws Exception {
			YarnInfoApplication app = new YarnInfoApplication();
			Properties appProperties = new Properties();
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.operation", "SUBMITTED");
			if (isFlagOn(options, verboseOption)) {
				appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.verbose", "true");
			}
			appProperties.setProperty("spring.yarn.internal.YarnInfoApplication.type", options.valueOf(typeOption));
			app.appProperties(appProperties);
			String info = app.run(new String[0]);
			Log.info(info);
		}

	}

}
