package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetCategoryRes;
import MentosServer.mentos.service.CategoryService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
	
	private final JwtService jwtService;
	private final CategoryService categoryService;
	
	@Autowired
	public CategoryController(JwtService jwtService, CategoryService categoryService) {
		this.jwtService = jwtService;
		this.categoryService = categoryService;
	}
	
	@GetMapping("/category")
	public BaseResponse<GetCategoryRes> getCategory(@RequestParam int flag){
		try {
			// jwt에서 memberId 뽑아내기
			int memberId = jwtService.getMemberId();
			// 해당 멤버가 멘토/멘티 프로필 생성시 선택했던 전공 카테고리 반환
			GetCategoryRes res = categoryService.getCategory(memberId, flag);
			return new BaseResponse<>(res);
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}
}
