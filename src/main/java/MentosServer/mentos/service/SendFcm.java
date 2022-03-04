package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;

public interface SendFcm {
    void sendMessage(int mentoringId, String title, String body, int receiverFlag) throws BaseException;
}
