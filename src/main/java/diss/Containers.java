package diss;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Containers {

    private static final String CREATE_TABLES = "CREATE TABLE IF NOT EXISTS container (name TEXT PRIMARY KEY NOT NULL)";
    private static final String ADD_CONTAINER = "INSERT INTO container (name) VALUES (?) ON CONFLICT DO NOTHING;";
    private static final String FIND_ALL_CONTAINERS = "SELECT name FROM container ORDER BY name DESC";
    private static final String REMOVE_SELECTED = "DELETE FROM container WHERE name = ?";
    private static final String REMOVE_ALL = "DELETE FROM container";

    public Containers() {
        try (var conn = getConnection()) {
            var stmt = conn.createStatement();
            stmt.executeUpdate(CREATE_TABLES);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public List<String> get() {
        try (var conn = getConnection()) {
            var stmt = conn.createStatement();

            var names = new ArrayList<String>();
            var rs = stmt.executeQuery(FIND_ALL_CONTAINERS);
            while (rs.next()) {
                var name = rs.getString("name");
                names.add(name);
            }
            return names;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }

    public void add(String name) {
        try (var conn = getConnection()) {
            var ps = conn.prepareStatement(ADD_CONTAINER);
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public void remove(List<String> names) {
        for (var name : names) {
            try (var conn = getConnection()) {
                var ps = conn.prepareStatement(REMOVE_SELECTED);
                ps.setString(1, name);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    public void removeAll() {
        try (var conn = getConnection()) {
            var stmt = conn.createStatement();
            stmt.executeUpdate(REMOVE_ALL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:diss.db");
    }

}