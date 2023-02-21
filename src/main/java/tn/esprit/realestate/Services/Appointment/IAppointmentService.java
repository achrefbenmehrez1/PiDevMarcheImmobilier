package tn.esprit.realestate.Services.Appointment;

import tn.esprit.realestate.Entities.Appointment;

import java.util.List;

public interface IAppointmentService {
    Appointment addAppointment(Appointment appointment, long propertyId, long agentId, long clientId);

    Appointment updateAppointment(Appointment appointment);

    boolean deleteAppointment(Appointment appointment);

    Appointment getAppointmentById(long id);

    List<Appointment> getAllAppointments(long userId);
}
