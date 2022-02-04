package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMyPageRes {
    private GetMentorProfileRes mentorProfile;
    private GetMenteeProfileRes menteeProfile;
}
