package hello.appmaster.am.mvc;

//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import hello.appmaster.am.cluster.ClusterAppmaster;
import hello.appmaster.am.cluster.ContainerCluster;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.yarn.am.YarnAppmaster;

/**
 * Controller for {@link ClusterAppmaster} managed container clusters.
 *
 * @author Janne Valkealahti
 *
 */
@Controller
@RequestMapping("/yarn/clusters")
public class ClustersController {

	private final static Log log = LogFactory.getLog(ClustersController.class);

	private ClusterAppmaster appmaster;

	@Autowired
	public void setYarnAppmaster(YarnAppmaster yarnAppmaster) {
		if (yarnAppmaster instanceof ClusterAppmaster) {
			appmaster = ((ClusterAppmaster)yarnAppmaster);
		}
	}

//	@RequestMapping(method = RequestMethod.GET)
//	public HttpEntity<ClustersStatesResponse> clustersStates() {
//		Collection<ContainerCluster> clusters = appmaster.getClusters();
//		ClustersStatesResponse response = new ClustersStatesResponse(clusters);
//		return new ResponseEntity<ClustersStatesResponse>(response, HttpStatus.OK);
//	}
//
//	@RequestMapping(method = RequestMethod.PUT)
//	public HttpEntity<CreateClusterResponse> createCluster() {
//		CreateClusterResponse response = new CreateClusterResponse();
//		return new ResponseEntity<CreateClusterResponse>(response, HttpStatus.OK);
//	}

//	@RequestMapping("/{id}")
//	public HttpEntity<ClustersStatesResponse> cluster(@PathVariable("id") String id) {
//		ClustersStatesResponse clusters = new ClustersStatesResponse();
//		return new ResponseEntity<ClustersStatesResponse>(clusters, HttpStatus.OK);
//	}


//	private static final String TEMPLATE = "Hello, %s!";
//
//    @RequestMapping("/greeting")
//    @ResponseBody
//    public HttpEntity<Greeting> greeting(
//            @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
//
//        Greeting greeting = new Greeting(String.format(TEMPLATE, name));
//        greeting.add(linkTo(methodOn(ClustersController.class).greeting(name)).withSelfRel());
//
//        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
//    }



//	@RequestMapping("/yarn/pump")
//	public @ResponseBody
//	String pump() {
//		((ManagedClustersAppmaster)appmaster).pump();
//		return "ok";
//	}
//
//	@RequestMapping("/yarn/allocate")
//	public @ResponseBody
//	String allocate(@RequestParam(value = "hosts", required = false) String[] hosts,
//			@RequestParam(value = "racks", required = false) String[] racks,
//			@RequestParam(value = "any", required = false) Integer any) {
//		log.info("XXX hosts:" + StringUtils.arrayToCommaDelimitedString(hosts));
//		log.info("XXX racks:" + StringUtils.arrayToCommaDelimitedString(racks));
//		log.info("XXX any:" + any);
//		((ManagedClustersAppmaster)appmaster).pump2(hosts, racks, any);
//		return "ok";
//	}



}