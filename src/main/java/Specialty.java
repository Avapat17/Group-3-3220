public class Specialty {

    private int id;
    private String name;

    public Specialty() {}

//creates specialty using id and name
    public Specialty(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    
}

