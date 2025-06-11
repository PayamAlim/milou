package aut.ap;
import aut.ap.model.*;
import aut.ap.service.UserService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);

        System.out.println("[L]ogin, [S]ign up:");
        while (true) {
            String command = scn.nextLine().trim().toLowerCase();

            if ("l".equals(command) || "login".equalsIgnoreCase(command)) {
                System.out.print("Email: ");
                String email = scn.nextLine().trim();

                System.out.print("Password: ");
                String password = scn.nextLine().trim();

                try {
                    User user = UserService.loginUser(email, password);
                    System.out.println("Welcome back, " + user.getName() + "!");
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
                System.out.println("Invalid command. Please try again.");
            }
            System.out.println("[L]ogin, [S]ign up, [Q]uit:");
        }
    }
}