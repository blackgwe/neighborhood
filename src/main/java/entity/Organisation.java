package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * The business of the organisation is brokering of goods of not quite everyday use
 * between private individuals, and optionally local businesses and local associations.
 */
@Entity
@Cacheable
public class Organisation extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;
    public String zipCode;
    public Float latitude;
    public Float longitude;
    public LocalDate createdDate;


    public static List<Organisation> getAllOrganisations() {
        return Organisation.listAll(Sort.by("createdDate"));
    }


    public static Organisation findByOrganisationId(Long id) {
        return findById(id);
    }


    public Organisation update() {
        this.persistAndFlush();
        return this;
    }


    public static Organisation addOrganisation(Organisation organisation) {
        organisation.persistAndFlush();
        return organisation;
    }


    public static boolean deleteOrganisation(Long id) {
        return Organisation.deleteById(id);
    }


    public static Organisation addProductToOrganisation(Long organisationId, Long productId) throws Throwable {

        Catalogue catalogue = new Catalogue();
        catalogue.organisation = findById(organisationId);
        catalogue.product = Product.findById(productId);
        catalogue.patron = ProductPatron.findById(1L);

        try {
            catalogue.persistAndFlush();
        } catch (Throwable throwable) {
            throw Catalogue.transformThrowable(throwable, organisationId, productId);
        }

        return catalogue.organisation;
    }


    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }

}
