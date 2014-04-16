package hello.appmaster.am.boot.actuate.endpoint.mvc;

import hello.appmaster.am.boot.actuate.endpoint.YarnContainerClusterEndpoint;
import hello.appmaster.am.cluster.ContainerCluster;
import hello.appmaster.am.mvc.ClusterStateResponse;

import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

public class YarnContainerClusterMvcEndpoint extends EndpointMvcAdapter {

	private final YarnContainerClusterEndpoint delegate;

	public YarnContainerClusterMvcEndpoint(YarnContainerClusterEndpoint delegate) {
		super(delegate);
		this.delegate = delegate;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public HttpEntity<ClusterStateResponse> cluster(@PathVariable("id") String id) {
		ContainerCluster containerCluster = delegate.invoke().get(id);
		if (containerCluster == null) {
			throw new NoSuchClusterException("No such cluster: " + id);
		}
		return new ResponseEntity<ClusterStateResponse>(new ClusterStateResponse(containerCluster), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/start", method = RequestMethod.GET)
	public HttpEntity<String> start(@PathVariable("id") String id) {
		delegate.startCluster(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/stop", method = RequestMethod.GET)
	public HttpEntity<String> stop(@PathVariable("id") String id) {
		delegate.stopCluster(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@SuppressWarnings("serial")
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such cluster")
	public static class NoSuchClusterException extends RuntimeException {

		public NoSuchClusterException(String string) {
			super(string);
		}

	}
}
