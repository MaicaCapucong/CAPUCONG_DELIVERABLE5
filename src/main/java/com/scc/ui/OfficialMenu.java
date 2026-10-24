package com.scc.ui;

import com.scc.dao.UserDao;
import java.util.Scanner;

public class OfficialMenu {
    private final UserDao userDao;
    private final Scanner sc;

    public OfficialMenu(UserDao userDao, Scanner sc) {
        this.userDao = userDao;
        this.sc = sc;
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("   BARANGAY OFFICIAL DASHBOARD          ");
            System.out.println("========================================");
            System.out.println("1. View Pending Clearance Requests");
            System.out.println("2. Review Approved Requests");
            System.out.println("3. View Resident Records");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");

            switch (sc.nextLine().trim()) {
                case "1" -> System.out.println("⏳ Pending Requests — Module Coming Soon");
                case "2" -> System.out.println("✅ Approved Requests — Module Coming Soon");
                case "3" -> System.out.println("👥 Resident Records — Module Coming Soon");
                case "4" -> { running = false; System.out.println("👋 Logged out"); }
                default -> System.out.println("❌ Invalid choice");
            }
        }
    }
}