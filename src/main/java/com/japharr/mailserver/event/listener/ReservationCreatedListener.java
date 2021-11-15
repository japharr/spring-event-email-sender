package com.japharr.mailserver.event.listener;

import com.japharr.mailserver.entity.Reservation;
import com.japharr.mailserver.event.ReservationCreatedEvent;
import com.japharr.mailserver.model.MailData;
import com.japharr.mailserver.service.EmailService;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class ReservationCreatedListener {
    private final EmailService emailService;

    @Async
    @EventListener
    public void onReservationCreated(ReservationCreatedEvent event) throws TemplateException, IOException {
        final var reservation = (Reservation) event.getSource();

        Map<String, Object> model = new HashMap<>();
        model.put("reservation", reservation);
        Map<String, String> images = new HashMap<>();
        images.put("menu1.png", "/static/images/menu1.png");
        images.put("menu2.png", "/static/images/menu2.png");
        images.put("people.jpg", "/static/images/people.jpg");

        var mailData =  MailData.builder()
            .subject("Reservation confirmation")
            .toEmail(reservation.getEmail())
            .model(model)
            .template("reservation-confirmation")
            .images(images)
            .build();

        emailService.sendMail(mailData);
    }
}
