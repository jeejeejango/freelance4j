package com.redhat.freelance4j.project.verticle.service;

import com.redhat.freelance4j.project.model.Project;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectServiceImpl implements ProjectService {

    private static final Set<String> statuses = new HashSet<>(Arrays.asList("open", "in_progress", "completed", "cancelled"));

    private MongoClient client;


    public ProjectServiceImpl(Vertx vertx, JsonObject config, MongoClient client) {
        this.client = client;
    }


    @Override
    public void getProjects(Handler<AsyncResult<List<Project>>> resulthandler) {
        JsonObject query = new JsonObject();
        client.find("projects", query, ar -> {
            if (ar.succeeded()) {
                List<Project> projects = ar.result().stream()
                    .map(Project::new)
                    .collect(Collectors.toList());
                resulthandler.handle(Future.succeededFuture(projects));
            } else {
                resulthandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }


    @Override
    public void getProjectsByStatus(String status, Handler<AsyncResult<List<Project>>> resulthandler) {
        if (status != null && !statuses.contains(status.trim().toLowerCase())) {
            resulthandler.handle(Future.failedFuture("Invalid project status"));
            return;
        }
        JsonObject query = new JsonObject().put("status", status);
        client.find("projects", query, ar -> {
            if (ar.succeeded()) {
                List<Project> projects = ar.result().stream()
                    .map(Project::new)
                    .collect(Collectors.toList());
                resulthandler.handle(Future.succeededFuture(projects));
            } else {
                resulthandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }


    @Override
    public void getProject(String projectId, Handler<AsyncResult<Project>> resulthandler) {
        JsonObject query = new JsonObject().put("projectId", projectId);
        client.find("projects", query, ar -> {
            if (ar.succeeded()) {
                Optional<JsonObject> result = ar.result().stream().findFirst();
                if (result.isPresent()) {
                    resulthandler.handle(Future.succeededFuture(new Project(result.get())));
                } else {
                    resulthandler.handle(Future.succeededFuture(null));
                }
            } else {
                resulthandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }


    @Override
    public void ping(Handler<AsyncResult<String>> resultHandler) {
        resultHandler.handle(Future.succeededFuture("OK"));
    }

}
