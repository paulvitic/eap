package net.vitic.eap.a.domain.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SuppressWarnings("WeakerAccess")
public class MfDate {

    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
    private final Date uDate;

    public MfDate(Date date) {
        this.uDate = new Date(date.getTime());
    }

    public java.sql.Date toSqlDate() {
        return new java.sql.Date(uDate.getTime());
    }

    public MfDate addDays(int days) {
        LocalDateTime localDateTime = uDate.toInstant()
                                           .atZone(ZoneId.systemDefault())
                                           .toLocalDateTime();

        localDateTime = localDateTime.plusDays(days);

        return new MfDate(Date.from(localDateTime.atZone(ZoneId.systemDefault())
                                                 .toInstant()));
    }

    @Override
    public String toString() {
        return df.format(uDate);
    }
}


