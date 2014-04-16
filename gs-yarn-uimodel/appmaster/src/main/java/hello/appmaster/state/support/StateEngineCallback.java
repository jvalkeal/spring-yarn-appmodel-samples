package hello.appmaster.state.support;

import hello.appmaster.state.StateEngine;

public interface StateEngineCallback<T> {

	T doInStateEngine(StateEngine engine);

}
