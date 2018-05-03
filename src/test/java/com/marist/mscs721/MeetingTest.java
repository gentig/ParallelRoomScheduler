/**
 * Yet Another Software License, 1.0
 *
 * Lots of text, specifying the users rights, and whatever ...
 */
package com.marist.mscs721;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.*;

public class MeetingTest {
    static final Logger logger = Logger.getLogger(RoomScheduler.class);
    /**
     * getDate
     *
     * Test the input for the date
     *
     * @TODO: 2/24/2018
     * Fix date to support two digit year, single digit month and single digit day
     * and make sure for comparison reasons Timestamp works with that format. getDate is
     * used in the application to get the accepted date before building the timestamp object.
     * @see Timestamp for the required format
     */
    @Test
    public void getDate(){
        String dateExpected = "2018-2-3";
        String dateInput = "2018-2-3";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d");
            LocalDate date = LocalDate.parse(dateInput, formatter);
            assertEquals(dateExpected,date.format(formatter));
        }
        catch (DateTimeParseException exc) {
            Assert.fail("Fail: " + exc);
        }
    }

    /**
     * setStartTime
     *
     * Testing timestamp format. For month and day we can use either single digit or double digits.
     * For the year only four digit format is accepted
     * */
    @Test
    public void setStartTime() {
        String date = "2018-2-3";//In the application we get this by calling getDate
        String time = "14:30:00";//In the application we get this by calling getTime
        try {
            Timestamp ts = Timestamp.valueOf(date + " " + time);
            assertTrue(true);
        }catch (IllegalArgumentException ie){
            Assert.fail("Fail: " + ie);
        }
    }
}