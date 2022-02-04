package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;

public interface MailCheckService<T>{
    String sendEmail(T req) throws BaseException;
    String parseEmail(String email);
    boolean checkEmail(T req);
}
