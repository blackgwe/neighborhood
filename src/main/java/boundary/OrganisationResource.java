package boundary;

import control.exception.AlreadyExistingException;
import entity.Catalog;
import entity.Organisation;
import entity.dto.ProductDTO;
import entity.dto.OrganisationDTO;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.*;

import java.util.ArrayList;
import java.util.List;

@GraphQLApi
public class OrganisationResource {

    @Query("allOrganisations")
    @Description("Get all organisations")
    public Uni<List<OrganisationDTO>> getAllOrganisations() {
        return Organisation.getAllOrganisations()
                .onItem().transform(OrganisationDTO::from);
    }

    @Query
    @Description("Get an organisation")
    public Uni<OrganisationDTO> getOrganisation(@Name("organisationId") long id) {
        return Organisation.findByOrganisationId(id)
                .onItem().transform(OrganisationDTO::from)
                .onFailure().recoverWithNull();
    }

    public Uni<List<ProductDTO>> productsFail(@Source(name = "OrganisationResponse") OrganisationDTO organisation) {
        return Catalog.getProductsByOrganisationQuery(organisation.id)
                .onItem()
                .transform(catalog -> ProductDTO.from(catalog.product))
                .collect()
                .asList();
    }
//
//    public List<ProductDTO> productsFail1(@Source(name = "OrganisationResponse") OrganisationDTO organisation) {
//        return CatalogEntity.getProductsByOrganisationQuery(organisation.id)
//                .map(catalog -> ProductDTO.from(catalog.product))
//                .collect(Collectors.toList());
//    }

    public Uni<List<ProductDTO>> productsOK(@Source(name = "OrganisationResponse") OrganisationDTO organisation) {
        List<ProductDTO> l = new ArrayList<ProductDTO>();
        ProductDTO p = new ProductDTO();
        p.name = "XXXXXX";
        p.id = -1L;
        l.add(p);

        return Uni.createFrom().item(l);
    }

    @Mutation
    @Description("Create an organisation")
    public Uni<OrganisationDTO> createOrganisation(Organisation organisation) {
        return Organisation.addOrganisation(organisation).onItem().transform(OrganisationDTO::from);
    }

    @Mutation
    @Description("Update an organisation")
    public Uni<OrganisationDTO> updateOrganisation(@Name("organisationId") long id, Organisation organisation) {
        return Organisation.updateOrganisation(id, organisation).onItem().transform(OrganisationDTO::from);
    }

    @Mutation
    @Description("Delete an organisation")
    public Uni<Boolean> deleteOrganisation(@Name("organisationId") long id) {
        return Organisation.deleteOrganisation(id);
    }

    @Mutation
    @Description("Add product to an organisation")
    public Uni<OrganisationDTO> addProductToOrganisation(@Name("organisationId") long organisationId, @Name("productId") long productId) {
        return Organisation.addProductToOrganisation(organisationId, productId).onItem().transform(OrganisationDTO::from).onFailure().
                transform(throwable -> new AlreadyExistingException("organisationId: " + organisationId + " and productId: " + productId));
    }

}