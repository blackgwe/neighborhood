package entity;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple3;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;

@Entity
@Cacheable
public class Product extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Length(min = 5)
    public String name;


    public static Uni<List<Product>> getAllProducts() {
        return Product.listAll();
//  does not work, not necessary? why?
//                .ifNoItem()
//                .after(Duration.ofMillis(1000))
//                .fail()
//                .onFailure()
//                .recoverWithUni(Uni.createFrom().<List<PanacheEntityBase>>item(Collections.EMPTY_LIST));
    }


    public static Uni<Product> addOrganisationToProduct(Long OrganisationId, Long ProductId) {

        Uni<Product> organisation = findById(ProductId);
        Uni<Organisation> product = Organisation.findByOrganisationId(OrganisationId);
        Uni<ProductPatron> patron = ProductPatron.findById(1L);

        Uni<Tuple3<Product, Organisation, ProductPatron>> organisationProductPatronUni = Uni
                .combine()
                .all()
                .unis(organisation, product, patron)
                .asTuple();

        return Panache
                .withTransaction(() -> organisationProductPatronUni
                        .onItem().ifNotNull()
                        .transform(Product::from)
                        .onItem().call(catalog -> catalog.persist())
                        .onItem().transform(catalog -> catalog.product));

    }

    private static Catalog from(Tuple3<Product, Organisation, ProductPatron> tuple) {

        Product product = tuple.getItem1();
        Organisation organisation = tuple.getItem2();
        ProductPatron patron = tuple.getItem3();

        Catalog result = null;

        if (product != null && organisation != null && patron != null) {
            result = new Catalog();
            result.product = product;
            result.organisation = organisation;
            result.patron = patron;
        }

        return result;
    }


    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }
}
