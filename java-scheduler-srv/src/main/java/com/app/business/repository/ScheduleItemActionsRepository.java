package com.app.business.repository;

import com.app.business.model.ScheduleItemAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduleItemActionsRepository extends JpaRepository<ScheduleItemAction, UUID> {
}
