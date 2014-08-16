package hello.appmaster;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.yarn.am.grid.GridMember;
import org.springframework.yarn.am.grid.support.AbstractGridProjection;
import org.springframework.yarn.am.grid.support.SatisfyStateData;

public class CustomGridProjection extends AbstractGridProjection {

	@Override
	public SatisfyStateData getSatisfyState() {
		SatisfyStateData data = new SatisfyStateData();

		// simply add delta for current size vs. requested count
		int delta = getProjectionData().getAny() - getMembers().size();
		data.getAllocateData().addAny(Math.max(delta, 0));

		// Simply remove using negative delta
		int removeCount = Math.max(-delta, 0);
		Iterator<GridMember> iterator = getMembers().iterator();
		ArrayList<GridMember> remove = new ArrayList<GridMember>();
		while (iterator.hasNext() && removeCount-- > 0) {
			remove.add(iterator.next());
		}
		data.setRemoveData(remove);

		return data;
	}

	@Override
	public boolean acceptMember(GridMember member) {
		if (!isSamePriority(member)) {
			return false;
		}

		String host = member.getContainer().getNodeId().getHost();
		for (GridMember m : getMembers()) {
			if (m.getContainer().getNodeId().getHost().equals(host)) {
				return false;
			}
		}

		if (getMembers().size() < getProjectionData().getAny()) {
			return addMember(member);
		} else {
			return false;
		}
	}

}
