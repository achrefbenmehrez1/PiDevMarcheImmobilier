package tn.esprit.realestate.Services.Appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.Entities.Property;
import tn.esprit.realestate.IServices.IAppointmentService;
import tn.esprit.realestate.Repositories.AppointmentRepository;
import tn.esprit.realestate.Repositories.PropertyRepository;
import tn.esprit.realestate.Repositories.UserRepository;

import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, PropertyRepository propertyRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }




    @Override
    public Appointment updateAppointment(Appointment appointment) {
        Appointment existingAppointment = appointmentRepository.findById(appointment.getId()).orElse(null);


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
        return null;
    }

    @Override
    public Appointment addAppointment(Appointment appointment, long propertyId, long agentId, long clientId) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        User agent = userRepository.findById(agentId).orElse(null);
        User client = userRepository.findById(clientId).orElse(null);


        appointment.setAgent(agent);
        appointment.setClient(client);

        return appointmentRepository.save(appointment);

    }
    @Override
    public List<Appointment> getAllAppointments(long userId) {
        return appointmentRepository.findByAgent_IdOrClient_Id(userId);
    }
}
