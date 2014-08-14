package org.celllife.appointmentreminders.framework.util;

public class MsisdnUtils {

    public static boolean isValidMsisdn(String msisdn) {

        if (msisdn.matches("^27[0-9]{9}$")) {
            return true;
        }
        else {
            return false;
        }

    }
}
