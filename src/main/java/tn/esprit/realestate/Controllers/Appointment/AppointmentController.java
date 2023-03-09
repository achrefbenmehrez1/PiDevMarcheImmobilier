package tn.esprit.realestate.Controllers.Appointment;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.Appointment.IAppointmentService;
import tn.esprit.realestate.Repositories.AppointmentRepository;
import tn.esprit.realestate.Services.Appointment.EmailService;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
   @Autowired
    private IAppointmentService appointmentService;
   @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private EmailService emailService;

    public AppointmentController(IAppointmentService appointmentService, AppointmentRepository appointmentRepository) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
    }

    @PutMapping("/update")
    public Appointment updateAppointment(@RequestBody Appointment appointment) {
        return appointmentService.updateAppointment( appointment);
    }

    @GetMapping("/agent/{userid}")
    public List<Appointment> getAllAppointmentsByAgentId(@PathVariable long userid) {
        return appointmentService.getAgentAppointments(userid);
    }

    @GetMapping("/client/{userid}")
    public List<Appointment> getAllAppointmentsByClientId(@PathVariable long userid) {
        return appointmentService.getClientAppointments(userid);
    }

    @DeleteMapping("/{id}")
    public boolean deleteAppointment(@PathVariable long id) {
        return appointmentService.deleteAppointment(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable long id) {
        return appointmentService.getAppointmentById(id);
    }

    @PostMapping("/{agentId}/{clientId}")
    public ResponseEntity<String> addAppointment(@RequestBody Appointment appointment, @PathVariable long agentId, @PathVariable long clientId) throws MessagingException, IOException, GeoIp2Exception {
       return appointmentService.addAppointment(appointment, agentId, clientId);


    }
    @GetMapping("/send-meeting-link/{email}")
    public String sendMeetingLink(@PathVariable String email) {
        String subject = "Meeting Link";
        String meetingLink = "https://meet.google.com/zcg-qzdr-kiq";
        try {
            emailService.sendMeetingLink(email, subject, meetingLink);
            return "Meeting link sent successfully to " + email;
        } catch (javax.mail.MessagingException e) {
            return "Error sending meeting link to " + email;
        }
    }
    @GetMapping("/{id}/google-meet-link")
    public ResponseEntity<String> generateGoogleMeetLink(@PathVariable Long id) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            String meetingLink = appointmentService.generateGoogleMeetLink(appointment);
            appointment.setMeetingLink(meetingLink);
            appointmentRepository.save(appointment);
            return ResponseEntity.ok(meetingLink);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
