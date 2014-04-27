package hello.appmaster.support.jackson;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.hadoop.yarn.api.records.ApplicationAttemptId;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.NodeId;
import org.apache.hadoop.yarn.api.records.Priority;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.api.records.Token;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests for {@link YarnProtoModule} and custom serializers.
 *
 * @author Janne Valkealahti
 *
 */
public class YarnProtoModuleTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void testCustomSerialization() throws Exception {
		context = new AnnotationConfigApplicationContext();
		context.register(ModulesConfig.class, HttpMessageConvertersAutoConfiguration.class);
		context.refresh();
		ObjectMapper mapper = context.getBean(ObjectMapper.class);
		assertThat(mapper.writeValueAsString(new ContainerBean()), is("{\"container\":{\"id\":\"container_0_0000_00_000000\",\"node\":\"host\"}}"));
		assertThat(mapper.writeValueAsString(new ContainerIdBean()), is("{\"containerId\":\"container_0_0000_00_000000\"}"));
	}

	@Configuration
	protected static class ModulesConfig {

		@Bean
		public Module yarnProtoModule() {
			return new YarnProtoModule();
		}
	}

	protected static class ContainerBean {
		public Container getContainer() {
			ApplicationId appId = ApplicationId.newInstance(0, 0);
			ApplicationAttemptId appAttemptId = ApplicationAttemptId.newInstance(appId, 0);
			ContainerId containerId = ContainerId.newInstance(appAttemptId, 0);
			NodeId nodeId = NodeId.newInstance("host", 0);
			Resource resource = Resource.newInstance(0, 0);
			Priority priority = Priority.newInstance(0);
			Token containerToken = Token.newInstance(new byte[0], "kind", new byte[0], "service");
			return Container.newInstance(containerId, nodeId, "nodeHttpAddress", resource, priority, containerToken);
		}
	}

	protected static class ContainerIdBean {
		public ContainerId getContainerId() {
			ApplicationId appId = ApplicationId.newInstance(0, 0);
			ApplicationAttemptId appAttemptId = ApplicationAttemptId.newInstance(appId, 0);
			return ContainerId.newInstance(appAttemptId, 0);
		}
	}

}
