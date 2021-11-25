package boundary;

import entity.ProductPatron;
import boundary.dto.ProductPatronDTO;
import org.eclipse.microprofile.graphql.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@GraphQLApi
public class ProductPatronResponseResource {

    @Query("allProductPatron")
    @Description("Get all patrons")
    public List<ProductPatronDTO> getAllProductPatrons() {

        Stream<ProductPatronDTO> productPatronDTOStream = ProductPatron.getAllPatrons().map(ProductPatronDTO::from);

        return productPatronDTOStream.collect(Collectors.toList());
    }


    @Query("getPatron")
    @Description("Gets the patron")
    public ProductPatronDTO getProductPatron(@Name("productPatronId") long id) {

        ProductPatron productPatron = ProductPatron.findById(id);

        return ProductPatronDTO.from(productPatron);
    }
}
