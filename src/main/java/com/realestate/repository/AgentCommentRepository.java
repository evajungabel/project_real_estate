package com.realestate.repository;

import com.realestate.domain.AgentComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentCommentRepository extends JpaRepository<AgentComment, Long> {
}
