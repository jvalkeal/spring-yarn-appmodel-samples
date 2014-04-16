package hello.appmaster.state.support;

import hello.appmaster.state.StateEngine;
import hello.appmaster.state.StateEngineDefinition;
import hello.appmaster.state.StateManager;

public abstract class AbstractStateManager implements StateManager {

	@Override
	public StateEngine getStateEngine(StateEngineDefinition definition) {
		return null;
	}

}
