package tn.esprit.realestate.IServices.Appointment;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.http.ResponseEntity;
import tn.esprit.realestate.Entities.Appointment;


import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public interface IAppointmentService {
    public ResponseEntity<String> addAppointment(Appointment appointment, long agentId, long clientId) throws MessagingException, IOException, GeoIp2Exception;

    public Appointment updateAppointment(Appointment appointment);

    public boolean deleteAppointment(Appointment appointment);

    public Appointment getAppointmentById(long id);

    public List<Appointment> getAgentAppointments(long userId);

    public List<Appointment> getClientAppointments(long userId);

    public String generateGoogleMeetLink(Appointment appointment);

}
