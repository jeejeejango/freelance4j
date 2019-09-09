package com.redhat.freelance4j.project.verticle.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.serviceproxy.ProxyHelper;

import java.util.Optional;

public class ProjectVerticle extends AbstractVerticle {

    private ProjectService service;

    private MongoClient client;


    @Override
    public void start(Future<Void> startFuture) throws Exception {

        client = MongoClient.createShared(vertx, config());

        service = ProjectService.create(vertx, config(), client);
        ProxyHelper.registerService(ProjectService.class, vertx, service, ProjectService.ADDRESS);

        startFuture.complete();
    }


    @Override
    public void stop() throws Exception {
        Optional.ofNullable(client).ifPresent(c -> c.close());
    }

}
