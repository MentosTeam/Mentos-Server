package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.repository.ReturnMentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ReturnMentosService {
	
	private final ReturnMentosRepository returnMentosRepository;
	
	@Autowired
	public ReturnMentosService(ReturnMentosRepository returnMentosRepository) {
		this.returnMentosRepository = returnMentosRepository;
	}
	
	public int returnMentos(String memberId) throws BaseException {
		try{
			return returnMentosRepository.returnMentos(memberId);
		} catch (Exception e){
			throw new BaseException(DATABASE_ERROR);
		}
	}
}
