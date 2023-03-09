package tn.esprit.realestate.Services.Appointment;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.Appointment.IAppointmentService;
import tn.esprit.realestate.Repositories.AppointmentRepository;
import tn.esprit.realestate.Entities.Property;
import tn.esprit.realestate.Repositories.PropertyRepository;
import tn.esprit.realestate.Repositories.UserRepository;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService {
    private static final String BASE_URL = "https://meet.google.com/new";
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
 @Autowired
 private HttpServletRequest request;


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
    public ResponseEntity<String> addAppointment(Appointment appointment, long agentId, long clientId) throws IOException, GeoIp2Exception {
        User agent = userRepository.findById(agentId).orElse(null);
        User client = userRepository.findById(clientId).orElse(null);

        appointment.setAgent(agent);
        appointment.setClient(client);


        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        String dbLocation = "C:\\Users\\ahmed\\OneDrive\\Bureau\\PiDevMarcheImmobilier 2 3\\PiDevMarcheImmobilier 2\\PiDevMarcheImmobilier\\src\\main\\resources\\static\\GeoLite2-City.mmdb";

        File database = new File(dbLocation);

        DatabaseReader dbReader = new DatabaseReader.Builder(database)
                .build();

        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = dbReader.city(ipAddress);

        Double latitude = response.getLocation().getLatitude();
        Double longitude = response.getLocation().getLongitude();
        String country = response.getCountry().getName();
        String city = response.getCity().getName();
        String state = response.getLeastSpecificSubdivision().getName();

        System.out.println(latitude);
        System.out.println(longitude);
        System.out.println(country);


        appointmentRepository.save(appointment);
        return new ResponseEntity<>("Appointment added successfully", HttpStatus.OK);

    }

    @Override
    public String generateGoogleMeetLink(Appointment appointment) {
        String meetingId = UUID.randomUUID().toString();
        return String.format("%s/%s?authuser=0&hs=122&hcb=%s", BASE_URL, meetingId, appointment.getId());
    }



}
