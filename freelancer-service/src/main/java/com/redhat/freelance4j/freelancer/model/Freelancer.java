package com.redhat.freelance4j.freelancer.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Freelancer implements Serializable {

    private static final long serialVersionUID = -1108043957592113528L;

    @Id
    private String freelancerId;

    private String firstName;

    private String lastName;

    private String emailAddress;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    private List<String> skills = new ArrayList<>();


    public String getFreelancerId() {
        return freelancerId;
    }


    public void setFreelancerId(String freelancerId) {
        this.freelancerId = freelancerId;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmailAddress() {
        return emailAddress;
    }


    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public List<String> getSkills() {
        return skills;
    }


    public void setSkills(List<String> skills) {
        this.skills = skills;
    }


    @Override
    public String toString() {
        return "Freelancer{" +
            "freelancerId='" + freelancerId + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", skills=" + skills +
            '}';
    }


}

