package hello.appmaster.am.grid.support;

import hello.appmaster.am.grid.Grid;
import hello.appmaster.am.grid.GridMember;

public interface GridMemberInterceptor {

	GridMember preAdd(GridMember member, Grid grid);

}
