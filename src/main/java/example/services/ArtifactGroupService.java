package example.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import example.models.ArtifactGroup;
import example.repository.ArtifactGroupRepository;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ArtifactGroupService {
	private final ArtifactGroupRepository artifactGroupRepository;

	public ArtifactGroupService(ArtifactGroupRepository artifactGroupRepository) {
		this.artifactGroupRepository = artifactGroupRepository;
	}

	public Optional<ArtifactGroup> findById(String id) {
		return artifactGroupRepository.findById(id);
	}
}
