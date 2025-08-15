package aut.ap;
import aut.ap.model.*;
import aut.ap.service.EmailService;
import aut.ap.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {
    // Sizes
    static Dimension mainSize = new Dimension(500, 300);
    static Dimension accSize = new Dimension(500, 400);
    static Dimension buttonSize = new Dimension(150, 50);
    static Dimension imageSize = new Dimension(190, 190);

    static HashMap<Integer, ArrayList<JList<String> > > accountUnreadList = new HashMap<>();
    static HashMap<Integer, ArrayList<JList<String> > > accountAllList = new HashMap<>();
    static HashMap<Integer, ArrayList<JList<String> > > accountSentList = new HashMap<>();

    public static void main(String[] args) {
        final int[] accCount = {0};

        // Main Frame
        JFrame mainFrame = new JFrame("MILOU");
        mainFrame.setLayout(null);
        mainFrame.setSize(mainSize);
        mainFrame.setLocationRelativeTo(null);

        // Public Panel
        JPanel publicPanel = new JPanel();
        publicPanel.setLayout(null);
        publicPanel.setSize(mainSize);
        publicPanel.setLocation(0, 0);
        publicPanel.setBackground(new Color(0xFFFFFF));

        ImageIcon milouIcon = new ImageIcon("assets\\milou.jpg");
        JLabel milouLabel = new JLabel(new ImageIcon(milouIcon.getImage().getScaledInstance(imageSize.width, imageSize.height, Image.SCALE_SMOOTH)));
        milouLabel.setBounds(50, 35, imageSize.width, imageSize.height);
        publicPanel.add(milouLabel);

        JButton logInButton = new JButton("Log In");
        logInButton.setLocation(300, 35);
        logInButton.setSize(buttonSize);
        publicPanel.add(logInButton);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setLocation(300, 105);
        signUpButton.setSize(buttonSize);
        publicPanel.add(signUpButton);

        JButton quitButton = new JButton("Quit");
        quitButton.setLocation(300, 175);
        quitButton.setSize(buttonSize);
        publicPanel.add(quitButton);

        mainFrame.add(publicPanel);

        // LogIn Panel
        JPanel logInPanel = new JPanel();
        logInPanel.setLayout(null);
        logInPanel.setSize(mainSize);
        logInPanel.setLocation(0, 0);
        logInPanel.setBackground(new Color(0xFFFFFF));

        JLabel logInEmailLabel = new JLabel("Email:");
        logInEmailLabel.setBounds(100, 45, 80, 25);
        JTextField logInEmailField = new JTextField();
        logInEmailField.setBounds(190, 45, 200, 25);
        logInPanel.add(logInEmailLabel);
        logInPanel.add(logInEmailField);

        JLabel logInPasswordLabel = new JLabel("Password:");
        logInPasswordLabel.setBounds(100, 75, 80, 25);
        JPasswordField logInPasswordField = new JPasswordField();
        logInPasswordField.setBounds(190, 75, 200, 25);
        logInPanel.add(logInPasswordLabel);
        logInPanel.add(logInPasswordField);

        JButton logInSubmitButton = new JButton("Submit");
        logInSubmitButton.setSize(buttonSize);
        logInSubmitButton.setLocation(190, 125);
        logInPanel.add(logInSubmitButton);

        JButton logInBackButton = new JButton("Back");
        logInBackButton.setSize(buttonSize);
        logInBackButton.setLocation(190, 175);
        logInPanel.add(logInBackButton);

        logInSubmitButton.addActionListener(e -> {
            String email = completeEmail(logInEmailField.getText());
            String password = new String(logInPasswordField.getPassword());

            try {
                User user = UserService.loginUser(email, password);
                JOptionPane.showMessageDialog(mainFrame,"Welcome back, " + user.getName() + "!\n");
                if (!accountUnreadList.containsKey(user.getId()))
                    accountUnreadList.put(user.getId(), new ArrayList<>());
                if (!accountAllList.containsKey(user.getId()))
                    accountAllList.put(user.getId(), new ArrayList<>());
                if (!accountSentList.containsKey(user.getId()))
                    accountSentList.put(user.getId(), new ArrayList<>());

                // Acc Frame
                JFrame newFrame = new JFrame(user.getName());
                newFrame.setSize(accSize);
                int accLocType = (accCount[0] + 1) % 4;
                newFrame.setLocation(((accLocType + 1) % 2) * 1040, ((accLocType / 2) % 2) * 400);

                // Acc Main Panel
                JPanel accMainPanel = new JPanel(null);
                accMainPanel.setBounds(0, 0, accSize.width, accSize.height);
                accMainPanel.setBackground(new Color(0xFFFFFF));

                showEmails(accMainPanel, "Unread Emails", EmailService.showUnreadEmails(user), 0, 0, accSize.width - (buttonSize.width + 20), accSize.height, user.getId());

                JButton sendButton = new JButton("Send");
                sendButton.setBounds(332, 10, buttonSize.width, buttonSize.height);
                accMainPanel.add(sendButton);

                JButton viewButton = new JButton("View");
                viewButton.setBounds(332, 70, buttonSize.width, buttonSize.height);
                accMainPanel.add(viewButton);

                JButton replyButton = new JButton("Reply");
                replyButton.setBounds(332, 130, buttonSize.width, buttonSize.height);
                accMainPanel.add(replyButton);

                JButton forwardButton = new JButton("Forward");
                forwardButton.setBounds(332, 190, buttonSize.width, buttonSize.height);
                accMainPanel.add(forwardButton);

                JButton deleteButton = new JButton("Delete");
                deleteButton.setBounds(332, 250, buttonSize.width, buttonSize.height);
                accMainPanel.add(deleteButton);

                JButton accQuitButton = new JButton("Log out");
                accQuitButton.setBounds(332, 310, buttonSize.width, buttonSize.height);
                accMainPanel.add(accQuitButton);

                // Send Panel
                JPanel sendPanel = new JPanel();
                sendPanel.setLayout(null);
                sendPanel.setSize(mainSize);
                sendPanel.setLocation(0, 0);
                sendPanel.setBackground(new Color(0xFFFFFF));

                JLabel sendRecipients = new JLabel("Recipient(s):");
                sendRecipients.setBounds(100, 45, 80, 25);
                JTextField sendRecipientsField = new JTextField();
                sendRecipientsField.setBounds(190, 45, 200, 25);
                sendPanel.add(sendRecipients);
                sendPanel.add(sendRecipientsField);

                JLabel sendSubject = new JLabel("Subject:");
                sendSubject.setBounds(100, 70, 80, 25);
                JTextField sendSubjectField = new JTextField();
                sendSubjectField.setBounds(190, 70, 200, 25);
                sendPanel.add(sendSubject);
                sendPanel.add(sendSubjectField);

                JLabel sendBody = new JLabel("Body:");
                sendBody.setBounds(100, 95, 80, 25);
                JTextField sendBodyField = new JTextField();
                sendBodyField.setBounds(190, 95, 200, 25);
                sendPanel.add(sendBody);
                sendPanel.add(sendBodyField);

                JButton sendSendButton = new JButton("Send");
                sendSendButton.setSize(buttonSize);
                sendSendButton.setLocation(190, 125);
                sendPanel.add(sendSendButton);

                JButton sendBackButton = new JButton("Back");
                sendBackButton.setSize(buttonSize);
                sendBackButton.setLocation(190, 175);
                sendPanel.add(sendBackButton);

                sendSendButton.addActionListener(e1 -> {
                    String subject = sendSubjectField.getText();
                    String body = sendBodyField.getText();
                    String recipients = sendRecipientsField.getText();
                    String[] recipientsList = recipients.split(", ");

                    ArrayList<String> wrongEmails = new ArrayList<>();
                    ArrayList<User> existEmails = new ArrayList<>();

                    for (String recipient: recipientsList)
                        if (UserService.findByEmail(completeEmail(recipient)) == null)
                            wrongEmails.add(recipient);
                        else
                            existEmails.add(UserService.findByEmail(completeEmail(recipient)));

                    try {
                        Email sentEmail = EmailService.sendEmail(user, subject, body, existEmails);
                        refreshUsers(existEmails);
                        refreshUsers(Arrays.asList(user));

                        String statusMessage = "Successfully sent your email.";
                        if (!wrongEmails.isEmpty()) {
                            statusMessage += "\nBUT NOT TO " ;
                            for (String wrongEmail: wrongEmails)
                                statusMessage += wrongEmail;
                            statusMessage += "; THEY DOES NOT EXIST";
                        }
                        statusMessage += "\nCode: " + EmailService.convertToCode(sentEmail.getId());

                        JOptionPane.showMessageDialog(newFrame, statusMessage);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(newFrame,"Error: " + ex.getMessage());
                    }
                });
                sendBackButton.addActionListener(e1 -> {
                    newFrame.remove(sendPanel);
                    newFrame.add(accMainPanel);
                    newFrame.repaint();
                    newFrame.revalidate();
                });

                sendButton.addActionListener(e1 -> {
                    newFrame.remove(accMainPanel);
                    newFrame.add(sendPanel);
                    newFrame.repaint();
                    newFrame.revalidate();
                });

                // View Panel
                JPanel viewPanel = new JPanel(null);
                viewPanel.setSize(accSize);
                viewPanel.setBackground(new Color(0xFFFFFF));

                ImageIcon icon = new ImageIcon("assets\\opened mail.jpg");
                JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(imageSize.width, imageSize.height, Image.SCALE_SMOOTH)));
                imageLabel.setBounds(50, 85, imageSize.width, imageSize.height);
                viewPanel.add(imageLabel);

                int startX = 280;
                int startY = 20;
                int gap = 20;

                JButton allEmailsButton = new JButton("All Emails");
                allEmailsButton.setLocation(startX, startY);
                allEmailsButton.setSize(buttonSize);
                viewPanel.add(allEmailsButton);

                JButton unreadEmailsButton = new JButton("Unread Emails");
                unreadEmailsButton.setLocation(startX, startY + (buttonSize.height + gap));
                unreadEmailsButton.setSize(buttonSize);
                viewPanel.add(unreadEmailsButton);

                JButton sentEmailsButton = new JButton("Sent Emails");
                sentEmailsButton.setLocation(startX, startY + 2 * (buttonSize.height + gap));
                sentEmailsButton.setSize(buttonSize);
                viewPanel.add(sentEmailsButton);

                JButton readByCodeButton = new JButton("Read by Code");
                readByCodeButton.setLocation(startX, startY + 3 * (buttonSize.height + gap));
                readByCodeButton.setSize(buttonSize);
                viewPanel.add(readByCodeButton);

                JButton viewBackButton = new JButton("Back");
                viewBackButton.setLocation(startX, startY + 4 * (buttonSize.height + gap));
                viewBackButton.setSize(buttonSize);
                viewPanel.add(viewBackButton);

                allEmailsButton.addActionListener(e2 -> {
                    try {
                        view(newFrame, viewPanel, "All Emails", EmailService.showAllEmails(user), user.getId());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(newFrame,"Error: " + ex.getMessage());
                    }
                });
                unreadEmailsButton.addActionListener(e2 -> {
                    try {
                        view(newFrame, viewPanel, "Unread Emails", EmailService.showUnreadEmails(user), user.getId());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(newFrame,"Error: " + ex.getMessage());
                    }
                });
                sentEmailsButton.addActionListener(e2 -> {
                    try {
                        view(newFrame, viewPanel, "Sent Emails", EmailService.showSentEmails(user), user.getId());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(newFrame,"Error: " + ex.getMessage());
                    }
                });
                readByCodeButton.addActionListener(e2 -> {
                    String code = JOptionPane.showInputDialog(newFrame, "Enter email code:");

                    if (code != null) {
                        try {
                            Email foundEmail = EmailService.findByCode(code);
                            EmailService.readEmail(user, code);
                            refreshUsers(Arrays.asList(user));

                            JPanel readPanel = new JPanel(null);
                            readPanel.setBounds(0, 0, accSize.width, accSize.height);

                            JTextArea messageArea = new JTextArea(foundEmail.toString());
                            messageArea.setLineWrap(true);
                            messageArea.setWrapStyleWord(true);
                            messageArea.setEditable(false);
                            JScrollPane messageScrollPane = new JScrollPane(messageArea);
                            messageScrollPane.setBounds(0, 0, accSize.width, accSize.height - (buttonSize.height + 80));
                            readPanel.add(messageScrollPane);

                            JButton messageBackButton = new JButton("Back");
                            messageBackButton.setBounds((accSize.width - buttonSize.width) / 2, 300, buttonSize.width, buttonSize.height);
                            readPanel.add(messageBackButton);

                            messageBackButton.addActionListener(e3 -> {
                                newFrame.remove(readPanel);
                                newFrame.add(viewPanel);
                                newFrame.revalidate();
                                newFrame.repaint();
                            });

                            newFrame.remove(viewPanel);
                            newFrame.add(readPanel);
                            newFrame.revalidate();
                            newFrame.repaint();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(newFrame,"Error: " + ex.getMessage());
                        }
                    }
                });
                viewBackButton.addActionListener(e2 -> {
                    newFrame.remove(viewPanel);
                    newFrame.add(accMainPanel);
                    newFrame.revalidate();
                    newFrame.repaint();
                });

                viewButton.addActionListener(e1 -> {
                    newFrame.remove(accMainPanel);
                    newFrame.add(viewPanel);
                    newFrame.revalidate();
                    newFrame.repaint();
                });

                // Reply Panel
                JPanel replyPanel = new JPanel();
                replyPanel.setLayout(null);
                replyPanel.setSize(mainSize);
                replyPanel.setLocation(0, 0);
                replyPanel.setBackground(new Color(0xFFFFFF));

                JLabel replyCode = new JLabel("Code:");
                replyCode.setBounds(100, 45, 80, 25);
                JTextField replyCodeField = new JTextField();
                replyCodeField.setBounds(190, 45, 200, 25);
                replyPanel.add(replyCode);
                replyPanel.add(replyCodeField);

                JLabel replyBody = new JLabel("Body:");
                replyBody.setBounds(100, 75, 80, 25);
                JTextField replyBodyField = new JTextField();
                replyBodyField.setBounds(190, 75, 200, 25);
                replyPanel.add(replyBody);
                replyPanel.add(replyBodyField);

                JButton sendReplyButton = new JButton("Reply");
                sendReplyButton.setSize(buttonSize);
                sendReplyButton.setLocation(190, 125);
                replyPanel.add(sendReplyButton);

                JButton replyBackButton = new JButton("Back");
                replyBackButton.setSize(buttonSize);
                replyBackButton.setLocation(190, 175);
                replyPanel.add(replyBackButton);

                sendReplyButton.addActionListener(e1 -> {
                    try {
                        String code = replyCodeField.getText();
                        String body = replyBodyField.getText();
                        Email repliedEmail = EmailService.replyEmail(user, code, body);
                        refreshUsers(EmailService.findRecipientsOfEmail(EmailService.convertToCode(repliedEmail.getId())));
                        refreshUsers(Arrays.asList(user));

                        JOptionPane.showMessageDialog(newFrame, "Successfully sent your reply to email" + code + "\nCode: " + EmailService.convertToCode(repliedEmail.getId()));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(newFrame, "Error: " + ex.getMessage());
                    }
                });
                replyBackButton.addActionListener(e1 -> {
                    newFrame.remove(replyPanel);
                    newFrame.add(accMainPanel);
                    newFrame.revalidate();
                    newFrame.repaint();
                });

                replyButton.addActionListener(e1 -> {
                    newFrame.remove(accMainPanel);
                    newFrame.add(replyPanel);

                    newFrame.revalidate();
                    newFrame.repaint();
                });

                // Forward Panel
                JPanel forwardPanel = new JPanel();
                forwardPanel.setLayout(null);
                forwardPanel.setSize(mainSize);
                forwardPanel.setLocation(0, 0);
                forwardPanel.setBackground(new Color(0xFFFFFF));

                JLabel forwardCode = new JLabel("Code:");
                forwardCode.setBounds(100, 45, 80, 25);
                JTextField forwardCodeField = new JTextField();
                forwardCodeField.setBounds(190, 45, 200, 25);
                forwardPanel.add(forwardCode);
                forwardPanel.add(forwardCodeField);

                JLabel forwardRecipient = new JLabel("Recipient(s):");
                forwardRecipient.setBounds(100, 75, 80, 25);
                JTextField forwardRecipientField  = new JTextField();
                forwardRecipientField.setBounds(190, 75, 200, 25);
                forwardPanel.add(forwardRecipient);
                forwardPanel.add(forwardRecipientField);

                JButton sendForwardButton = new JButton("Forward");
                sendForwardButton.setSize(buttonSize);
                sendForwardButton.setLocation(190, 125);
                forwardPanel.add(sendForwardButton);

                JButton forwardBackButton = new JButton("Back");
                forwardBackButton.setSize(buttonSize);
                forwardBackButton.setLocation(190, 175);
                forwardPanel.add(forwardBackButton);

                sendForwardButton.addActionListener(e2 -> {
                    String code = forwardCodeField.getText();
                    String recipients = forwardRecipientField.getText();
                    String[] recipientsList = recipients.split(", ");

                    ArrayList<String> wrongEmails = new ArrayList<>();
                    ArrayList<User> existEmails = new ArrayList<>();

                    for (String recipient: recipientsList)
                        if (UserService.findByEmail(completeEmail(recipient)) == null)
                            wrongEmails.add(recipient);
                        else
                            existEmails.add(UserService.findByEmail(completeEmail(recipient)));

                    try {
                        Email forwardedEmail = EmailService.forwardEmail(user, code, existEmails);
                        refreshUsers(existEmails);
                        refreshUsers(Arrays.asList(user));

                        String statusMessage = "Successfully forwarded your email.";
                        if (!wrongEmails.isEmpty()) {
                            statusMessage += "\nBUT NOT TO " ;
                            for (String wrongEmail: wrongEmails)
                                statusMessage += wrongEmail;
                            statusMessage += "; THEY DOES NOT EXIST";
                        }
                        statusMessage += "\nCode: " + EmailService.convertToCode(forwardedEmail.getId());

                        JOptionPane.showMessageDialog(newFrame, statusMessage);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(newFrame,"Error: " + ex.getMessage());
                    }
                });
                forwardBackButton.addActionListener(e2 -> {
                    newFrame.remove(forwardPanel);
                    newFrame.add(accMainPanel);
                    newFrame.revalidate();
                    newFrame.repaint();
                });

                forwardButton.addActionListener(e1 -> {
                    newFrame.remove(accMainPanel);
                    newFrame.add(forwardPanel);

                    newFrame.revalidate();
                    newFrame.repaint();
                });

                // Delete Button
                deleteButton.addActionListener(e1 -> {
                    while (true) {
                        String code = JOptionPane.showInputDialog(newFrame, "Enter email code:");

                        if (code != null) {
                            try {
                                Email deletedEmail = EmailService.findByCode(code);
                                List<User> recipients = EmailService.findRecipientsOfEmail(code);
                                EmailService.deleteEmail(user, code);
                                refreshUsers(recipients);
                                refreshUsers(Arrays.asList(user));

                                JOptionPane.showMessageDialog(newFrame, "Successfully deleted email\nCode: " + EmailService.convertToCode(deletedEmail.getId()));
                                break;
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(newFrame, "Error: " + ex.getMessage());
                            }
                        }
                        else
                            break;
                    }
                });

                // LogOut Button
                accQuitButton.addActionListener(e1 -> newFrame.dispose());

                newFrame.add(accMainPanel);

                newFrame.setVisible(true);

                accCount[0]++;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame,"Error: " + ex.getMessage());
            }
        });
        logInBackButton.addActionListener(e -> {
            mainFrame.remove(logInPanel);
            mainFrame.add(publicPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        logInButton.addActionListener(e -> {
            mainFrame.remove(publicPanel);
            mainFrame.add(logInPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // SignUp Panel
        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(null);
        signUpPanel.setSize(mainSize);
        signUpPanel.setLocation(0, 0);
        signUpPanel.setBackground(new Color(0xFFFFFF));

        JLabel signUpNameLabel = new JLabel("Name:");
        signUpNameLabel.setBounds(100, 15, 80, 25);
        JTextField signUpNameField = new JTextField();
        signUpNameField.setBounds(190, 15, 200, 25);
        signUpPanel.add(signUpNameLabel);
        signUpPanel.add(signUpNameField);

        JLabel signUpEmailLabel = new JLabel("Email:");
        signUpEmailLabel.setBounds(100, 45, 80, 25);
        JTextField signUpEmailField = new JTextField();
        signUpEmailField.setBounds(190, 45, 200, 25);
        signUpPanel.add(signUpEmailLabel);
        signUpPanel.add(signUpEmailField);

        JLabel signUpPasswordLabel = new JLabel("Password:");
        signUpPasswordLabel.setBounds(100, 75, 80, 25);
        JPasswordField signUpPasswordField = new JPasswordField();
        signUpPasswordField.setBounds(190, 75, 200, 25);
        signUpPanel.add(signUpPasswordLabel);
        signUpPanel.add(signUpPasswordField);

        JButton signUpSubmitButton = new JButton("Submit");
        signUpSubmitButton.setSize(buttonSize);
        signUpSubmitButton.setLocation(190, 125);
        signUpPanel.add(signUpSubmitButton);

        JButton signUpBackButton = new JButton("Back");
        signUpBackButton.setSize(buttonSize);
        signUpBackButton.setLocation(190, 175);
        signUpPanel.add(signUpBackButton);

        signUpSubmitButton.addActionListener(e -> {
            String name = signUpNameField.getText();
            String email = completeEmail(signUpEmailField.getText());
            String password = new String(signUpPasswordField.getPassword());

            try {
                UserService.registerUser(name, email, password);
                JOptionPane.showMessageDialog(mainFrame,"Your new account is created.\nGo ahead and login!");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainFrame,"Error: " + ex.getMessage());
            }
        });
        signUpBackButton.addActionListener(e -> {
            mainFrame.remove(signUpPanel);
            mainFrame.add(publicPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        signUpButton.addActionListener(e -> {
            mainFrame.remove(publicPanel);
            mainFrame.add(signUpPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // Quit Button
        quitButton.addActionListener(e -> {
            try {
                PrintWriter writer = new PrintWriter("src\\main\\logs\\hibernate.log");
                writer.print("");
                writer.close();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(mainFrame,"Error: " + ex.getMessage());
            }
            System.exit(0);
        });

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public static DefaultListModel<String> defaultListModelMaker(String title, List<Email> emails) {
        DefaultListModel<String> emailListModel = new DefaultListModel<>();
        emailListModel.addElement(title + ": (" + emails.size() + ")");
        for (Email email : emails) {
            emailListModel.addElement("From: " + email.getSender().getEmail() + " | Subject: " + email.getSubject() + "(" + EmailService.convertToCode(email.getId()) + ")");
        }
        return emailListModel;
    }

    public static void showEmails(JPanel panel, String title, List<Email> emails, int x, int y, int width, int height, int userId) {
        DefaultListModel<String> emailListModel = defaultListModelMaker(title, emails);

        JList<String> emailList = new JList<>(emailListModel);
        if (title.equalsIgnoreCase("Unread Emails"))
            accountUnreadList.get(userId).add(emailList);
        if (title.equalsIgnoreCase("All Emails"))
            accountAllList.get(userId).add(emailList);
        if (title.equalsIgnoreCase("Sent Emails"))
            accountSentList.get(userId).add(emailList);
        JScrollPane scrollPane = new JScrollPane(emailList);
        scrollPane.setBounds(x, y, width, height);
        panel.add(scrollPane);
    }

    public static void view(JFrame frame, JPanel viewPanel, String title, List<Email> emails, int userId) {
        JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, accSize.width, accSize.height);

        showEmails(panel, title, emails, 0, 0, accSize.width, accSize.height - (buttonSize.width + 20), userId);

        JButton unreadEmailBackButton = new JButton("Back");
        unreadEmailBackButton.setBounds((accSize.width - buttonSize.width) / 2, 300, buttonSize.width, buttonSize.height);
        panel.add(unreadEmailBackButton);

        unreadEmailBackButton.addActionListener(e3 -> {
            frame.remove(panel);
            frame.add(viewPanel);
            frame.revalidate();
            frame.repaint();
        });

        frame.remove(viewPanel);
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    public static String completeEmail(String email) {
        if (!email.endsWith("@milou.com"))
            email += "@milou.com";
        return email;
    }

    public static void refreshUnread(List<User> users) {
        for (User user: users)
            if (accountUnreadList.containsKey(user.getId()))
                for (JList<String> list: accountUnreadList.get(user.getId()))
                    list.setModel(defaultListModelMaker("Unread Emails", EmailService.showUnreadEmails(user)));
    }

    public static void refreshAll(List<User> users) {
        for (User user: users)
            if (accountAllList.containsKey(user.getId()))
                for (JList<String> list: accountAllList.get(user.getId()))
                    list.setModel(defaultListModelMaker("All Emails", EmailService.showAllEmails(user)));
    }

    public static void refreshSent(List<User> users) {
        for (User user: users)
            if (accountSentList.containsKey(user.getId()))
                for (JList<String> list: accountSentList.get(user.getId()))
                    list.setModel(defaultListModelMaker("Sent Emails", EmailService.showSentEmails(user)));
    }

    public static void refreshUsers(List<User> users) {
        refreshUnread(users);
        refreshAll(users);
        refreshSent(users);
    }
}