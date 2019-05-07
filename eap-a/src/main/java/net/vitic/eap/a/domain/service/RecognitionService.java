package net.vitic.eap.a.domain.service;

import net.vitic.eap.a.domain.ApplicationException;
import net.vitic.eap.a.domain.model.MfDate;
import net.vitic.eap.a.domain.model.Money;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Domain Logic Pattern - TRANSACTION SCRIPT
 */
public class RecognitionService {

    private final RecognitionDataGateway db;

    RecognitionService(RecognitionDataGateway db) {this.db = db;}

    public Money recognizedRevenue(long contractNumber, MfDate asOf) throws ApplicationException {
        Money result = Money.dollars(0);
        try (ResultSet recognitions = db.findRecognitionsFor(contractNumber, asOf);){
            if (!recognitions.next()) {
                throw new ApplicationException(
                    new IllegalStateException("Contract " + contractNumber + " not found."));
            } else {
                result = result.add(Money.dollars(recognitions.getBigDecimal("amount")));
            }
            return result;
        } catch (SQLException e) {throw new ApplicationException (e);
        }
    }


    public void calculateRevenueRecognitions(long contractNumber) throws ApplicationException {

        try (ResultSet contracts = db.findContract(contractNumber);){
            if (!contracts.next()) {
                throw new ApplicationException(
                    new IllegalStateException("Contract " + contractNumber + " not found."));
            } else {

                Money totalRevenue = Money.dollars(contracts.getBigDecimal("revenue"));
                MfDate recognitionDate = new MfDate(contracts.getDate("dateSigned"));
                String type = contracts.getString("type");

                switch (type) {
                    case "S": {
                        insertRecognitions(contractNumber, totalRevenue, recognitionDate, 60, 90);
                        break;
                    }
                    case "W":
                        db.insertRecognition(contractNumber, totalRevenue, recognitionDate);
                        break;

                    case "D": {
                        insertRecognitions(contractNumber, totalRevenue, recognitionDate, 30, 60);
                        break;
                    }
                    default:
                        throw new ApplicationException(
                            new IllegalStateException("Unexpected value: " + type));
                }
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    private void insertRecognitions(long contractNumber,
                                    Money totalRevenue,
                                    MfDate recognitionDate,
                                    int firstDate,
                                    int secondDate) throws SQLException {
        if (totalRevenue != null) {
            Money[] allocation = totalRevenue.allocate(3);
            db.insertRecognition(contractNumber, allocation[0], recognitionDate);
            db.insertRecognition(contractNumber, allocation[1], recognitionDate.addDays(firstDate));
            db.insertRecognition(contractNumber, allocation[2], recognitionDate.addDays(secondDate));
        }
    }
}
