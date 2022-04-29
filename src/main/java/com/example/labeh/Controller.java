package com.example.labeh;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private final Provider provider;

    @Autowired
    public Controller(Provider provider) {
        this.provider = provider;
    }

    @GetMapping("/")
    public String index() {
        return "Hello world";
    }

    @GetMapping("/read")
    @Read
    public Patient readPatient(@IdParam IdType patientId){
        return provider.getPatientById(patientId);
    }
}
