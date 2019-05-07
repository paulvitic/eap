package net.vitic.eap.a.domain.service;

import net.vitic.eap.a.domain.model.MfDate;
import net.vitic.eap.a.domain.model.Money;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RecognitionDataGateway {

    ResultSet findRecognitionsFor(long contractNumber, MfDate asOf) throws SQLException;

    ResultSet findContract(long contractNumber) throws SQLException;

    void insertRecognition(long contractNumber, Money money, MfDate recognitionDate) throws SQLException;
}
