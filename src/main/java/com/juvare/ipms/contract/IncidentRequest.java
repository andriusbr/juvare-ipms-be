package com.juvare.ipms.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juvare.ipms.model.Incident;
import com.juvare.ipms.validator.PastOrCurrentDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class IncidentRequest {

    @NotBlank(message = "Incident name cannot be empty.")
    private String name;

    @NotNull(message = "Start date cannot be empty.")
    @PastOrCurrentDate(message = "Start date cannot be greater than the current date.")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonProperty("start_date")
    private Date startDate;

    public Incident toIncident(){
        return new Incident(getName(), getStartDate());
    }
}
