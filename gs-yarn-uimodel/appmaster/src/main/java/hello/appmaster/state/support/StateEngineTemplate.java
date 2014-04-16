package hello.appmaster.state.support;

import hello.appmaster.state.StateEngine;
import hello.appmaster.state.StateEngineDefinition;
import hello.appmaster.state.StateManager;

public class StateEngineTemplate extends DefaultStateEngineDefinition implements StateEngineOperations {

	private StateManager stateManager;

	public StateEngineTemplate() {
	}

	public StateEngineTemplate(StateManager stateManager) {
		this.stateManager = stateManager;
	}

	public StateEngineTemplate(StateManager stateManager, StateEngineDefinition stateEngineDefinition) {
		super(stateEngineDefinition);
		this.stateManager = stateManager;
	}

	@Override
	public <T> T execute(StateEngineCallback<T> action) {
		StateEngine stateEngine = stateManager.getStateEngine(getStateEngineDefinition());
		T result = action.doInStateEngine(stateEngine);
		return result;
	}

}
