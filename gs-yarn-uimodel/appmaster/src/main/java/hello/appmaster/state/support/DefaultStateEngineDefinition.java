package hello.appmaster.state.support;

import hello.appmaster.state.StateEngineDefinition;

public class DefaultStateEngineDefinition implements StateEngineDefinition {

	private StateEngineDefinition stateEngineDefinition;

	public DefaultStateEngineDefinition() {

	}

	public DefaultStateEngineDefinition(StateEngineDefinition other) {
		this.stateEngineDefinition = other;
	}

	public StateEngineDefinition getStateEngineDefinition() {
		return stateEngineDefinition;
	}

}
