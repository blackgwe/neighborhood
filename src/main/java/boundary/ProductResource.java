package boundary;

import entity.Product;
import entity.Catalogue;
import boundary.dto.ProductDTO;
import boundary.dto.OrganisationDTO;
import org.eclipse.microprofile.graphql.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@GraphQLApi
public class ProductResource {

    @Query("allProducts")
    @Description("Get all Products")
    public List<ProductDTO> getAllProducts() {
        return Product.getAllProducts().stream().map(ProductDTO::from).collect(Collectors.toList());
    }

    @Query
    @Description("Get an product")
    public ProductDTO getProduct(@Name("productId") long id) {

        Product product = Product.findById(id);
        return ProductDTO.from(product);
    }

    public List<OrganisationDTO> organisations(@Source(name = "ProductResponse") ProductDTO product) {

        return Catalogue
                .getOrganisationsByProductQuery(product.id)
                .map(item -> OrganisationDTO.from(item.organisation))
                .collect(Collectors.toList());
    }

    @Mutation
    @Transactional
    @Description("Add organisation to product")
    public ProductDTO addOrganisationToProduct1(@Name("organisationId") long organisationId,
                                                @Name("productId") long productId) throws Throwable {

        Product product = Product.addOrganisationToProduct(organisationId, productId);
        return ProductDTO.from(product);


    }

    @Mutation
    @Transactional
    @Description("Add product")
    public  ProductDTO addProduct(@Name("name") String name,
                                  @Name("ean") String ean) throws Throwable {

        Product product = Product.createAndPersist(name, ean);
        return ProductDTO.from(product);
    }

}
