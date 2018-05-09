/**
 * Yet Another Software License, 1.0
 *
 * Lots of text, specifying the users rights, and whatever ...
 */
package com.marist.mscs721;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This is the test suite class to run all test classes with all their
 * test cases
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //RoomSchedulerTest.class,
        JsonFilesTest.class,
        MeetingTest.class,
        RoomTest.class
})

public class JUnitTestSuite {
}
