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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.concurrent.*;

public class JavaThreadTest {
    public static long[] array = new long[100000000];//a million values
    static final Logger logger = Logger.getLogger(RoomScheduler.class);
    static List<Map<String, String>> rooms = new ArrayList<>();

    /**
     * buildRoomWithStarEndTime
     *
     * This function is used as an example to build a list with meetings.
     * It basically builds a list as a list of rooms and adds a hashmap <String,String>
     * as two entries for meetings. This function is not used now but it is here
     * just as an reference
     */
    @Test
    public void buildRoomWithStarEndTime() {
        //build 10 rooms
        for (int i = 0; i < 10; i++) {
            rooms.add(i, new HashMap<>());
        }

        //get only one room
        Map<String, String> scRoom = rooms.get(1);
        long nrOfMeetings = adddMeetings(scRoom, 5);
        System.out.println("Meetings Added: " + nrOfMeetings);
        //System.out.println("Size of Rooms: "+rooms.size());
        //Add meetings
        for (Map<String, String> hm : rooms) {
            for (int i = 0; i < 4; i++) {
                hm.put("" + i, "" + (i + 1));
            }
        }
        //Print all rooms and meetings
        for (int i = 0; i < rooms.size(); i++) {
            for (Map.Entry<String, String> entry : rooms.get(i).entrySet()) {
                System.out.println("Start: " + entry.getKey());
                System.out.println("End: " + entry.getValue());
            }
        }
    }

    /*-----------------------------------------------Parallelizing adding meeting--------------------------------*/
    /**
     * addMeetings
     *
     * This function is called crom callableHelper to add meetings
     * to the rooms.
     */
    public Long adddMeetings(Map<String, String> room, int numberOfMeetings) {
        for (int i = 0; i < numberOfMeetings; i++) {
            room.put("" + i, "" + (i + 1));
        }
        return (long) room.size();
    }

    /**
     * addMeetingsVoid
     *
     * This function is similar to the function above but it does not
     * return anything. Used for executors without the future
     */
    public void adddMeetingsVoid(Map<String, String> room, int numberOfMeetings) {
        for (int i = 0; i < numberOfMeetings; i++) {
            room.put("" + i, "" + (i + 1));
        }
        System.out.println(room.size());
    }

    /**
     * createRooms
     *
     * This function creates rooms which is a list consisting of meetings
     * e.g. the list is the list of rooms and hashmap in it represents a meeting
     * <String, String>
     * */
    public boolean createRooms(int numberOfRooms) {
        for (int i = 0; i < numberOfRooms; i++) {
            rooms.add(i, new HashMap<>());
        }
        if (rooms.size() == numberOfRooms) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * testRooms
     *
     * This function tests if the list of rooms with meetings is created
     */
    @Test
    public void testRooms() {
        boolean tr = createRooms(10);
        Assert.assertTrue(tr);
    }

    /**
     * executorExecuteMeeting
     *
     * This functions executes the runnable task immediately
     */
    @Test
    public void executorExecuteMeetings() {
        int numberOfRooms = 40000;
        int numberOfMeetings = 130;
        boolean tr = createRooms(numberOfRooms);
        if (tr) {
            ExecutorService executor = Executors.newFixedThreadPool(64);
            for (int i = 0; i < numberOfRooms; i++) {
                Map<String, String> room = rooms.get(i);
                Runnable task = () -> {
                    adddMeetingsVoid(room, numberOfMeetings);
                };
                executor.execute(task);
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
            System.out.println("Finished all tasks");
        }
    }

    /**
     * parallelInvokeAllRooms
     *
     * This function uses invokeAll with futures to get the value from callable
     * when thread finishes execution
     */
    @Test
    public void parallelInvokeAllRooms() {
        /**
         * we need two parallel threads, one to add
         * all elements in the array and one to count all
         * of them
         */
        ExecutorService executor = Executors.newFixedThreadPool(16);
        List<Callable<Long>> callableList = new ArrayList<>();
        //Settings for testing
        int numberOfRooms = 40000;//rooms
        int numberOfMeetings = 100;//meetings
        boolean tr = createRooms(numberOfRooms);
        //Populate callableList
        for (int i = 0; i < rooms.size(); i++) {
            Map<String, String> room = rooms.get(i);
            callableList.add(threadHelperCallableRooms(room, numberOfMeetings));
        }
        //Executor
        try {
            //Invoke all is blocking for all tasks to finish
            executor.invokeAll(callableList)
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .forEach(
                            logger::info
                    );
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }

    /**
     * sequentialAddMeetings
     *
     * This function will add meetings in sequence without parallel
     * threads
     */
    @Test
    public void sequentialAddMeetings() {
        int numberOfRooms = 40000;//rooms
        int numberOfMeetings = 130;//meetings
        boolean tr = createRooms(numberOfRooms);
        if (tr) {
            for (int i = 0; i < rooms.size(); i++) {
                Map<String, String> room = rooms.get(i);
                adddMeetingsVoid(room, numberOfMeetings);
            }
        }
    }

    /**
     * threadHelperCallable
     *
     * This function returns a callable to be used in executors
     * @return callable that returns a long
     */
    public Callable<Long> threadHelperCallableRooms(Map<String, String> room, int numberOfMeetings) {
        return () -> {
            return adddMeetings(room, numberOfMeetings);
        };
    }

    /*-----------------------------Parallel and sequential testing array below------------------------------------*/
    /**
     *
     * !!!!!!!!!!!!!!!!!!!---- IMPORTANT----!!!!!!!!!!!!!!!!!!!
     *
     * Below there are multiple functions to run certain tasks in parallel and
     * in sequence. Using this example we can clearly see the CPU throughput
     * and speed of parallel processing and sequential processing.
     *
     */

    /**
     * parallelInvokeAll
     *
     * Trying to parallelize the array
     */
    @Test
    public void parallelInvokeAll() {
        /**
         * we need two parallel threads, one to add
         * all elements in the array and one to count all
         * of them
         */
        ExecutorService executor = Executors.newFixedThreadPool(4);
        //Callable
        List<Callable<Long>> callable = Arrays.asList(
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable()
        );
        //Executor
        try {
            //Invoke all is blocking for all task to finish
            executor.invokeAll(callable)
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .forEach(
                            logger::info
                    );
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }

    /**
     * parallelFutureSubmit
     *
     * We are using future to get the return value from a callable
     * task. Remember that future.get is blocking
     */
    @Test
    public void parallelFutureSubmit() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Long>> futures = new ArrayList<>();
        //Callable
        List<Callable<Long>> callable = Arrays.asList(
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable(),
                threadHelperCallable()
        );

        for (Callable<Long> callableItem : callable) {
            futures.add(executor.submit(callableItem));
        }
        //Getting the answers from each tasks
        for (Future ft : futures) {
            try {
                System.out.println(ft.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }

    /**
     * threadHelperCallable
     *
     * This function returns a callable
     * @return callable that returns a long
     */
    public Callable<Long> threadHelperCallable() {
        return () -> {
            return someTask();
        };
    }

    /**
     * executorExecute
     *
     * This functions executes the runnable task immediately
     */
    @Test
    public void executorExecute() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            Runnable task = () -> {
                someTaskVoid();//print value from task
            };
            executor.execute(task);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all tasks");
    }

    /**
     * someTask
     *
     * This function performs some task or work called by callable
     * and runs in the thread. This task can be called by a function
     * in sequence as well. When we use futures we need to return something
     * from callable that calls this function in order to print it while work
     * is being done.
     *
     * @returns long sum
     */
    public Long someTask() {
        long sum = 0;
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 1000; j++) {
                for (int d = 0; d < 1000; d++) {
                    sum += d;
                }
            }
        }
        return sum;
    }

    /**
     * someTask
     *
     * This function performs some task or work called by callable
     * and runs in the thread. This task can be called by a function
     * in sequence as well. This function does not return anything. If
     * we are not using futures, as in the case of executor.execute is better
     * to use this function to print something when work is being done.
     */
    public void someTaskVoid() {
        long sum = 0;
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 1000; j++) {
                for (int d = 0; d < 1000; d++) {
                    sum += d;
                }
            }
        }
        System.out.println("Sum is: " + sum);
    }

    /**
     * testSequence
     *
     * This function runs the task in a sequence without
     * threads and parallelization so we can see the difference in CPU
     * throughput and speed compared to running the task in parallel
     */
    @Test
    public void testSequence() {
        for (int i = 0; i < 20; i++) {
            someTask();
        }
    }

    /*-----------------------------------------------Extra below----------------------------------------------*/
    /**
     * addMeetings
     *
     * This function tests the creation time for meetings to be
     * used with threads to add a number of meetings per room
     */
    @Test
    public void adddMeetings() {
        int numberOfMeetings = 1000;
        long meetingSize = 0L;
        String dateString = "2018-06-01 08:00:00";//start date

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss",Locale.US);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        if (numberOfMeetings > 1000) {
            logger.error("Number of meetings bigger than 1000. System willl exit");
            System.exit(0);
        } else {
            //Current approach for meeting time
            //Timestamp startTimestamp = Timestamp.valueOf("2018-06-01" + " " + "00:00:00" + ":00");//we need seconds for timestamp
            //Timestamp endTimestamp = Timestamp.valueOf("2018-06-01" + " " + "00:10:00" + ":00" + ":00");//we need seconds for timestamp
            //String subject = "Parallel";
            //Meeting meeting = new Meeting(startTimestamp, endTimestamp, subject);

            //Building meeting times
            for (int i = 0; i < numberOfMeetings; i++) {
                Timestamp startTimestamp = Timestamp.valueOf(dateTime.format(formatter));//we need seconds for timestamp
                Timestamp endTimestamp = Timestamp.valueOf(dateTime.plusHours(1).format(formatter));//we need seconds for timestamp
                dateTime = dateTime.plusHours(1);
                dateTime = dateTime.plusMinutes(5);//adding 5 min to avoid starting at end time

                System.out.println(startTimestamp.toLocalDateTime().format(formatter));
                System.out.println(endTimestamp.toLocalDateTime().format(formatter));
            }
        }
    }
}