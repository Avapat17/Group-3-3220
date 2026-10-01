import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class Doctor {
    
    private int id;
    private String firstName;
    private String lastName;
    private List<Integer> specialtyIds = new ArrayList<>();

    public Doctor(int id, String firstName, String lastName, List<Integer> specialtyIds) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialtyIds = specialtyIds;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public List<Integer> getSpecialtyIds() { return specialtyIds; }

    //Handling specialtyID; can be an array or single value
    @JsonSetter("specialtyID")
    public void setSpecialtyId(JsonNode value) {
        specialtyIds.clear();
        if (value == null || value.isNull()) {
            return;
        }
        if (value.isArray()) {
            value.forEach(id -> specialtyIds.add(id.intValue()));
        } else {
            specialtyIds.add(value.intValue());
        }
    }

}
