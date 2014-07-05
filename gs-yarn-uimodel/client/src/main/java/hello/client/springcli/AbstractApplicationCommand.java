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
