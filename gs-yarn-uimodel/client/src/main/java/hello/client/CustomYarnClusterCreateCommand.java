package hello.client;

import java.util.Properties;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.springframework.util.StringUtils;
import org.springframework.yarn.boot.cli.YarnClusterCreateCommand;

public class CustomYarnClusterCreateCommand extends YarnClusterCreateCommand {

	public CustomYarnClusterCreateCommand() {
		super(new CustomClusterCreateOptionHandler());
	}

	private static class CustomClusterCreateOptionHandler extends ClusterCreateOptionHandler {

		private OptionSpec<String> customPropOption;

		@Override
		protected void options() {
			super.options();
			this.customPropOption = option("custom-prop", "Custom property").withOptionalArg();
		}

		@Override
		protected Properties getExtraProperties(OptionSet options) {
			String customProp = options.valueOf(customPropOption);
			Properties props = new Properties();
			if (StringUtils.hasText(customProp)) {
				props.put("customProp", customProp);
			}
			return props;
		}

	}

}
