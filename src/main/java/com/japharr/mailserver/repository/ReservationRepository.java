package com.japharr.mailserver.repository;

import com.japharr.mailserver.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
