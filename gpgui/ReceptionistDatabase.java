package com.groupproject.gpgui;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReceptionistDatabase {

    private final String URL;

    public ReceptionistDatabase() {
        this("doctorsoffice.db");
    }

    public ReceptionistDatabase(String fileName) {
        URL = "jdbc:sqlite:" + fileName;

        try {
            createTables();
        } catch (SQLException e) {
            throw new RuntimeException("Database setup failed.", e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            connection.close();
            throw e;
        }

        return connection;
    }

    public void createTables() throws SQLException {

        String[] tables = {
            "CREATE TABLE IF NOT EXISTS patient (id INTEGER PRIMARY KEY, name TEXT NOT NULL, phone TEXT NOT NULL, email TEXT, address TEXT, doctor_name TEXT)",

            "CREATE TABLE IF NOT EXISTS doctors (id INTEGER PRIMARY KEY, first_name TEXT NOT NULL, last_name TEXT NOT NULL)",

            "CREATE TABLE IF NOT EXISTS specialties (id INTEGER PRIMARY KEY, name TEXT NOT NULL)",

            "CREATE TABLE IF NOT EXISTS doctor_specialties (doctor_id INTEGER, specialty_id INTEGER, PRIMARY KEY (doctor_id, specialty_id), FOREIGN KEY (doctor_id) REFERENCES doctors(id), FOREIGN KEY (specialty_id) REFERENCES specialties(id))",

            "CREATE TABLE IF NOT EXISTS availability (id INTEGER PRIMARY KEY AUTOINCREMENT, doctor_id INTEGER NOT NULL, date TEXT NOT NULL, start_time TEXT NOT NULL, UNIQUE (doctor_id, date, start_time), FOREIGN KEY (doctor_id) REFERENCES doctors(id))",

            "CREATE TABLE IF NOT EXISTS appointments (id INTEGER PRIMARY KEY AUTOINCREMENT, patient_id INTEGER NOT NULL, availability_id INTEGER NOT NULL, status TEXT NOT NULL DEFAULT 'Booked' CHECK (status IN ('Booked', 'Cancelled')), FOREIGN KEY (patient_id) REFERENCES patient(id), FOREIGN KEY (availability_id) REFERENCES availability(id))",

            "CREATE UNIQUE INDEX IF NOT EXISTS unique_booking ON appointments(availability_id) WHERE status = 'Booked'"
        };

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            for (String sql : tables) {
                statement.execute(sql);
            }
        }
    }

    public int addPatient(String name, String phone,
                          String email, String address,
                          String doctorName) {

        String sql = """
            INSERT INTO patient (name, phone, email, address, doctor_name)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, phone);
            statement.setString(3, email);
            statement.setString(4, address);
            statement.setString(5, doctorName);
            statement.executeUpdate();

        //allow SQL to assign the patientID, then retrieve it
            try (Statement idStatement = connection.createStatement();
                 ResultSet result = idStatement.executeQuery(
                    "SELECT last_insert_rowid()")) {
                if (!result.next()) {
                    throw new SQLException("Failed to retrieve patient ID.");
                }
                return result.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not add patient.", e);
        }
    }

    public void modifyPatient(int patientId, String name, String phone,
                              String email, String address,
                              String doctorName) {

        String sql = """
            UPDATE patient
            SET name = ?, phone = ?, email = ?, address = ?, doctor_name = ?
            WHERE id = ?
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, phone);
            statement.setString(3, email);
            statement.setString(4, address);
            statement.setString(5, doctorName);
            statement.setInt(6, patientId);

            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException(
                    "Patient not found.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not update patient.", e);
        }
    }

    public void findPatient(int patientId) {

        String sql = "SELECT * FROM patient WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    System.out.println("Patient found: " + results.getString("name")
                        + ", Doctor: " + results.getString("doctor_name"));
                } else {
                    throw new IllegalStateException("Patient not found.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not search patient.", e);
        }
    }

    public void addDoctor(int doctorId, String firstName,
                          String lastName, int specialtyId) {

        String sql1 = "INSERT INTO doctors (id, first_name, last_name) VALUES (?, ?, ?)";
        String sql2 = "INSERT INTO doctor_specialties (doctor_id, specialty_id) VALUES (?, ?)";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement statement = connection.prepareStatement(sql1)) {
                    statement.setInt(1, doctorId);
                    statement.setString(2, firstName);
                    statement.setString(3, lastName);
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = connection.prepareStatement(sql2)) {
                    statement.setInt(1, doctorId);
                    statement.setInt(2, specialtyId);
                    statement.executeUpdate();
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not add doctor.", e);
        }
    }

    public List<LocalTime> viewAvailability(int doctorId, LocalDate date) {

        List<LocalTime> times = new ArrayList<>();

        String sql = """
            SELECT a.start_time FROM availability a
            WHERE a.doctor_id = ? AND a.date = ?
            AND NOT EXISTS (
                SELECT 1 FROM appointments p
                WHERE p.availability_id = a.id AND p.status = 'Booked')
            ORDER BY a.start_time
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, doctorId);
            statement.setString(2, date.toString());

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    times.add(LocalTime.parse(results.getString("start_time")));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not view availability.", e);
        }

        return times;
    }

    public void addAvailability(int doctorId, LocalDate date,
                                LocalTime startTime) {

        if (doctorId <= 0 || date == null || startTime == null ||
            date.getDayOfWeek().getValue() > 5 ||
            startTime.isBefore(LocalTime.of(8, 0)) ||
            startTime.isAfter(LocalTime.of(15, 30)) ||
            startTime.getMinute() % 30 != 0 ||
            !startTime.equals(LocalTime.of(startTime.getHour(), startTime.getMinute()))) {

            throw new IllegalArgumentException("Invalid time slot.");
        }

        String sql = "INSERT INTO availability (doctor_id, date, start_time) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, doctorId);
            statement.setString(2, date.toString());
            statement.setString(3, startTime.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not add availability.", e);
        }
    }

    public void cancelAppointment(int appointmentId) {

        String sql = "UPDATE appointments SET status = 'Cancelled' WHERE id = ? AND status = 'Booked'";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);

            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException(
                    "Appointment not found or already cancelled.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not cancel appointment.", e);
        }
    }
}
