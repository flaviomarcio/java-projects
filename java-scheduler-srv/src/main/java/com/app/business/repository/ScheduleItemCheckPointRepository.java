package com.app.business.repository;

import com.app.business.model.ScheduleItemCheckPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduleItemCheckPointRepository extends JpaRepository<ScheduleItemCheckPoint, UUID> {
}
