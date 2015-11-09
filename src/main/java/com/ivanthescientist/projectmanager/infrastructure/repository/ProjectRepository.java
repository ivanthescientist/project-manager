package com.ivanthescientist.projectmanager.infrastructure.repository;

import com.ivanthescientist.projectmanager.domain.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
