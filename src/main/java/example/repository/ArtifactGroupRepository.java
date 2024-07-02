package example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import example.models.ArtifactGroup;

public interface ArtifactGroupRepository extends JpaRepository<ArtifactGroup, String> {}