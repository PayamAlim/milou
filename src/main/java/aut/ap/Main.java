package aut.ap;
import aut.ap.model.*;
import aut.ap.service.EmailService;
import aut.ap.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);

        while (true) {
            System.out.println("[L]ogin, [S]ign up, [Q]uit:");
            String command = scn.nextLine().trim().toLowerCase();

            if ("l".equals(command) || "login".equalsIgnoreCase(command)) {
                System.out.print("Email: ");
                String email = completeEmail(scn.nextLine().trim());

                System.out.print("Password: ");
                String password = scn.nextLine().trim();

                try {
                    User user = UserService.loginUser(email, password);
                    System.out.println("Welcome back, " + user.getName() + "!");

                    showEmails("Unread Emails", EmailService.showUnreadEmails(user));

                    while (true) {
                        System.out.println("[S]end, [V]iew, [R]eply, [F]orward, Read by [C]ode, [Q]uit: ");
                        String cmd = scn.nextLine().trim().toLowerCase();

                        if ("s".equals(cmd) || "send".equalsIgnoreCase(cmd)) {
                            System.out.print("Recipient(s) (comma-separated): ");
                            String[] recipientsEmail = scn.nextLine().split(", ");

                            ArrayList<String> wrongEmails = new ArrayList<>();
                            ArrayList<User> existEmails = new ArrayList<>();

                            for (String recipient: recipientsEmail)
                                if (UserService.findByEmail(completeEmail(recipient)) == null)
                                    wrongEmails.add(recipient);
                                else
                                    existEmails.add(UserService.findByEmail(completeEmail(recipient)));

                            System.out.print("Subject: ");
                            String subject = scn.nextLine();

                            System.out.print("Body: ");
                            String body = scn.nextLine();

                            try {
                                Email sentEmail = EmailService.sendEmail(user, subject, body, existEmails);
                                System.out.println("Successfully sent your email.\n");

                                if (!wrongEmails.isEmpty())
                                    System.out.println("BUT NOT TO " + wrongEmails + "; THEY DOES NOT EXIST\n");

                                System.out.println("Code: " + EmailService.convertToCode(sentEmail.getId()));
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }

                        } else if ("v".equals(cmd) || "view".equalsIgnoreCase(cmd)) {
                            System.out.println("[A]ll, [U]nread, [S]ent: ");
                            String choice = scn.nextLine().trim().toLowerCase();

                            try {
                                if ("a".equals(choice) || "all".equalsIgnoreCase(choice))
                                    showEmails("All Emails", EmailService.showAllEmails(user));
                                else if ("u".equals(choice) || "unread".equalsIgnoreCase(choice))
                                    showEmails("Unread Emails", EmailService.showUnreadEmails(user));
                                else if ("s".equals(choice) || "sent".equalsIgnoreCase(choice))
                                    showEmails("Sent Emails", EmailService.showSentEmails(user));
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }

                        } else if ("r".equals(cmd) || "reply".equalsIgnoreCase(cmd)) {
                            System.out.print("Code: ");
                            String code = scn.nextLine().trim();

                            System.out.print("Body: ");
                            String body = scn.nextLine();

                            try {
                                Email repliedEmail = EmailService.replyEmail(user, code, body);

                                System.out.println("Successfully sent your reply to email" + code + "\n");
                                System.out.println("Code :" + EmailService.convertToCode(repliedEmail.getId()));
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }

                        } else if ("f".equals(cmd) || "forward".equalsIgnoreCase(cmd)) {
                            System.out.print("Code: ");
                            String code = scn.nextLine().trim();

                            System.out.print("Recipient(s) (comma-separated): ");
                            String[] recipientsEmail = scn.nextLine().split(", ");

                            ArrayList<String> wrongEmails = new ArrayList<>();
                            ArrayList<User> existEmails = new ArrayList<>();

                            for (String recipient: recipientsEmail)
                                if (UserService.findByEmail(completeEmail(recipient)) == null)
                                    wrongEmails.add(recipient);
                                else
                                    existEmails.add(UserService.findByEmail(completeEmail(recipient)));

                            try {
                                Email forwardedEmail = EmailService.forwardEmail(user, code, existEmails);

                                System.out.println("Successfully forwarded your email.");

                                if (!wrongEmails.isEmpty())
                                    System.out.println("BUT NOT TO " + wrongEmails + "; THEY DOES NOT EXIST\n");

                                System.out.println("Code: " + EmailService.convertToCode(forwardedEmail.getId()));
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }

                        } else if ("c".equals(cmd) || "read by code".equalsIgnoreCase(cmd)) {
                            System.out.print("Code: ");
                            String code = scn.nextLine().trim();

                            try {
                                Email foundEmail = EmailService.findByCode(code);

                                EmailService.readEmail(user, foundEmail);

                                System.out.println(foundEmail);
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }

                        } else if ("q".equals(cmd) || "quit".equalsIgnoreCase(cmd))
                            break;
                        else {
                            System.out.println("Invalid command. Please try again.");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            } else if ("s".equals(command) || "sign up".equalsIgnoreCase(command)) {
                System.out.print("Name: ");
                String name = scn.nextLine().trim();

                System.out.print("Email: ");
                String email = scn.nextLine().trim();

                System.out.print("Password: ");
                String password = scn.nextLine().trim();

                try {
                    UserService.registerUser(name, email, password);
                    System.out.println("Your new account is created.");
                    System.out.println("Go ahead and login!");
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if ("q".equals(command) || "quit".equalsIgnoreCase(command))
                break;
            else {
                System.err.println("Invalid command. Please try again.");
            }
        }
    }

    public static void showEmails(String title, List<Email> emails) {
        System.out.println(title + ": (" + emails.size() + ")\n");
        for (Email email: emails)
            System.out.println("+ " + email.getSender().getEmail() + " - " + email.getSubject() + "(" + EmailService.convertToCode(email.getId()) + ")");
    }

    public static String completeEmail(String email) {
        if (!email.contains("@"))
            email += "@milou.com";
        return email;
    }
}