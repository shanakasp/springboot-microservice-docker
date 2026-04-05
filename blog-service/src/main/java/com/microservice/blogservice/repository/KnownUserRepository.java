package com.microservice.blogservice.repository;

import com.microservice.blogservice.model.KnownUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnownUserRepository extends JpaRepository<KnownUser, Long> {
}