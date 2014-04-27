package hello.appmaster.support.jackson;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Jackson {@link Module} registering custom serializers
 * handling YARN proto buffer based records.
 *
 * @author Janne Valkealahti
 *
 */
public class YarnProtoModule extends SimpleModule {

	private static final long serialVersionUID = 3060130404108559404L;

	public YarnProtoModule() {
		addSerializer(Container.class, new ContainerSerializer());
		addSerializer(ContainerId.class, new ContainerIdSerializer());
	}

}
