package example.model;

import com.yahoo.elide.annotation.Include;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Include
@Entity
@Getter
@Setter
public class Legal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "decision_id", nullable = false)
    private Decision decision;
}