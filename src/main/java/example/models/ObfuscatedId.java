package example.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Embeddable
@Data
public class ObfuscatedId {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ")
    @SequenceGenerator(name = "POST_SEQ", allocationSize = 1)
    Long id;
}
