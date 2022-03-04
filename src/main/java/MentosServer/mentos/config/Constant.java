package MentosServer.mentos.config;

// 프로젝트에서 공통적으로 사용하는 상수들
public class Constant {
    // 예시 public static final String IP_ADDRESS = "127.0.0.1";
    public static final String passwordRegex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$";//8~20자 특문 1개 숫자 1개
    public static final String nameRegex ="^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+$";
    public static final String createTitle="멘토링 요청이 도착했어요 \uD83C\uDF89";
    public static final String createBody="멘토링 현황에서 수락 여부를 알려주세요-!";
    public static final String acceptTitle="\uD83C\uDF89 멘토가 멘토링을 수락했어요 \uD83C\uDF89";
    public static final String acceptBody="멘토링이 시작되었습니다-!\n" +
            "\n" +
            "오늘도 멘토-쓰를 통해\n" +
            "한 층 더 발전된 하루를 만들기 바랍니다!";
    public static final String rejectTitle="멘토가 멘토링 요청을 수락하지 않았어요";
    public static final String rejectBody="괜찮아요 \uD83D\uDE0A \n" +
            "멘토-쓰 찾기에서 나에게 맞는 멘토를 다시 찾아보아요!";
    public static final String endMentoringTitle="멘토링이 종료되었습니다-!";
    public static final String endMentoringBody="⭐️멘토링 별점 및 후기 남기기 잊지마세요✏️";




}

