package example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yahoo.elide.spring.datastore.config.DataStoreBuilderCustomizer;

import example.datastore.OperationDataStore;
import example.models.Mail;
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
//            builder.dataStore(new OperationDataStore(validator, Mail.class));
        };
    }
}
