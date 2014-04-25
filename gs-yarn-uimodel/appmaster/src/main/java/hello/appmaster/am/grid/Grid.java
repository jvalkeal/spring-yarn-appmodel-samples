package hello.appmaster.am.grid;

import hello.appmaster.am.grid.listener.GridListener;
import hello.appmaster.am.grid.support.GridMemberInterceptor;

import java.util.Collection;

import org.apache.hadoop.yarn.api.records.ContainerId;

/**
 * Grid allows to track members in a grid.
 *
 * @author Janne Valkealahti
 *
 */
public interface Grid {

	/**
	 * Gets collection of grid members know to the grid system or
	 * empty collection if there are no known members.
	 *
	 * @return Collection of grid members
	 */
	Collection<GridMember> getMembers();

	/**
	 * Gets a grid member.
	 *
	 * @param id the container id identifier
	 * @return Grid member or <code>NULL</code> if member doesn't exist
	 */
	GridMember getMember(ContainerId id);

	/**
	 * Adds a new grid member.
	 * <p>
     * If a grid refuses to add a particular member for any reason
     * other than that it already contains the member, it <i>must</i> throw
     * an exception (rather than returning <tt>false</tt>).  This preserves
     * the invariant that a grid always contains the specified node
     * after this call returns.
     *
	 * @param member the grid member
	 * @return <tt>true</tt> if this grid changed as a result of the call
	 */
	boolean addMember(GridMember member);

	/**
	 * Removes a grid member.
	 * <p>
	 * Removes a single instance of the specified member from this
     * grid, if it is present.
	 *
	 * @param id the container id identifier
	 * @return <tt>true</tt> if a member was removed as a result of this call
	 */
	boolean removeMember(ContainerId id);

	/**
	 * Adds a listener to be notified of grid members events.
	 *
	 * @param listener the grid listener
	 */
	void addGridListener(GridListener listener);

	void addInterceptor(GridMemberInterceptor interceptor);

}
