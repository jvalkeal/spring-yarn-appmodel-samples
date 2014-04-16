package hello.appmaster.am.grid.listener;

import hello.appmaster.am.grid.GridProjection;
import hello.appmaster.am.grid.GridMember;

import java.util.Iterator;

/**
 * Composite listener for handling container group events.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultProjectedGridListener extends AbstractCompositeListener<ProjectedGridListener>
		implements ProjectedGridListener {

	@Override
	public void projectionAdded(GridProjection group) {
		for (Iterator<ProjectedGridListener> iterator = getListeners().reverse(); iterator.hasNext();) {
			iterator.next().projectionAdded(group);
		}
	}

	@Override
	public void projectionRemoved(GridProjection group) {
		for (Iterator<ProjectedGridListener> iterator = getListeners().reverse(); iterator.hasNext();) {
			iterator.next().projectionRemoved(group);
		}
	}

	@Override
	public void memberAdded(GridProjection group, GridMember node) {
		for (Iterator<ProjectedGridListener> iterator = getListeners().reverse(); iterator.hasNext();) {
			iterator.next().memberAdded(group, node);
		}
	}

	@Override
	public void memberRemoved(GridProjection group, GridMember node) {
		for (Iterator<ProjectedGridListener> iterator = getListeners().reverse(); iterator.hasNext();) {
			iterator.next().memberRemoved(group, node);
		}
	}

}
