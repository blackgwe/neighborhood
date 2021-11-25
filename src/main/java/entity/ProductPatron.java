package entity;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;

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
    public String lastName;
    public String contactData;


    public static Uni<List<ProductPatron>> getAllPatrons() {
        return ProductPatron.listAll(Sort.by("id"));
    }


    public static Uni<ProductPatron> findByPatronId(Long id) {
        return findById(id);
    }


    public static Uni<ProductPatron> updatePatron(Long id, ProductPatron productPatron) {
        return Panache
                .withTransaction(() -> findByPatronId(id)
                        .onItem()
                        .ifNotNull().transform(entity -> {
                            entity.firstName = productPatron.firstName;
                            entity.lastName = productPatron.lastName;
                            entity.contactData = productPatron.contactData;
                            return entity;
                        })
                        .onFailure().recoverWithNull());
    }


    public static Uni<ProductPatron> addPatron(ProductPatron productPatron) {
        return Panache
                .withTransaction(productPatron::persist)
                .replaceWith(productPatron)
                .ifNoItem().after(Duration.ofMillis(10000)).fail()
                .onFailure().transform(IllegalStateException::new);
    }


    public static Uni<Boolean> deletePatron(Long id) {
        return Panache.withTransaction(() -> deleteById(id));
    }


    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }

}