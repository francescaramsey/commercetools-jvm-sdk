package io.sphere.sdk.carts.queries;

import io.sphere.sdk.queries.*;

public interface CustomLineItemCollectionQueryModel<T> extends LineItemLikeCollectionQueryModel<T> {
    @Override
    DiscountedLineItemPriceForQuantityQueryModel<T> discountedPricePerQuantity();

    @Override
    StringQueryModel<T> id();

    @Override
    QueryPredicate<T> isEmpty();

    @Override
    QueryPredicate<T> isNotEmpty();

    @Override
    LocalizedStringQueryModel<T> name();

    @Override
    LongQuerySortingModel<T> quantity();

    @Override
    ItemStateCollectionQueryModel<T> state();

    StringQueryModel<T> slug();

    MoneyQueryModel<T> money();
}
