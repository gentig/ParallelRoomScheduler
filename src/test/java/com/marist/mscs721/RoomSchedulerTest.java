package com.marist.mscs721;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class RoomSchedulerTest {

    /**
     * date
     *
     * Test the input for the date
     *
     * @TODO: 2/24/2018
     * Fix date to support two digit year, single digit month and single digit day
     * and make sure for comparison reasons Timestamp works with that format
     * @see Timestamp for the required format
     */
    @Test
    public void date() throws DateTimeParseException{
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

    @Test
    public void getTimestamp() {
        String date = "2018-2-3";
        String time = "14:30:00";
        try {
            Timestamp ts = Timestamp.valueOf(date + " " + time);
            assertTrue(true);
        }catch (IllegalArgumentException ie){
            Assert.fail("Fail: " + ie);
        }
    }

    @Test
    public void dateTime() throws DateTimeParseException{
        String dateTimeExpected = "2018-02-15 14:30";
        String dateTimeInput = "2018-02-15 14:30";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime date = LocalDateTime.parse(dateTimeInput, formatter);
            //Do not use toString() for date because it uses default format. Use .format(formatter) for desired output
            assertEquals(dateTimeExpected,date.format(formatter));
        }
        catch (DateTimeParseException exc) {
            Assert.fail("Fail: " + exc);
        }
    }

    @Test
    public void getTime() throws DateTimeParseException{
        String timeExpected = "02:30";
        String timeInput = "02:30";
        try {
            LocalTime time = LocalTime.parse(timeInput);
            assertEquals(timeExpected,time.toString());
        }
        catch (DateTimeParseException exc) {
            Assert.fail("Fail: " + exc);
            //throw exc;      // Rethrow the exception.
        }
    }

    @Test
    public void predicate() {
        Timestamp someTs = Timestamp.valueOf("2018-06-18 18:33:00");
        CompareTimestamp compare = (start,end)-> start.after(end);
        assertTrue(compare.compare(someTs,new Timestamp(System.currentTimeMillis())));
    }
}