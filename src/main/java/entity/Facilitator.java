package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.stream.Stream;

/**
 * A Facilitator supports the organisation (often as an employee) and is responsible for
 * <ul>
 *   <li>the product catalogue of the organisation</li>
 *   <li>product patrons of the organisation</li>
 *   <li>members of the organisation</li>
 * </ul>
 *
 * He takes over services such as
 * <ul>
 * <li>catalogue maintenance, e.g.support the suppliers to include their products in the catalogue.</li>
 * <li>mediation of the offered goods from the catalogue (bringing together product patron and consumer)</li>
 * <li>dispute settlement</li>
 * <li>[optional] identity checks of the patrons</li>
 * </ul>
 *
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
public class Facilitator extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Length(min = 3, max = 20)
    public String shortName;

    @Length(min = 3, max = 255)
    public String lastName;

    @Length(min = 3, max = 255)
    public String firstName;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Organisation organisation;


    public static Stream<Facilitator> getAllFacilitators() {
        return Facilitator.findAll(Sort.by("id")).stream();
    }


    public static Facilitator updateFacilitator(Long id, Facilitator facilitator) {
        facilitator.persistAndFlush();
        return facilitator;
    }


    public static Facilitator addFacilitator(Facilitator facilitator) {
        facilitator.persistAndFlush();
        return facilitator;
    }


    public static boolean deleteFacilitator(Long id) {
        return Facilitator.deleteById(id);
    }


    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }

}