package boundary;

import entity.Facilitator;
import entity.Organisation;
import boundary.dto.FacilitatorDTO;
import boundary.dto.OrganisationDTO;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@GraphQLApi
public class FacilitatorResource {

    @Query("allFacilitators")
    @Description("Get all Facilitators")
    public List<FacilitatorDTO> getAllFacilitators() {
        Stream<FacilitatorDTO> facilitatorDTOStream = Facilitator.getAllFacilitators().map(FacilitatorDTO::from);
        return facilitatorDTOStream.collect(Collectors.toList());
    }


    @Query
    @Description("Get the facilitator")
    public FacilitatorDTO getFacilitator(@Name("facilitatorId") long id) {
        Facilitator facilitator = Facilitator.findById(id);
        return FacilitatorDTO.from(facilitator);
    }


    @Query
    @Description("Get his organisation")
    public OrganisationDTO getOrganisation(@Name("organisationId") long id) {
        return OrganisationDTO.from(Organisation.findByOrganisationId(id));
    }
}
