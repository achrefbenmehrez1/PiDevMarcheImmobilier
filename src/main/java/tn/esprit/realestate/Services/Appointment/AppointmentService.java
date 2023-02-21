package tn.esprit.realestate.Services.Appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.AppUser;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.Entities.Property;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Repositories.AppUserRepository;
import tn.esprit.realestate.Repositories.AppointmentRepository;
import tn.esprit.realestate.Repositories.PropertyRepository;
import tn.esprit.realestate.Repositories.UserRepository;

import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PropertyRepository propertyRepository;
    private final AppUserRepository userRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, PropertyRepository propertyRepository, AppUserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Appointment addAppointment(Appointment appointment, long propertyId, long agentId, long clientId) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        AppUser agent = userRepository.findById(agentId).orElse(null);
        AppUser client = userRepository.findById(clientId).orElse(null);

        appointment.setProperty(property);
        appointment.setAgent(agent);
        appointment.setClient(client);

        return appointmentRepository.save(appointment);
    }

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
        if(appointment.getId() == null)
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
    public List<Appointment> getAllAppointments(long userId) {
        return appointmentRepository.findAllByUserId(userId);
    }
}
