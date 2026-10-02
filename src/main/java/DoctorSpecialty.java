public class DoctorSpecialty {

    private int doctorId; //stores doctorID
    private int specialtyId; //stores specialtyID

    public DoctorSpecialty() {}

    public DoctorSpecialty(int doctorId, int specialtyId) { //creating DoctorSpecialty using doctorID and specialtyID
        this.doctorId = doctorId;
        this.specialtyId = specialtyId;
    }

    public int getDoctorId() { return doctorId; }
    public int getSpecialtyId() { return specialtyId; }
}
