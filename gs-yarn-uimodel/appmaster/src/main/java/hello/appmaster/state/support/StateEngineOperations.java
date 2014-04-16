package hello.appmaster.state.support;


public interface StateEngineOperations {

	<T> T execute(StateEngineCallback<T> action);

}
