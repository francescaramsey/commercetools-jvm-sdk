package io.sphere.sdk.categories;

import com.google.common.base.Optional;
import io.sphere.sdk.queries.EmbeddedQueryModel;
import io.sphere.sdk.queries.LocalizedStringQueryModel;
import io.sphere.sdk.queries.QueryModel;

public class CategoryQueryModel<T> extends EmbeddedQueryModel<T, CategoryQueryModel> {
    private static final CategoryQueryModel<CategoryQueryModel> instance = new CategoryQueryModel<>(Optional.<QueryModel<CategoryQueryModel>>absent(), Optional.<String>absent());

    public static CategoryQueryModel<CategoryQueryModel> get() {
        return instance;
    }

    private CategoryQueryModel(Optional<? extends QueryModel<T>> parent, Optional<String> pathSegment) {
        super(parent, pathSegment);
    }

    public LocalizedStringQueryModel<T> slug() {
        return localizedStringQueryModel("slug");
    }

    public LocalizedStringQueryModel<T> name() {
        return localizedStringQueryModel("name");
    }
}
