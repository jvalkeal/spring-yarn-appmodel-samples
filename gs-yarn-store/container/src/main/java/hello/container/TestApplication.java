package hello.container;

//import java.util.Map;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.hadoop.store.PartitionDataStoreWriter;
//import org.springframework.data.hadoop.store.config.annotation.EnableDataStoreTextWriter;
//import org.springframework.data.hadoop.store.config.annotation.SpringDataStoreTextWriterConfigurerAdapter;
//import org.springframework.data.hadoop.store.config.annotation.builders.DataStoreTextWriterConfigurer;

//@SpringBootApplication
public class TestApplication /*implements CommandLineRunner*/ {

//	private static final Log log = LogFactory.getLog(ContainerApplication.HdfsStoreWriter.class);
//
//	@Autowired
//	private PartitionDataStoreWriter<String, Map<String, Object>> writer;

//	@Override
//	public void run(String... args) throws Exception {
//		for (int i = 0; i < 1000000; i++) {
//			log.info("Writing " + i);
//			writer.write(Integer.toString(i));
//			Thread.sleep(1000);
//		}
//	}
//
//	@Configuration
//	@EnableDataStoreTextWriter
//	static class Config extends SpringDataStoreTextWriterConfigurerAdapter {
//
//		@Override
//		public void configure(DataStoreTextWriterConfigurer config) throws Exception {
//			config
//				.basePath("/tmp/HdfsStoreWriter")
//				.idleTimeout(3000)
//				.inWritingSuffix(".tmp")
//				.withPartitionStrategy()
//					.map("dateFormat('yyyy/MM/dd/HH/mm/ss', timestamp)")
//					.and()
//				.withNamingStrategy()
//					.name("data")
//					.uuid()
//					.rolling()
//					.name("txt", ".")
//					.and()
//				.withRolloverStrategy()
//					.size("1M");
//		}
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(TestApplication.class, args);
//	}

}
