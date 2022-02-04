package MentosServer.mentos.model.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Mento {
    private Member member;
    private int mentoMajorFirst;
    private int mentoMajorSecond;
    private String mentoImage;
    private String mentoIntro;

    @Builder
    public Mento(Member member,int mentoMajorFirst,int mentoMajorSecond,String mentoImage,String mentoIntro){
        this.member = member;
        this.mentoMajorFirst=mentoMajorFirst;
        this.mentoMajorSecond=mentoMajorSecond;
        this.mentoImage=mentoImage;
        this.mentoIntro=mentoIntro;
    }
}
