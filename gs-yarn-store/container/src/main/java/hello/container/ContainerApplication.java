package hello.container;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.store.PartitionDataStoreWriter;
import org.springframework.data.hadoop.store.config.annotation.EnableDataStoreWriter;
import org.springframework.data.hadoop.store.config.annotation.SpringDataStoreWriterConfigurerAdapter;
import org.springframework.data.hadoop.store.config.annotation.builders.DataStoreWriterConfigurer;
import org.springframework.yarn.annotation.OnContainerStart;
import org.springframework.yarn.annotation.YarnComponent;
import org.springframework.yarn.container.YarnContainerSupport;

@SpringBootApplication
public class ContainerApplication {

	@YarnComponent
	static class HdfsStoreWriter extends YarnContainerSupport {

		@Autowired
		private PartitionDataStoreWriter<String, Map<String, Object>> writer;

		@OnContainerStart
		public ScheduledFuture<?> writeToHdfs() throws Exception {
			return getTaskScheduler().schedule(new FutureTask<Void>(new Runnable() {
				@Override
				public void run() {
					try {
						for (int i = 0; i < 1000000; i++) {
							writer.write(Integer.toString(i));
						}
						writer.close();
					} catch (IOException e) {
					}
				}
			}, null), new Date());
		}

	}

	@Configuration
	@EnableDataStoreWriter
	static class Config extends SpringDataStoreWriterConfigurerAdapter {

		@Override
		public void configure(DataStoreWriterConfigurer config) throws Exception {
			config
				.basePath("/tmp/HdfsStoreWriter")
				.idleTimeout(60000)
				.withPartitionStrategy()
					.map("dateFormat('yyyy/MM/dd/HH/mm', timestamp)")
					.and()
				.withNamingStrategy()
					.name("data")
					.uuid()
					.rolling()
					.and()
				.withRolloverStrategy()
					.size("1M");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ContainerApplication.class, args);
	}

}
