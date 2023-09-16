package hu.progmasters.moovsmart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "agent_comments")
public class AgentComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "estate_agent_id")
    private EstateAgent estateAgent;

    @Column(name = "custom_user_id")
    private String customUsername;

    @Column(name = "comment")
    private String comment;
}
