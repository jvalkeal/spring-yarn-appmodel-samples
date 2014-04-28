package hello.appmaster.am.grid.support;

import hello.appmaster.am.grid.GridMember;
import hello.appmaster.am.grid.GridProjection;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * {@link GridProjection} which accepts any node for its members.
 *
 * @author Janne Valkealahti
 *
 */
public class AnyGridProjection extends AbstractGridProjection {

	private int count;

	public AnyGridProjection() {
		super();
	}

	public AnyGridProjection(int count) {
		super();
		this.count = count;
	}

	@Override
	public SatisfyStateData getSatisfyState() {
		SatisfyStateData data = new SatisfyStateData();

		// simply add delta for current size vs. requested count
		int delta = count - getMembers().size();
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
	public void setProjectionData(ProjectionData data) {
		super.setProjectionData(data);
		count = data.getAny() != null ? data.getAny() : 0;
	}

	@Override
	public boolean acceptMember(GridMember member) {
		if (getMembers().size() < count) {
			return super.acceptMember(member);
		} else {
			return false;
		}
	}

}
