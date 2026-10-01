public class DoctorSpecialty {

    private int doctorId;
    private int specialtyId;

    public DoctorSpecialty() {}

    public DoctorSpecialty(int doctorId, int specialtyId) {
        this.doctorId = doctorId;
        this.specialtyId = specialtyId;
    }

    public int getDoctorId() { return doctorId; }
    public int getSpecialtyId() { return specialtyId; }
}
