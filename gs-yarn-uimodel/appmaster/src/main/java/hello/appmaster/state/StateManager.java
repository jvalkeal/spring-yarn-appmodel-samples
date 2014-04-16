package hello.appmaster.state;

public interface StateManager {

	StateEngine getStateEngine(StateEngineDefinition definition);

}
