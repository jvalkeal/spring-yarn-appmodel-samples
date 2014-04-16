package hello.appmaster.am.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ManagedClustersAppmaster extends AbstractContainerClusterAppmaster {

	private static final Log log = LogFactory.getLog(ManagedClustersAppmaster.class);

//	public void pump() {
//		getAllocator().allocateContainers(1);
//	}
//
//	public void pump2(String[] hosts, String[] racks, Integer any) {
//		ContainerAllocateData allocateData = new ContainerAllocateData();
//		if (!ObjectUtils.isEmpty(hosts)) {
//			HashMap<String, Integer> host = new HashMap<String, Integer>();
//			for (String h : hosts) {
//				if (host.containsKey(h)) {
//					host.put(h, host.get(h)+1);
//				} else {
//					host.put(h, 1);
//				}
//			}
//
//			for (Entry<String, Integer> e : host.entrySet()) {
//				allocateData.addHosts(e.getKey(), e.getValue());
//			}
//		}
//
//		if (!ObjectUtils.isEmpty(racks)) {
//			HashMap<String, Integer> rack = new HashMap<String, Integer>();
//			for (String r : racks) {
//				if (rack.containsKey(r)) {
//					rack.put(r, rack.get(r)+1);
//				} else {
//					rack.put(r, 1);
//				}
//			}
//			for (Entry<String, Integer> e : rack.entrySet()) {
//				allocateData.addRacks(e.getKey(), e.getValue());
//			}
//		}
//
//		if (any != null) {
//			allocateData.addAny(any);
//		}
//
//		getAllocator().allocateContainers(allocateData);
//	}

}
