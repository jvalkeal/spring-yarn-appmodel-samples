package hello.appmaster.am.boot.actuate.endpoint.mvc;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import hello.appmaster.TestUtils;
import hello.appmaster.am.boot.actuate.endpoint.YarnContainerClusterEndpoint;
import hello.appmaster.am.boot.actuate.endpoint.mvc.YarnContainerClusterMvcEndpointTests.TestConfiguration;
import hello.appmaster.am.cluster.ContainerCluster;
import hello.appmaster.am.cluster.ContainerClusterState;
import hello.appmaster.am.cluster.ManagedContainerClusterAppmaster;
import hello.appmaster.am.grid.support.HostsGridProjection;
import hello.appmaster.am.grid.support.SatisfyStateData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.EndpointWebMvcAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.ManagementServerPropertiesAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.yarn.am.YarnAppmaster;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestConfiguration.class })
@WebAppConfiguration
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class YarnContainerClusterMvcEndpointTests {

	private final static String BASE = "/" + YarnContainerClusterEndpoint.ENDPOINT_ID;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	private TestYarnAppmaster appmaster;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		appmaster = (TestYarnAppmaster) context.getBean(YarnAppmaster.class);
	}

	@Test
	public void testInitialHome() throws Exception {
		mvc.
			perform(get(BASE)).
			andExpect(status().isOk()).
			andExpect(content().string(containsString("\"links\":")));
	}

	@Test
	public void testClusterCreate() throws Exception {
		String content = "{\"clusterId\":\"foo\",\"projection\":\"HOSTS\",\"projectionData\":{\"any\":1,\"hosts\":{\"host1\":11,\"host2\":22}}}";
		mvc.
			perform(post(BASE).content(content).contentType(MediaType.APPLICATION_JSON)).
			andExpect(status().isOk()).
			andExpect(content().string(containsString("foo")));
		mvc.
			perform(get(BASE)).
			andExpect(status().isOk()).
			andExpect(content().string(containsString("\"links\":")));

		Map<String, ContainerCluster> clusters = TestUtils.readField("clusters", appmaster);
		assertThat(clusters.size(), is(1));
		assertThat(clusters.containsKey("foo"), is(true));
		assertThat(clusters.get("foo").getGridProjection(), instanceOf(HostsGridProjection.class));
		assertThat(clusters.get("foo").getGridProjection().getSatisfyState().getAllocateData().getAny(), is(0));
		assertThat(clusters.get("foo").getGridProjection().getSatisfyState().getAllocateData().getHosts().size(), is(2));
		assertThat(clusters.get("foo").getGridProjection().getSatisfyState().getAllocateData().getHosts().get("host1"), is(11));
		assertThat(clusters.get("foo").getGridProjection().getSatisfyState().getAllocateData().getHosts().get("host2"), is(22));
		assertThat(clusters.get("foo").getContainerClusterState().getState(), is(ContainerClusterState.State.INITIAL));
	}

	@Test
	public void testClusterStatus() throws Exception {
		String content = "{\"clusterId\":\"foo\",\"projection\":\"HOSTS\",\"projectionData\":{\"any\":1,\"hosts\":{\"host1\":11,\"host2\":22}}}";
		mvc.
			perform(post(BASE).content(content).contentType(MediaType.APPLICATION_JSON)).
			andExpect(status().isOk()).
			andExpect(content().string(containsString("foo")));
		mvc.
			perform(get(BASE + "/foo")).
			andExpect(status().isOk()).
			andExpect(content().string(containsString("foo")));

	}

	@Test
	public void testClusterStart() throws Exception {
		String content = "{\"clusterId\":\"foo\",\"projection\":\"HOSTS\",\"projectionData\":{\"hosts\":{\"host1\":11,\"host2\":22}}}";
		mvc.
			perform(post(BASE).content(content).contentType(MediaType.APPLICATION_JSON)).
			andExpect(status().isOk()).
			andExpect(content().string(containsString("foo")));
		mvc.
			perform(post(BASE + "/foo/start")).
			andExpect(status().isOk()).
			andExpect(content().string(containsString("start ok")));

		Map<String, ContainerCluster> clusters = TestUtils.readField("clusters", appmaster);
		assertThat(clusters.size(), is(1));
		assertThat(clusters.containsKey("foo"), is(true));
		assertThat(clusters.get("foo").getContainerClusterState().getState(), is(ContainerClusterState.State.ALLOCATING));
	}

	@Import({ EndpointWebMvcAutoConfiguration.class, ManagementServerPropertiesAutoConfiguration.class })
	@EnableWebMvc
	@Configuration
	public static class TestConfiguration {

		@Bean
		public YarnContainerClusterEndpoint endpoint() {
			return new YarnContainerClusterEndpoint();
		}

		@Bean
		public YarnContainerClusterMvcEndpoint mvcEndpoint() {
			return new YarnContainerClusterMvcEndpoint(endpoint());
		}

		@Bean
		public YarnAppmaster appMaster() {
			return new TestYarnAppmaster();
		}

	}

	public static class TestYarnAppmaster extends ManagedContainerClusterAppmaster {
		@Override
		protected void onInit() throws Exception {
			setConfiguration(new org.apache.hadoop.conf.Configuration());
			super.onInit();
		}
		@Override
		protected void doStart() {
		}
		@Override
		protected void doStop() {
		}
	}

}
