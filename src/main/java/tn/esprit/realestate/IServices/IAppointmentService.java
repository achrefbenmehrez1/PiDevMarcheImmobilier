package tn.esprit.realestate.IServices;

import tn.esprit.realestate.Dto.AddAppointmentResponse;
import tn.esprit.realestate.Entities.Appointment;

import java.util.List;

public interface IAppointmentService {
    public AddAppointmentResponse addAppointment(Appointment appointment, long propertyId, long agentId, long clientId);
    public Appointment updateAppointment(Appointment appointment);
    public boolean deleteAppointment(Appointment appointment);
    public Appointment getAppointmentById(long id);
    public List<Appointment> getAgentAppointments(long userId);
    public List<Appointment> getClientAppointments(long userId);
}
