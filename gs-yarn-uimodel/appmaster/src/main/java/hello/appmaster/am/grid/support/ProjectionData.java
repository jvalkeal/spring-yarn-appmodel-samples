package hello.appmaster.am.grid.support;

import java.util.HashMap;
import java.util.Map;

public class ProjectionData {

	private Integer any = 0;
	private Map<String, Integer> hosts = new HashMap<String, Integer>();
	private Map<String, Integer> racks = new HashMap<String, Integer>();

	public ProjectionData() {
	}

	public ProjectionData(int any) {
		this.any = any;
	}

	public ProjectionData(int any, Map<String, Integer> hosts, Map<String, Integer> racks) {
		this.any = any;
		this.hosts = hosts;
		this.racks = racks;
	}

	public Integer getAny() {
		return any;
	}

	public void setAny(int any) {
		this.any = any;
	}

	public Map<String, Integer> getHosts() {
		return hosts;
	}

	public void setHosts(Map<String, Integer> hosts) {
		this.hosts = hosts;
	}

	public Map<String, Integer> getRacks() {
		return racks;
	}

	public void setRacks(Map<String, Integer> racks) {
		this.racks = racks;
	}

	public void setHost(String host, Integer count) {
		hosts.put(host, count);
	}

}
