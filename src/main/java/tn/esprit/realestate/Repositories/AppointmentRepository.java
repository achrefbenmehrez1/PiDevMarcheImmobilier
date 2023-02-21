package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Appointment;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByUserId(long userId);
}