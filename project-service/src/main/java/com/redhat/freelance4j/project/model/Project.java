package com.redhat.freelance4j.project.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

@DataObject
public class Project implements Serializable {

    private static final long serialVersionUID = -6994655395272795259L;

    private String projectId;

    private String ownerFirstName;

    private String ownerLastName;

    private String ownerEmail;

    private String title;

    private String description;

    private String status;


    public Project() {

    }


    public Project(JsonObject json) {
        this.projectId = json.getString("projectId");
        this.ownerFirstName = json.getString("ownerFirstName");
        this.ownerLastName = json.getString("ownerLastName");
        this.ownerEmail = json.getString("ownerEmail");
        this.title = json.getString("title");
        this.description = json.getString("description");
        this.status = json.getString("status");
    }


    public String getProjectId() {
        return projectId;
    }


    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    public String getOwnerFirstName() {
        return ownerFirstName;
    }


    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }


    public String getOwnerLastName() {
        return ownerLastName;
    }


    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }


    public String getOwnerEmail() {
        return ownerEmail;
    }


    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public JsonObject toJson() {

        final JsonObject json = new JsonObject();
        json.put("projectId", this.projectId);
        json.put("ownerFirstName", this.ownerFirstName);
        json.put("ownerLastName", this.ownerLastName);
        json.put("ownerEmail", this.ownerEmail);
        json.put("title", this.title);
        json.put("description", this.description);
        json.put("status", this.status);
        return json;
    }

}
