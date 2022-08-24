package com.halftusk.authentication.authservice.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.halftusk.authentication.authservice.model.request.SendEmailNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    public final AmazonSimpleEmailService amazonSimpleEmailService;

    @Value("${app.email.registration.senderEmail}")
    private String senderEmail;


    @Value("${app.email.registration.regSubject}")
    private String regSubject;

    @Value("${app.email.registration.otpSubject}")
    private String otpSubject;

    public EmailService(AmazonSimpleEmailService amazonSimpleEmailService) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
    }

    public boolean sendEmail(SendEmailNotificationRequest request) {
        String emailTemplate;
        String emailSubject;

        if(!request.isOtpEnabled()){
            emailTemplate = getEmailBodyForRegistration(request);
            emailSubject = regSubject;
        }else {
            emailTemplate = getEmailBodyForOtp(request);
            emailSubject = otpSubject;
        }
        String receiverEmail = request.getEmailId();

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(receiverEmail);
            simpleMailMessage.setSubject(regSubject);
            simpleMailMessage.setText(emailTemplate);
            amazonSimpleEmailService.sendEmail(prepareMessage(simpleMailMessage));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    private SendEmailRequest prepareMessage(SimpleMailMessage simpleMailMessage) {
        final Destination destination = new Destination();
        destination.withToAddresses(simpleMailMessage.getTo());
        final Content subject = new Content(simpleMailMessage.getSubject());
        Body body = new Body().withHtml(new Content(simpleMailMessage.getText()));
        return new SendEmailRequest()
                .withSource(simpleMailMessage.getFrom())
                .withDestination(destination)
                .withMessage(new Message(subject, body))
                .withReturnPath(simpleMailMessage.getFrom());

    }

    private String getEmailBodyForOtp(SendEmailNotificationRequest request) {

        String messageContent =  "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <title>One Time Code</title>\n" +
                "</head>\n" +
                "<body style=\"background: whitesmoke; padding: 30px; height: 100%\">\n" +
                "<h5 style=\"font-size: 18px; margin-bottom: 6px\">Dear Customer,</h5>\n" +
                "<p style=\"font-size: 16px; font-weight: 500\">Your OTP for login is : "+request.getOtp()+"</p>\n" +
                "</br><p>Please don't share the details to anyone.</p>\n" +
                "</body>\n" +
                "</html>";
        log.info("Email content for otp:: {}", messageContent);
        return messageContent;
    }

    private String getEmailBodyForRegistration(SendEmailNotificationRequest request) {
        String messageContent =  "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <title>One Time Code</title>\n" +
                "</head>\n" +
                "<body style=\"background: whitesmoke; padding: 30px; height: 100%\">\n" +
                "<h5 style=\"font-size: 18px; margin-bottom: 6px\">Dear Customer,</h5>\n" +
                "<p style=\"font-size: 16px; font-weight: 500\">Please find the otp to complete the registration</p>\n" +
                "<b>"+request.getOtp()+"</b>"+
                "</br><p>Please don't share the details to anyone.</p>\n" +
                "</body>\n" +
                "</html>";
        log.info("Email content for registration:: {}", messageContent);
        return messageContent;
    }
}
