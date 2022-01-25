package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.MenteeWithNickName;
import MentosServer.mentos.model.dto.GetMenteeSearchReq;
import MentosServer.mentos.model.dto.MenteeSearchDto;
import MentosServer.mentos.repository.MenteeSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MenteeSearchService {
	
	private final MenteeSearchRepository menteeSearchRepository;
	
	@Autowired
	public MenteeSearchService(MenteeSearchRepository menteeSearchRepository) {
		this.menteeSearchRepository = menteeSearchRepository;
	}
	
	// memberId로 선택했던 전공 찾기
	public String getMajorById(String memberId) throws BaseException {
		try{
			return menteeSearchRepository.getMajorById(memberId);
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	// memberId로 mentor 학교 찾기
	public String getSchoolById(String memberId) throws BaseException{
		try{
			return menteeSearchRepository.getSchoolById(memberId);
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	// 검색 결과 반환
	public ArrayList<MenteeSearchDto> menteeSearch(GetMenteeSearchReq req, String schoolId) throws BaseException{
		try{
			List<MenteeWithNickName> mentees = menteeSearchRepository.getMentee(req, schoolId);
			// UpdateAt으로 정렬 실행
			ArrayList<MenteeSearchDto> menteeDto = sortByUpdateAt(mentees);
			return menteeDto;
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	/**
	 * updateAt 기준으로 정렬 수행
	 * @param arr
	 * @return
	 */
	public ArrayList<MenteeSearchDto> sortByUpdateAt(List<MenteeWithNickName> arr) {
		ArrayList<MenteeSearchDto> ret = new ArrayList<MenteeSearchDto>();
		Collections.sort(arr);
		for(MenteeWithNickName m : arr){
			ret.add(new MenteeSearchDto(Integer.toString(m.getMemberId()), m.getMemberNickName(), Integer.toString(m.getMentiMajorFirst()), Integer.toString(m.getMentiMajorSecond()),
					m.getMentiImage(), m.getMentiIntro()));
		}
		return ret;
	}
}
