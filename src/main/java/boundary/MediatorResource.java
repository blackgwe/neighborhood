package boundary;

import entity.Mediator;
import entity.Organisation;
import entity.dto.MediatorDTO;
import entity.dto.OrganisationDTO;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
public class MediatorResource {

    @Query("allMediators")
    @Description("Get all Mediators")
    public Uni<List<MediatorDTO>> getAllMediators() {
        return Mediator.getAllMediators().onItem().transform(MediatorDTO::from);
    }


    @Query
    @Description("Get an mediator")
    public Uni<MediatorDTO> getMediator(@Name("mediatorId") long id) {
        Uni<Mediator> mediator = Mediator.findById(id);
        return mediator.onItem().transform(MediatorDTO::from);
    }


    @Query
    @Description("Get its organisation")
    public Uni<OrganisationDTO> getOrganisation(@Name("organisationId") long id) {
        return Organisation.findByOrganisationId(id)
                .onItem().transform(OrganisationDTO::from)
                .onFailure().recoverWithNull();
    }
}
