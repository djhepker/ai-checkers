package main.java.ai.table;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

class QValueRepository {

    private static final Logger logger = LoggerFactory.getLogger(QValueRepository.class);
    private static final int BATCH_SIZE = 1000;
    private final AtomicBoolean isInitialized;
    private final HikariDataSource dataSource;

    QValueRepository(String url) {
        this.isInitialized = new AtomicBoolean(false);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setMaximumPoolSize(4);
        config.setMinimumIdle(1);
        config.setIdleTimeout(120000); // 2 minutes
        config.setConnectionTimeout(30000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(config);
        initializeDatabase();
    }

    int getMaxQAction(String serialKey) {
        final String sql = "SELECT Action FROM QTable WHERE HexKey = ? ORDER BY QValue DESC LIMIT 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, serialKey);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt("Action") : 0;
            }
        } catch (SQLException e) {
            logger.error("Failed to fetch max Q-action for serialKey: {}", serialKey, e);
        }
        return 0;
    }

    double getQValueFromTable(String serialKey, int action) {
        final String sql = "SELECT QValue FROM QTable WHERE HexKey = ? AND Action = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, serialKey);
            pstmt.setInt(2, action);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getDouble("QValue") : 0.0;
            }
        } catch (SQLException e) {
            logger.error("Failed to fetch Q-value for serialKey: {}, action: {}", serialKey, action, e);
        }
        return 0.0;
    }

    double getMaxQValue(String serialKey) {
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

    void updateQTable(Map<String, double[]> qTable) {
        final String sql = "INSERT OR REPLACE INTO QTable (HexKey, Action, QValue) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ppdStmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            int batchCount = 0;

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

                    if (++batchCount >= BATCH_SIZE) {
                        ppdStmt.executeBatch();
                        batchCount = 0;
                    }
                }
            }

            if (batchCount > 0) {
                ppdStmt.executeBatch();
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("Failed to update QTable", e);
        }
    }

    void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }

    private void initializeDatabase() {
        if (isInitialized.compareAndSet(false, true)) {
            createTable();
        }
    }

    private void createTable() {
        final String sqlCreateTable = """
                CREATE TABLE IF NOT EXISTS QTable (
                    HexKey TEXT NOT NULL,
                    Action INTEGER NOT NULL,
                    QValue REAL NOT NULL,
                    PRIMARY KEY (HexKey, Action)
                ) WITHOUT ROWID""";

        final String sqlCreateIndex = "CREATE INDEX IF NOT EXISTS idx_qvalue ON QTable (HexKey, QValue DESC);";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);
            stmt.execute(sqlCreateTable);
            stmt.execute(sqlCreateIndex);
            conn.commit();
        } catch (SQLException e) {
            logger.error("Failed to create table/index", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}