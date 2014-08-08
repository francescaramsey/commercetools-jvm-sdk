/**
 * Products are the sellable goods in an e-commerce project on SPHERE.IO.
 *
 * <ol>
 *     <li><a href="#create-product">Create a Product</a></li>
 *     <li><a href="#query-product">Query Products</a></li>
 *     <li><a href="#update-product">Update a Product</a></li>
 *     <li><a href="#delete-product">Delete a Product</a></li>
 * </ol>
 *
 * <h3 id="create-product">Create a Product</h3>
 *
 * A {@link io.sphere.sdk.products.Product} must belong to a {@link io.sphere.sdk.producttypes.ProductType},
 * so you need to <a href="../producttypes/package-summary.html#create-product-types">create a product type</a> first.
 *
 * Products can be created in the backend by executing a {@link io.sphere.sdk.products.commands.ProductCreateCommand}:
 *
 * {@include.example example.CreateProductExamples#createWithClient()}
 *
 * {@link io.sphere.sdk.products.commands.ProductCreateCommand} requires one instance of {@link io.sphere.sdk.products.NewProduct}
 * as constructor parameter. It can be created with {@link io.sphere.sdk.products.NewProductBuilder}:
 *
 * {@include.example test.SimpleCottonTShirtNewProductSupplier}
 *
 * <h3 id="query-product">Query Products</h3>
 *
 * Use {@link io.sphere.sdk.products.queries.ProductQueryModel} to query for products:
 *
 * {@include.example example.ByEnglishNameProductQuerySupplier}
 *
 * <h3 id="update-product">Update a Product</h3>
 * <h3 id="delete-product">Delete a Product</h3>
 *
 * Use {@link io.sphere.sdk.products.commands.ProductDeleteByIdCommand} to delete a product:
 *
 * {@include.example example.ProductDeleteExample#delete()}
 */
package io.sphere.sdk.products;