package io.sphere.sdk.products.commands;

import io.sphere.sdk.products.*;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeFixtures;
import io.sphere.sdk.search.SearchKeyword;
import io.sphere.sdk.search.SearchKeywords;
import io.sphere.sdk.search.tokenizer.CustomSuggestTokenizer;
import io.sphere.sdk.taxcategories.TaxCategory;
import io.sphere.sdk.test.IntegrationTest;
import io.sphere.sdk.types.CustomFieldsDraft;
import org.junit.Test;

import java.util.Locale;

import static io.sphere.sdk.producttypes.ProductTypeFixtures.withEmptyProductType;
import static io.sphere.sdk.states.StateFixtures.withStateByBuilder;
import static io.sphere.sdk.states.StateType.PRODUCT_STATE;
import static io.sphere.sdk.taxcategories.TaxCategoryFixtures.defaultTaxCategory;
import static io.sphere.sdk.test.SphereTestUtils.*;
import static io.sphere.sdk.types.TypeFixtures.STRING_FIELD_NAME;
import static io.sphere.sdk.types.TypeFixtures.withUpdateableType;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductCreateCommandTest extends IntegrationTest {
    @Test
    public void createProductWithExternalImage() throws Exception {
        withStateByBuilder(client(), stateBuilder -> stateBuilder.initial(true).type(PRODUCT_STATE), initialProductState -> {
            final ProductType productType = ProductTypeFixtures.defaultProductType(client());
            final Image image = Image.ofWidthAndHeight("http://www.commercetools.com/assets/img/ct_logo_farbe.gif", 460, 102, "commercetools logo");
            final ProductVariantDraft masterVariant = ProductVariantDraftBuilder.of()
                    .images(image)
                    .build();
            final TaxCategory taxCategory = defaultTaxCategory(client());
            final SearchKeywords searchKeywords = SearchKeywords.of(Locale.ENGLISH, asList(SearchKeyword.of("foo bar baz", CustomSuggestTokenizer.of(asList("foo, baz")))));
            final ProductDraft productDraft = ProductDraftBuilder
                    .of(productType, en("product with external image"), randomSlug(), masterVariant)
                    .taxCategory(taxCategory)
                    .searchKeywords(searchKeywords)
                    .state(initialProductState)
                    .build();
            final Product product = execute(ProductCreateCommand.of(productDraft));
            final Image loadedImage = product.getMasterData().getStaged().getMasterVariant().getImages().get(0);
            assertThat(loadedImage).isEqualTo(image);
            assertThat(product.getTaxCategory()).isEqualTo(taxCategory.toReference());
            assertThat(product.getMasterData().getStaged().getSearchKeywords()).isEqualTo(searchKeywords);
            assertThat(product.getState()).isEqualTo(initialProductState.toReference());

            //clean up test
            execute(ProductDeleteCommand.of(product));
        });
    }

    @Test
    public void createProductWithCustomPrice() {
        withEmptyProductType(client(), randomKey(), productType ->
                withUpdateableType(client(), type -> {
                    final String value = "foo";
                    final PriceDraft price = PriceDraft.of(EURO_1)
                            .withCustom(CustomFieldsDraft.ofTypeIdAndObjects(type.getId(), singletonMap(STRING_FIELD_NAME, value)));
                    final ProductVariantDraft masterVariant = ProductVariantDraftBuilder.of().price(price).build();
                    final ProductDraft productDraft = ProductDraftBuilder.of(productType, randomSlug(), randomSlug(), masterVariant).build();

                    final Product product = execute(ProductCreateCommand.of(productDraft));
                    final Price loadedPrice = product.getMasterData().getStaged().getMasterVariant().getPrices().get(0);

                    assertThat(loadedPrice.getValue()).isEqualTo(EURO_1);
                    assertThat(loadedPrice.getCustom().getFieldAsString(STRING_FIELD_NAME)).isEqualTo(value);

                    execute(ProductDeleteCommand.of(product));

                    return type;
                })
        );

    }
}