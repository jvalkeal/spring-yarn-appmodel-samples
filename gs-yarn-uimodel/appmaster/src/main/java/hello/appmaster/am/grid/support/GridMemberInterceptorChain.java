package hello.appmaster.am.grid.support;

import hello.appmaster.am.grid.Grid;
import hello.appmaster.am.grid.GridMember;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GridMemberInterceptorChain {

	private static final Log log = LogFactory.getLog(GridMemberInterceptorChain.class);

	private final List<GridMemberInterceptor> interceptors = new CopyOnWriteArrayList<GridMemberInterceptor>();

	public boolean set(List<GridMemberInterceptor> interceptors) {
		synchronized (this.interceptors) {
			this.interceptors.clear();
			return this.interceptors.addAll(interceptors);
		}
	}

	public boolean add(GridMemberInterceptor interceptor) {
		return this.interceptors.add(interceptor);
	}

	public List<GridMemberInterceptor> getInterceptors() {
		return Collections.unmodifiableList(this.interceptors);
	}

	public GridMember preAdd(GridMember member, Grid grid) {

		// if we don't have interceptors just pass
		// through grid member
		if (interceptors.isEmpty()) {
			return member;
		}

		for (GridMemberInterceptor interceptor : interceptors) {
			if (interceptor.preAdd(member, grid) != null) {
				return member;
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Returning null from preAdd because all interceptors returned null");
		}
		return null;
	}

}
