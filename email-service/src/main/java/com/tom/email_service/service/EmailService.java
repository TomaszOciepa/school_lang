package com.tom.email_service.service;

import com.tom.email_service.model.UserKeycloakDto;
import com.tom.email_service.util.DecryptPassword;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final DecryptPassword decryptPassword;

    public EmailService(JavaMailSender mailSender, DecryptPassword decryptPassword) {
        this.mailSender = mailSender;
        this.decryptPassword = decryptPassword;
    }


    @RabbitListener(queues = "email-password")
    public void receiveNewUser(UserKeycloakDto userKeycloakDto) {
        sendEmail(userKeycloakDto.getEmail(), "Rejestracja konta w Lang School", userKeycloakDto);
    }

    private void sendEmail(String to, String subject, UserKeycloakDto account) {
         String encryptPassword = account.getCredentials().get(0).getValue();
        String decryptPassword = decryptPassword(encryptPassword);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(
                "Witaj " + account.getFirstName() + ".\n" +
                        "Dziękujemy za rejestrację w naszym serwisie.\n"+
                        "Twój login to: "+account.getEmail() +"\n"+
                        "Twoje tymczasowe hasło to: "+ decryptPassword +"\n"+
                        "Zaloguj się na lang-school"+"\n"+
                        "Serdecznie pozdrawiamy.\n"+
                        "Lang School"
        );
        message.setFrom("lang.school@gmail.com");

        mailSender.send(message);
    }

    private String decryptPassword(String password){
        return decryptPassword.decryptPassword(password);
    }
}
