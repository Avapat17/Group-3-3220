import java.util.List;

public class Main{

    public static void main(String[] args) throws Exception{
        
        Database.createTables();
        DataImporter importer = new DataImporter();

        List<Doctor> doctors = importer.loadDoctors();
        List<Specialty> specialties = importer.loadSpecialties();

        Database repository = new Database();

        for (Specialty specialty : specialties) {
            repository.insertSpecialty(specialty);
        }

        for (Doctor doctor : doctors) {
            repository.insertDoctor(doctor);
            for (int specialtyId : doctor.getSpecialtyIds()) {
                repository.insertDoctorSpecialty(doctor.getId(), specialtyId);
            }
        }

    }

}