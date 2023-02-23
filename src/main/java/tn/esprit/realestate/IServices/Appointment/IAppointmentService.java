package tn.esprit.realestate.IServices.Appointment;

import org.springframework.http.ResponseEntity;
import tn.esprit.realestate.Entities.Appointment;

import java.util.List;

public interface IAppointmentService {
    public ResponseEntity<String> addAppointment(Appointment appointment, long agentId, long clientId);

    public Appointment updateAppointment(Appointment appointment);

    public boolean deleteAppointment(Appointment appointment);

    public Appointment getAppointmentById(long id);

    public List<Appointment> getAgentAppointments(long userId);

    public List<Appointment> getClientAppointments(long userId);

    public String generateGoogleMeetLink(Appointment appointment);
}
