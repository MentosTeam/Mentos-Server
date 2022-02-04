package MentosServer.mentos.repository;

import MentosServer.mentos.model.domain.Mento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SettingRepositoryTest {
    @Autowired
    SettingRepository settingRepository;

    @Test
    void getMentoProfile() {
        //given
        int memberId = 1;
        //when
        Mento mentoProfile = settingRepository.getMentoProfile(memberId);
        //then
        assertNotNull(mentoProfile);
    }

    @Test
    void getMenteeProfile() {
        //given
        //when
        //then
    }
}