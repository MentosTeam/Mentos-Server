package MentosServer.mentos.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.AssertTrue;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void getImageUrl() {
        //given
        int postId =1;
        //when
        Optional<String> url = boardRepository.getImageUrl(1);
        //then
        assertEquals(url.get(),"null");
    }
}