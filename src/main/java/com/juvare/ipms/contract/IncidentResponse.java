package com.juvare.ipms.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.juvare.ipms.model.Incident;
import lombok.Data;

import java.util.Date;

@Data
public class IncidentResponse {

    private Long id;

    private String name;

    @JsonFormat(pattern="yyyy-MM-dd")
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
