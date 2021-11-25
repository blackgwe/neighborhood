package entity;

import control.exception.AlreadyExistingException;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Multi;
import io.vertx.pgclient.PgException;
import org.hibernate.HibernateException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * The catalog lists products that are made available by patrons and can be used by the members of an organization.
 *
 * Triple of
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
                name = "Catalog.getByOrganisationId",
                query = "SELECT c FROM Catalog c JOIN FETCH c.product where c.organisation.id = ?1"
        ),
        @NamedQuery(
                name = "Catalog.getByProductId",
                query = "SELECT c FROM Catalog c JOIN FETCH c.organisation where c.product.id = ?1")
})
public class Catalog extends PanacheEntityBase {

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


    public static Multi<Catalog> getProductsByOrganisationQuery(Long organisationId) {
        return stream("#Catalog.getByOrganisationId", organisationId);
    }


    public static Multi<Catalog> getOrganisationsByProductQuery(Long productId) {
        return stream("#Catalog.getByProductId", productId);
    }


    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }


    // todo use interface with default implementation
    public static Throwable transformThrowable(Throwable throwable, long organisationId, long productId) {
        if (throwable.getLocalizedMessage().matches("(.*)duplicate.key(.*)")) {
            return new AlreadyExistingException("organisationId: " + organisationId + " and productId: " + productId);
        }

        Throwable cause = throwable.getCause();
        if (cause instanceof HibernateException && cause.getCause() instanceof PgException) {
            return cause.getCause();
        }

        return throwable;
    }
}
