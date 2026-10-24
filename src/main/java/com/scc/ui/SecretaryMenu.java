package com.scc.ui;

import com.scc.dao.UserDao;
import java.util.Scanner;

public class SecretaryMenu {
    private final UserDao userDao;
    private final Scanner sc;

    public SecretaryMenu(UserDao userDao, Scanner sc) {
        this.userDao = userDao;
        this.sc = sc;
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("    BARANGAY SECRETARY DASHBOARD        ");
            System.out.println("========================================");
            System.out.println("1. Manage Users & Approvals");
            System.out.println("2. View All Clearance Requests");
            System.out.println("3. Process Clearance Application");
            System.out.println("4. Generate Reports");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            switch (sc.nextLine().trim()) {
                case "1" -> new AdminMenu(userDao, sc).display();
                case "2" -> System.out.println("📋 Clearance Requests — Module Coming Soon");
                case "3" -> System.out.println("✅ Process Application — Module Coming Soon");
                case "4" -> System.out.println("📊 Reports — Module Coming Soon");
                case "5" -> { running = false; System.out.println("👋 Logged out"); }
                default -> System.out.println("❌ Invalid choice");
            }
        }
    }
}