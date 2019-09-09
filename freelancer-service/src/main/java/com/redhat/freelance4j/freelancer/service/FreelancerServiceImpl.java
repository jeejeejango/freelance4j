package com.redhat.freelance4j.freelancer.service;

import com.redhat.freelance4j.freelancer.model.Freelancer;
import com.redhat.freelance4j.freelancer.repository.FreelancerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FreelancerServiceImpl implements FreelancerService {

    @Autowired
    private FreelancerRepository freelancerRepository;


    @Override
    public List<Freelancer> getFreelancers() {
        return freelancerRepository.findAll();
    }


    @Override
    public Freelancer getFreelancerById(String freelancerId) {
        return freelancerRepository.findOne(freelancerId);
    }


}
