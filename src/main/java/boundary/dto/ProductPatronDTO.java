package boundary.dto;

import entity.ProductPatron;
import org.eclipse.microprofile.graphql.Name;

import java.util.List;
import java.util.stream.Collectors;

@Name("ProductPatronResponse")
public class ProductPatronDTO {

    public Long id;

    public String firstName;
    public String lastName;
    public String contactData;


    public static ProductPatronDTO from(ProductPatron productPatron) {
        ProductPatronDTO dto = new ProductPatronDTO();
        dto.id = productPatron.id;
        dto.firstName = productPatron.firstName;
        dto.lastName = productPatron.lastName;
        dto.contactData = productPatron.contactData;
        return dto;
    }


    public static List<ProductPatronDTO> from(List<ProductPatron> productPatron) {
        return productPatron.stream().map(ProductPatronDTO::from).collect(Collectors.toList());
    }
}
