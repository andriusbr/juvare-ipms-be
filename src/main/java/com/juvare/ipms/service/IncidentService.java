package com.juvare.ipms.service;

import com.juvare.ipms.contract.IncidentRequest;
import com.juvare.ipms.contract.IncidentResponse;
import com.juvare.ipms.contract.PagedResults;
import com.juvare.ipms.model.Incident;
import com.juvare.ipms.repository.IncidentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public PagedResults<IncidentResponse> getIncidents(String query, Pageable pageable) {
        Page<Incident> incidentPage = incidentRepository.findByNameStartsWith(query, pageable);
        Page<IncidentResponse> res = incidentPage.map(incident -> IncidentResponse.fromIncident(incident));
        return new PagedResults<>(res);
    }

    public Incident createIncident(IncidentRequest incidentRequest) {
        Incident newIncident = incidentRequest.toIncident();
        return incidentRepository.save(newIncident);
    }
}
