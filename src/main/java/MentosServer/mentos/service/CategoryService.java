package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.GetCategoryRes;
import MentosServer.mentos.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public GetCategoryRes getCategory(int memberId, int flag) throws BaseException {
		try{
			return flag == 0 ? categoryRepository.getMentorCategory(memberId) : categoryRepository.getMenteeCategory(memberId);
		} catch(Exception exception){
			throw new BaseException(DATABASE_ERROR);
		}
	}
}
