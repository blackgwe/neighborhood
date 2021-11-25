package boundary;

import entity.ProductPatron;
import entity.dto.ProductPatronDTO;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
public class ProductPatronResponseResource {

    @Query("allProductPatron")
    @Description("Get all patrons")
    public Uni<List<ProductPatronDTO>> getAllProductPatrons() {
        return ProductPatron.getAllPatrons().onItem().transform(ProductPatronDTO::from);
    }

    @Query("getPatron")
    @Description("Gets the patron")
    public Uni<ProductPatronDTO> getProductPatron(@Name("productPatronId") long id) {
        Uni<ProductPatron> productPatron = ProductPatron.findById(id);
        return productPatron.onItem().transform(ProductPatronDTO::from);
    }

}
