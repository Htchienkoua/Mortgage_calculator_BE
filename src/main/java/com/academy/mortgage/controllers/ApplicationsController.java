package com.academy.mortgage.controllers;

import com.academy.mortgage.model.Applications;
import com.academy.mortgage.repositories.ApplicationsRepository;
import com.academy.mortgage.services.ApplicationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/applications")
public class ApplicationsController {
    @Autowired
    ApplicationsService applicationsService;

    @GetMapping()
    public List<Applications> all() {
        return applicationsService.getApplications();
    }

    @PostMapping
    public void save(@RequestBody Applications application) {
        applicationsService.addApplication(application);
    }
}
