package hello.appmaster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.yarn.am.YarnAppmaster;
import org.springframework.yarn.am.monitor.ContainerMonitor;
import org.springframework.yarn.am.monitor.DefaultContainerMonitor;

@Controller
public class StatusController {

	private final static Log log = LogFactory.getLog(StatusController.class);

	@Autowired
	private YarnAppmaster appmaster;

	@RequestMapping("/yarn")
	public @ResponseBody
	String status() {
		ContainerMonitor monitor = ((GridAppmaster)appmaster).getMonitor();
		String msg = "";
		if (monitor instanceof DefaultContainerMonitor) {
			msg = ((DefaultContainerMonitor) monitor).toDebugString();
		}
		return "hello " + msg;
	}

	@RequestMapping("/yarn/pump")
	public @ResponseBody
	String pump() {
		((GridAppmaster)appmaster).pump();
		return "ok";
	}

	@RequestMapping("/yarn/allocate")
	public @ResponseBody
	String allocate(@RequestParam(value = "hosts", required = false) String[] hosts,
			@RequestParam(value = "racks", required = false) String[] racks,
			@RequestParam(value = "any", required = false) Integer any) {
		log.info("XXX hosts:" + StringUtils.arrayToCommaDelimitedString(hosts));
		log.info("XXX racks:" + StringUtils.arrayToCommaDelimitedString(racks));
		log.info("XXX any:" + any);
		((GridAppmaster)appmaster).pump2(hosts, racks, any);
		return "ok";
	}

}