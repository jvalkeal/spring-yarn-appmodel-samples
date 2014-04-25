package hello.appmaster.am.grid;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;

/**
 * Interface representing a grid member.
 *
 * @author Janne Valkealahti
 *
 */
public interface GridMember {

	/**
	 * Gets a container member identifier.
	 *
	 * @return container member identifier
	 */
	ContainerId getId();

	Container getContainer();

}
