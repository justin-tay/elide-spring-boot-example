package example.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.support.DefaultExecutionGraphQlResponse;

import com.yahoo.elide.Elide;
import com.yahoo.elide.RefreshableElide;
import com.yahoo.elide.core.datastore.DataStoreTransaction;
import com.yahoo.elide.core.dictionary.EntityDictionary;
import com.yahoo.elide.core.request.route.Route;
import com.yahoo.elide.core.security.User;
import com.yahoo.elide.graphql.GraphQLRequestScope;
import com.yahoo.elide.graphql.GraphQLSettings;
import com.yahoo.elide.graphql.ModelBuilder;
import com.yahoo.elide.graphql.NonEntityDictionary;
import com.yahoo.elide.graphql.PersistentResourceFetcher;
import com.yahoo.elide.graphql.parser.GraphQLEntityProjectionMaker;
import com.yahoo.elide.graphql.parser.GraphQLProjectionInfo;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import reactor.core.publisher.Mono;

/**
 * ElideSpringGraphQlAutoConfiguration.
 */
@Configuration
@ConditionalOnClass({ GraphQL.class, GraphQlSource.class, GraphQLSettings.class })
public class ElideSpringGraphQlAutoConfiguration {
	@Bean
	GraphQlSourceBuilderCustomizer elideGraphQlSourceBuilderCustomizer(RefreshableElide refreshableElide) {
		Elide elide = refreshableElide.getElide();
		EntityDictionary dictionary = elide.getElideSettings().getEntityDictionary();

		NonEntityDictionary nonEntityDictionary = new NonEntityDictionary(dictionary.getScanner(),
				dictionary.getSerdeLookup());

		PersistentResourceFetcher fetcher = new PersistentResourceFetcher(nonEntityDictionary);
		ModelBuilder modelBuilder = new ModelBuilder(elide.getElideSettings().getEntityDictionary(),
				nonEntityDictionary, elide.getElideSettings(), fetcher, EntityDictionary.NO_VERSION);
		GraphQLSchema elideGraphQlSchema = modelBuilder.build();
		return builder -> builder.configureGraphQl(graphQlBuilder -> {
			GraphQLSchema springGraphQlSchema = graphQlBuilder.build().getGraphQLSchema();
			graphQlBuilder.schema(springGraphQlSchema.transform(schema -> {
				if (springGraphQlSchema.getQueryType() != null) {
					schema.query(springGraphQlSchema.getQueryType().transform(objectType -> {
						elideGraphQlSchema.getQueryType().getFields().forEach(objectType::field);
					}));
				} else {
					schema.query(elideGraphQlSchema.getQueryType());
				}
				if (springGraphQlSchema.getMutationType() != null) {
					schema.mutation(springGraphQlSchema.getMutationType().transform(objectType -> {
						elideGraphQlSchema.getMutationType().getFields().forEach(objectType::field);
					}));
				} else {
					schema.mutation(elideGraphQlSchema.getMutationType());
				}
				schema.additionalTypes(elideGraphQlSchema.getAdditionalTypes());
				schema.codeRegistry(springGraphQlSchema.getCodeRegistry().transform(codeRegistry -> {
					codeRegistry.dataFetchers(elideGraphQlSchema.getCodeRegistry());
					codeRegistry.typeResolvers(elideGraphQlSchema.getCodeRegistry());
				}));
			}));
		});
	}

	@Bean
	WebGraphQlInterceptor elideWebGraphQlInterceptor(RefreshableElide refreshableElide) {
		return (request, chain) -> {
			String apiVersion = EntityDictionary.NO_VERSION;
			Elide elide = refreshableElide.getElide();
			String queryText = request.getDocument();
			UUID requestId = UUID.randomUUID();
			boolean isMutation = true;
			ExecutionInput[] result = new ExecutionInput[1];
			try (DataStoreTransaction tx = isMutation ? elide.getDataStore().beginTransaction()
					: elide.getDataStore().beginReadTransaction()) {
				elide.getTransactionRegistry().addRunningTransaction(requestId, tx);
				Map<String, Object> variables = request.getVariables();

				GraphQLProjectionInfo projectionInfo = null;
				try {
					projectionInfo = new GraphQLEntityProjectionMaker(elide.getElideSettings(),
							variables, apiVersion).make(queryText);
				} catch (Exception e) {
					// TODO handle unknown entities from spring graphql more gracefully
					return chain.next(request);
				}
				Route route = Route.builder().baseUrl(request.getUri().toUriString()).apiVersion(apiVersion)
						.headers(request.getHeaders()).build();
				User principal = null;
				GraphQLRequestScope requestScope = GraphQLRequestScope.builder().route(route).dataStoreTransaction(tx)
						.user(principal).requestId(requestId).elideSettings(elide.getElideSettings())
						.projectionInfo(projectionInfo).build();

				request.configureExecutionInput((executionInput, builder) -> {
					result[0] = executionInput;
					return builder.localContext(requestScope).build();
				});
				return chain.next(request).map(response -> {
					try {
						tx.preCommit(requestScope);
						requestScope.getPermissionExecutor().executeCommitChecks();
						if (isMutation) {
							if (!response.getErrors().isEmpty()) {
								// Do not commit. Throw OK response to process tx.close correctly.
								return response;
							}
							requestScope.saveOrCreateObjects();
						}

						tx.flush(requestScope);

						requestScope.runQueuedPreCommitTriggers();
						elide.getAuditLogger().commit();
						tx.commit(requestScope);
						requestScope.runQueuedPostCommitTriggers();
						return response;
					} catch (IOException e) {
						return response.transform(r -> {
							r.data(null).errors(List.of(GraphQLError.newError().message(e.getMessage()).build()));
						});
					} catch (RuntimeException e) {
						return response.transform(r -> {
							r.data(null).errors(List.of(GraphQLError.newError().message(e.getMessage()).build()));
						});
					} finally {
						elide.getTransactionRegistry().removeRunningTransaction(requestId);
						elide.getAuditLogger().clear();
						try {
							tx.close();
						} catch (IOException e) {
							// do nothing
						}
					}
				});
			} catch (IOException e) {
				return Mono.just(new WebGraphQlResponse(
						new DefaultExecutionGraphQlResponse(result[0], ExecutionResult.newExecutionResult()
								.addError(GraphQLError.newError().message(e.getMessage()).build()).build())));
			} catch (RuntimeException e) {
				return Mono.just(new WebGraphQlResponse(
						new DefaultExecutionGraphQlResponse(result[0], ExecutionResult.newExecutionResult()
								.addError(GraphQLError.newError().message(e.getMessage()).build()).build())));
			} finally {
				elide.getTransactionRegistry().removeRunningTransaction(requestId);
				elide.getAuditLogger().clear();
			}
		};
	}
}
