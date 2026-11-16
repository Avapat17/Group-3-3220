import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class TimeAppointment {

    private int id;
    private int doctorId;
    private LocalDate date;
    private LocalTime startTime;

    public TimeAppointment(int id, int doctorId,
                           LocalDate date, LocalTime startTime) {

        if (id <= 0 || doctorId <= 0) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        if (date == null || startTime == null) {
            throw new IllegalArgumentException("Date and time are required.");
        }

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
            date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Clinic is closed on weekends.");
        }

        if (startTime.isBefore(LocalTime.of(8, 0)) ||
        	    startTime.isAfter(LocalTime.of(15, 30)) ||
        	    startTime.getMinute() % 30 != 0) {

        	    throw new IllegalArgumentException("Invalid appointment time.");
        	}

        this.id = id;
        this.doctorId = doctorId;
        this.date = date;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return startTime.plusMinutes(30);
    }
}
