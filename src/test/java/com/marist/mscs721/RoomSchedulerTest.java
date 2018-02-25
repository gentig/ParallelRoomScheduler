package com.marist.mscs721;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class RoomSchedulerTest {
    static final Logger logger = Logger.getLogger(RoomScheduler.class);
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
    public void date(){
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

    @Test
    public void removeRoom() {
        ArrayList<String> roomList = new ArrayList<>();
        roomList.add("One");
        roomList.add("Two");
        //ArrayList<String> nullList = null;
        //Testing a case where isListEmpty checks if list is empty by calling isEmpty() on the list
        //assertTrue || assertFalse will fail with NullPointerException if List is null
        //We can avoid this by catching exception.
        //roomList = null;//Making roomList null fails the test
        try{
            if(roomList == null){
                Assert.fail("Null list");
            }else {
                //Should not get here if list is null
                //logger.info("List size is: "+ roomList.size());
                assertTrue(true);
            }
        }catch (NullPointerException ex){
            /**
             * @TODO: 2/22/2018 use logger
             */
            logger.error("List of rooms is null " + ex.fillInStackTrace());
            Assert.fail("Null list in catch");
        }
        //We can test the above try catch with just the statement
        //assertFalse(nullList.isEmpty());

    }

    @Test
    public void findRoomIndex() {
        //If list is null program terminates with NullPointerException
        ArrayList<String> strings = new ArrayList<>();
        strings.add("genti");
        String name = "genti";
        for (String str : strings) {
            assertEquals(str.compareTo(name),0);
        }
    }


}