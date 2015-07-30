package io.sphere.sdk.queries;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.client.JsonEndpoint;
import io.sphere.sdk.client.SphereRequestBase;
import io.sphere.sdk.expansion.ExpansionDslUtil;
import io.sphere.sdk.expansion.ExpansionPath;
import io.sphere.sdk.expansion.MetaModelExpansionDslExpansionModelRead;
import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.http.HttpQueryParameter;
import io.sphere.sdk.http.HttpResponse;
import io.sphere.sdk.http.UrlQueryBuilder;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.sphere.sdk.http.HttpStatusCode.NOT_FOUND_404;
import static io.sphere.sdk.queries.QueryParameterKeys.EXPAND;
import static io.sphere.sdk.utils.ListUtils.listOf;
import static java.util.Objects.requireNonNull;

/**
 * @param <R> result type, maybe directly {@code T} or sth. like {@code List<T>}
 * @param <T> type of the query result
 * @param <C> type of the class implementing this class
 * @param <E> type of the expansion model
 */
public class MetaModelFetchDslImpl<R, T, C extends MetaModelFetchDsl<R, T, C, E>, E> extends SphereRequestBase implements MetaModelFetchDsl<R, T, C, E>, MetaModelExpansionDslExpansionModelRead<T, C, E> {

    final JsonEndpoint<R> endpoint;
    /**
     for example an ID, a key, slug, token
     */
    final String identifierToSearchFor;
    final List<ExpansionPath<T>> expansionPaths;
    final List<HttpQueryParameter> additionalParameters;
    final E expansionModel;
    final Function<MetaModelFetchDslBuilder<R, T, C, E>, C> builderFunction;

    protected MetaModelFetchDslImpl(final JsonEndpoint<R> endpoint, final String identifierToSearchFor, final E expansionModel, final Function<MetaModelFetchDslBuilder<R, T, C, E>, C> builderFunction, final List<HttpQueryParameter> additionalParameters) {
        this(endpoint, identifierToSearchFor, Collections.emptyList(), expansionModel, builderFunction, additionalParameters);
    }

    protected MetaModelFetchDslImpl(final String identifierToSearchFor, final JsonEndpoint<R> endpoint, final E expansionModel, final Function<MetaModelFetchDslBuilder<R, T, C, E>, C> builderFunction) {
        this(endpoint, identifierToSearchFor, Collections.emptyList(), expansionModel, builderFunction, Collections.emptyList());
    }

    protected MetaModelFetchDslImpl(final JsonEndpoint<R> endpoint, final String identifierToSearchFor, final E expansionModel, final Function<MetaModelFetchDslBuilder<R, T, C, E>, C> builderFunction) {
        this(endpoint, identifierToSearchFor, Collections.emptyList(), expansionModel, builderFunction, Collections.emptyList());
    }

    public MetaModelFetchDslImpl(final MetaModelFetchDslBuilder<R, T, C, E> builder) {
        this(builder.endpoint, builder.identifierToSearchFor, builder.expansionPaths, builder.expansionModel, builder.builderFunction, builder.additionalParameters);
    }

    protected MetaModelFetchDslImpl(final JsonEndpoint<R> endpoint, final String identifierToSearchFor, final List<ExpansionPath<T>> expansionPaths, final E expansionModel, final Function<MetaModelFetchDslBuilder<R, T, C, E>, C> builderFunction, final List<HttpQueryParameter> additionalParameters) {
        this.endpoint = requireNonNull(endpoint);
        this.identifierToSearchFor = requireNonNull(identifierToSearchFor);
        this.expansionPaths = requireNonNull(expansionPaths);
        this.expansionModel = requireNonNull(expansionModel);
        this.builderFunction = requireNonNull(builderFunction);
        this.additionalParameters = requireNonNull(additionalParameters);
    }

    @Nullable
    @Override
    public R deserialize(final HttpResponse httpResponse) {
        return Optional.of(httpResponse)
                .filter(r -> r.getStatusCode() != NOT_FOUND_404)
                .map(r -> resultMapperOf(typeReference()).apply(httpResponse))
                .orElse(null);
    }

    @Override
    public HttpRequestIntent httpRequestIntent() {
        if (!endpoint.endpoint().startsWith("/")) {
            throw new RuntimeException("By convention the paths start with a slash, see baseEndpointWithoutId()");
        }
        final boolean urlEncoded = true;
        final UrlQueryBuilder builder = UrlQueryBuilder.of();
        expansionPaths().forEach(path -> builder.add(EXPAND, path.toSphereExpand(), urlEncoded));
        additionalQueryParameters().forEach(parameter -> builder.add(parameter.getKey(), parameter.getValue(), urlEncoded));
        final String queryParameters = builder.toStringWithOptionalQuestionMark();
        final String path = endpoint.endpoint() + "/" + identifierToSearchFor + (queryParameters.length() > 1 ? queryParameters : "");
        return HttpRequestIntent.of(HttpMethod.GET, path);
    }

    protected TypeReference<R> typeReference() {
        return endpoint.typeReference();
    }


    protected List<HttpQueryParameter> additionalQueryParameters() {
        return additionalParameters;
    }

    @Override
    public boolean canDeserialize(final HttpResponse httpResponse) {
        return httpResponse.hasSuccessResponseCode() || httpResponse.getStatusCode() == NOT_FOUND_404;
    }

    @Override
    public List<ExpansionPath<T>> expansionPaths() {
        return expansionPaths;
    }

    @Override
    public Fetch<R> toFetch() {
        return this;
    }

    @Override
    public final C withExpansionPaths(final List<ExpansionPath<T>> expansionPaths) {
        return copyBuilder().expansionPaths(expansionPaths).build();
    }

    @Override
    public C withExpansionPaths(final ExpansionPath<T> expansionPath) {
        return ExpansionDslUtil.withExpansionPaths(this, expansionPath);
    }

    @Override
    public C withExpansionPaths(final Function<E, ExpansionPath<T>> m) {
        return ExpansionDslUtil.withExpansionPaths(this, m);
    }

    @Override
    public C plusExpansionPaths(final List<ExpansionPath<T>> expansionPaths) {
        return withExpansionPaths(listOf(expansionPaths(), expansionPaths));
    }

    @Override
    public C plusExpansionPaths(final ExpansionPath<T> expansionPath) {
        return ExpansionDslUtil.plusExpansionPaths(this, expansionPath);
    }

    @Override
    public C plusExpansionPaths(final Function<E, ExpansionPath<T>> m) {
        return ExpansionDslUtil.plusExpansionPaths(this, m);
    }

    @Override
    public E expansionModel() {
        return expansionModel;
    }

    protected MetaModelFetchDslBuilder<R, T, C, E> copyBuilder() {
        return new MetaModelFetchDslBuilder<>(this);
    }
}
