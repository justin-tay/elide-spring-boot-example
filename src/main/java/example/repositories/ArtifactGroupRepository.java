package example.repositories;

import org.springframework.data.repository.Repository;

import example.models.ArtifactGroup;

/**
 * {@link Repository} for {@link ArtifactGroup}.
 */
public interface ArtifactGroupRepository extends QueryRepository<ArtifactGroup, Long> {
}
