package org.celllife.appointmentreminders.framework.utils;

import junit.framework.Assert;
import org.celllife.appointmentreminders.framework.util.MsisdnUtils;
import org.celllife.appointmentreminders.test.TestConfiguration;
import org.junit.Test;

public class TestMsisdnUtils extends TestConfiguration {

    @Test
    public void testIsValidMsisdn() {
        Boolean result = MsisdnUtils.isValidMsisdn("27724194158");
        Assert.assertTrue(result);
        result = MsisdnUtils.isValidMsisdn("27864194158");
        Assert.assertTrue(result);
        result = MsisdnUtils.isValidMsisdn("2772419415");
        Assert.assertFalse(result);
        result = MsisdnUtils.isValidMsisdn("2772419415b");
        Assert.assertFalse(result);
        result = MsisdnUtils.isValidMsisdn("2872419415b");
        Assert.assertFalse(result);
    }

}
