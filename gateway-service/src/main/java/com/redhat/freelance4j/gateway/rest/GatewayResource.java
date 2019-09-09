package com.redhat.freelance4j.gateway.rest;

import com.redhat.freelance4j.gateway.model.Freelancer;
import com.redhat.freelance4j.gateway.model.Project;
import com.redhat.freelance4j.gateway.service.FreelancerService;
import com.redhat.freelance4j.gateway.service.ProjectService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/gateway")
/**
 * @author boonp
 * @since 08/09/2019 5:11 PM
 */
public class GatewayResource {

    @Inject
    private ProjectService projectService;

    @Inject
    private FreelancerService freelancerService;


    @GET
    @Path("/projects")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Project> getProjects() {
        return projectService.getProjects();
    }


    @GET
    @Path("/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Project getProjectById(@PathParam("projectId") String projectId) {
        return projectService.getProjectById(projectId);
    }


    @GET
    @Path("/projects/status/{theStatus}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Project> getProjectByStatus(@PathParam("theStatus") String status) {
        return projectService.getProjectsByStatus(status);
    }


    @GET
    @Path("/freelancers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Freelancer> getFreelancers() {
        return freelancerService.getFreelancers();
    }


    @GET
    @Path("/freelancers/{freelancerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Freelancer getFreelancerById(@PathParam("freelancerId") String freelancerId) {
        return freelancerService.getFreelancerById(freelancerId);
    }


}
