package util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests the isValid() method of the Date class.
 * It includes tests for valid and invalid dates, including leap years,
 * invalid days, months, and negative years.
 * Author: Divit Shetty (dps190)
 */

public class DateTest {

    // Test case 1: Leap year date (valid)
    /**
     * Test case for a valid leap year date (February 29, 2020).
     * This date should be valid because 2020 is a leap year.
     */
    @Test
    public void testValidDate1(){
        Date validDate1 = new Date(2020, Date.FEBRUARY, 29);
        assertTrue(validDate1.isValid());
    }

    // Test case 2: Non-leap year date (valid)
    /**
     * Test case for a valid non-leap year date (December 31, 2021).
     * This date should be valid because it is a valid calendar date.
     */
    @Test
    public void testValidDate(){
        Date validDate2 = new Date(2021, Date.DECEMBER, 31);
        assertTrue(validDate2.isValid());
    }

    // Invalid dates test cases

    // Test case 3: Non-leap year February 29 (invalid)
    /**
     * Test case for an invalid non-leap year February 29 date (February 29, 2019).
     * This date should be invalid because 2019 is not a leap year.
     */
    @Test
    public void testInvalidDate1() {
        Date invalidDate1 = new Date(2019, Date.FEBRUARY, 29);
        assertFalse(invalidDate1.isValid());
    }

    // Test case 4: April 31st (invalid)
    /**
     * Test case for an invalid April 31st date (April 31, 2023).
     * This date should be invalid because April only has 30 days.
     */
    @Test
    public void testInvalidDate2(){
        Date invalidDate2 = new Date(2023, Date.APRIL, 31);
        assertFalse(invalidDate2.isValid());
    }

    // Test case 5: Invalid month (greater than 12)
    /**
     * Test case for an invalid month.
     * This date should be invalid because there is no 13th month
     */
    @Test
    public void testInvalidDate3(){
        Date invalidDate3 = new Date(2022, 13, 1);
        assertFalse(invalidDate3.isValid());
    }

    // Test case 6: Invalid negative day
    /**
     * Test case for an invalid negative day (February -1, 2023).
     * This date should be invalid because days cannot be negative.
     */
    @Test
    public void testInvalidDate4(){
        Date invalidDate4 = new Date(2023, Date.FEBRUARY, -1);
        assertFalse(invalidDate4.isValid());
    }
}