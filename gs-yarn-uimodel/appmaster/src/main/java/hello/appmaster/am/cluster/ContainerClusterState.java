/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hello.appmaster.am.cluster;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple state machine for container clusters.
 *
 * @author Janne Valkealahti
 *
 */
public class ContainerClusterState {

	/** Current state with initial as a start state */
	private volatile State currentState = State.INITIAL;

	/** Map of legal state transfers */
	private static Map<State, EnumSet<State>> stateTransitionMap;

	static {
		stateTransitionMap = new HashMap<State, EnumSet<State>>();
		stateTransitionMap.put(State.INITIAL, EnumSet.of(State.RUNNING, State.ALLOCATING));
		stateTransitionMap.put(State.ALLOCATING, EnumSet.of(State.RUNNING));
		stateTransitionMap.put(State.RUNNING, EnumSet.of(State.ALLOCATING, State.STOPPING));
		stateTransitionMap.put(State.STOPPING, EnumSet.of(State.STOPPED));
	}

	/**
	 * Process command for this machine.
	 *
	 * @param event the event
	 */
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

	/**
	 * Possible states of this machine.
	 */
	public enum State {
		INITIAL,
		RUNNING,
		ALLOCATING,
		STOPPING,
		STOPPED
	}

	/**
	 * Possible events controlling this machine.
	 */
	public enum Event {
		START,
		CONFIGURE,
		CONTINUE,
		STOP,
		END
	}

}
