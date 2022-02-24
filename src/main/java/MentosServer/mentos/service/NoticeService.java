package MentosServer.mentos.service;


import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.GetNotificationRes;
import MentosServer.mentos.model.dto.GetNoticeRes;
import MentosServer.mentos.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;


    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // Notice 전체 조회
    public List<GetNoticeRes> noticeList() throws BaseException {
        try {
            List<GetNoticeRes> getNoticeRes = noticeRepository.getNoticeList();
            return getNoticeRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetNotificationRes> notificationList(int memberId, int statusFlag) throws BaseException {
        try {
            List<GetNotificationRes> getNotificationRes = noticeRepository.getNotification(memberId,statusFlag);
            return getNotificationRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}