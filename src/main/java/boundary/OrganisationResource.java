package boundary;

import entity.Catalogue;
import entity.Organisation;
import boundary.dto.ProductDTO;
import boundary.dto.OrganisationDTO;
import org.eclipse.microprofile.graphql.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@GraphQLApi
public class OrganisationResource {

    @Query("allOrganisations")
    @Description("Get all organisations")
    public List<OrganisationDTO> getAllOrganisations() {
        return OrganisationDTO.from(Organisation.getAllOrganisations());
    }

    @Description("Get an organisation")
    public OrganisationDTO getOrganisation(@Name("organisationId") long id) {
        return OrganisationDTO.from(Organisation.findByOrganisationId(id));
    }

    public List<ProductDTO> products(@Source(name = "OrganisationResponse") OrganisationDTO organisation) {

        Stream<ProductDTO> productDTOStream = Catalogue
                .getProductsByOrganisationQuery(organisation.id)
                .map(catalogItem -> ProductDTO.from(catalogItem.product));

        return productDTOStream.collect(Collectors.toList());
    }

    @Mutation
    @Transactional
    @Description("Create an organisation")
    public OrganisationDTO createOrganisation(Organisation organisation) {
        return OrganisationDTO.from(Organisation.addOrganisation(organisation));
    }

    @Mutation
    @Transactional
    @Description("Update an organisation")
    public OrganisationDTO updateOrganisation(@Name("organisationId") long id, Organisation organisation) {
        return OrganisationDTO.from(organisation.update());
    }

    @Mutation
    @Description("Delete an organisation")
    public Boolean deleteOrganisation(@Name("organisationId") long id) {
        return Organisation.deleteOrganisation(id);
    }

    @Mutation
    @Transactional
    @Description("Add product to an organisation")
    public OrganisationDTO addProductToOrganisation(@Name("organisationId") long organisationId,
                                                    @Name("productId") long productId) throws Throwable {

        return OrganisationDTO.from(Organisation.addProductToOrganisation(organisationId, productId));
    }
}