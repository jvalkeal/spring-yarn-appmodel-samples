package hello.appmaster.am.grid.listener;

import hello.appmaster.am.grid.GridMember;

import java.util.Iterator;

/**
 * Composite listener for handling grid container node events.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultGridListener extends
		AbstractCompositeListener<GridListener> implements GridListener {

	@Override
	public void memberAdded(GridMember node) {
		for (Iterator<GridListener> iterator = getListeners().reverse(); iterator.hasNext();) {
			iterator.next().memberAdded(node);
		}
	}

	@Override
	public void memberRemoved(GridMember node) {
		for (Iterator<GridListener> iterator = getListeners().reverse(); iterator.hasNext();) {
			iterator.next().memberRemoved(node);
		}
	}

}
