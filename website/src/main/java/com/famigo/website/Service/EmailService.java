package com.famigo.website.Service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    //private PasswordEncoder passwordEncoder;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void createResponse(String userEmail) {
        //ConfirmationToken confirmationToken = new ConfirmationToken(user);
        String addr = userEmail;
        String subj = "Verify Account!";
        String body = "To verify your account, please click here : ";

        sendEmail(addr, subj, body);
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
      
        javaMailSender.send(message);
    }

    public void register(User user, String siteURL)
        throws UnsupportedEncodingException, MessagingException {
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
     
    //String randomCode = RandomString.make(64);
   // user.setVerificationCode(randomCode);
    user.setEnabled(false);
          
    sendVerificationEmail(user, siteURL);
}
}
