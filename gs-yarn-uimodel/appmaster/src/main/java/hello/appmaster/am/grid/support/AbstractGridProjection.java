package hello.appmaster.am.grid.support;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.yarn.api.records.ContainerId;
import org.springframework.util.Assert;

import hello.appmaster.am.grid.GridMember;
import hello.appmaster.am.grid.GridProjection;

/**
 * Base implementation of a {@link GridProjection}.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractGridProjection implements GridProjection {

	private final ConcurrentHashMap<ContainerId, GridMember> members = new ConcurrentHashMap<ContainerId, GridMember>();

	private final ConcurrentHashMap<String, HostCountHolder> hostCounts = new ConcurrentHashMap<String, HostCountHolder>();

	/**
	 * Instantiates a new abstract grid projection.
	 */
	public AbstractGridProjection() {
	}

	@Override
	public boolean acceptMember(GridMember member) {
		return addMember(member);
	}

	@Override
	public Collection<GridMember> getMembers() {
		return members.values();
	}

	@Override
	public abstract SatisfyStateData getSatisfyState();

	@Override
	public abstract void setProjectionData(ProjectionData data);

	protected boolean addMember(GridMember member) {
		Assert.notNull(member, "Node must not be null");
		if (members.putIfAbsent(member.getId(), member) == null) {
			incrementHostCount(member);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public GridMember removeMember(GridMember member) {
		GridMember removed = members.remove(member.getContainer().getId());
		if (removed != null) {
			decrementHostCount(removed);
		}
		return removed;
	}

	protected int getHostCount(String host) {
		HostCountHolder holder = hostCounts.get(host);
		return holder != null ? holder.count : 0;
	}

	protected Collection<GridMember> getHostCountMembers(String host) {
		HostCountHolder holder = hostCounts.get(host);
		return holder != null ? holder.members : Collections.<GridMember>emptyList();
	}

	private void incrementHostCount(GridMember member) {
		String host = null;
		if (member.getContainer().getNodeId() != null) {
			host = member.getContainer().getNodeId().getHost();
		}
		if (host != null) {
			if (!hostCounts.containsKey(host)) {
				hostCounts.put(host, new HostCountHolder());
			}
			hostCounts.get(host).add(member);
		}
	}

	private void decrementHostCount(GridMember member) {
		String host = null;
		if (member.getContainer().getNodeId() != null) {
			host = member.getContainer().getNodeId().getHost();
		}
		if (host != null) {
			hostCounts.get(host).remove(member);
		}
	}

	private static class HostCountHolder {
		Integer count = 0;
		Collection<GridMember> members = new HashSet<GridMember>();
		void add(GridMember member) {
			if (members.add(member)) {
				count++;
			}
		}
		void remove(GridMember member) {
			if (members.remove(member)) {
				count--;
			}
		}
	}

}
