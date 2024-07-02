package example.models.hooks;

import java.util.Optional;

import com.yahoo.elide.annotation.LifeCycleHookBinding;
import com.yahoo.elide.core.lifecycle.LifeCycleHook;
import com.yahoo.elide.core.security.ChangeSpec;
import com.yahoo.elide.core.security.RequestScope;

import example.models.ArtifactGroup;
import example.repository.ArtifactGroupRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArtifactGroupHook implements LifeCycleHook<ArtifactGroup> {

	private final ArtifactGroupRepository artifactGroupRespository;

	public ArtifactGroupHook(ArtifactGroupRepository artifactGroupRespository) {
		this.artifactGroupRespository = artifactGroupRespository;
	}

	@Override
	public void execute(LifeCycleHookBinding.Operation operation, LifeCycleHookBinding.TransactionPhase phase,
			ArtifactGroup elideEntity, RequestScope requestScope, Optional<ChangeSpec> changes) {

		changes.ifPresentOrElse((cs) -> log.info("PHASE = " + phase + ", CHANGE SPEC: " + cs.getFieldName() + " - "
				+ cs.getOriginal() + " changed to " + cs.getModified()),
				() -> log.info("PHASE = " + phase + ", NO CHANGE SPEC"));

		Optional<ArtifactGroup> artifactGroupOptional = artifactGroupRespository.findById(elideEntity.getName());
		ArtifactGroup original = null;
		if (artifactGroupOptional.isPresent()) {
			original = artifactGroupOptional.get();
		}
		log.info("PHASE = " + phase + ", DATABASE VALUE = "
				+ (original != null ? original.getDescription() : "not found") + ", HOOK VALUE = "
				+ elideEntity.getDescription());
	}
}