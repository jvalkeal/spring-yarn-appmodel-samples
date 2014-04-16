package hello.appmaster.am.cluster;

import org.apache.hadoop.yarn.api.records.Resource;

public class ClusterDescriptor {

	private int count;
	private Resource capability;
	private State state = State.OFFLINE;

	public ClusterDescriptor(int count, Resource capability, State state) {
		this.count = count;
		this.capability = capability;
		this.state = state;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Resource getCapability() {
		return capability;
	}

	public void setCapability(Resource capability) {
		this.capability = capability;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public enum State {
		OFFLINE,
		ONLINE
	}

}
