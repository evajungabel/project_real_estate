package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.AgentComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentCommentRepository extends JpaRepository<AgentComment, Long> {
}
