package com.app.business.repository;

import com.app.business.model.ScheduleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleGroupRepository extends JpaRepository<ScheduleGroup, UUID> {
    List<ScheduleGroup> findByEnabled(boolean enabled);
}
