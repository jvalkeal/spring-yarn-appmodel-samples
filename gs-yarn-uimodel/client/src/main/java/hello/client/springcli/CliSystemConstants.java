package hello.client.springcli;

import static java.util.Arrays.asList;

import java.util.List;

public abstract class CliSystemConstants {

	public final static List<String> OPTIONS_APPLICATION_ID  = asList("application-id", "a");

	public final static List<String> OPTIONS_APPLICATION_TYPE  = asList("application-type", "t");

	public final static List<String> OPTIONS_APPLICATION_VERSION  = asList("application-version", "v");

	public final static List<String> OPTIONS_CLUSTER_ID  = asList("cluster-id", "c");

	public final static String DESC_APPLICATION_ID = "Specify YARN application id";

	public final static String DESC_CLUSTER_ID = "Specify cluster id";

	public final static String DESC_APPLICATION_TYPE = "Application type";

	public final static String DESC_APPLICATION_VERSION = "Application version";

}
