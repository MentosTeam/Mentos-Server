package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMenteeProfileRes {
    private ProfileDto basicInformation;
    private String schoolName;
    private int numOfMentoring;
}
