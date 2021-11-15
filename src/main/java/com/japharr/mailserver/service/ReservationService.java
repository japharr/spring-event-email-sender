package com.japharr.mailserver.service;

import com.japharr.mailserver.entity.Reservation;
import com.japharr.mailserver.event.ReservationCreatedEvent;
import com.japharr.mailserver.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void createReservation(Reservation reservation) {
        reservationRepository.save(reservation);
        applicationEventPublisher.publishEvent(new ReservationCreatedEvent(reservation));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

}
