package com.ivanthescientist.projectmanager.infrastructure.repository;

import com.ivanthescientist.projectmanager.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByEmail(String email);
}
