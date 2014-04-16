package hello.appmaster.state;

import hello.appmaster.state.annotation.Transition;
import hello.appmaster.state.annotation.Transitional;
import hello.appmaster.state.support.StateEngineCallback;
import hello.appmaster.state.support.StateEngineOperations;
import hello.appmaster.state.support.StateEngineTemplate;

public class FooTests {

	public void testTemplate() {

		StateEngineOperations operations = new StateEngineTemplate();
		Boolean result = operations.execute(new StateEngineCallback<Boolean>() {
			@Override
			public Boolean doInStateEngine(StateEngine engine) {
//				engine.
				return true;
			}
		});

	}

	public void testStateChange() {

	}

	private static class Foo1 {
		@Transition(from = "state1", to = "state2")
		void state1ToState2() {
		}
	}

	private static class Foo2 {
		@Transitional
		void state1ToState2() {
		}
	}

}
