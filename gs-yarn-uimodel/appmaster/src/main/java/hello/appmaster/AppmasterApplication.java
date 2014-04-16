package hello.appmaster;

import hello.appmaster.am.boot.actuate.endpoint.YarnContainerClusterEndpoint;
import hello.appmaster.am.boot.actuate.endpoint.mvc.YarnContainerClusterMvcEndpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class AppmasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppmasterApplication.class, args);
	}

	@Bean
	@ConditionalOnMissingBean
	public YarnContainerClusterEndpoint yarnContainerClusterEndpoint() {
		return new YarnContainerClusterEndpoint();
	}

	@Bean
	@ConditionalOnBean(YarnContainerClusterEndpoint.class)
	@ConditionalOnExpression("${endpoints.cluster.enabled:true}")
	public YarnContainerClusterMvcEndpoint yarnContainerClusterMvcEndpoint(YarnContainerClusterEndpoint delegate) {
		return new YarnContainerClusterMvcEndpoint(delegate);
	}

}
