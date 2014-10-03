package org.celllife.appointmentreminders.application.clinic;

import junit.framework.Assert;
import org.celllife.appointmentreminders.domain.clinic.Clinic;
import org.celllife.appointmentreminders.domain.exception.ClinicCodeExistsException;
import org.celllife.appointmentreminders.domain.exception.ClinicCodeNonexistentException;
import org.celllife.appointmentreminders.test.TestConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClinicServiceTest extends TestConfiguration {

    @Autowired
    ClinicService clinicService;

    @Before
    public void setUp() {
        clinicService.deleteAllClinics();
    }

    @Test
    public void testGetAllClinics() throws ClinicCodeExistsException {

        Clinic clinic = new Clinic();
        clinic.setName("Test Clinic");
        clinic.setCode("0001");
        clinicService.save(clinic);

        clinic = new Clinic();
        clinic.setName("Test Clinic 2");
        clinic.setCode("0002");
        clinicService.save(clinic);

        clinic = new Clinic();
        clinic.setName("Test Clinic 3");
        clinic.setCode("0003");
        clinicService.save(clinic);

        List<Clinic> clinicList = clinicService.getAllClinics();

        Assert.assertEquals(3, clinicList.size());

    }

    @Test
    public void testFindClinicByCode() throws ClinicCodeExistsException, ClinicCodeNonexistentException {

        Clinic clinic = new Clinic();
        clinic.setName("Test Clinic");
        clinic.setCode("0001");
        clinicService.save(clinic);

        Assert.assertNotNull(clinicService.findClinicByCode("0001"));

    }

    @Test(expected = ClinicCodeNonexistentException.class)
    public void testFindClinicByCode_NotFound() throws ClinicCodeExistsException, ClinicCodeNonexistentException {

        Assert.assertNull(clinicService.findClinicByCode("0002"));

    }

}
