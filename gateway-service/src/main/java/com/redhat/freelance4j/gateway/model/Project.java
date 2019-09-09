package com.redhat.freelance4j.gateway.model;

import java.io.Serializable;

public class Project implements Serializable {

    private static final long serialVersionUID = -7304814269819778383L;

    private String projectId;

    private String ownerFirstName;

    private String ownerLastName;

    private String ownerEmail;

    private String title;

    private String description;

    private String status;


    public Project() {
    }


    public Project(String projectId, String ownerFirstName, String ownerLastName, String ownerEmail, String title, String description, String status) {
        this.projectId = projectId;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.ownerEmail = ownerEmail;
        this.title = title;
        this.description = description;
        this.status = status;
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


}
