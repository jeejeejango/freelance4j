package com.redhat.freelance4j.freelancer.repository;

import com.redhat.freelance4j.freelancer.model.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author boonp
 * @since 05/09/2019 10:44 PM
 */
@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer, String> {

}
