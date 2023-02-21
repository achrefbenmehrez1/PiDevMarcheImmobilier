package tn.esprit.realestate.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Dto.AddAppointmentResponse;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.IServices.IAppointmentService;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private IAppointmentService appointmentService;

    @Autowired
    public AppointmentController(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/{propertyId}/{agentId}/{clientId}")
    public AddAppointmentResponse addAppointment(@RequestBody Appointment appointment, @PathVariable long propertyId, @PathVariable long agentId, @PathVariable long clientId) {
        return appointmentService.addAppointment(appointment, propertyId, agentId, clientId);
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
}
