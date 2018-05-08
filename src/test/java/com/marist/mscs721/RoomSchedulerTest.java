/**
 * Yet Another Software License, 1.0
 *
 * Lots of text, specifying the users rights, and whatever ...
 */
package com.marist.mscs721;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

/**
 * We need to prefix all methods of Assert with Assert
 * Use static import if you want to use assert methods without the class prefix.
 * I prefer to do it like this for now
 */
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.concurrent.TimeUnit.SECONDS;

public class RoomSchedulerTest {
    static final Logger logger = Logger.getLogger(RoomScheduler.class);
    public static long [] array = new long[100000000];//a million values
    public static List<Long> saveVal = new ArrayList<>();

    @Test
    public void importRooms() throws IOException {
        Path pathToDir = Paths.get("jsonFiles");//"jsonFiles" is directory of all Json files
        JsonFiles jsonFiles = new JsonFiles();//callback object for walking directories and files
        Files.walkFileTree(pathToDir , jsonFiles);
        List<Path> allJsonFilesInPath = JsonFiles.getPaths();//get all paths from JsonFiles
        logger.info("List length is: " + allJsonFilesInPath.size());

        List<List<Path>> subSets = Lists.partition(allJsonFilesInPath, 3);//list size of each subset
        long count = subSets.stream().count();//count the number lists in the subsets
        Integer i = (int) (long) count;//danger of overflow, long is bigger than int
        logger.info("Subset is: " + count);//size of each subset list
        logger.info("Integer is: " + i);//size of each subset list
        logger.info(100/3);
        List<List<Path>> aList = new ArrayList<>();
        List<Callable<Integer>> callables = new ArrayList<>();
        for (List<Path> pathList : subSets) {
            //callables.add(threadHelperCallable(pathList));
            aList.add(pathList);
        }
        ExecutorService executor = Executors.newFixedThreadPool(i);
        try {
            executor.invokeAll(callables)
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
                           System.out::println
                            //logger::info
                    );
        } catch (InterruptedException e) {
            logger.error("",e);
        }

    }


    @Test
    public void sumSerial(){
        long [] array = new long[10000000];        //int total = 0;
        //populate array
        for(int i = 1; i < array.length + 1; i++){
            int index = i - 1;
            //System.out.println("Index " + index);
            //System.out.println("Value " + i);
            array[index] = i;
            //System.out.println(i);
        }

        System.out.println("length of array " +array.length);
        long sum = Arrays.stream(array).sum();
        long sum2 = LongStream.of(array).sum();

        long total = 0;
        for (long x : array) {
            //System.out.println("Sum " + total + "+" + array[i]);
            total = total + x;
            //System.out.println("x is: " + x);
        }
        System.out.println("total is " + total);
        System.out.println("Stream sum " + sum);
        System.out.println("Stream sum2 " + sum2);
        //System.out.println("Manual " + total);
    }

    /**
     * parallelArray
     * Trying to parallelize the array
     */
    @Test
    public void parallelArray() {
        //populate array
        for(int i = 1; i < array.length + 1; i++){
            int index = i - 1;//safe using int up to a million for index
            array[index] = i;
        }
        /**
         * we need two parallel threads, one to add
         * all elements in the array and one to count all
         * of them
         */
        ExecutorService executor = Executors.newFixedThreadPool(4);
        //Callable
        List<Callable<Long>> callable = Arrays.asList(
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                //threadHelperCallable("add"),
                threadHelperCallable("search"),
                threadHelperCallable("search"),
                threadHelperCallable("search"),
                threadHelperCallable("search"),
                threadHelperCallable("search")
        );

        List<Future<String>> futures = new ArrayList<>();
        for(Callable callableItem: callable){
            futures.add(executor.submit(callableItem));
        }

        for (Future ft: futures) {
            try {
                System.out.println(ft.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

        ////Executor
        //try {
        //    //Invoke all is blocking for all task to finish
        //    executor.invokeAll(callable)
        //            .stream()
        //            .map(future -> {
        //                try {
        //                    return future.get();
        //                }
        //                catch (Exception e) {
        //                    throw new IllegalStateException(e);
        //                }
        //            })
        //            .forEach(
        //                    logger::info
        //            );
        //} catch (InterruptedException e) {
        //    logger.error("",e);
        //}

    }

    /**
     * helperCallable
     *
     * This function creates callable with parameters
     */
    public Callable<Long> threadHelperCallable(String task) {
        if(task.equals("add")) {
            return () -> {
                //System.out.println(Thread.currentThread().getName());
              return addAllArray();
            };
        }else if(task.equals("count")) {
            return () -> {
                //System.out.println(Thread.currentThread().getName());
                return countAllInArray();
            };
        }else if(task.equals("search")) {
            return () -> {
                //System.out.println(Thread.currentThread().getName());
                return arraySearch();
            };
        }else {
            return () -> {
                //System.out.println(Thread.currentThread().getName());
                return 1L;
            };
        }

    }//end parallel

    public static Long addAllArray(){
        long total = 0;
        for (long x : array ) {
            total+=x;
        }
        return total;
    }
    public static Long countAllInArray(){
        long total = 0;
        for (long x : array ) {
            total++;
        }
        return total;
    }
    public static Long arraySearch(){
        long found = 0;
        long addWork = 0;
        for(long val: array){
            if(val == 100 ){
                for(int i = 0; i < val; i++){
                    addWork+=i;
                    for(int z =0; z < 10; z++){
                        int g = z + 1 -1;
                        for (int s = 0; s < 10; s++){
                            int u = s + 1 - 1;
                        }
                    }
                }
               found = val;
            }else{
                for (int x =0; x < 100; x++){
                    int y = x + 1 - 1;
                    for(int p = 0; p < 10; p++){
                        int r = p + 1 -1;
                        for(int n =0; n < 10; n++){
                            int w = n + 1 -1;
                        }
                    }
                }
            }
        }
        return found;
    }

    @Test
    public void notParallel(){
        //populate array
        for(int i = 1; i < array.length + 1; i++){
            int index = i - 1;//safe using int up to a million for index
            array[index] = i;
        }
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        //logger.info(addAllArray());
        logger.info(arraySearch());
        logger.info(arraySearch());
        logger.info(arraySearch());
        logger.info(arraySearch());
        logger.info(arraySearch());
    }

    @Test
    public void sequentialStream() {
        List<Long> lng = new ArrayList<>();
        for (int i = 1; i < 1000000; i++) {
            lng.add((long) i);
        }

        Runnable seq = () -> {
            Boolean ans = lng.stream().isParallel();
            long lg = lng.stream().count();
            System.out.println(ans);
            System.out.println(lg);
        };

        Runnable par = () -> {
            //Boolean ansPar = lng.parallelStream().isParallel();
            long lgPar = lng.parallelStream().count();
            //System.out.println(ansPar);
            //System.out.println(lgPar);
            System.out.println(Thread.currentThread().getName());
        };

        Thread th = new Thread(seq);
        th.start();
        Thread th2 = new Thread(par);
        th2.start();
    }

    @Test
    public void parallelStream(){
        List<Long> lng = new ArrayList<>();
        for(int i = 1; i < 1000000; i++){
            lng.add((long)i);
        }

        Boolean ans = lng.parallelStream().isParallel();
        long lg = lng.parallelStream().count();
        System.out.println(ans);
        System.out.println(lg);
    }
    /**
     * findRoomByDate
     *
     * Get all available rooms by date
     */
    @Test
    public void findRoomByDate()
    {
        //Desired date and time to schedule a room
        String dateThatWorksStart = "2018-10-27";
        String timeThatWorksStart = "08:00";
        String dateThatWorksEnd = "2018-10-27";
        String timeThatWorksEnd = "18:00";
        Timestamp TSDateThatWorksStart;
        Timestamp TSDateThatWorksEnd;
        //holds meetings for a specific room
        ArrayList<Meeting> m = new ArrayList<>();
        Timestamp startTimestamp;
        Timestamp endTimestamp;
        int errorCounter = 0;
       /**
        * Note: Logic should be
        * if search startTime and endTime are before the current room meeting startTime
        * room is good, if search startTime and endTime are bigger than current room meeting
        * endTime, room is good. Otherwise room is not available for the search time.*/

       //Use getDate and getTime to make sure that the date and time are correct
       String correctDateThatWorksStart = RoomScheduler.getDate(dateThatWorksStart);
       String correctTimeThatWorksStart = RoomScheduler.getTime(timeThatWorksStart);
       String correctDateThatWorksEnd = RoomScheduler.getDate(dateThatWorksEnd);
       String correctTimeThatWorksEnd = RoomScheduler.getTime(timeThatWorksEnd);
       //Timestamp
       TSDateThatWorksStart = Timestamp.valueOf(correctDateThatWorksStart + " " + correctTimeThatWorksStart + ":00");
       TSDateThatWorksEnd = Timestamp.valueOf(correctDateThatWorksEnd + " " + correctTimeThatWorksEnd + ":00");
       //Populate meetings (10 at least)
       for(int i = 1; i < 10; i++) {
           //Start and end times as strings
           String dateStart = RoomScheduler.getDate("2018-04-2" + i);
           String timeStart = RoomScheduler.getTime("08:00");//need a time in the format hh:ii
           String dateEnd = RoomScheduler.getDate("2018-04-2" + i);
           String timeEnd = RoomScheduler.getTime("18:00");
           //Timestamps for meeting object
           startTimestamp = Timestamp.valueOf(dateStart + " " + timeStart + ":00");
           endTimestamp = Timestamp.valueOf(dateEnd + " " + timeEnd + ":00");
           //Subject for Meeting object
           String subject = "Date " + i;
           //Adding all meeting to array
           m.add(new Meeting(startTimestamp,endTimestamp,subject));
       }

       //Check if entered Date time is in the past
       Boolean cmpStart = TSDateThatWorksStart.before(new Timestamp(System.currentTimeMillis()));
       Boolean cmpEnd = TSDateThatWorksEnd.before(new Timestamp(System.currentTimeMillis()));
       if (cmpStart || cmpEnd) {
           //System.out.println("Time given is in the past");
           logger.error("Time given is in the past");
       }else if(TSDateThatWorksEnd.before(TSDateThatWorksStart)){
           //System.out.println("End date before start date");
           logger.error("End date before the start date");
       } else {
           //Print all Meetings start and end time
           for (Meeting me : m) {
               //System.out.println(me.toString());
               if (TSDateThatWorksStart.after(me.getStartTime()) && TSDateThatWorksStart.before(me.getStopTime())) {
                   //System.out.println("Cannot Book, Time is in between schedule");
                   //logger.error("Cannot Book, Time is in between schedule");
                   errorCounter++;
               }else if(me.getStartTime().after(TSDateThatWorksStart) && me.getStartTime().before(TSDateThatWorksEnd)){
                   //System.out.println("Cannot book, Time Conflict");
                   //logger.error("Cannot book, Time Conflict");
                   errorCounter++;
               }else if(TSDateThatWorksStart.equals(me.getStartTime()) || TSDateThatWorksEnd.equals(me.getStopTime())){
                   //logger.error("Start or End time same as the schedule");
                   errorCounter++;
               }
           }
       }
       if(errorCounter > 0){
           //logger.error("Room cannot be booked for the given date");
       }else{
           //logger.info("Room can be scheduled for the given date");
       }
       //System.out.println("Room booking schedule");
       //for (Meeting sm : m){
           //System.out.println(sm.toString());
       //}
       //Expect to fail if expected is 1
       Assert.assertEquals(0, errorCounter);
    }

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
            Assert.assertEquals(dateExpected,date.format(formatter));
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
            Assert.assertTrue(true);
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
            Assert.assertEquals(dateTimeExpected,date.format(formatter));
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
            Assert.assertEquals(timeExpected,time.toString());
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
        Assert.assertTrue(compare.compare(someTs,new Timestamp(System.currentTimeMillis())));
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
                Assert.assertTrue(true);
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
            Assert.assertEquals(str.compareTo(name),0);
        }
    }

    @Test
    public void scheduleRoomsFromDateTime()
    {
        //Date format Y-m-d

        //Time format H:i
    }


}