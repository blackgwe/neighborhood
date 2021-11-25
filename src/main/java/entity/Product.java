package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.validator.constraints.EAN;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Cacheable
public class Product extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Length(min = 3)
    public String name;

    @EAN
    public String ean;

    public Byte eanVariant;

    @Length(min = 1, max = 63)
    public String payloadInterface;

    @NotNull
    @Range(min=0L, max=65535)
    public Integer payloadVersion;

    @NotEmpty
    public String payloadJson;


    public static List<Product> getAllProducts() {
        return Product.listAll();
    }


    public static Product addOrganisationToProduct(Long organisationId, Long productId) throws Throwable {

        Product product = findById(productId);
        Organisation organisation = Organisation.findById(organisationId);
        ProductPatron patron = ProductPatron.findById(1L);

        if (product != null && organisation != null && patron != null) {
            Catalogue catalogue = new Catalogue();
            catalogue.product = product;
            catalogue.organisation = organisation;
            catalogue.patron = patron;

            try {
                catalogue.persistAndFlush();
            } catch (Throwable throwable) {
                throw Catalogue.transformThrowable(throwable, organisationId, productId);
            }
        }

        return product;
    }

    public static Product createAndPersist(String name,
                                           String ean) throws Throwable {

        Product product = new Product();
        product.name = name;
        product.ean = ean;

        try {
            product.persistAndFlush();
        } catch (Throwable throwable) {
            throw Product.transformThrowable(throwable);
        }

        return product;
    }


    private static Throwable transformThrowable(Throwable throwable) {

        while (throwable.getMessage().contains("could not execute statement")) {
            throwable = throwable.getCause();
        }

        if (throwable instanceof ConstraintViolationException && throwable.getMessage().contains("Validation failed")) {
            return new control.exception.ConstraintViolationException(throwable.getMessage());
        }

        return throwable;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }
}
