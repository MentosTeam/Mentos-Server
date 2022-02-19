package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.MailDto;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import static MentosServer.mentos.config.BaseResponseStatus.MAIL_SEND_ERROR;

@Service
@AllArgsConstructor
public class MailService {

	private JavaMailSender mailSender;
	private static final String FROM_ADDRESS = "MENTOS@gmail.com";

	public void mailSend(MailDto mailDto) throws BaseException{
		try{
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(mailDto.getAddress());
			message.setFrom(MailService.FROM_ADDRESS);
			message.setSubject(mailDto.getTitle());
			message.setText(mailDto.getMessage());
			mailSender.send(message);
		} catch (Exception exception) {
			throw new BaseException(MAIL_SEND_ERROR);
		}
	}
}
