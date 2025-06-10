package aut.ap.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    @Column(unique = true)
    private String email;

    @Basic(optional = false)
    private String password;

    @Basic(optional = false)
    @Column(name = "signUp_time")
    private Timestamp signUpTime;

    @PrePersist
    private void fillTime() {
        signUpTime = Timestamp.valueOf(LocalDateTime.now());
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Timestamp getSignUpTime() { return signUpTime; }
}