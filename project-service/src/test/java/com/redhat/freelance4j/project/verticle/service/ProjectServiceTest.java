package com.redhat.freelance4j.project.verticle.service;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(VertxUnitRunner.class)
public class ProjectServiceTest extends MongoTestBase {

    private Vertx vertx;


    @Before
    public void setup(TestContext context) throws Exception {
        vertx = Vertx.vertx();
        vertx.exceptionHandler(context.exceptionHandler());
        JsonObject config = getConfig();
        mongoClient = MongoClient.createNonShared(vertx, config);
        Async async = context.async();
        dropCollection(mongoClient, "projects", async, context);
        async.await(10000);
    }


    @After
    public void tearDown() throws Exception {
        mongoClient.close();
        vertx.close();
    }


    @Test
    public void testGetProjects(TestContext context) throws Exception {
        Async saveAsync = context.async(2);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
            .put("projectId", projectId1)
            .put("ownerFirstName", "firsName1")
            .put("ownerLastName", "lastName1")
            .put("ownerEmail", "email1@test.com")
            .put("title", "project1")
            .put("description", "projectDescription1")
            .put("status", "open");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        String projectId2 = "222222";
        JsonObject json2 = new JsonObject()
            .put("projectId", projectId2)
            .put("ownerFirstName", "firsName2")
            .put("ownerLastName", "lastName2")
            .put("ownerEmail", "email2@test.com")
            .put("title", "project2")
            .put("description", "projectDescription2")
            .put("status", "in_progress");

        mongoClient.save("projects", json2, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProjects(ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().size(), equalTo(2));
                Set<String> itemIds = ar.result().stream().map(p -> p.getProjectId()).collect(Collectors.toSet());
                assertThat(itemIds.size(), equalTo(2));
                assertThat(itemIds, allOf(hasItem(projectId1), hasItem(projectId2)));
                async.complete();
            }
        });
    }


    @Test
    public void testGetProject(TestContext context) throws Exception {
        Async saveAsync = context.async(2);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
            .put("projectId", projectId1)
            .put("ownerFirstName", "firsName1")
            .put("ownerLastName", "lastName1")
            .put("ownerEmail", "email1@test.com")
            .put("title", "project1")
            .put("description", "projectDescription1")
            .put("status", "open");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        String projectId2 = "222222";
        JsonObject json2 = new JsonObject()
            .put("projectId", projectId2)
            .put("ownerFirstName", "firsName2")
            .put("ownerLastName", "lastName2")
            .put("ownerEmail", "email2@test.com")
            .put("title", "project2")
            .put("description", "projectDescription2")
            .put("status", "in_progress");

        mongoClient.save("projects", json2, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProject("111111", ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().getProjectId(), equalTo("111111"));
                assertThat(ar.result().getTitle(), equalTo("project1"));
                async.complete();
            }
        });
    }


    @Test
    public void testGetNonExistingProject(TestContext context) throws Exception {
        Async saveAsync = context.async(1);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
            .put("projectId", projectId1)
            .put("ownerFirstName", "firsName1")
            .put("ownerLastName", "lastName1")
            .put("ownerEmail", "email1@test.com")
            .put("title", "project1")
            .put("description", "projectDescription1")
            .put("status", "open");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProject("222222", ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), nullValue());
                async.complete();
            }
        });
    }


    @Test
    public void testGetProjectsByStatus(TestContext context) throws Exception {
        Async saveAsync = context.async(2);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
            .put("projectId", projectId1)
            .put("ownerFirstName", "firsName1")
            .put("ownerLastName", "lastName1")
            .put("ownerEmail", "email1@test.com")
            .put("title", "project1")
            .put("description", "projectDescription1")
            .put("status", "open");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        String projectId2 = "222222";
        JsonObject json2 = new JsonObject()
            .put("projectId", projectId2)
            .put("ownerFirstName", "firsName2")
            .put("ownerLastName", "lastName2")
            .put("ownerEmail", "email2@test.com")
            .put("title", "project2")
            .put("description", "projectDescription2")
            .put("status", "in_progress");

        mongoClient.save("projects", json2, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProjectsByStatus("open", ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().size(), equalTo(1));
                Set<String> itemIds = ar.result().stream().map(p -> p.getProjectId()).collect(Collectors.toSet());
                assertThat(itemIds.size(), equalTo(1));
                assertThat(itemIds, allOf(hasItem(projectId1)));
                async.complete();
            }
        });
    }


    @Test
    public void testGetProjectsByInvalidStatus(TestContext context) throws Exception {
        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProjectsByStatus("unknown", ar -> {
            assertThat(ar.failed(), is(true));
            if (ar.failed()) {
                async.complete();
            } else {
                context.fail(ar.cause().getMessage());
            }
        });
    }


    @Test
    public void testPing(TestContext context) throws Exception {
        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();
        service.ping(ar -> {
            assertThat(ar.succeeded(), equalTo(true));
            async.complete();
        });
    }

}
