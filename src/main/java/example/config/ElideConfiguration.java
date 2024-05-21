package example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yahoo.elide.graphql.GraphQLSettingsBuilderCustomizer;

import example.Description;

@Configuration
public class ElideConfiguration {
	@Bean
	GraphQLSettingsBuilderCustomizer graphqlSettingsBuilderCustomizer() {
		return graphqlSettings -> graphqlSettings.graphqlFieldDefinitionCustomizer(
				((fieldDefinition, parentClass, attributeClass, attribute, fetcher, entityDictionary) -> {
					Description description = entityDictionary.getAttributeOrRelationAnnotation(parentClass,
							Description.class, attribute);
					if (description != null) {
						fieldDefinition.description(description.value());
					}
				}));
	}
}
