package example.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yahoo.elide.spring.datastore.config.DataStoreBuilderCustomizer;

import example.datastore.OperationDataStore;
import example.datastore.SpringDataDataStore;
import example.models.ArtifactGroupPage;
import example.models.ArtifactGroupStream;
import example.models.Mail;
import example.repositories.ArtifactGroupRepository;
import example.services.CursorEncoder;
import example.services.JacksonCursorEncoder;
import example.services.QueryService;
import jakarta.validation.Validator;

/**
 * Configuration for Elide.
 */
@Configuration
public class ElideConfiguration {
    /**
     * Creates the {@link OperationDataStore}.
     *
     * @return the builder
     */
    @Bean
    DataStoreBuilderCustomizer operationDataStoreBuilderCustomizer(Validator validator) {
        return builder -> {
            builder.dataStore(new OperationDataStore(validator, Mail.class));
        };
    }

    /**
     * Creates the {@link SpringDataDataStore}.
     *
     * @return the builder
     */
    @Bean
    DataStoreBuilderCustomizer springDataDataStoreBuilderCustomizer(QueryService queryService,
            CursorEncoder cursorEncoder) {
        return builder -> {
            builder.dataStore(new SpringDataDataStore(queryService, cursorEncoder));
        };
    }

    /**
     * Creates the {@link QueryService}.
     *
     * @return the service
     */
    @Bean
    QueryService queryService(ArtifactGroupRepository artifactGroupRepository) {
        Map<Class<?>, JpaSpecificationExecutor<?>> repositories = new HashMap<>();
        repositories.put(ArtifactGroupStream.class, artifactGroupRepository);
        repositories.put(ArtifactGroupPage.class, artifactGroupRepository);
        return new QueryService(repositories);
    }

    /**
     * Creates the {@link CursorEncoder}.
     * 
     * @return the cursor encoder
     */
    @Bean
    CursorEncoder cursorEncoder() {
        return new JacksonCursorEncoder();
    }
}
