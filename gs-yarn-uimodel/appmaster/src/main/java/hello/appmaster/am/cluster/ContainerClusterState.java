package hello.appmaster.am.cluster;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple state machine.
 *
 * @author Janne Valkealahti
 *
 */
public class ContainerClusterState {

	private volatile State currentState = State.INITIAL;

	private static Map<State, EnumSet<State>> stateTransitionMap;

	static {
		stateTransitionMap = new HashMap<State, EnumSet<State>>();
		stateTransitionMap.put(State.INITIAL, EnumSet.of(State.RUNNING, State.ALLOCATING));
		stateTransitionMap.put(State.ALLOCATING, EnumSet.of(State.RUNNING));
		stateTransitionMap.put(State.RUNNING, EnumSet.of(State.ALLOCATING, State.STOPPING));
		stateTransitionMap.put(State.STOPPING, EnumSet.of(State.STOPPED));
	}

	public void command(String command) {
		Event event = Event.valueOf(Event.class, command.toUpperCase());
		command(event);
	}

	public void command(Event event) {
		switch (event) {
		case START:
			setCurrent(State.RUNNING);
			break;
		case CONFIGURE:
			setCurrent(State.ALLOCATING);
			break;
		case CONTINUE:
			setCurrent(State.RUNNING);
			break;
		case STOP:
			setCurrent(State.STOPPING);
			break;
		case END:
			setCurrent(State.STOPPED);
			break;
		default:
			break;
		}
	}

	public State getState() {
		return currentState;
	}

	private State setCurrent(State desiredState) {
		if (!isReachable(desiredState)) {
			throw getException(currentState, desiredState);
		}
		return currentState = desiredState;
	}

	private boolean isReachable(State desiredState) {
		if (desiredState == currentState) {
			return true;
		} else {
			return stateTransitionMap.get(currentState).contains(desiredState);
		}
	}

	private static IllegalStateException getException(State from, State to) {
		return new IllegalStateException("Can't switch to state " + to + " from " + from);
	}

	@Override
	public String toString() {
		return "ContainerClusterState [getState()=" + getState() + "]";
	}

	public enum State {
		INITIAL,
		RUNNING,
		ALLOCATING,
		STOPPING,
		STOPPED
	}

	public enum Event {
		START,
		CONFIGURE,
		CONTINUE,
		STOP,
		END
	}

}
