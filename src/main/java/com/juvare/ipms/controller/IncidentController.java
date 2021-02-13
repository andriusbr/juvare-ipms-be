package com.juvare.ipms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/incident")
public class IncidentController {

    @GetMapping("")
    public String getIncidents() {
        return "Incident list";
    }
}
