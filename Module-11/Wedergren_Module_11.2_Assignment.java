// Amanda Wedergren
// October 1, 2025
// Module 11.2 Assignment

// JSON Example using Jackson

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JacksonExample {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Deserialize JSON from file into Java object
            User user = mapper.readValue(new File("user.json"), User.class);
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());

            // Serialize Java object into JSON string
            String jsonString = mapper.writeValueAsString(user);
            System.out.println("JSON Output: " + jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class User {
    private String name;
    private String email;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

