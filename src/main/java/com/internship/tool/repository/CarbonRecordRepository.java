package com.internship.tool.repository;

import com.internship.tool.entity.CarbonRecord;
import com.internship.tool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarbonRecordRepository extends JpaRepository<CarbonRecord, Long> {
    List<CarbonRecord> findByCategoryContainingIgnoreCase(String category);
    List<CarbonRecord> findByStatusIgnoreCase(String status);
    List<CarbonRecord> findByUser(User user);

}