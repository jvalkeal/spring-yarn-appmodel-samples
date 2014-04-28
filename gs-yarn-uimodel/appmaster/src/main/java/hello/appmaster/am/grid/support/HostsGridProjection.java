package hello.appmaster.am.grid.support;

import hello.appmaster.am.grid.GridMember;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class HostsGridProjection extends AbstractGridProjection {

	private final Map<String, Integer> hosts = new ConcurrentHashMap<String, Integer>();

	public HostsGridProjection() {
		super();
	}

	public HostsGridProjection(Map<String, Integer> hosts) {
		super();
		this.hosts.putAll(hosts);
	}

	@Override
	public SatisfyStateData getSatisfyState() {
		SatisfyStateData data = new SatisfyStateData();
		ArrayList<GridMember> remove = new ArrayList<GridMember>();

		for (Entry<String, Integer> entry : hosts.entrySet()) {
			int delta = entry.getValue() - getHostCount(entry.getKey());
			data.getAllocateData().addHosts(entry.getKey(), Math.max(delta, 0));

			int removeCount = Math.max(-delta, 0);
			Iterator<GridMember> iterator = getHostCountMembers(entry.getKey()).iterator();
			while (iterator.hasNext() && removeCount-- > 0) {
				remove.add(iterator.next());
			}

		}

		data.setRemoveData(remove);

		return data;
	}

	@Override
	public void setProjectionData(ProjectionData data) {
		super.setProjectionData(data);
		if(data.getHosts() != null) {
			hosts.clear();
			hosts.putAll(data.getHosts());
		}
	}

	@Override
	public boolean acceptMember(GridMember member) {

		String host = member.getContainer().getNodeId().getHost();
		Collection<GridMember> hostCountMembers = getHostCountMembers(host);

		Integer target = hosts.get(host);
		if (target == null) {
			return false;
		}

		if (hostCountMembers.size() < target) {
			return super.acceptMember(member);
		} else {
			return false;
		}
	}

}
