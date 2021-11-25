package boundary.dto;

import entity.Organisation;
import org.eclipse.microprofile.graphql.Name;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Name("OrganisationResponse")
public class OrganisationDTO {

    public Long id;
    public String title;
    public String zipCode;
    public Float latitude;
    public Float longitude;

    @JsonbDateFormat("yyyy-MM-dd")
    public LocalDate createdDate;


    public static OrganisationDTO from(Organisation organisation) {
        OrganisationDTO dto = new OrganisationDTO();
        dto.id = organisation.id;
        dto.title = organisation.title;
        dto.zipCode = organisation.zipCode;
        dto.latitude = organisation.latitude;
        dto.longitude = organisation.longitude;
        dto.createdDate = organisation.createdDate;
        return dto;
    }


    public static List<OrganisationDTO> from(List<Organisation> organisation) {
        return organisation.stream().map(OrganisationDTO::from).collect(Collectors.toList());
    }
}
