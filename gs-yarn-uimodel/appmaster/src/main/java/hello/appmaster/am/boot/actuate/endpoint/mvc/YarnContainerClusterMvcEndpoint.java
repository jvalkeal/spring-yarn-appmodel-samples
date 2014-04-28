package hello.appmaster.am.boot.actuate.endpoint.mvc;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collection;

import hello.appmaster.am.boot.actuate.endpoint.YarnContainerClusterEndpoint;
import hello.appmaster.am.cluster.ContainerCluster;
import hello.appmaster.am.grid.GridProjection;
import hello.appmaster.am.grid.support.AnyGridProjection;
import hello.appmaster.am.grid.support.HostsGridProjection;
import hello.appmaster.am.grid.support.ProjectionData;

import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * {@link MvcEndpoint} adding rest api for {@link YarnContainerClusterEndpoint}.
 *
 * @author Janne Valkealahti
 *
 */
public class YarnContainerClusterMvcEndpoint extends EndpointMvcAdapter {

	private final YarnContainerClusterEndpoint delegate;

	public YarnContainerClusterMvcEndpoint(YarnContainerClusterEndpoint delegate) {
		super(delegate);
		this.delegate = delegate;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object invoke() {
		Collection<ContainerCluster> clusters = delegate.getClusters().values();
		YarnContainerClusterEndpointResponse response = new YarnContainerClusterEndpointResponse(clusters);
		for (ContainerCluster cluster : clusters) {
			response.add(
					linkTo(
						methodOn(YarnContainerClusterMvcEndpoint.class).
						cluster(cluster.getId())
					).
					withRel("cluster")
				);
		}

		return response;
	}

	/**
	 * Creates a new container cluster.
	 *
	 * @param request the container cluster create request
	 * @return the container cluster create response
	 */
	@RequestMapping(method = RequestMethod.POST, headers = {"content-type=application/json"})
	@ResponseBody
	public HttpEntity<CreateContainerClusterResponse> createCluster(@RequestBody CreateContainerClusterRequest request) {

		GridProjection projection = null;
		if (CreateContainerClusterRequest.ProjectionType.HOSTS.equals(request.getProjection())) {
			projection = new HostsGridProjection();
		} else if (CreateContainerClusterRequest.ProjectionType.ANY.equals(request.getProjection())) {
			projection = new AnyGridProjection();
		}
		ProjectionData projectionData = new ProjectionData();
		if (request.getProjectionData().getAny() != null) {
			projectionData.setAny(request.getProjectionData().getAny());
		}
		if (request.getProjectionData().getHosts() != null) {
			projectionData.setHosts(request.getProjectionData().getHosts());
		}
		if (request.getProjectionData().getRacks() != null) {
			projectionData.setRacks(request.getProjectionData().getRacks());
		}
		projection.setProjectionData(projectionData);

		delegate.createCluster(request.getClusterId(), projection);

		CreateContainerClusterResponse response = new CreateContainerClusterResponse();
		response.setClusterId(request.getClusterId());

		response.add(
			linkTo(
				methodOn(YarnContainerClusterMvcEndpoint.class).
				cluster(request.getClusterId())
			).
			withSelfRel()
		);

		return new ResponseEntity<CreateContainerClusterResponse>(response, HttpStatus.OK);
	}

	/**
	 * Gets a status of a specific container cluster.
	 *
	 * @param clusterId the container cluster identifier
	 * @return the container cluster status response
	 */
	@RequestMapping(value = "/{clusterId}", method = RequestMethod.GET)
	public HttpEntity<ContainerClusterStatusResponse> cluster(@PathVariable("clusterId") String clusterId) {
		ContainerCluster cluster = delegate.getClusters().get(clusterId);
		if (cluster == null) {
			throw new NoSuchClusterException("No such cluster: " + clusterId);
		}
		return new ResponseEntity<ContainerClusterStatusResponse>(new ContainerClusterStatusResponse(cluster), HttpStatus.OK);
	}

	@RequestMapping(value = "/{clusterId}/start", method = RequestMethod.POST)
	public HttpEntity<String> start(@PathVariable("clusterId") String clusterId) {
		delegate.startCluster(clusterId);
		return new ResponseEntity<String>("start ok", HttpStatus.OK);
	}

	@RequestMapping(value = "/{clusterId}/stop", method = RequestMethod.POST)
	public HttpEntity<String> stop(@PathVariable("clusterId") String clusterId) {
		delegate.stopCluster(clusterId);
		return new ResponseEntity<String>("stop ok", HttpStatus.OK);
	}

	@RequestMapping(value = "/{clusterId}", method = RequestMethod.PATCH)
	public HttpEntity<String> modify(@PathVariable("clusterId") String clusterId, @RequestBody CreateContainerClusterRequest request) {
		ContainerCluster cluster = delegate.getClusters().get(clusterId);
		if (cluster == null) {
			throw new NoSuchClusterException("No such cluster: " + clusterId);
		}
		ProjectionData data = new ProjectionData();
		if (request.getProjectionData().getAny() != null) {
			data.setAny(request.getProjectionData().getAny());
		}
		if (request.getProjectionData().getHosts() != null) {
			data.setHosts(request.getProjectionData().getHosts());
		}
		if (request.getProjectionData().getRacks() != null) {
			data.setRacks(request.getProjectionData().getRacks());
		}
		delegate.modifyCluster(clusterId, data);

		return new ResponseEntity<String>("modify ok", HttpStatus.OK);
	}

	@SuppressWarnings("serial")
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such cluster")
	public static class NoSuchClusterException extends RuntimeException {

		public NoSuchClusterException(String string) {
			super(string);
		}

	}

}
