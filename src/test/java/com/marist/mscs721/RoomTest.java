/**
 * Yet Another Software License, 1.0
 *
 * Lots of text, specifying the users rights, and whatever ...
 */
package com.marist.mscs721;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.InputMismatchException;

import static org.junit.Assert.*;

public class RoomTest {
    static final Logger logger = Logger.getLogger(RoomScheduler.class);

    @Test
    public void setCapacity() {
        String roomName = "TestName";
        String roomCapacity = "999";//Make this big to test failure
        try {
            int intCapacity = Integer.parseInt(roomCapacity);
            //If no error see if we have an positive int value
            if(intCapacity > 0){
                Room rm = new Room(roomName,intCapacity,"","");
                assertTrue(true);
            }else{
                Assert.fail("Capacity less than one");
            }

        }catch (NumberFormatException ie){
            Assert.fail("Input cannot be parsed an an int");
        }
    }
}