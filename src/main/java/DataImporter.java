import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

//loading data from JSON files
public class DataImporter {

    //ObjectMapper can convert between JSON & Java objects
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Doctor> loadDoctors() throws Exception {

        InputStream input = getClass().getResourceAsStream("/doctors.json");
        return mapper.readValue(input, new TypeReference<List<Doctor>>() {});

    }

    public List<Specialty> loadSpecialties() throws Exception {

        InputStream input = getClass().getResourceAsStream("/specialties.json");
        return mapper.readValue(input, new TypeReference<List<Specialty>>() {});

    }
}
