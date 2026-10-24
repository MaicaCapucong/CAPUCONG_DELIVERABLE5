package com.scc.ui;

import com.scc.dao.UserDao;
import java.util.Scanner;

public class ResidentMenu {
    private final UserDao userDao;
    private final Scanner sc;

    public ResidentMenu(UserDao userDao, Scanner sc) {
        this.userDao = userDao;
        this.sc = sc;
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("       RESIDENT DASHBOARD                ");
            System.out.println("========================================");
            System.out.println("1. Submit Clearance Request");
            System.out.println("2. Check Request Status");
            System.out.println("3. View My Request History");
            System.out.println("4. Update My Profile");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            switch (sc.nextLine().trim()) {
                case "1" -> System.out.println("📝 Submit Request — Module Coming Soon");
                case "2" -> System.out.println("🔍 Check Status — Module Coming Soon");
                case "3" -> System.out.println("📜 Request History — Module Coming Soon");
                case "4" -> System.out.println("✏️ Update Profile — Module Coming Soon");
                case "5" -> { running = false; System.out.println("👋 Logged out"); }
                default -> System.out.println("❌ Invalid choice");
            }
        }
    }
}