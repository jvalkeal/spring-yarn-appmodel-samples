package hello.appmaster.am.cluster;

import org.apache.hadoop.yarn.api.records.Container;

public interface ClusterMatcher {

	boolean accept(Container container);

}
