package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@AllArgsConstructor
@Data
public class GetMenteeSearchRes {

	private ArrayList<MenteeSearchDto> mentiArr;
}
