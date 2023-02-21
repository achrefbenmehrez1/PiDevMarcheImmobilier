package tn.esprit.realestate.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.realestate.Entities.Appointment;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddAppointmentResponse {

        private String message;
        private Date date;
}
