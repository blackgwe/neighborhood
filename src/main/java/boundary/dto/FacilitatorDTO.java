package boundary.dto;

import entity.Facilitator;
import org.eclipse.microprofile.graphql.Name;

import java.util.List;
import java.util.stream.Collectors;

@Name("FacilitatorResponse")
public class FacilitatorDTO {

    public Long id;

    public String firstName;
    public String lastName;
    public String shortName;


    public static FacilitatorDTO from(Facilitator facilitator) {
        FacilitatorDTO dto = new FacilitatorDTO();
        dto.id = facilitator.id;
        dto.firstName = facilitator.firstName;
        dto.lastName = facilitator.lastName;
        dto.shortName = facilitator.shortName;
        return dto;
    }


    public static List<FacilitatorDTO> from(List<Facilitator> facilitator) {
        return facilitator.stream().map(FacilitatorDTO::from).collect(Collectors.toList());
    }
}
