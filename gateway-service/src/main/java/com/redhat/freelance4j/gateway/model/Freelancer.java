package com.redhat.freelance4j.gateway.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author boonp
 * @since 08/09/2019 5:14 PM
 */
public class Freelancer implements Serializable {

    private static final long serialVersionUID = -7304814269819778382L;

    private String freelancerId;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private List<String> skills = new ArrayList<>();


    public Freelancer() {
    }


    public Freelancer(String freelancerId, String firstName, String lastName, String emailAddress, List<String> skills) {
        this.freelancerId = freelancerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.skills = skills;
    }


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


}
