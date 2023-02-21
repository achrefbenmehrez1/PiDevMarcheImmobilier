package tn.esprit.realestate.Services.Appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Dto.AddAppointmentResponse;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.Entities.Property;
import tn.esprit.realestate.IServices.IAppointmentService;
import tn.esprit.realestate.Repositories.AppointmentRepository;
import tn.esprit.realestate.Repositories.PropertyRepository;
import tn.esprit.realestate.Repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    public Appointment updateAppointment(Appointment appointment) {
        Appointment existingAppointment = appointmentRepository.findById(appointment.getId()).orElse(null);

        if (existingAppointment != null) {
            existingAppointment.setAgent(appointment.getAgent());
            existingAppointment.setClient(appointment.getClient());
            existingAppointment.setProperty(appointment.getProperty());
            existingAppointment.setDescription(appointment.getDescription());
            existingAppointment.setDate(appointment.getDate());

            appointmentRepository.save(existingAppointment);
        }

        return existingAppointment;
    }

    @Override
    public boolean deleteAppointment(Appointment appointment) {
        if (appointment.getId() == null)
            return false;
        else {
            appointmentRepository.delete(appointment);
            return true;
        }
    }

    @Override
    public Appointment getAppointmentById(long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Appointment> getAgentAppointments(long userId) {
        return appointmentRepository.getAllAppointmentsByAgentId(userId);
    }

    @Override
    public List<Appointment> getClientAppointments(long userId) {
        return appointmentRepository.getAllAppointmentsByClientId(userId);
    }

    @Override
    public AddAppointmentResponse addAppointment(Appointment appointment, long propertyId, long agentId, long clientId) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        User agent = userRepository.findById(agentId).orElse(null);
        User client = userRepository.findById(clientId).orElse(null);

        appointment.setProperty(property);
        appointment.setAgent(agent);
        appointment.setClient(client);

        appointmentRepository.save(appointment);
        System.out.println(appointment);
        return AddAppointmentResponse.builder()
                .message("Appointment added successfully")
                .date(appointment.getDate())
                .build();

    }
}
