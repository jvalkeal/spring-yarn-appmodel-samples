package hello.appmaster.am.grid.support;

import hello.appmaster.am.grid.Grid;
import hello.appmaster.am.grid.GridMember;
import hello.appmaster.am.grid.listener.GridListener;
import hello.appmaster.am.grid.listener.DefaultGridListener;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.yarn.api.records.ContainerId;
import org.springframework.util.Assert;

/**
 * Simple {@code ContainerGrid} base implementation keeping {@code ContainerNode}s
 * in a {@code ConcurrentHashMap}.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractGrid implements Grid {

	/** Listener dispatcher for container grid events */
	private DefaultGridListener gridListeners = new DefaultGridListener();

	private ConcurrentHashMap<ContainerId, GridMember> nodes = new ConcurrentHashMap<ContainerId, GridMember>();

	@Override
	public Collection<GridMember> getMembers() {
		return nodes.values();
	}

	@Override
	public GridMember getMember(ContainerId id) {
		return nodes.get(id);
	}

	@Override
	public boolean addMember(GridMember node) {
		Assert.notNull(node, "Node must not be null");
		if (nodes.putIfAbsent(node.getId(), node) == null) {
			notifyMemberAdded(node);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeMember(ContainerId id) {
		Assert.notNull(id, "Node identifier must not be null");
		GridMember removed = nodes.remove(id);
		if (removed != null) {
			notifyMemberRemoved(removed);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void addGridListener(GridListener listener) {
		gridListeners.register(listener);
	}

	/**
	 * Notifies registered {@code ContainerGridListener}s that
	 * a {@code ContainerNode} has been added to a {@code ContainerGrid}.
	 *
	 * @param member the node
	 */
	protected void notifyMemberAdded(GridMember member) {
		gridListeners.memberAdded(member);
	}

	/**
	 * Notifies registered {@code ContainerGridListener}s that
	 * a {@code ContainerNode} has been removed from a {@code ContainerGrid}.
	 *
	 * @param member the node
	 */
	protected void notifyMemberRemoved(GridMember member) {
		gridListeners.memberRemoved(member);
	}

}
