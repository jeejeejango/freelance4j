package com.redhat.freelance4j.gateway.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Health
public class ServerStateHealthCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder serverState = HealthCheckResponse.named("server-state");
        return serverState.up().build();
    }
}
