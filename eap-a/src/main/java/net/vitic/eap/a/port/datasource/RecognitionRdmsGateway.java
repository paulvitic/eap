package net.vitic.eap.a.port.datasource;

import net.vitic.eap.a.domain.model.MfDate;
import net.vitic.eap.a.domain.model.Money;
import net.vitic.eap.a.domain.service.RecognitionDataGateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Data Source Pattern - Table Data Gateway
 */
public class RecognitionRdmsGateway implements RecognitionDataGateway {

    private static final String findRecognitionsStatement =
        "SELECT amount " +
        "  FROM revenueRecognitions " +
        "  WHERE contract = ? AND recognizedOn  <= ?";

    private static final String findContractStatement =
        "SELECT * " +
        "  FROM contracts c, products p " +
        "  WHERE c.ID = ? AND c.product = p.ID";

    private static final String insertRecognitionStatement =
        "INSERT INTO revenueRecognitions VALUES (?, ?, ?)";

    private final Connection db;

    public RecognitionRdmsGateway(Connection db) {this.db = db;}

    public ResultSet findRecognitionsFor(long contractID, MfDate asof) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(findRecognitionsStatement);
        stmt.setLong(1, contractID);
        stmt.setDate(2, asof.toSqlDate());
        return stmt.executeQuery();
    }

    public ResultSet findContract (long contractID) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(findContractStatement);
        stmt.setLong(1, contractID);
        return stmt.executeQuery();
    }


    public void insertRecognition (long contractID, Money amount, MfDate asof) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(insertRecognitionStatement);
        stmt.setLong(1, contractID);
        stmt.setBigDecimal(2, amount.amount());
        stmt.setDate(3, asof.toSqlDate());
        stmt.executeUpdate();
    }
}
