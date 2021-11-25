package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.stream.Stream;

/**
 * A ProductPatron makes a product available to the community (members of an organisation).
 */
@Entity
@Cacheable
public class ProductPatron extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String firstName;

    @NotBlank
    @Length(min = 3)
    public String lastName;

    public String contactData;


    public static Stream<ProductPatron> getAllPatrons() {
        return ProductPatron.findAll(Sort.by("id")).stream();
    }


    public static ProductPatron findByPatronId(Long id) {
        return findById(id);
    }


    public static ProductPatron updatePatron(Long id, ProductPatron productPatron) {
        productPatron.persistAndFlush();
        return productPatron;
    }


    public static ProductPatron addPatron(ProductPatron productPatron) {
        productPatron.persistAndFlush();
        return productPatron;
    }


    public static boolean deletePatron(Long id) {
        return ProductPatron.deleteById(id);
    }


    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }

}