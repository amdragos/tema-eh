package com.example.labeh;

import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hl7.fhir.r4.model.ResourceType.OperationOutcome;

@Component
public class Provider implements IResourceProvider {

    private List<Patient> patients = new ArrayList<>();

    @Autowired
    public Provider(){
        Patient patient = new Patient();
        List<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier();
        identifier.setValue("1");
        identifiers.add(identifier);
        patient.setIdentifier(identifiers);

        List<HumanName> humanNames = new ArrayList<>();
        HumanName name = new HumanName();
        name.addGiven("ANA");
        humanNames.add(name);
        patient.setName(humanNames);
        patients.add(patient);
    }

    /**
     * The getResourceType method comes from IResourceProvider, and must
     * be overridden to indicate what type of resource this provider
     * supplies.
     */
    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @Read
    public Patient getPatientById(@IdParam IdType patientId) {
        Patient patient1 = patients.stream().filter(patient -> patient.getIdentifier().get(0).getValue().equals(patientId.getIdPart())).findFirst().get();
        System.out.println(patient1.getName());
        return patient1;
    }

    @Update
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient patient) {
        Patient updatedPatient = getPatientById(theId);
        updatedPatient.setGender(patient.getGender());
        updatedPatient.setName(patient.getName());
       updatedPatient.setAddress(patient.getAddress());

        return new MethodOutcome();
    }

    @Create
    public MethodOutcome createPatient(@ResourceParam Patient patient) {
        patients.add(patient);

        MethodOutcome retVal = new MethodOutcome();
        retVal.setId(new IdType("Patient", patient.getIdentifier().get(0).getValue(), "1"));
        return retVal;
    }

    @Delete()
    public void deletePatient(@IdParam IdType id) {
        Optional<Patient> patient = patients.stream().filter(patient1 -> patient1.getId().equals(id.getIdPart())).findFirst();
        if (!patient.isPresent()) {
            throw new ResourceNotFoundException("Nu exista pacientul");
        }
        patients.remove(patient.get());
        return;
    }

}