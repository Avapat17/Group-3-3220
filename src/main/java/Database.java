import java.sql.*;

public class Database {

    private static final String URL = "jdbc:sqlite:doctors.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTables() throws SQLException {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // DOCTORS TABLE
            statement.execute("""
                CREATE TABLE IF NOT EXISTS doctors (
                    id INTEGER PRIMARY KEY,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL
                )
                """);

            // SPECIALTIES TABLE
            statement.execute("""
                CREATE TABLE IF NOT EXISTS specialties (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL
                )
                """);

            // DOCTOR X SPECIALTIES; links doctors to specialties by their IDs
            statement.execute("""
                CREATE TABLE IF NOT EXISTS doctor_specialties (
                    doctor_id INTEGER,
                    specialty_id INTEGER,
                    PRIMARY KEY (doctor_id, specialty_id),
                    FOREIGN KEY (doctor_id) REFERENCES doctors(id),
                    FOREIGN KEY (specialty_id) REFERENCES specialties(id)
                )
                """);

            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

public void insertDoctor(Doctor doctor) {

        String sql = """
            INSERT INTO doctors (id, first_name, last_name)
            VALUES (?, ?, ?)
            """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, doctor.getId());
            statement.setString(2, doctor.getFirstName());
            statement.setString(3, doctor.getLastName());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSpecialty(Specialty specialty) {

        String sql = """
            INSERT INTO specialties (id, name)
            VALUES (?, ?)
            """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, specialty.getId());
            statement.setString(2, specialty.getName());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertDoctorSpecialty(int doctorId, int specialtyId) {

        String sql = """
            INSERT INTO doctor_specialties
                (doctor_id, specialty_id)
            VALUES (?, ?)
            """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, doctorId);
            statement.setInt(2, specialtyId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 


}