package entity;

import control.exception.AlreadyExistingException;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.postgresql.util.PSQLException;

import javax.persistence.*;
import java.util.stream.Stream;

/**
 * The catalogue lists products that are made available by patrons and can be used by the members of an organization.
 *
 * Technically, it's a triple of
 * <ul>
 *     <li>product</li>
 *     <li>patron of that product</li>
 *     <li>organisation</li>
 * </ul>
 */
@Entity
@Cacheable
@Table(
        indexes = {
                @Index(name = "organisation_id_index", columnList = "organisation_id"),
                @Index(name = "product_id_index", columnList = "product_id"),
                @Index(name = "patron_id_index", columnList = "patron_id"),
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"organisation_id", "product_id", "patron_id"})
        })
@NamedQueries(value = {
        @NamedQuery(
                name = "Catalogue.getByOrganisationId",
                query = "SELECT c FROM Catalogue c JOIN FETCH c.product where c.organisation.id = ?1"
        ),
        @NamedQuery(
                name = "Catalogue.getByProductId",
                query = "SELECT c FROM Catalogue c JOIN FETCH c.organisation where c.product.id = ?1")
})
public class Catalogue extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(nullable = false)
    public Organisation organisation;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(nullable = false)
    public Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(nullable = false)
    public ProductPatron patron;


    public static Stream<Catalogue> getProductsByOrganisationQuery(Long organisationId) {
        return stream("#Catalogue.getByOrganisationId", organisationId);
    }


    public static Stream<Catalogue> getOrganisationsByProductQuery(Long productId) {
        return stream("#Catalogue.getByProductId", productId);
    }


    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }

    public static Throwable transformThrowable(Throwable throwable, long organisationId, long productId) {

        while (throwable.getMessage().contains("could not execute statement")) {
            throwable = throwable.getCause();
        }

        if (throwable instanceof PSQLException && throwable.getMessage().contains("duplicate key")) {
           return new AlreadyExistingException("[organisationId: " + organisationId + " and productId: " + productId +"]");
        }

        return throwable;
    }
}
