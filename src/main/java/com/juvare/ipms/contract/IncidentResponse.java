package com.juvare.ipms.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juvare.ipms.model.Incident;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class IncidentResponse {

    private Long id;

    private String name;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonProperty("start_date")
    private Date startDate;


    public IncidentResponse(Long id, String name, Date startDate){
        this.id = id;
        this.name = name;
        this.startDate = startDate;
    }

    public static IncidentResponse fromIncident(Incident incident){
        return new IncidentResponse(incident.getId(), incident.getName(), incident.getStartDate());
    }
}
