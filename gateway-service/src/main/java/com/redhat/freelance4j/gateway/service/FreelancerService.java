package com.redhat.freelance4j.gateway.service;

import com.redhat.freelance4j.gateway.model.Freelancer;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author boonp
 * @since 08/09/2019 6:09 PM
 */
@ApplicationScoped
public class FreelancerService {

    private WebTarget freelancerService;

    @Inject
    @ConfigurationValue("freelance4j.freelancer-url")
    private String freelancerUrl;


    public List<Freelancer> getFreelancers() {
        Response response = freelancerService.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<Freelancer>>() {
            });
        } else if (response.getStatus() == 204) {
            return null;
        } else {
            throw new ServiceUnavailableException("Freelancer Service is unavailable");
        }
    }


    public Freelancer getFreelancerById(String freelancerId) {
        Response response = freelancerService.path(freelancerId).request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            return response.readEntity(Freelancer.class);
        } else if (response.getStatus() == 204) {
            return null;
        } else {
            throw new ServiceUnavailableException("Freelancer Service is unavailable");
        }
    }


    @PostConstruct
    public void init() {
        freelancerService = ((ResteasyClientBuilder) ClientBuilder.newBuilder())
            .connectionPoolSize(10).build().target(freelancerUrl).path("freelancers");
    }
}
