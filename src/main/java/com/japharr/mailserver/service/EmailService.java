package com.japharr.mailserver.service;

import com.japharr.mailserver.model.MailData;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final Configuration configuration;
    private final JavaMailSender javaMailSender;

    public void sendMail(MailData mailData) throws IOException, TemplateException {
        log.info("sendMail: {}", mailData);
        var template = configuration.getTemplate(mailData.getTemplate() + ".ftl");
        var content = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailData.getModel());

        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            var messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(mailData.getToEmail());
            messageHelper.setSubject(mailData.getSubject());
            messageHelper.setText(content, true);
            mailData.getImages().forEach((name, location) ->
                {
                    try {
                        messageHelper.addInline(name, new ClassPathResource(location));
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            );
        };

        javaMailSender.send(mimeMessagePreparator);
    }
}
