package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao;
    private final Scanner sc;

    public AdminMenu(UserDao userDao, Scanner sc) {
        this.userDao = userDao;
        this.sc = sc;
    }

    private void printUserTable(List<User> users) {
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-5s | %-15s | %-18s | %-10s%n", "ID", "USERNAME", "ROLE", "STATUS");
        System.out.println("-------------------------------------------------------");
        if (users.isEmpty()) {
            System.out.println("     No records found");
        } else {
            for (User u : users) {
                System.out.printf("%-5d | %-15s | %-18s | %-10s%n",
                        u.id(), u.username(), u.roles(), u.status());
            }
        }
        System.out.println("-------------------------------------------------------");
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("         ADMINISTRATIVE PANEL           ");
            System.out.println("========================================");
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Add New User");
            System.out.println("5. Update User");
            System.out.println("6. Delete User");
            System.out.println("7. Logout");
            System.out.print("Enter choice: ");

            switch (sc.nextLine().trim()) {
                case "1" -> viewAllUsers();
                case "2" -> viewPending();
                case "3" -> approveOne();
                case "4" -> addNewUser();
                case "5" -> updateUser();
                case "6" -> deleteUser();
                case "7" -> { running = false; System.out.println("👋 Logged out"); }
                default -> System.out.println("❌ Invalid choice");
            }
        }
    }

    private void viewAllUsers() {
        System.out.println("\n--- All Registered Accounts ---");
        printUserTable(userDao.getAllUsers());
    }

    private void viewPending() {
        System.out.println("\n--- Pending Approvals ---");
        printUserTable(userDao.getPendingUsers());
    }

    private void approveOne() {
        List<User> pending = userDao.getPendingUsers();
        if (pending.isEmpty()) {
            System.out.println("✅ No pending accounts");
            return;
        }
        printUserTable(pending);

        System.out.print("Enter User ID to approve (0 = Cancel): ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            if (id == 0) {
                System.out.println("⚠️ Approval cancelled");
                return;
            }
            boolean exists = pending.stream().anyMatch(u -> u.id() == id);
            if (!exists) {
                System.out.println("❌ ID not in pending list — cancelled");
                return;
            }
            if (userDao.approveUser(id)) {
                System.out.println("✅ User approved successfully!");
            } else {
                System.out.println("❌ Approval failed");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input — cancelled");
        }
    }

    private void addNewUser() {
        System.out.print("Username: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();

        System.out.println("Role: 1=SECRETARY | 2=BARANGAY OFFICIAL | 3=RESIDENT");
        System.out.print("Choice: ");
        String role;
        try {
            int r = Integer.parseInt(sc.nextLine().trim());
            role = switch (r) {
                case 1 -> "SECRETARY";
                case 2 -> "BARANGAY OFFICIAL";
                case 3 -> "RESIDENT";
                default -> null;
            };
            if (role == null) { System.out.println("❌ Invalid role"); return; }
        } catch (NumberFormatException e) { System.out.println("❌ Enter number"); return; }

        System.out.println("Status: 1=PENDING | 2=APPROVED");
        System.out.print("Choice: ");
        String status;
        try {
            int s = Integer.parseInt(sc.nextLine().trim());
            status = switch (s) {
                case 1 -> "PENDING";
                case 2 -> "APPROVED";
                default -> null;
            };
            if (status == null) { System.out.println("❌ Invalid status"); return; }
        } catch (NumberFormatException e) { System.out.println("❌ Enter number"); return; }

        System.out.println(userDao.addUserWithStatus(u, p, role, status)
                ? "✅ User added — Status: " + status : "❌ Failed");
    }

    private void updateUser() {
        viewAllUsers();
        System.out.print("User ID to update: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            System.out.print("New Role: ");
            String role = sc.nextLine().trim().toUpperCase();
            System.out.print("New Status: ");
            String status = sc.nextLine().trim().toUpperCase();
            System.out.println(userDao.updateUser(id, role, status) ? "✅ Updated" : "❌ Not found");
        } catch (NumberFormatException e) { System.out.println("❌ Invalid ID"); }
    }

    private void deleteUser() {
        viewAllUsers();
        System.out.print("User ID to DELETE: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Confirm (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println(userDao.deleteUser(id) ? "✅ Deleted" : "❌ Failed");
            }
        } catch (NumberFormatException e) { System.out.println("❌ Invalid ID"); }
    }
}