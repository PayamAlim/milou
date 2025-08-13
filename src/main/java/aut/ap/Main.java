package aut.ap;
import aut.ap.model.*;
import aut.ap.service.EmailService;
import aut.ap.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        HashMap<Integer, JFrame> accountFrames = new HashMap<>();

        // Sizes
        Dimension mainSize = new Dimension(500, 300);
        Dimension AccSize = new Dimension(500, 400);
        Dimension buttonSize = new Dimension(150, 50);

        JFrame mainFrame = new JFrame("MILOU");
        mainFrame.setLayout(null);
        mainFrame.setSize(mainSize);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel publicPanel = new JPanel();
        publicPanel.setLayout(null);
        publicPanel.setSize(mainSize);
        publicPanel.setLocation(0, 0);

        ImageIcon milouIcon = new ImageIcon("assets\\milou.jpg");
        JLabel milouLabel = new JLabel(new ImageIcon(milouIcon.getImage().getScaledInstance(190, 190, Image.SCALE_SMOOTH)));
        milouLabel.setBounds(50, 35, 190, 190);

        JButton logInButton = new JButton("Log In");
        logInButton.setLocation(300, 35);
        logInButton.setSize(buttonSize);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setLocation(300, 105);
        signUpButton.setSize(buttonSize);

        JButton quitButton = new JButton("Quit");
        quitButton.setLocation(300, 175);
        quitButton.setSize(buttonSize);

        publicPanel.add(milouLabel);
        publicPanel.add(logInButton);
        publicPanel.add(signUpButton);
        publicPanel.add(quitButton);
        mainFrame.add(publicPanel);

        JPanel logInPanel = new JPanel();
        logInPanel.setLayout(null);
        logInPanel.setSize(mainSize);
        logInPanel.setLocation(0, 0);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(100, 45, 80, 25);
        JTextField emailField = new JTextField();
        emailField.setBounds(190, 45, 200, 25);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 75, 80, 25);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(190, 75, 200, 25);

        JButton logInSubmitButton = new JButton("Submit");
        logInSubmitButton.setSize(buttonSize);
        logInSubmitButton.setLocation(190, 125);

        JButton logInBackButton = new JButton("Back");
        logInBackButton.setSize(buttonSize);
        logInBackButton.setLocation(190, 175);

        logInSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = completeEmail(emailField.getText());
                String password = new String(passwordField.getPassword());

                try {
                    User user = UserService.loginUser(email, password);
                    JOptionPane.showMessageDialog(mainFrame,"Welcome back, " + user.getName() + "!\n");

                    JFrame newFrame = new JFrame(user.getName());
                    newFrame.setSize(AccSize);
                    int accLocType = accountFrames.size() % 4;
                    newFrame.setLocation(((accLocType + 1) % 2) * 1040, ((accLocType / 2) % 2) * 400);
                    newFrame.setVisible(true);

                    accountFrames.put(user.getId(), newFrame);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame,"Error: " + ex.getMessage());
                }
            }
        });

        logInBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.remove(logInPanel);
                mainFrame.add(publicPanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        logInPanel.add(emailLabel);
        logInPanel.add(emailField);
        logInPanel.add(passwordLabel);
        logInPanel.add(passwordField);
        logInPanel.add(logInSubmitButton);
        logInPanel.add(logInBackButton);

        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(null);
        signUpPanel.setSize(mainSize);
        signUpPanel.setLocation(0, 0);

        JLabel signUpNameLabel = new JLabel("Name:");
        signUpNameLabel.setBounds(100, 15, 80, 25);
        JTextField signUpNameField = new JTextField();
        signUpNameField.setBounds(190, 15, 200, 25);

        JLabel signUpEmailLabel = new JLabel("Email:");
        signUpEmailLabel.setBounds(100, 45, 80, 25);
        JTextField signUpEmailField = new JTextField();
        signUpEmailField.setBounds(190, 45, 200, 25);

        JLabel signUpPasswordLabel = new JLabel("Password:");
        signUpPasswordLabel.setBounds(100, 75, 80, 25);
        JPasswordField signUpPasswordField = new JPasswordField();
        signUpPasswordField.setBounds(190, 75, 200, 25);

        JButton signUpSubmitButton = new JButton("Submit");
        signUpSubmitButton.setSize(buttonSize);
        signUpSubmitButton.setLocation(190, 125);

        JButton signUpBackButton = new JButton("Back");
        signUpBackButton.setSize(buttonSize);
        signUpBackButton.setLocation(190, 175);

        signUpPanel.add(signUpNameLabel);
        signUpPanel.add(signUpNameField);
        signUpPanel.add(signUpEmailLabel);
        signUpPanel.add(signUpEmailField);
        signUpPanel.add(signUpPasswordLabel);
        signUpPanel.add(signUpPasswordField);
        signUpPanel.add(signUpSubmitButton);
        signUpPanel.add(signUpBackButton);

        signUpSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = signUpNameField.getText();
                String email = completeEmail(signUpEmailField.getText());
                String password = new String(signUpPasswordField.getPassword());

                try {
                    UserService.registerUser(name, email, password);
                    JOptionPane.showMessageDialog(mainFrame,"Your new account is created.\nGo ahead and login!");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(mainFrame,"Error: " + ex.getMessage());
                }
            }
        });

        signUpBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.remove(signUpPanel);
                mainFrame.add(publicPanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.remove(publicPanel);
                mainFrame.add(logInPanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.remove(publicPanel);
                mainFrame.add(signUpPanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        quitButton.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        /*

        if ("l".equals(command) || "login".equalsIgnoreCase(command)) {

            try {
                User user = UserService.loginUser(email, password);
                System.out.println("Welcome back, " + user.getName() + "!\n");

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
                            System.out.println("Code: " + EmailService.convertToCode(repliedEmail.getId()));
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
        }
        } else if ("q".equals(command) || "quit".equalsIgnoreCase(command)) {
            try {
                PrintWriter writer = new PrintWriter("E:\\Code\\Milou\\src\\main\\logs\\hibernate.log");
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
            break;
        }*/
        mainFrame.setVisible(true);
    }

    public static void showEmails(String title, List<Email> emails) {
        System.out.println(title + ": (" + emails.size() + ")");
        for (Email email: emails)
            System.out.println("+ " + email.getSender().getEmail() + " - " + email.getSubject() + "(" + EmailService.convertToCode(email.getId()) + ")");
    }

    public static String completeEmail(String email) {
        if (!email.contains("@"))
            email += "@milou.com";
        return email;
    }
}