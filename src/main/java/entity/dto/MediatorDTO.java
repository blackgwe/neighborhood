package entity.dto;

import entity.Mediator;
import org.eclipse.microprofile.graphql.Name;

import java.util.List;
import java.util.stream.Collectors;

@Name("MediatorResponse")
public class MediatorDTO {

    public Long id;

    public String firstName;
    public String lastName;
    public String shortName;


    public static MediatorDTO from(Mediator mediator) {
        MediatorDTO dto = new MediatorDTO();
        dto.id = mediator.id;
        dto.firstName = mediator.firstName;
        dto.lastName = mediator.lastName;
        dto.shortName = mediator.shortName;
        return dto;
    }


    public static List<MediatorDTO> from(List<Mediator> mediator) {
        return mediator.stream().map(MediatorDTO::from).collect(Collectors.toList());
    }
}
