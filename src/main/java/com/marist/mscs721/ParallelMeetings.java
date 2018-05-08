package com.marist.mscs721;

import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will be used for parallel functionality for
 * scheduling meetings
 * */
public class ParallelMeetings{
    static final Logger logger = Logger.getLogger(ParallelMeetings.class);
    private static  int numberOfMeetings = 1000000;
    /**
     * parallelScheduleMeetings
     *
     * This function will schedule random meetings for
     * each room so we can see how parallelization help us
     * do things faster
     */
    public static void parallelScheduleMeetings(List<Room> roomsList) {
        /**
         * we need two parallel threads, one to add
         * all elements in the array and one to count all
         * of them
         */
        ExecutorService executor = Executors.newFixedThreadPool(16);
        List<Callable<Long>> callableList = new ArrayList<>();

        for(int i = 0; i < roomsList.size(); i++){
            Room room = roomsList.get(i);
            callableList.add(threadHelperCallableRooms(room,numberOfMeetings));
        }

        //Executor
        try {
            //Invoke all is blocking for all tasks to finish
            executor.invokeAll(callableList)
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        }
                        catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .forEach(
                            logger::info
                    );
        } catch (InterruptedException e) {
            logger.error("",e);
        }
        executor.shutdown();
        while (!executor.isTerminated()){}
    }

    /**
     * threadHelperCallable
     *
     * This function returns a callable
     *
     * @return callable that returns a long
     */
    public static Callable<Long> threadHelperCallableRooms(Room room, int numberOfMeetings) {
        return () -> {
            return adddMeetings(room,numberOfMeetings);
        };
    }

    //Add Room task
    public static Long adddMeetings(Room room, int numberOfMeetings){
        String dateString = "2018-06-01 08:00:00";//Starting in june
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        if(numberOfMeetings > 1000000){
            logger.error("Number of meetings bigger than 1000000. System willl exit");
            System.exit(0);
        }else {
            String subject = "Parallel";
            for (int i = 0; i < numberOfMeetings; i++) {
                Timestamp startTimestamp = Timestamp.valueOf(dateTime.format(formatter));//we need seconds for timestamp
                Timestamp endTimestamp = Timestamp.valueOf(dateTime.plusHours(1).format(formatter));//we need seconds for timestamp
                dateTime = dateTime.plusHours(1);
                dateTime = dateTime.plusMinutes(5);//adding 5 min to avoid starting at end time
                Meeting meeting = new Meeting(startTimestamp, endTimestamp, subject);
                room.addMeeting(meeting);
            }
        }
        return (long) room.getMeetings().size();
    }
}