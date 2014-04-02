package hello.appmaster;

import java.util.HashMap;
import java.util.Map.Entry;

import org.springframework.util.ObjectUtils;
import org.springframework.yarn.am.StaticEventingAppmaster;
import org.springframework.yarn.am.allocate.AbstractAllocator;
import org.springframework.yarn.am.allocate.ContainerAllocateData;

public class GridAppmaster extends StaticEventingAppmaster {

	@Override
	public void submitApplication() {
		registerAppmaster();
		start();
		if(getAllocator() instanceof AbstractAllocator) {
			((AbstractAllocator)getAllocator()).setApplicationAttemptId(getApplicationAttemptId());
		}
	}


	@Override
	protected void notifyCompleted() {
	}

	public void pump() {
		getAllocator().allocateContainers(1);
	}

	public void pump2(String[] hosts, String[] racks, Integer any) {
//		getAllocator().allocateContainers(1);
		ContainerAllocateData allocateData = new ContainerAllocateData();
		if (!ObjectUtils.isEmpty(hosts)) {
			HashMap<String, Integer> host = new HashMap<String, Integer>();
			for (String h : hosts) {
				if (host.containsKey(h)) {
					host.put(h, host.get(h)+1);
				} else {
					host.put(h, 1);
				}
			}

			for (Entry<String, Integer> e : host.entrySet()) {
				allocateData.addHosts(e.getKey(), e.getValue());
			}
		}

		if (!ObjectUtils.isEmpty(racks)) {
			HashMap<String, Integer> rack = new HashMap<String, Integer>();
			for (String r : racks) {
				if (rack.containsKey(r)) {
					rack.put(r, rack.get(r)+1);
				} else {
					rack.put(r, 1);
				}
			}
			for (Entry<String, Integer> e : rack.entrySet()) {
				allocateData.addRacks(e.getKey(), e.getValue());
			}
		}

		if (any != null) {
			allocateData.addAny(any);
		}

		getAllocator().allocateContainers(allocateData);
	}

}
