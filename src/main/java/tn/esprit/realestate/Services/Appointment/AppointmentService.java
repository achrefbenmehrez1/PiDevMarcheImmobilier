package tn.esprit.realestate.Services.Appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    private static final String BASE_URL = "https://meet.google.com/new";

    @Override
    public Appointment updateAppointment(Appointment appointment) {
        Appointment existingAppointment = appointmentRepository.findById(appointment.getId()).orElse(null);

        if (existingAppointment != null) {
            existingAppointment.setAgent(appointment.getAgent());
            existingAppointment.setClient(appointment.getClient());
            existingAppointment.setStartDate(appointment.getStartDate());
            existingAppointment.setEndDate(appointment.getEndDate());
            existingAppointment.setTitle(appointment.getTitle());
            existingAppointment.setAppointmentType(appointment.getAppointmentType());
            existingAppointment.setLocation(appointment.getLocation());

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
    public ResponseEntity<String> addAppointment(Appointment appointment, long agentId, long clientId) {
        User agent = userRepository.findById(agentId).orElse(null);
        User client = userRepository.findById(clientId).orElse(null);

        appointment.setAgent(agent);
        appointment.setClient(client);

        appointmentRepository.save(appointment);
        return new ResponseEntity<>("Appointment added successfully", HttpStatus.OK);
    }

    @Override
    public String generateGoogleMeetLink(Appointment appointment) {
        String meetingId = UUID.randomUUID().toString();
        return String.format("%s/%s?authuser=0&hs=122&hcb=%s", BASE_URL, meetingId, appointment.getId());
    }
}
