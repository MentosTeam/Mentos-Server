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
			// 각 멘티들을 전공에 맞게 분할 (First = 멘토 1 전공, Second = 멘토 2전공, Third = 그 외 전공)
			GetMentorMainRes ret = new GetMentorMainRes(mainDto.getMentos(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
			for(MainMenteeDto mentee : menteeList) {
				if(mentee.getFirstMajorCategory().equals(Integer.toString(mainDto.getMajorFirst()))) ret.getFirst().add(mentee);
				else if(mentee.getFirstMajorCategory().equals(Integer.toString(mainDto.getMajorSecond()))) ret.getSecond().add(mentee);
				else ret.getThird().add(mentee);
			}
			return ret;
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	public GetMenteeMainRes getMentorList(String memberId) throws BaseException {
		try{
			MainDto mainDto = mainRepository.getMenteeData(memberId);
			List<MainMentorDto> mentorList = mainRepository.getMentorList(mainDto);
			// 각 멘토의 글들을 전공에 맞게 분할 (First = 멘티 1 전공, Second = 멘티 2전공, Third = 그 외 전공)
			GetMenteeMainRes ret = new GetMenteeMainRes(mainDto.getMentos(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
			for(MainMentorDto mentor : mentorList) {
				if(mentor.getMajorCategoryId() == mainDto.getMajorFirst()) ret.getFirst().add(mentor);
				else if(mentor.getMajorCategoryId() == mainDto.getMajorSecond()) ret.getSecond().add(mentor);
				else ret.getThird().add(mentor);
			}
			return ret;
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
}