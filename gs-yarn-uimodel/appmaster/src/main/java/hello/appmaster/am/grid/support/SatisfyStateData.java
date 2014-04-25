package hello.appmaster.am.grid.support;

import hello.appmaster.am.grid.GridMember;

import java.util.ArrayList;
import java.util.List;

import org.springframework.yarn.am.allocate.ContainerAllocateData;

public class SatisfyStateData {

	private List<GridMember> remove;

	private ContainerAllocateData allocateData;

	public SatisfyStateData() {
		remove = new ArrayList<GridMember>();
		allocateData = new ContainerAllocateData();
	}

	public SatisfyStateData(List<GridMember> remove, ContainerAllocateData allocateData) {
		this.remove = remove;
		this.allocateData = allocateData;
	}

	public List<GridMember> getRemoveData() {
		return remove;
	}

	public void setRemoveData(List<GridMember> remove) {
		this.remove = remove;
	}

	public ContainerAllocateData getAllocateData() {
		return allocateData;
	}

	public void setAllocateData(ContainerAllocateData allocateData) {
		this.allocateData = allocateData;
	}

}
