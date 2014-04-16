package hello.appmaster.am.grid;

import hello.appmaster.am.grid.listener.GridListener;

import java.util.Collection;

import org.apache.hadoop.yarn.api.records.ContainerId;

/**
 * Container grid allows to track members in a grid. Contract for
 * interacting with user depends on the actual implementation which
 * can be either build-in discovery system or static without any
 * discovery logic.
 * <p>
 * If implementation has build-in discovery a use of setter methods outside
 * of the implementation should be discouraged. Although it is up to
 * the implementation what is actually supported.
 * <p>
 * Implementation can also be static in terms of that there are
 * no logic for discovery and in case of that user is responsible
 * to feed data into the implementation. In case of that the implementation
 * would only have structure to store the necessary information.
 * This is useful in cases where access to grid system needs to be abstracted
 * still keeping the grid logic in its own implementation.
 * <p>
 * This interface and its extended interfaces are strongly typed
 * order to allow extending returned types (i.e. {@link GridMember}
 * to suit the needs of a custom implementations.
 *
 *
 * @author Janne Valkealahti
 *
 */
public interface Grid {

	/**
	 * Gets collection of container nodes know to the grid system or
	 * empty collection if there are no known nodes.
	 *
	 * @return Collection of grid nodes
	 */
	Collection<GridMember> getMembers();

	/**
	 * Gets a container node by its identifier.
	 *
	 * @param id the container node identifier
	 * @return Container node or <code>NULL</code> if node doesn't exist
	 */
	GridMember getMember(ContainerId id);

	/**
	 * Adds a new container node.
	 * <p>
     * If a grid refuses to add a particular node for any reason
     * other than that it already contains the node, it <i>must</i> throw
     * an exception (rather than returning <tt>false</tt>).  This preserves
     * the invariant that a grid always contains the specified node
     * after this call returns.
     *
	 * @param member the container node
	 * @return <tt>true</tt> if this grid changed as a result of the call
	 */
	boolean addMember(GridMember member);

	/**
	 * Removes a container node by its identifier.
	 * <p>
	 * Removes a single instance of the specified node from this
     * grid, if it is present.
	 *
	 * @param id the container node identifier
	 * @return <tt>true</tt> if a node was removed as a result of this call
	 */
	boolean removeMember(ContainerId id);

	/**
	 * Adds a listener to be notified of grid container node events.
	 *
	 * @param listener the container grid listener
	 */
	void addGridListener(GridListener listener);

}
