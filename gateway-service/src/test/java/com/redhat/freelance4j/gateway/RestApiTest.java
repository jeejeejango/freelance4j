package com.redhat.freelance4j.gateway;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(Arquillian.class)
public class RestApiTest {

    private static String port = "18080";

    private Client client;


    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addPackages(true, RestApplication.class.getPackage())
            .addAsResource("project-local.yml", "project-defaults.yml");
    }


    @Before
    public void before() throws Exception {
        client = ClientBuilder.newClient();
    }


    @After
    public void after() throws Exception {
        client.close();
    }


    @Test
    @RunAsClient
    public void testGetProjects() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("gateway").path("projects");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(200));
        JsonArray values = Json.parse(response.readEntity(String.class)).asArray();
        assertThat(values.size(), equalTo(5));
    }


    @Test
    @RunAsClient
    public void testGetProjectById() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("gateway").path("projects").path("111111");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(200));
        JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
        assertThat(value.getString("projectId", null), equalTo("111111"));
    }


    @Test
    @RunAsClient
    public void testGetProjectsByStatus() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("gateway").path("projects").path("status").path("open");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(200));
        JsonArray values = Json.parse(response.readEntity(String.class)).asArray();
        assertThat(values.size(), equalTo(1));
        assertThat(values.get(0).asObject().getString("projectId", null), equalTo("555555"));
    }


    @Test
    @RunAsClient
    public void testGetFreelancers() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("gateway").path("freelancers");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(200));
        JsonArray values = Json.parse(response.readEntity(String.class)).asArray();
        assertThat(values.size(), equalTo(3));
    }


    @Test
    @RunAsClient
    public void testGetFreelancerById() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("gateway").path("freelancers").path("1");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(200));
        JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
        assertThat(value.getString("freelancerId", null), equalTo("1"));
        assertThat(value.getString("firstName", null), equalTo("John"));
    }


    @Test
    @RunAsClient
    public void testGetFreelancerByIdNotFound() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("gateway").path("freelancers").path("100");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(204));
    }


    @Test
    @RunAsClient
    public void testHealthCheck() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/health");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(200));
        JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
        assertThat(value.getString("outcome", ""), equalTo("UP"));
        JsonArray checks = value.get("checks").asArray();
        assertThat(checks.size(), equalTo(1));
        JsonObject state = checks.get(0).asObject();
    }


}
