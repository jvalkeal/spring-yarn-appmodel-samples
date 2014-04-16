package hello.appmaster.am.grid.listener;

import java.util.List;

/**
 * Base implementation for all composite listeners.
 *
 * @author Janne Valkealahti
 *
 * @param <T> the type of the listener
 */
public class AbstractCompositeListener<T> {

	/** List of ordered composite listeners */
	private OrderedComposite<T> listeners;

	/**
	 * Constructs instance with an empty listener list.
	 */
	public AbstractCompositeListener() {
		listeners = new OrderedComposite<T>();
	}

	/**
	 * Sets the list of listeners. This clears
	 * all existing listeners.
	 *
	 * @param listeners the new listeners
	 */
	public void setListeners(List<? extends T> listeners) {
		this.listeners.setItems(listeners);
	}

	/**
	 * Register a new listener.
	 *
	 * @param listener the listener
	 */
	public void register(T listener) {
		listeners.add(listener);
	}

	/**
	 * Gets the listeners.
	 *
	 * @return the listeners
	 */
	public OrderedComposite<T> getListeners() {
		return listeners;
	}

}
