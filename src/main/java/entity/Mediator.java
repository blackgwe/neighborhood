package entity;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;

/**
 * A Mediator belongs to an organisation and is responsible for
 * <ul>
 *   <li>the product catalog of the organisation</li>
 *   <li>product patrons of the organisation</li>
 *   <li>members of the organisation</li>
 * </ul>
 */
@Entity
@Cacheable
@Table(
        indexes = {
                @Index(name = "organisation_id_index_4711", columnList = "organisation_id"),
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"organisation_id", "id"})
        })
public class Mediator extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String shortName;
    public String lastName;
    public String firstName;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Organisation organisation;


    public static Uni<List<Mediator>> getAllMediators() {
        return Mediator.listAll(Sort.by("id"));
    }


    public static Uni<Mediator> findByMediatorId(Long id) {
        return findById(id);
    }


    public static Uni<Mediator> updateMediator(Long id, Mediator mediator) {
        return Panache
                .withTransaction(() -> findByMediatorId(id)
                        .onItem()
                        .ifNotNull().transform(entity -> {
                            entity.lastName = mediator.lastName;
                            entity.firstName = mediator.firstName;
                            entity.shortName = mediator.shortName;
                            entity.organisation = mediator.organisation;
                            return entity;
                        })
                        .onFailure().recoverWithNull());
    }



    public static Uni<Mediator> addMediator(Mediator mediator) {
        return Panache
                .withTransaction(mediator::persist)
                .replaceWith(mediator)
                .ifNoItem().after(Duration.ofMillis(10000)).fail()
                .onFailure().transform(IllegalStateException::new);
    }


    public static Uni<Boolean> deleteMediator(Long id) {
        return Panache.withTransaction(() -> deleteById(id));
    }


    public static Uni<Organisation> findByOrganisationId(Long id) {
        return findById(id);
    }


    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }

}