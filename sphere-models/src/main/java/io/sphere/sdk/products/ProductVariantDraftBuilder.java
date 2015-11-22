package io.sphere.sdk.products;

import io.sphere.sdk.products.attributes.AttributeDraft;
import io.sphere.sdk.products.attributes.NamedAttributeAccess;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Builder;
import io.sphere.sdk.utils.ListUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public final class ProductVariantDraftBuilder extends Base implements Builder<ProductVariantDraft> {
    @Nullable
    private String sku;

    private List<PriceDraft> prices = Collections.emptyList();

    private List<AttributeDraft> attributes = Collections.emptyList();

    private List<Image> images = Collections.emptyList();

    private ProductVariantDraftBuilder() {
    }

    public static ProductVariantDraftBuilder of() {
        return new ProductVariantDraftBuilder();
    }

    public ProductVariantDraftBuilder sku(@Nullable final String sku) {
        this.sku = sku;
        return this;
    }

    public ProductVariantDraftBuilder images(final List<Image> images) {
        this.images = images;
        return this;
    }

    public ProductVariantDraftBuilder images(final Image image) {
        return images(Collections.singletonList(image));
    }

    public ProductVariantDraftBuilder prices(final List<PriceDraft> prices) {
        this.prices = prices;
        return this;
    }

    public ProductVariantDraftBuilder prices(final PriceDraft ... prices) {
        return prices(asList(prices));
    }

    public ProductVariantDraftBuilder price(final PriceDraft price) {
        return prices(Collections.singletonList(price));
    }

    public ProductVariantDraftBuilder attributes(final List<AttributeDraft> attributes) {
        this.attributes = attributes;
        return this;
    }

    public ProductVariantDraftBuilder attributes(final AttributeDraft ... attributes) {
        return attributes(asList(attributes));
    }

    public <T> ProductVariantDraftBuilder plusAttribute(final NamedAttributeAccess<T> namedAccess, final T value) {
        return plusAttribute(AttributeDraft.of(namedAccess, value));
    }

    public <T> ProductVariantDraftBuilder plusAttribute(final String name, final T value) {
        return plusAttribute(AttributeDraft.of(name, value));
    }

    public ProductVariantDraftBuilder plusAttribute(final AttributeDraft attribute) {
        return attributes(ListUtils.listOf(attributes, attribute));
    }

    @Override
    public ProductVariantDraft build() {
        return new ProductVariantDraftImpl(sku, prices, attributes, images);
    }
}
