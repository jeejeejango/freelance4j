package com.redhat.freelance4j.freelancer.service;

import com.redhat.freelance4j.freelancer.model.Freelancer;

import java.util.List;

public interface FreelancerService {

    public List<Freelancer> getFreelancers();


    public Freelancer getFreelancerById(String freelancerId);


}
