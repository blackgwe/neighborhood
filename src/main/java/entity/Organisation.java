package entity;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple3;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Entity
@Cacheable
public class Organisation extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;
    public String zipCode;
    public LocalDate releaseDate;


    public static Uni<List<Organisation>> getAllOrganisations() {
        return Organisation.listAll(Sort.by("releaseDate"));
// does not work, why?
//                .ifNoItem()
//                .after(Duration.ofMillis(10000))
//                .fail()
//                .onFailure()
//                .recoverWithUni(Uni.createFrom().<List<PanacheEntityBase>>item(Collections.EMPTY_LIST));
    }


    public static Uni<Organisation> findByOrganisationId(Long id) {
        return findById(id);
    }


    public static Uni<Organisation> updateOrganisation(Long id, Organisation organisation) {
        return Panache
                .withTransaction(() -> findByOrganisationId(id)
                        .onItem()
                        .ifNotNull().transform(entity -> {
                            entity.title = organisation.title;
                            return entity;
                        })
                        .onFailure().recoverWithNull());
    }


    public static Uni<Organisation> addOrganisation(Organisation organisation) {
        return Panache
                .withTransaction(organisation::persist)
                .replaceWith(organisation)
                .ifNoItem().after(Duration.ofMillis(10000)).fail()
                .onFailure().transform(IllegalStateException::new);
    }


    public static Uni<Boolean> deleteOrganisation(Long id) {
        return Panache.withTransaction(() -> deleteById(id));
    }


    public static Uni<Organisation> addProductToOrganisation(Long OrganisationId, Long productId) {

        Uni<Organisation> organisation = findById(OrganisationId);
        Uni<Product> product = Product.findById(productId);
        Uni<ProductPatron> patron = ProductPatron.findById(1L);

        Uni<Tuple3<Product, Organisation, ProductPatron>> OrganisationProductUni = Uni
                .combine().all().unis(product, organisation, patron).asTuple();

        return Panache
                .withTransaction(() -> OrganisationProductUni
                        .onItem().ifNotNull()
                        .transform(entity -> {

                            if (entity.getItem1() == null || entity.getItem2() == null || entity.getItem3() == null) {
                                return null;
                            }

                            Catalog catalog = new Catalog();
                            catalog.product = entity.getItem1();
                            catalog.organisation = entity.getItem2();
                            catalog.patron = entity.getItem3();
                            return catalog;
                        })
                        .onItem().call(catalog -> catalog.persist())
                        .onItem().transform(catalog -> catalog.organisation));

    }


    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }

}
