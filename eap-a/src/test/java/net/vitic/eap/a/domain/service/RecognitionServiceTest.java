package net.vitic.eap.a.domain.service;

import net.vitic.eap.a.domain.ApplicationException;
import net.vitic.eap.a.port.datasource.RecognitionRdmsGateway;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class RecognitionServiceTest {

    private static final Logger log = LoggerFactory.getLogger(RecognitionServiceTest.class);

    private Connection conn;
    private RecognitionService service;

    @BeforeEach
    void setUp() {
        try (InputStream input = RecognitionServiceTest.class.getClassLoader().getResourceAsStream("config-test.properties")) {

            assert input != null;
            Properties prop = new Properties();
            prop.load(input);

            Class.forName(prop.getProperty("db.driver"));

            Flyway flyway = Flyway.configure()
                                  .configuration(prop)
                                  .baselineVersion("0")
                                  .baselineOnMigrate(true)
                                  .load();

            flyway.clean();
            flyway.baseline();
            flyway.migrate();

            this.conn = DriverManager.getConnection(prop.getProperty("db.url"),
                                                    prop.getProperty("db.user"),
                                                    prop.getProperty("db.password"));

            this.service = new RecognitionService(new RecognitionRdmsGateway(conn));

        } catch (IOException | SQLException | ClassNotFoundException  e) {
            log.error("Test error", e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            conn.close();
        } catch (SQLException e) {
            log.error("Test error", e);
        }
    }

    @Test
    void name() {
        try {
            service.calculateRevenueRecognitions(122L);
        } catch (ApplicationException e) {
            log.error("Test error", e);
        }
    }
}