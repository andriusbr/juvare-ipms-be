package com.juvare.ipms.controller;

import com.juvare.ipms.contract.IncidentRequest;
import com.juvare.ipms.contract.IncidentResponse;
import com.juvare.ipms.contract.PagedResults;
import com.juvare.ipms.model.Incident;
import com.juvare.ipms.service.IncidentService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/incident")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("")
    public PagedResults<IncidentResponse> getIncidents(@RequestParam(name="query", defaultValue = "") String query, Pageable pageable) {
        return incidentService.getIncidents(query, pageable);
    }

    @PostMapping("")
    public IncidentResponse createIncident(@Valid @RequestBody IncidentRequest incidentRequest) {
        Incident incident = incidentService.createIncident(incidentRequest);
        return IncidentResponse.fromIncident(incident);
    }
}
