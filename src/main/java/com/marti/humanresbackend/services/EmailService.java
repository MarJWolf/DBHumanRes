package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final UserService userService;

    private final TemplateEngine templateEngine;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.г");
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${frontendUrl}")
    private String frontendUrl;

    public void sendEmail(String msgBody, String subject, List<String> recipients) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setText(msgBody, true);
            FileSystemResource file = new FileSystemResource(new File("C:/Users/Marti/IdeaProjects/DBHumanRes/src/main/resources/images/CompanyLogo.png"));
            mimeMessageHelper.addInline("company_logo", file);
            mimeMessageHelper.setSubject(subject);
            for (String recipient : recipients) {
                mimeMessageHelper.addTo(recipient);
            }
            javaMailSender.send(mimeMessage);
            log.info("Email '" + subject + "' was sent to: " + recipients);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not send email.");
        }
    }

    @Async
    public void sendEmailCreatedWorkleave(WorkLeave w) {
        User workleaveUser = userService.getUserById(w.getUserId());
        String subject = "Създадено искане за отпуск";
        String msgBody = getEmailMsg("email_created_workleave", workleaveUser, w);
        List<String> recipients = getManagerAndAdmins(workleaveUser);
        sendEmail(msgBody, subject, recipients);
    }

    @Async
    public void sendEmailUpcomingWorkleave(WorkLeave w, List<User> users) {
        User workleaveUser = userService.getUserById(w.getUserId());
        List<String> recipients = users.stream().map(User::getEmail).toList();
        String subject = "Наближаващ необработен отпуск";
        String msgBody = getEmailMsg("email_upcoming_workleave", workleaveUser, w);
        sendEmail(msgBody, subject, recipients);
    }

    @Async
    public void sendEmailUpdatedWorkleave(WorkLeave w) {
        User workleaveUser = userService.getUserById(w.getUserId());
        List<String> recipients = List.of(workleaveUser.getEmail());
        String subject = "Oбработен отпуск";
        String msgBody = getEmailMsg("email_workleave_status_change", workleaveUser, w);
        sendEmail(msgBody, subject, recipients);
    }

    @Async
    public void sendEmailCancelledWorkleave(WorkLeave w) {
        User workleaveUser = userService.getUserById(w.getUserId());
        String subject = "Отказ от отпуск";
        String msgBody = getEmailMsg("email_cancelled_workleave", workleaveUser, w);
        List<String> recipients = getManagerAndAdmins(workleaveUser);
        sendEmail(msgBody, subject, recipients);
    }

    private String getEmailMsg(String template, User workleaveUser, WorkLeave w) {
        Context context = getContextData(workleaveUser, w);
        return templateEngine.process(template, context);
    }

    private Context getContextData(User workleaveUser, WorkLeave w) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("user", workleaveUser.getFullName());
        context.setVariable("type", Type.getTranslationBG(w.getType()));
        context.setVariable("startDate", w.getStartDate().format(dateTimeFormatter));
        context.setVariable("endDate", w.getEndDate().format(dateTimeFormatter));
        context.setVariable("fillDate", w.getFillDate().format(dateTimeFormatter));
        context.setVariable("frontendUrl", frontendUrl);
        context.setVariable("status", w.getStatus());
        return context;
    }

    private List<String> getManagerAndAdmins(User workleaveUser) {
        List<User> admins = userService.getAdmins();
        List<String> recipients = new ArrayList<>();
        if (workleaveUser.getManagerId() != null) {
            User manager = userService.getUserById(workleaveUser.getManagerId());
            recipients.add(manager.getEmail());
        }
        for (User admin : admins) {
            recipients.add(admin.getEmail());
        }
        return recipients;

    }
}
