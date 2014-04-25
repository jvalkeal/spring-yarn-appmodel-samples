package hello.appmaster.am.cluster;

/**
 * Simple state engine.
 *
 * @author Janne Valkealahti
 *
 */
public class ContainerClusterState {

	private State state = State.INITIAL;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setStarting() {
		if (state.equals(State.INITIAL) || state.equals(State.STARTING) || state.equals(State.STARTED)) {
			state = State.STARTING;
		} else {
			throw getException(state, State.STARTING);
		}
	}

	public boolean isStarting() {
		return state.equals(State.STARTING);
	}

	public void setStarted() {
		if (state.equals(State.STARTING) || state.equals(State.STARTED)) {
			state = State.STARTED;
		} else {
			throw getException(state, State.STARTED);
		}
	}

	public boolean isStarted() {
		return state.equals(State.STARTED);
	}

	public void setStopping() {
		if (state.equals(State.STARTING) || state.equals(State.STARTED) || state.equals(State.STOPPING)) {
			state = State.STOPPING;
		} else {
			throw getException(state, State.STOPPING);
		}
	}

	public boolean isStopping() {
		return state.equals(State.STOPPING);
	}

	public void setStopped() {
		if (state.equals(State.STOPPING) || state.equals(State.STOPPED)) {
			state = State.STOPPED;
		} else {
			throw getException(state, State.STOPPED);
		}
	}

	private static IllegalStateException getException(State from, State to) {
		return new IllegalStateException("Can't switch to state " + to + " from " + from);
	}

	public enum State {

		/**
		 * Initial state when cluster has been created but not yet started
		 */
		INITIAL,
		/**
		 * State indicating that cluster start has been requested but no
		 * allocation requests has been made
		 */
		STARTING,
		/**
		 * State indicating that cluster has been entered into allocation mode
		 */
		STARTED,
		/**
		 * State indicating that cluster has been entered into stopping mode
		 */
		STOPPING,
		/**
		 * State indicating that cluster has been been stopped and there are no
		 * running containers or pending allocations
		 */
		STOPPED
	}

}
