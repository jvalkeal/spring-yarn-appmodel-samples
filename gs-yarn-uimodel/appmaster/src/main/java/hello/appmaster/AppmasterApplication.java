package hello.appmaster;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.yarn.am.grid.GridProjection;
import org.springframework.yarn.am.grid.GridProjectionFactory;
import org.springframework.yarn.am.grid.support.ProjectionData;

@Configuration
@EnableAutoConfiguration
public class AppmasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppmasterApplication.class, args);
	}

	@Bean
	public GridProjectionFactory testGridProjectionFactory() {
		return new GridProjectionFactory() {

			@Override
			public Set<String> getRegisteredProjectionTypes() {
				return new HashSet<String>(Arrays.asList("custom"));
			}

			@Override
			public GridProjection getGridProjection(ProjectionData projectionData) {
				CustomGridProjection gridProjection = new CustomGridProjection();
				gridProjection.setPriority(projectionData.getPriority());
				gridProjection.setProjectionData(projectionData);
				return gridProjection;
			}
		};
	}

}
