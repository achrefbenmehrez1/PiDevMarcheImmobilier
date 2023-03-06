package tn.esprit.realestate.Controllers.Appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.IServices.Appointment.IAppointmentService;
import tn.esprit.realestate.Repositories.AppointmentRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private IAppointmentService appointmentService;
    private AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentController(IAppointmentService appointmentService, AppointmentRepository appointmentRepository) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
    }

    @PutMapping("/update")
    public Appointment updateAppointment(@RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(appointment);
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

    @PostMapping("/{properyId}/{agentId}/{clientId}")
    public ResponseEntity<String> addAppointment(@RequestBody Appointment appointment, @PathVariable long propertyId, @PathVariable long agentId, @PathVariable long clientId) {
        return appointmentService.addAppointment(appointment, propertyId, agentId, clientId);
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
