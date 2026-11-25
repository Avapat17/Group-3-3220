package com.groupproject.gpgui;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;

public class Receptionist {

    private final ReceptionistDatabase database;

    public Receptionist(ReceptionistDatabase database) {
        if (database == null) {
            throw new IllegalArgumentException("Database cannot be null.");
        }

        this.database = database;
    }

    public int registerPatient(String name, String phone,
                               String email, String address,
                               String doctorName) {

        if (name == null || name.isBlank() || phone == null || phone.isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid patient information.");
        }

        return database.addPatient(name, phone, email, address, doctorName);
    }

    public void updatePatient(int patientId, String name,
                              String phone, String email,
                              String address, String doctorName) {

        if (patientId <= 0 || name == null || name.isBlank()
                || phone == null || phone.isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid patient information.");
        }

        database.modifyPatient(patientId, name, phone, email, address,
                doctorName);
    }

    public void searchPatient(int patientId) {

        if (patientId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid patient ID.");
        }

        database.findPatient(patientId);
        //NEED TO DISPLAY SOMETHING IN GUI
    }

    public void registerDoctor(int doctorId, String firstName,
                               String lastName, int specialtyId) {

        if (doctorId <= 0 || specialtyId <= 0
                || firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid doctor information.");
        }

        database.addDoctor(doctorId, firstName, lastName, specialtyId);
    }

    public List<LocalTime> viewAvailability(int doctorId, LocalDate date) {

        if (doctorId <= 0 || date == null) {
            throw new IllegalArgumentException(
                    "Invalid doctor ID or date.");
        }

        return database.viewAvailability(doctorId, date);
    }

    public void manageAvailability(int doctorId, LocalDate date,
                                   LocalTime startTime) {

        if (doctorId <= 0 || date == null || startTime == null) {
            throw new IllegalArgumentException(
                    "Doctor, date and time are required.");
        }

        DayOfWeek day = date.getDayOfWeek();

        if (day == DayOfWeek.SATURDAY ||
                day == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException(
                    "The clinic is closed on weekends.");
        }

        LocalTime opening = LocalTime.of(8, 0);
        LocalTime lastStart = LocalTime.of(15, 30);

        if (startTime.isBefore(opening) ||
                startTime.isAfter(lastStart) ||
                startTime.getMinute() % 30 != 0){

            throw new IllegalArgumentException(
                    "Invalid appointment time.");
        }

        database.addAvailability(doctorId, date, startTime);
    }

    public void cancelAppointment(int appointmentId) {

        if (appointmentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid appointment ID.");
        }

        database.cancelAppointment(appointmentId);
    }
}
