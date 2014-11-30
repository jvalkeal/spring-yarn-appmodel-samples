package hello.appmaster;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.Container;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.am.container.ContainerRegisterInfo;
import org.springframework.yarn.am.container.ContainerShutdown;
import org.springframework.yarn.event.LoggingListener;

@SpringBootApplication
public class AppmasterApplication {

	private static final Log log = LogFactory.getLog(AppmasterApplication.class);

	@Bean
	public LoggingListener loggingListener() {
		return new LoggingListener("info");
	}

	public static void main(String[] args) {
		SpringApplication.run(AppmasterApplication.class, args);
	}

	@Bean(name=YarnSystemConstants.DEFAULT_CONTAINER_SHUTDOWN)
	public ContainerShutdown yarnContainerShutdown() {
		return new FooContainerShutdown();
	}

	public static class FooContainerShutdown implements ContainerShutdown {

		@Override
		public boolean shutdown(Map<Container, ContainerRegisterInfo> containers) {
			log.info("XXX1 shutdown " + containers);
			log.info("XXX2 shutdown " + containers.size());
			RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
			for (ContainerRegisterInfo info : containers.values()) {
				log.info("XXX2 shutdown calling" + info.getTrackUrl());
				restTemplate.postForObject(info.getTrackUrl() + "/shutdown", null, Void.class);
			}
			return true;
		}

	}

}
