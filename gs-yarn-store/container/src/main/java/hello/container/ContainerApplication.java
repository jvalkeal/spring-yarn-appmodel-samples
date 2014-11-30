package hello.container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.yarn.annotation.OnContainerStart;
import org.springframework.yarn.annotation.YarnComponent;
import org.springframework.yarn.boot.actuate.endpoint.mvc.domain.ContainerRegisterRequest;
import org.springframework.yarn.container.YarnContainerSupport;
import org.springframework.yarn.support.NetworkUtils;

@SpringBootApplication
@EnableConfigurationProperties({ContainerApplication.EnvProperties.class})
public class ContainerApplication implements ApplicationListener<ApplicationEvent> {

	private static final Log log = LogFactory.getLog(ContainerApplication.class);

	// SHDP_AMSERVICE_TRACKURL
	@Autowired
	EnvProperties envProperties;

	@ConfigurationProperties
	public static class EnvProperties {
		String trackUrl;
		String containerId;
		public void setSHDP_AMSERVICE_TRACKURL(String trackUrl) {
			log.info("XXX trackUrl=" + trackUrl);
			this.trackUrl = trackUrl;
		}

		public void setSHDP_CONTAINERID(String containerId) {
			log.info("XXX containerId=" + containerId);
			this.containerId = containerId;
		}

	}


//	@YarnComponent
//	static class RegisterComponent extends YarnContainerSupport {
//
//		@Autowired
//		EnvProperties envProperties;
//
//		@OnContainerStart
//		@Order(Ordered.HIGHEST_PRECEDENCE)
//		public void register() {
//			RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
//			ContainerRegisterRequest request = new ContainerRegisterRequest(envProperties.containerId, "jeejee");
//			restTemplate.postForObject(envProperties.trackUrl + "/yarn_containerregister", request, Object.class);
//		}
//
//	}



	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		log.info("XXXX onApplicationEvent event=" + event);

		if (event instanceof EmbeddedServletContainerInitializedEvent) {
			String namespace = ((EmbeddedServletContainerInitializedEvent) event).getApplicationContext().getNamespace();
			int port = ((EmbeddedServletContainerInitializedEvent) event).getEmbeddedServletContainer().getPort();
			log.info("XXX got namespace/port " + namespace + "/" + port);

			RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
			String trackUrl = "http://" + NetworkUtils.getDefaultAddress() + ":" + port;
			ContainerRegisterRequest request = new ContainerRegisterRequest(envProperties.containerId, trackUrl);
			restTemplate.postForObject(envProperties.trackUrl + "/yarn_containerregister", request, Object.class);

		}
    }

//		Object source = event.getSource();
//		if (source instanceof AnnotationConfigEmbeddedWebApplicationContext) {
//			EmbeddedServletContainer embeddedServletContainer = ((AnnotationConfigEmbeddedWebApplicationContext) source)
//					.getEmbeddedServletContainer();
//			int port = embeddedServletContainer.getPort();
//			log.info("XXXX Got port from EmbeddedServletContainer port=" + port);
//
//		}


	@YarnComponent
	static class HdfsStoreWriter extends YarnContainerSupport {


		@OnContainerStart
		public void justWaitHere() throws Exception {
			for (int i = 0; i<1000; i++) {
				log.info("Hello sleeping " + i);
				Thread.sleep(1000);
			}
		}

//		@Autowired
//		@Qualifier("writer1")
//		private PartitionDataStoreWriter<String, Map<String, Object>> writer1;
//
//		@Autowired
//		@Qualifier("writer2")
//		private PartitionDataStoreWriter<String, Map<String, Object>> writer2;

//		@OnContainerStart
//		public ScheduledFuture<?> writeToHdfs() throws Exception {
//			return getTaskScheduler().schedule(new FutureTask<Void>(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						while(true) {
//							for (int i = 0; i < 1000000; i++) {
//								writer.write(Integer.toString(i));
//							}
//							Thread.sleep(1000);
//						}
//					} catch (Exception e) {
//						log.error("Error in writing",e);
//					} finally {
//						try {
//							writer.close();
//						} catch (IOException e) {
//							log.warn("Error in close",e);
//						}
//					}
//				}
//			}, null), new Date());
//		}

//		@OnContainerStart
//		public ListenableFuture<?> writer1() throws Exception {
//			final MyFuture myFuture = new MyFuture();
//			getTaskScheduler().schedule(new FutureTask<Void>(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						while(!myFuture.interrupted) {
//							for (int i = 0; i < 1000000; i++) {
//								writer1.write(Integer.toString(i));
//							}
//							Thread.sleep(1000);
//						}
//					} catch (Exception e) {
//						log.error("Error in writing",e);
//					} finally {
//						try {
//							writer1.close();
//						} catch (IOException e) {
//							log.warn("Error in close",e);
//						}
//					}
//				}
//			}, null), new Date());
//			return myFuture;
//		}

//		@OnContainerStart
//		public ListenableFuture<?> writer2() throws Exception {
//			final MyFuture myFuture = new MyFuture();
//			getTaskScheduler().schedule(new FutureTask<Void>(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						while(!myFuture.interrupted) {
//							for (int i = 0; i < 1000000; i++) {
//								writer2.write(Integer.toString(i));
//							}
//							Thread.sleep(1000);
//						}
//					} catch (Exception e) {
//						log.error("Error in writing",e);
//					} finally {
//						try {
//							writer2.close();
//						} catch (IOException e) {
//							log.warn("Error in close",e);
//						}
//					}
//				}
//			}, null), new Date());
//			return myFuture;
//		}

//		@OnContainerStart
//		public ListenableFuture<Boolean> writeToHdfs() throws Exception {
//
//			final MyFuture myFuture = new MyFuture();
//
//			ListenableFutureTask<Boolean> task = new ListenableFutureTask<Boolean>(new Runnable() {
//
//				@Override
//				public void run() {
//					Boolean ret = null;
//					while(true) {
//						boolean interrupted = Thread.currentThread().isInterrupted();
//						log.info("hello " + interrupted);
//						if (interrupted) {
//							break;
//						}
//						log.info("hello not interrupted");
//
//						try {
//							for (int i = 0; i < 1000000; i++) {
//								writer.write(Integer.toString(i));
//							}
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							log.info("interrupted", e);
//							ret = true;
//						} catch (IOException e) {
//							log.info("writer io", e);
//							ret = true;
//						} finally {
//						}
//						if (ret != null) {
//							myFuture.set(ret);
//							break;
//						}
//					}
//					try {
//						log.info("closing writer");
//						writer.close();
//					} catch (IOException e) {
//						log.warn("Error in close", e);
//					}
//					myFuture.set(ret);
//					log.info("exiting from run()");
//				}
//			}, null);
//
//			getTaskScheduler().schedule(task, new Date());
//
//			return myFuture;
//		}

//		@OnContainerStart
//		public Future<Boolean> writeToHdfs() throws Exception {
//
//			final MyFuture myFuture = new MyFuture();
//
//			final ListenableFutureTask<Boolean> task = new ListenableFutureTask<Boolean>(new Runnable() {
//
//				@Override
//				public void run() {
//					Boolean ret = null;
//					log.info("hello");
//					try {
//						for (int i = 0; i < 1000000; i++) {
//							writer.write(Integer.toString(i));
//						}
//					} catch (IOException e) {
//						log.info("writer io", e);
//						ret = true;
//					} finally {
//						try {
//							log.info("closing writer");
//							writer.close();
//						} catch (Exception e) {
//							log.warn("Error in close", e);
//						}
//					}
//					myFuture.set(ret);
//					log.info("exiting from run()");
//				}
//			}, null);
//
//			getTaskScheduler().scheduleAtFixedRate(task, 1000);
//
//			return myFuture;
//		}

	}


//	static class MyFuture extends SettableListenableFuture<Boolean> {
//
//		boolean interrupted = false;
//
//		@Override
//		protected void interruptTask() {
//			interrupted = true;
//			HdfsStoreWriter.log.info("interruptTask");
//		}
//	}

//	@Configuration
//	@EnableDataStoreTextWriter
//	static class Config extends SpringDataStoreTextWriterConfigurerAdapter {
//
//		@Override
//		public void configure(DataStoreTextWriterConfigurer config) throws Exception {
//			config
//				.basePath("/tmp/HdfsStoreWriter")
//				.idleTimeout(60000)
//				.inWritingSuffix(".tmp")
//				.withPartitionStrategy()
//					.map("dateFormat('yyyy/MM/dd/HH/mm', timestamp)")
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

//	@Configuration
//	@EnableDataStoreTextWriter(name="writer1")
//	static class Config1 extends SpringDataStoreTextWriterConfigurerAdapter {
//
//		@Override
//		public void configure(DataStoreTextWriterConfigurer config) throws Exception {
//			config
//				.basePath("/tmp/store/HdfsStoreWriter1")
//				.idleTimeout(60000)
//				.inWritingSuffix(".tmp")
//				.withPartitionStrategy()
//					.map("dateFormat('yyyy/MM/dd/HH/mm', timestamp)")
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

//	@Configuration
//	@EnableDataStoreTextWriter(name="writer2")
//	static class Config2 extends SpringDataStoreTextWriterConfigurerAdapter {
//
//		@Override
//		public void configure(DataStoreTextWriterConfigurer config) throws Exception {
//			config
//				.basePath("/tmp/store/HdfsStoreWriter2")
//				.idleTimeout(60000)
//				.inWritingSuffix(".tmp")
//				.withPartitionStrategy()
//					.map("dateFormat('yyyy/MM/dd/HH/mm', timestamp)")
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

	public static void main(String[] args) {
		SpringApplication.run(ContainerApplication.class, args);
	}

}
