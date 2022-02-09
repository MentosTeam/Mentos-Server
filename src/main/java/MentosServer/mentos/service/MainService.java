package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.*;
import MentosServer.mentos.repository.MainRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Slf4j
public class MainService {
	
	private final MainRepository mainRepository;
	
	@Autowired
	public MainService(MainRepository mainRepository) {
		this.mainRepository = mainRepository;
	}
	
	public GetMentorMainRes getMenteeList(String memberId) throws BaseException{
		try{
			MainDto mainDto = mainRepository.getMentorData(memberId);
			List<MainMenteeDto> menteeList = mainRepository.getMenteeList(mainDto);
			// 각 멘티들을 전공에 맞게 분할 (First = 멘토 1 전공, Second = 멘토 2전공, others = 그 외 전공)
			GetMentorMainRes ret = new GetMentorMainRes(mainDto.getMentos(), new ArrayList<>(), new ArrayList<>());
			
			MenteeCategory first = new MenteeCategory(mainDto.getMajorFirst(), new ArrayList<MainMenteeDto>());
			MenteeCategory second = new MenteeCategory(mainDto.getMajorSecond(), new ArrayList<MainMenteeDto>());
			
			for(MainMenteeDto mentee : menteeList) {
				if (mentee.getFirstMajorCategory() == mainDto.getMajorFirst())
					first.getMentee().add(mentee);
				else if (mentee.getFirstMajorCategory() == mainDto.getMajorSecond())
					second.getMentee().add(mentee);
				else ret.getOtherMentee().add(mentee);
			}
			
			ret.getMenteeCategory().add(first);
			if(second.getMenteeCategoryId() != 0) ret.getMenteeCategory().add(second);
			
			return ret;
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	public GetMenteeMainRes getMentorList(String memberId) throws BaseException {
		try{
			MainDto mainDto = mainRepository.getMenteeData(memberId);
			List<MainMentorDto> mentorList = mainRepository.getMentorList(mainDto);
			// 각 멘토의 글들을 전공에 맞게 분할 (First = 멘티 1 전공, Second = 멘티 2전공, Other = 그 외 전공)
			GetMenteeMainRes ret = new GetMenteeMainRes(mainDto.getMentos(), new ArrayList<>(), new ArrayList<>());

			MentorCategory first = new MentorCategory(mainDto.getMajorFirst(), new ArrayList<MainMentorRes>());
			MentorCategory second = new MentorCategory(mainDto.getMajorSecond(), new ArrayList<MainMentorRes>());
			
			for(MainMentorDto mentor : mentorList) {
				if(mentor.getPostCategoryId() == mainDto.getMajorFirst()) first.getMentorPost().add(changeMentorRes(mentor));
				else if(mentor.getPostCategoryId() == mainDto.getMajorSecond()) second.getMentorPost().add(changeMentorRes(mentor));
				else ret.getOtherMentor().add(new MainOtherMentorRes(mentor.getNickName(), mentor.getMentorMajor(), mentor.getMentorImage(),
							Integer.toString(mentor.getMentorYear()) + "학번", mentor.getMentorStudentId(), mentor.getFirstMajorCategory(), mentor.getSecondMajorCategory()));
			}
			
			ret.getMentorCategory().add(first);
			if(second.getMentorCategoryId() != 0) ret.getMentorCategory().add(second);
			
			return ret;
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	private MainMentorRes changeMentorRes(MainMentorDto mentor){
		return new MainMentorRes(mentor.getNickName(), mentor.getMentorMajor(), mentor.getMentorImage(), mentor.getMentorStudentId(),
				mentor.getPostId(), mentor.getPostCategoryId(), mentor.getPostTitle(), mentor.getPostContents(), mentor.getPostImgUrl());
	}
}
