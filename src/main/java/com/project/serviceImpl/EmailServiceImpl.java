package com.project.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.project.dto.EmailDetails;
import com.project.service.EmailService;


@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String senderEmail;
	
	
	@Override
	public void sendEmailAlert(EmailDetails emailDetails) {
		
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(senderEmail);
			System.out.println("From EmailId: " + senderEmail);
			mailMessage.setTo(emailDetails.getRecipient());
			System.out.println("To EmailId: " + emailDetails.getRecipient());
			mailMessage.setText(emailDetails.getMessageBody());
			mailMessage.setSubject(emailDetails.getSubject());
			
			javaMailSender.send(mailMessage);
			System.out.println("Mail Sent SuccessFully");
			
		} catch(MailException e) {
			throw new RuntimeException(e);
			
		}
		
		
	}

}
