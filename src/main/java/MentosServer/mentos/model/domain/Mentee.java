package MentosServer.mentos.model.domain;


import lombok.Builder;
import lombok.Getter;

@Getter
public class Mentee {
    private Member member;
    private int menteeMajorFirst;
    private int menteeMajorSecond;
    private String menteeImage;
    private String menteeIntro;

    @Builder
    public Mentee(Member member,int menteeMajorFirst,int menteeMajorSecond,String menteeImage,String menteeIntro){
        this.member = member;
        this.menteeMajorFirst=menteeMajorFirst;
        this.menteeMajorSecond=menteeMajorSecond;
        this.menteeImage=menteeImage;
        this.menteeIntro=menteeIntro;
    }
}
