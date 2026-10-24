package com.scc.dao;

import com.scc.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public User login(String username, String password) {
        String sql = "SELECT id, username, password, roles, status FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if ("PENDING".equalsIgnoreCase(rs.getString("status"))) {
                    System.out.println("⏳ Account PENDING — wait for approval");
                    return null;
                }
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("roles"),
                        rs.getString("status")
                );
            }
            System.out.println("❌ Invalid credentials");
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }

    public boolean registerUser(String username, String password, String role) {
        String sql = "INSERT INTO users(username, password, roles, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role.toUpperCase());
            pstmt.executeUpdate();
            System.out.println("✅ Registered — awaiting approval");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Registration failed: " + e.getMessage());
            return false;
        }
    }

    // ✅ D5: Return ALL users as List<User>
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, roles, status FROM users ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        "", // hide password
                        rs.getString("roles"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return list;
    }

    // ✅ D5: Return PENDING users as List<User>
    public List<User> getPendingUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, roles, status FROM users WHERE status = 'PENDING' ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        "",
                        rs.getString("roles"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return list;
    }

    // ✅ D5: Parameterized update
    public boolean approveUser(int userId) {
        String sql = "UPDATE users SET status = 'APPROVED' WHERE id = ? AND status = 'PENDING'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Approval error: " + e.getMessage());
            return false;
        }
    }

    public boolean addUserWithStatus(String username, String password, String role, String status) {
        String sql = "INSERT INTO users(username, password, roles, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setString(4, status);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean updateUser(int id, String newRole, String newStatus) {
        String sql = "UPDATE users SET roles = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newRole);
            pstmt.setString(2, newStatus);
            pstmt.setInt(3, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}