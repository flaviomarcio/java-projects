package com.app.business.repository.ofservice;

import com.app.business.model.ofservice.NotifyProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotifyProducerRepository extends JpaRepository<NotifyProducer, UUID> {

}

