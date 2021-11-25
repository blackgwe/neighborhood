package entity.dto;

import entity.Product;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import org.eclipse.microprofile.graphql.Name;

import java.util.List;
import java.util.stream.Collectors;

@Name("ProductResponse")
public class ProductDTO {

    public String name;
    public Long id;


    public static ProductDTO from(Product product) {
        ProductDTO p = new ProductDTO();
        p.id = product.id;
        p.name = product.name;
        return p;
    }


    public static List<ProductDTO> from(List<Product> product) {
        return product.stream().map(ProductDTO::from).collect(Collectors.toList());
    }

    public static ProductDTO from(PanacheEntityBase entity) {
        if (entity instanceof Product) {
            return from((Product)entity);
        }

        return null;
    }
}
