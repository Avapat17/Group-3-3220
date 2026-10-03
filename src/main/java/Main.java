import java.util.List;

public class Main{

    public static void main(String[] args) throws Exception{
        
        Database.createTables();
        DataImporter importer = new DataImporter();
//loads doctor and specialty information
        List<Doctor> doctors = importer.loadDoctors();
        List<Specialty> specialties = importer.loadSpecialties();

        Database repository = new Database();
//inserts each specialty into each database
        for (Specialty specialty : specialties) {
            repository.insertSpecialty(specialty);
        }
//inserts each doctor into the database
        for (Doctor doctor : doctors) {
            repository.insertDoctor(doctor);
            for (int specialtyId : doctor.getSpecialtyIds()) {
                repository.insertDoctorSpecialty(doctor.getId(), specialtyId);
            }
        }

    }

}
