package com.ivanthescientist.projectmanager.infrastructure.repository;

import com.ivanthescientist.projectmanager.domain.model.MaterialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRecordRepository extends JpaRepository<MaterialRecord, Long>{

}
