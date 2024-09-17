package com.github.adminfaces.starter.bean;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Named(value = "sampleUserBean")
@ViewScoped
public class SampleUserBean implements Serializable {

    private List<SampleUser> sampleUsers;

    @PostConstruct
    public void init() {
        sampleUsers = new ArrayList<>();
        // Generar datos de ejemplo
        sampleUsers.add(new SampleUser(1, "12345678", "John Doe", "john@example.com", "Admin", "2024-09-10"));
        sampleUsers.add(new SampleUser(2, "87654321", "Jane Smith", "jane@example.com", "User", "2024-09-09"));
        sampleUsers.add(new SampleUser(3, "45678912", "Robert Brown", "robert@example.com", "User", "2024-09-08"));
    }

    @Getter
    @Setter
    public static class SampleUser {
        private Integer userID;
        private String dni;
        private String name;
        private String email;
        private String category;
        private String date;

        public SampleUser(Integer userID, String dni, String name, String email, String category, String date) {
            this.userID = userID;
            this.dni = dni;
            this.name = name;
            this.email = email;
            this.category = category;
            this.date = date;
        }
    }
}
