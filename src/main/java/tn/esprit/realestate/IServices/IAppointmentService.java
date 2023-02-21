package tn.esprit.realestate.IServices;

import tn.esprit.realestate.Entities.Appointment;

import java.util.List;

public interface IAppointmentService {
    public Appointment addAppointment(Appointment appointment, long propertyId, long agentId, long clientId);

    public Appointment updateAppointment(Appointment appointment);

    public boolean deleteAppointment(Appointment appointment);

    public Appointment getAppointmentById(long id);

    public List<Appointment> getAllAppointments(long userId);
}
