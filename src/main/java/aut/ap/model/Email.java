package aut.ap.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic(optional = false)
    @Column(unique = true)
    private String code;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Basic(optional = false)
    private String subject;

    @Basic(optional = false)
    private String body;

    @Basic(optional = false)
    @Column(name = "sent_time")
    private Timestamp sendTime;

    @PrePersist
    protected void fillSendTime() {
        sendTime = Timestamp.valueOf(LocalDateTime.now());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Timestamp getSentAt() { return sendTime; }
}