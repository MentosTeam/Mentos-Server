package MentosServer.mentos.service;

import MentosServer.mentos.repository.PasswordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {

    PasswordService passwordService;
    @Mock
    PasswordRepository passwordRepository;
    @Mock
    MailService mailService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        passwordService = new PasswordService(passwordRepository,mailService);
    }

    //랜덤으로 비밀번호 생성되는지 확인만 하는 테스트 코드
    @Test
    void createRandomPwTest() {
        //given
        //when
        String randomPwd = passwordService.createRandomPw();
        //then
        System.out.println("randomPwd = " + randomPwd);
        assertEquals(randomPwd.length(),10);
    }
}