package org.ua.cafe.repo;

import org.ua.cafe.db.DbConnection;
import org.ua.cafe.model.Client;
import org.ua.cafe.model.Dessert;
import org.ua.cafe.model.Drink;
import org.ua.cafe.model.Staff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CafeRepository implements CafeRepositoryInterface {

    @Override
    public List<Drink> findAllDrinks() {
        List<Drink> drinks = new ArrayList<>();
        String sql = "SELECT \"напій_id\", \"назва_укр\", \"ціна\" FROM \"Напої\"";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                drinks.add(new Drink(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return drinks;
    }

    @Override
    public List<String> findTopNPopularDrinksLastMonth(int limit) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT n.\"назва_укр\", COUNT(dz.\"напій_id\") AS total_count " +
                "FROM \"Деталі_замовлення\" dz " +
                "JOIN \"Напої\" n ON dz.\"напій_id\" = n.\"напій_id\" " +
                "JOIN \"Замовлення\" z ON dz.\"замовлення_id\" = z.\"замовлення_id\" " +
                "WHERE z.\"дата_час_замовлення\" >= CURRENT_DATE - INTERVAL '1 month' " +
                "GROUP BY n.\"назва_укр\" " +
                "ORDER BY total_count DESC LIMIT ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("назва_укр") + " (Замовлено: " + rs.getInt("total_count") + " раз)");
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    @Override
    public List<Dessert> findAllDesserts() {
        List<Dessert> desserts = new ArrayList<>();
        String sql = "SELECT \"десерт_id\", \"назва_укр\", \"ціна\" FROM \"Десерти\"";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                desserts.add(new Dessert(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return desserts;
    }

    @Override
    public List<String> findTopNPopularDessertsLastDays(int limit, int days) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT d.\"назва_укр\", SUM(dz.\"кількість\") AS total_sold " +
                "FROM \"Деталі_замовлення\" dz " +
                "JOIN \"Десерти\" d ON dz.\"десерт_id\" = d.\"десерт_id\" " +
                "JOIN \"Замовлення\" z ON dz.\"замовлення_id\" = z.\"замовлення_id\" " +
                "WHERE z.\"дата_час_замовлення\" >= CURRENT_DATE - INTERVAL '" + days + " days' " +
                "GROUP BY d.\"назва_укр\" " +
                "ORDER BY total_sold DESC LIMIT ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("назва_укр") + " (Продано: " + rs.getInt("total_sold") + " шт.)");
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    @Override
    public List<Staff> findAllBaristas() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT p.\"персонал_id\", p.\"піб\", pos.\"назва_позиції\", p.\"телефон\" " +
                "FROM \"Персонал\" p JOIN \"Позиції\" pos ON p.\"позиція_id\" = pos.\"позиція_id\" " +
                "WHERE pos.\"назва_позиції\" = 'Бариста'";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                staffList.add(new Staff(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return staffList;
    }

    @Override
    public List<Staff> findAllWaiters() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT p.\"персонал_id\", p.\"піб\", pos.\"назва_позиції\", p.\"телефон\" " +
                "FROM \"Персонал\" p JOIN \"Позиції\" pos ON p.\"позиція_id\" = pos.\"позиція_id\" " +
                "WHERE pos.\"назва_позиції\" = 'Офіціант'";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                staffList.add(new Staff(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return staffList;
    }

    @Override
    public double getAverageOrderSumByDate(LocalDate date) {
        String sql = "SELECT AVG(\"загальна_сума\") FROM \"Замовлення\" WHERE DATE(\"дата_час_замовлення\") = ?";
        double avgSum = 0.0;
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    avgSum = rs.getDouble(1);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return avgSum;
    }

    @Override
    public List<Double> findLargestOrderSumByDate(LocalDate date) {
        List<Double> sums = new ArrayList<>();
        String sql = "SELECT \"загальна_сума\" " +
                "FROM \"Замовлення\" " +
                "WHERE DATE(\"дата_час_замовлення\") = ? " +
                "AND \"загальна_сума\" = ( " +
                "SELECT MAX(\"загальна_сума\") " +
                "FROM \"Замовлення\" " +
                "WHERE DATE(\"дата_час_замовлення\") = ? " +
                ")";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(date));
            stmt.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sums.add(rs.getDouble(1));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return sums;
    }

    @Override
    public List<Client> findRegularClients(int minVisits, int days) {
        List<Client> clients = new ArrayList<>();
        // ВИПРАВЛЕНО: Повернено до 3-х полів: клієнт_id, піб, телефон,
        // оскільки ваш клас Client не має конструктора на 4 аргументи.
        String sql = "SELECT k.\"клієнт_id\", k.\"піб\", k.\"телефон\" " +
                "FROM \"Клієнти\" k JOIN \"Замовлення\" z ON k.\"клієнт_id\" = z.\"клієнт_id\" " +
                "WHERE z.\"дата_час_замовлення\" >= CURRENT_DATE - INTERVAL '" + days + " days' " +
                "GROUP BY k.\"клієнт_id\", k.\"піб\", k.\"телефон\" " +
                "HAVING COUNT(z.\"замовлення_id\") >= ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, minVisits);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Виклик конструктора з 3 аргументами
                    clients.add(new Client(rs.getInt(1), rs.getString(2), rs.getString(3)));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return clients;
    }

    @Override
    public boolean createNewOrder(int clientId, int staffId, double totalSum) {
        String sql = "INSERT INTO \"Замовлення\" (\"клієнт_id\", \"staff_id\", \"дата_час_замовлення\", \"загальна_сума\") " +
                "VALUES (?, ?, NOW(), ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, staffId);
            stmt.setDouble(3, totalSum);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}