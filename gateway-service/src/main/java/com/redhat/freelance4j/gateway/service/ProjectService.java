package com.redhat.freelance4j.gateway.service;

import com.redhat.freelance4j.gateway.model.Project;
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
 * @since 08/09/2019 5:43 PM
 */
@ApplicationScoped
public class ProjectService {

    private WebTarget projectService;

    @Inject
    @ConfigurationValue("freelance4j.project-url")
    private String projectUrl;


    public List<Project> getProjects() {
        Response response = projectService.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<Project>>() {
            });
        } else if (response.getStatus() == 404) {
            return null;
        } else {
            throw new ServiceUnavailableException("Project Service is unavailable");
        }
    }


    public Project getProjectById(String projectId) {
        Response response = projectService.path(projectId).request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            return response.readEntity(Project.class);
        } else if (response.getStatus() == 404) {
            return null;
        } else {
            throw new ServiceUnavailableException("Project Service is unavailable");
        }
    }


    public List<Project> getProjectsByStatus(String status) {
        Response response = projectService.path("status").path(status).request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<Project>>() {
            });
        } else if (response.getStatus() == 404) {
            return null;
        } else {
            throw new ServiceUnavailableException("Project Service is unavailable");
        }
    }


    @PostConstruct
    public void init() {
        projectService = ((ResteasyClientBuilder) ClientBuilder.newBuilder())
            .connectionPoolSize(10).build().target(projectUrl).path("projects");
    }

}
