package com.groupproject.gpgui;

public class Appointment {

    private final int id;
    private final int patientId;
    private final int availabilityId;
    private String status;

    public Appointment(int id, int patientId,
                       int availabilityId, String status) {

        if (id <= 0 || patientId <= 0 || availabilityId <= 0) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        if (!"Booked".equals(status) &&
            !"Cancelled".equals(status)) {
            throw new IllegalArgumentException("Invalid status.");
        }

        this.id = id;
        this.patientId = patientId;
        this.availabilityId = availabilityId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getAvailabilityId() {
        return availabilityId;
    }

    public String getStatus() {
        return status;
    }

    public void cancelAppointment() {
        status = "Cancelled";
    }
}
