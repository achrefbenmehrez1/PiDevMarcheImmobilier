package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.realestate.Entities.Appointment;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("select a from Appointment a where a.agent.id = ?1 or a.client.id = ?1")
    List<Appointment> findByAgent_IdOrClient_Id(Long id);

}