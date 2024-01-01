package com.realestate.repository;

import com.realestate.domain.EstateAgent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstateAgentRepository extends JpaRepository<EstateAgent,Long> {
}
