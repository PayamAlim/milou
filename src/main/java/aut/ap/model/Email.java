package aut.ap.model;

import aut.ap.service.EmailService;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
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
        String code = EmailService.convertToCode(id);

        String date = sendTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<User> recipients = EmailService.findRecipientsOfEmail(this);
        String recipientList = recipients.getFirst().getEmail();
        for (int i = 1; i < recipients.size(); i ++)
            recipientList += recipients.get(i).getEmail() + ", ";

        return "Code: " + code + "\n" +
                "Recipient(s): " + recipientList + "\n" +
                "Subject: " + subject + "\n" +
                "Date: " + date + "\n\n" +
                body;

    }
}