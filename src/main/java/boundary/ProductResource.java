package boundary;

import entity.Product;
import entity.Catalog;
import entity.dto.ProductDTO;
import entity.dto.OrganisationDTO;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.*;

import java.time.Duration;
import java.util.List;

@GraphQLApi
public class ProductResource {

    @Query("allProducts")
    @Description("Get all Products")
    public Uni<List<ProductDTO>> getAllProducts() {
        return Product.getAllProducts().onItem().transform(ProductDTO::from);
    }

    @Query
    @Description("Get an product")
    public Uni<ProductDTO> getProduct(@Name("productId") long id) {
        Uni<Product> product = Product.findById(id);
        return product.onItem().transform(ProductDTO::from);
    }

    public Uni<List<OrganisationDTO>> organisations(@Source(name = "ProductResponse") ProductDTO product) {
        return Catalog
                .getOrganisationsByProductQuery(product.id)
                .onItem()
                .transform(catalog -> catalog.organisation)
                .collect()
                .asList()
                .onItem()
                .transform(OrganisationDTO::from);
    }

    @Mutation
    @Description("Add organisation to product")
    public Uni<ProductDTO> addOrganisationToProduct(@Name("organisationId") long organisationId,
                                                    @Name("productId") long productId) {
        return Product
                .addOrganisationToProduct(organisationId, productId)
                .onItem()
                .transform(ProductDTO::from)
                .onFailure()
                .transform(throwable -> Catalog.transformThrowable(throwable, organisationId, productId)
                );
    }

    @Mutation
    @Description("Add product")
    public static Uni<ProductDTO> addProduct(@Name("name") String name) {

        Product product = new Product();
        product.name =name;

        return Panache
                .withTransaction(product::persist)
                .onItem()
                .transform(ProductDTO::from)
                .ifNoItem().after(Duration.ofMillis(10000)).fail()
                .onFailure().transform(IllegalStateException::new);
    }
}
