package main.java.hepker.ai.table;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

class QValueRepository {
    private final String url;
    private HikariDataSource dataSource;

    public QValueRepository(String url) {
        this.url = url;
        setupDataSource();
    }

    private void setupDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(600000);
        config.setConnectionTimeout(30000);

        dataSource = new HikariDataSource(config);
    }

    public void createTable() {
        final String sqlCreateTable = "CREATE TABLE IF NOT EXISTS QTable ("
                + "HexKey TEXT NOT NULL, "
                + "Action INTEGER NOT NULL, "
                + "QValue REAL NOT NULL, "
                + "PRIMARY KEY (HexKey, Action) "
                + ") WITHOUT ROWID;";

        final String sqlCreateIndex = "CREATE INDEX IF NOT EXISTS idx_qvalue ON QTable (HexKey, QValue DESC);";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlCreateTable)) {
            stmt.execute();
            try (PreparedStatement stmtIndex = conn.prepareStatement(sqlCreateIndex)) {
                stmtIndex.execute();
            }
        } catch (SQLException e) {
            System.out.println("Error creating table or index: " + e.getMessage());
        }
    }

    public int getMaxQAction(String serialKey) {
        final String sql = "SELECT Action FROM QTable WHERE HexKey = ? ORDER BY QValue DESC LIMIT 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, serialKey);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Action");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching max Q-action: " + e.getMessage());
        }
        return 0;
    }

    public double getQValueFromTable(String serialKey, int moveChoice) {
        final String sql = "SELECT QValue FROM QTable WHERE HexKey = ? AND Action = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, serialKey);
            pstmt.setInt(2, moveChoice);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("QValue");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Q-value: " + e.getMessage());
        }
        return 0.0;
    }

    public double getMaxQValue(String serialKey) {
        final String sql = "SELECT MAX(QValue) AS maxQValue FROM QTable WHERE HexKey = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, serialKey);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("maxQValue");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching max Q-value: " + e.getMessage());
        }
        return 0.0;
    }

    public void updateQTable(Map<String, double[]> qTable) {
        final String sql = "INSERT OR REPLACE INTO QTable (HexKey, Action, QValue) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ppdStmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            for (Map.Entry<String, double[]> entry : qTable.entrySet()) {
                String key = entry.getKey();
                double[] qValues = entry.getValue();
                for (int i = 0; i < qValues.length; i++) {
                    if (Double.isNaN(qValues[i])) {
                        continue;
                    }
                    ppdStmt.setString(1, key);
                    ppdStmt.setInt(2, i);
                    ppdStmt.setDouble(3, qValues[i]);
                    ppdStmt.addBatch();
                }
            }
            ppdStmt.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error updating QTable: " + e.getMessage());
            e.printStackTrace();
        }
    }
}