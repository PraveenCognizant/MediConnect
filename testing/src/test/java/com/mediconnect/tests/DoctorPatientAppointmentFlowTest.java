package com.mediconnect.tests;

import com.mediconnect.base.BaseTest;
import com.mediconnect.base.DriverFactory;
import com.mediconnect.pages.BookAppointmentPage;
import com.mediconnect.pages.DoctorAppointmentsPage;
import com.mediconnect.pages.LoginPage;
import com.mediconnect.pages.ScheduleSlotsPage;
import com.mediconnect.dataprovider.JsonDataProviders;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class DoctorPatientAppointmentFlowTest extends BaseTest {

    @Test(dataProvider = "appointmentFlowData", dataProviderClass = JsonDataProviders.class)
    public void doctorSchedulesSlotPatientBooksAndDoctorAccepts(
            String doctorEmail,
            String doctorPassword,
            String doctorName,
            String specialization,
            String session,
            String date,
            String patientEmail,
            String patientPassword,
            String problem
    ) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).openLoggedOut();
        loginPage.loginAsDoctor(doctorEmail, doctorPassword);
        Assert.assertTrue(loginPage.isOnPath("/doctordashboard"), "Doctor should login before scheduling");

        ScheduleSlotsPage schedulePage = new ScheduleSlotsPage(DriverFactory.getDriver()).open();
        schedulePage.addSchedule(date, session);
        Assert.assertTrue(schedulePage.hasSchedule(date), "Doctor schedule should be created for " + date);

        loginPage.openLoggedOut();
        loginPage.loginAsPatient(patientEmail, patientPassword);
        Assert.assertTrue(loginPage.isOnPath("/userdashboard"), "Patient should login before booking");

        BookAppointmentPage appointmentPage = new BookAppointmentPage(DriverFactory.getDriver()).open();
        Assert.assertEquals(appointmentPage.patientEmail(), patientEmail, "Patient email should be prefilled");
        appointmentPage.chooseSpecializationContaining(specialization);
        appointmentPage.chooseDoctorContaining(doctorName);
        Assert.assertTrue(
                appointmentPage.hasAvailableSlotsForSelectedDoctor(),
                "Expected available slot for doctor " + doctorName + " on " + date
        );
        appointmentPage.chooseDate(date);
        appointmentPage.chooseSlot(session);
        appointmentPage.enterProblem(problem);
        Assert.assertFalse(appointmentPage.isConfirmBookingDisabled(), "Booking button should be enabled");
        appointmentPage.confirmBooking();
        Assert.assertTrue(appointmentPage.isOnUserDashboard(), "Patient booking should redirect to dashboard");

        loginPage.openLoggedOut();
        loginPage.loginAsDoctor(doctorEmail, doctorPassword);
        Assert.assertTrue(loginPage.isOnPath("/doctordashboard"), "Doctor should login again before accepting");

        DoctorAppointmentsPage appointmentsPage = new DoctorAppointmentsPage(DriverFactory.getDriver()).open();
        appointmentsPage.acceptRequest(patientEmail, date, problem);
        Assert.assertTrue(
                appointmentsPage.isAccepted(patientEmail, date, problem),
                "Doctor should accept John's appointment request"
        );
    }
}
