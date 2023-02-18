package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
}