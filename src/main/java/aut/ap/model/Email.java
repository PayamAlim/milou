package aut.ap.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @Basic(optional = false)
    private User sender;

    @Basic(optional = false)
    private String subject;

    @Basic(optional = false)
    private String body;

    @Basic(optional = false)
    @Column(name = "send_time")
    private Timestamp sendTime;

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Timestamp getSendTime() { return sendTime; }

    // Constructors
    public Email() {};

    public Email(User sender, String subject, String body) {
        this.sender = sender;
        this.subject = subject;
        this.body = body;
    }

    @PrePersist
    protected void fillSendTime() {
        sendTime = Timestamp.valueOf(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", sender=" + sender.getEmail() +
                ", subject='" + subject + '\'' +
                ", body='" + body.substring(0, Math.min(body.length(), 50)) + "...'" +
                ", sendTime=" + sendTime +
                '}';
    }
}