package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
}