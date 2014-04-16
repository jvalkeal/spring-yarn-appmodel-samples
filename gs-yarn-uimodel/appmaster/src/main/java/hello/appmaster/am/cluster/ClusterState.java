package hello.appmaster.am.cluster;

public class ClusterState {

	private State state = State.INITIAL;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public enum State {
		INITIAL,
		STARTING,
		STARTED,
		STOPPING,
		STOPPED
	}

}
