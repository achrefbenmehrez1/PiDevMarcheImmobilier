package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Appointment;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.agent.id = ?1")
    List<Appointment> getAllAppointmentsByAgentId(long userId);

    @Query("SELECT a FROM Appointment a WHERE a.client.id = ?1")
    List<Appointment> getAllAppointmentsByClientId(long userId);
}