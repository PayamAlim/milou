package aut.ap.service;

import aut.ap.framework.SingletonSessionFactory;
import aut.ap.model.Email;
import aut.ap.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class EmailService {
    public static Email sendEmail(User sender, String subject, String body, List<User> recipients) {
        if (recipients.isEmpty()) throw new IllegalArgumentException("No recipients");

        Email email = makeEmail(sender, subject, body);

        for (User recipient: recipients) {
            SingletonSessionFactory.get()
                    .inTransaction(session ->
                            session.createNativeMutationQuery("insert into email_recipients(email_id, recipient_id) " +
                                            "values (:email_id, :recipient_id)")
                                    .setParameter("email_id", email.getId())
                                    .setParameter("recipient_id", recipient.getId())
                                    .executeUpdate());
        }

        return email;
    }

    public static List<Email> showAllEmails(User viewer) {
        if (viewer == null) throw new IllegalArgumentException("Viewer is no one");

        return SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createNativeQuery("select e.id, e.sender_id, e.subject, e.body, e.send_time \n" +
                                        "from emails e " +
                                        "join email_recipients er on er.email_id = e.id " +
                                        "where recipient_id = :recipient_id"
                                        , Email.class)
                                .setParameter("recipient_id", viewer.getId())
                                .getResultList());
    }

    public static List<Email> showUnreadEmails(User viewer) {
        if (viewer == null) throw new IllegalArgumentException("Sender is no one");

        return SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createNativeQuery("select e.id, e.sender_id, e.subject, e.body, e.send_time \n" +
                                        "from emails e " +
                                        "join email_recipients er on er.email_id = e.id " +
                                        "where recipient_id = :recipient_id and er.read_time is null"
                                        , Email.class)
                                .setParameter("recipient_id", viewer.getId())
                                .getResultList());
    }

    public static List<Email> showSentEmails(User viewer) {
        if (viewer == null) throw new IllegalArgumentException("Sender is no one");

        return SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createNativeQuery("select * from emails e " +
                                        "where sender_id = :sender_id", Email.class)
                                .setParameter("sender_id", viewer.getId())
                                .getResultList());
    }

    public static void readEmail(User reader, Email email) {
        if (reader == null) throw new IllegalArgumentException("Sender is no one");

        if (!email.getSender().getId().equals(reader.getId()) && !findRecipientsOfEmail(email).contains(reader))
            throw new IllegalArgumentException("You cannot read this email.");

        if (!SingletonSessionFactory.get().fromTransaction(session ->
                session.createNativeQuery("select read_time " +
                                "from email_recipients " +
                                "where recipient_id = :reader_id and email_id = :email_id", Timestamp.class)
                        .setParameter("reader_id", reader.getId())
                        .setParameter("email_id", email.getId())
                        .getResultList()).isEmpty()
        && SingletonSessionFactory.get().fromTransaction(session ->
                        session.createNativeQuery("select read_time " +
                                        "from email_recipients " +
                                        "where recipient_id = :reader_id and email_id = :email_id", Timestamp.class)
                                .setParameter("reader_id", reader.getId())
                                .setParameter("email_id", email.getId())
                                .getSingleResult()) != null)
            return;

        SingletonSessionFactory.get().inTransaction(session ->
                session.createNativeMutationQuery("update email_recipients " +
                        "set read_time = :now " +
                        "where recipient_id = :reader_id and email_id = :email_id")
                        .setParameter("now", Timestamp.valueOf(LocalDateTime.now()))
                        .setParameter("reader_id", reader.getId())
                        .setParameter("email_id", email.getId())
                        .executeUpdate());

    }

    public static Email replyEmail(User sender, String code, String body) {
        Email email = findByCode(code);
        List<User> recipients = findRecipientsOfEmail(email);
        recipients.add(email.getSender());
        recipients.remove(sender);

        Email reply = makeEmail(sender, "[Re] " + email.getSubject(), body);

        sendEmail(sender, reply.getSubject(), reply.getBody(), recipients);

        return reply;
    }

    public static Email forwardEmail(User sender, String code, List<User> recipients) {
        Email email = findByCode(code);

        Email forwardedEmail = makeEmail(sender, "[Fw] " + email.getSubject(), email.getBody());

        sendEmail(sender, forwardedEmail.getSubject(), forwardedEmail.getBody(), recipients);

        return forwardedEmail;
    }

    public static Email findByCode(String code) {
        if (code == null || code.isEmpty())
            throw new IllegalArgumentException("code cannot be empty");

        Integer emailId = Integer.parseInt(code);

        Email foundEmail = SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createNativeQuery("select * from emails e " +
                                                "where e.id = :email_id"
                                        , Email.class)
                                .setParameter("email_id", emailId)
                                .getResultStream()
                                .findFirst()
                                .orElse(null));

        if (foundEmail == null)
            throw new IllegalArgumentException("No email with code: " + code);
        return foundEmail;
    }

    public static List<User> findRecipientsOfEmail(Email email) {
        return SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createNativeQuery("select u.id, u.name, u.email, u.password, u.signUp_time " +
                                        "from users u " +
                                        "join email_recipients er on er.recipient_id = u.id " +
                                        "where er.email_id = :email_id"
                                        , User.class)
                                .setParameter("email_id", email.getId())
                                .getResultList());
    }

    private static Email makeEmail(User sender, String subject, String body) {
        if (sender == null) throw new IllegalArgumentException("Sender is no one");

        if (subject == null || subject.isEmpty())
            throw new IllegalArgumentException("Subject cannot be empty");

        if (body == null || body.isEmpty())
            throw new IllegalArgumentException("Body cannot be empty");

        Email email = new Email(sender, subject, body);

        SingletonSessionFactory.get()
                .inTransaction(session ->
                        session.persist(email));

        return email;
    }

    public static String convertToCode(Integer id) {
        String code = Integer.toString(id, 36);
        int len = code.length();
        for (int i = 0; i < 6 - len; i++)
            code = "0" + code;
        return code;
    }
}
