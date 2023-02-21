package tn.esprit.realestate.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.Services.Appointment.IAppointmentService;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private IAppointmentService appointmentService;

    @Autowired
    public AppointmentController(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String getAllAppointments() {
        return "test";
    }

    @PostMapping("/{propertyId}/{agentId}/{clientId}")
    public Appointment addAppointment(@RequestBody Appointment appointment, @PathVariable long propertyId, @PathVariable long agentId, @PathVariable long clientId) {
        return appointmentService.addAppointment(appointment, propertyId, agentId, clientId);
    }

    @PutMapping
    public Appointment updateAppointment(@RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(appointment);
    }

    @GetMapping("/all/{userid}")
    public List<Appointment> getAllAppointmentsByAgentId(@PathVariable long userid) {
        return appointmentService.getAllAppointments(userid);
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
