/**
 * Yet Another Software License, 1.0
 *
 * Lots of text, specifying the users rights, and whatever ...
 */
package com.marist.mscs721;

//Using Gson library for JSON
import com.google.gson.Gson;
//Log4j for debugging
import org.apache.log4j.Logger;
import java.io.*;
//Using java8 nio for file manipulation
import java.nio.file.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static javafx.application.Platform.exit;

public class RoomScheduler {
    static final Logger logger = Logger.getLogger(RoomScheduler.class);
	private static Scanner keyboard = new Scanner(System.in);
	private static final int MINIMUM_ROOM_CAPACITY = 1;
	private static final int SAME_STRING = 0;
	private static final int MAX_SUBJECT_LENGTH = 200;
	//jsonDir is a path outside the project. Not used for now. I might use it later
    private static final String DEVIDER = "---------------------";
    /**
     * Compare two timestamps. Timestamp in java can be compared directly without
     * using a custom interface. Keep this here just as a reference if you need to
     * to do a customized caparison.
     */
    private static CompareTimestamp compare;

    /**
     * main
     *
     *Initialize program
     * @param args command line arguments
     * @throws IOException throws exception from functions
     */
	public static void main(String[] args) throws IOException {
		Boolean end = false;
		ArrayList<Room> rooms = new ArrayList<>();
		//logger.info("Importing rooms if they are available. We can use JSON files to persist the data" +
        //        "so we import any available rooms first.");
        while (!end) {
            switch (mainMenu()) {
                case 1:
                    System.out.println(addRoom(rooms));
                    break;
                case 2:
                    System.out.println(removeRoom(rooms));
                    break;
                case 3:
                    System.out.println(scheduleRoom(rooms));
                    break;
                case 4:
                    System.out.println(listSchedule(rooms));
                    break;
                case 5:
                    System.out.println(listRooms(rooms));
                    break;
                case 6:
                    System.out.println(exportRooms(rooms));
                    break;
                case 7:
                    System.out.println(importRooms(rooms));
                    break;
                case 8:
                    System.out.println(findRoomByDate(rooms));
                    break;
                default:
                    System.out.println("Wrong menu selection");
                    break;
            }

        }

	}

	/**
     * findRoomByDate
     *
     * Find all available rooms by specifying a date
     *
     * @TODO reuse variables
     */
	public static String findRoomByDate(ArrayList<Room> roomList)
    {
        if(isListEmpty(roomList)){
            logger.error("No rooms available");
            return "Error";
        }
        //Start Date
        System.out.println("Start Date? (yyyy-mm-dd):");
        String startDate = keyboard.next();
        String dateString = getDate(startDate);
        //Check if date is correct
        if(SAME_STRING == dateString.compareTo("false")){
            return "Error";
        }
        //Start Time
        System.out.println("Start Time? (HH:mm)");
        String startTime = keyboard.next();
        String timeString = getTime(startTime);
        //Check if time is correct
        if(SAME_STRING == timeString.compareTo("false")){
            return "Error";
        }
        //End Date
        System.out.println("End Date? (yyyy-mm-dd):");
        String endDate = keyboard.next();
        String dateStringEnd = getDate(endDate);
        if(SAME_STRING == dateStringEnd.compareTo("false")){
            return "Error";
        }
        //End Time
        System.out.println("End Time? (HH:mm)");
        String endTime = keyboard.next();
        String timeStringEnd = getTime(endTime);
        if(SAME_STRING == timeStringEnd.compareTo("false")){
            return "Error";
        }
        //Current meeting timestamps
        Timestamp startTimestamp = Timestamp.valueOf(dateString + " " + timeString + ":00");//we need seconds for timestamp
        Timestamp endTimestamp = Timestamp.valueOf(dateStringEnd + " " + timeStringEnd + ":00");//we need seconds for timestamp
        if(timeInThePast(startTimestamp,endTimestamp)){
            logger.error("Time in the past");
        }
        for (Room rm : roomList){
            ArrayList<Meeting> meetings = rm.getMeetings();
            int errorCounter = 0;
            if(!isListEmpty(meetings)) {
                for (Meeting me : meetings) {
                    //System.out.println(me.toString());
                    if (startTimestamp.after(me.getStartTime()) && startTimestamp.before(me.getStopTime())) {
                        //logger.error("Cannot Book, Time is in between schedule");
                        errorCounter++;
                    } else if (me.getStartTime().after(startTimestamp) && me.getStartTime().before(endTimestamp)) {
                        //logger.error("Cannot book, Time Conflict");
                        errorCounter++;
                    }
                }
                if(errorCounter > 0){
                    //logger.error("Room " + rm.getName() + " cannot be booked for the given date");
                }else{
                    logger.info("Room " + rm.getName() + " can be scheduled for the given date");
                }
            }else {
                    logger.info("Room " + rm.getName() + " can be scheduled for the given date");
            }
        }
        return "";
    }

	/**
     * listSchedule
     *
     * List the schedule for the room
     *
     * @param roomList a list of rooms
     * @return String
     * */
	protected static String listSchedule(ArrayList<Room> roomList) {
		String roomName = "";
        if(!isListEmpty(roomList))
        {
		    roomName = getRoomName();
            if(roomExists(roomName,roomList)) {
                System.out.println(roomName + " Schedule");
                System.out.println(DEVIDER);
                for (Meeting m : getRoomFromName(roomList, roomName).getMeetings()) {
			        System.out.println(m.toString());
		        }
		        return "";
            }else {
                logger.error("No rooms with the name " + roomName + " found");
                return "Error";
            }
        }
        logger.error("List of rooms is empty, no schedules available");
        return "Error";
	}

	/**
	 * mainMenu
     *
	 * Display the menu and user selection
	 *
	 * @return int
	 */
	protected static int mainMenu() {
		/**
		 * Make sure we have an int
		 * Display the menu until we get an integer
		 * Let the user know that they need to input an int
		 * Display the menu again
		 */
		int value = -1;
        while(value < 0){
            System.out.println("Main Menu:");
            System.out.println("  1 - Add a room");
            System.out.println("  2 - Remove a room");
            System.out.println("  3 - Schedule a room");
            System.out.println("  4 - List Schedule");
            System.out.println("  5 - List Rooms");
            System.out.println("  6 - Export Rooms");
            System.out.println("  7 - Import Rooms");
            System.out.println("  8 - Find Available Room  ");
            System.out.println("Enter your selection: ");
            if(keyboard.hasNextInt()) {
                value = keyboard.nextInt();
                keyboard.nextLine();
                break;
            }else
            {
                logger.error("Commands executed. System will exit!");
                System.exit(0);
            }
        }
		return value;//return what ever the value
	}

	/**
     * addRoom
     *
     * Adding the room for user.
     *
     * @param  roomList the list of rooms
     * @return String
     */
	protected static String addRoom(ArrayList<Room> roomList) {
        System.out.println("Add a room:");
		String name = getRoomName();
		System.out.println("Room capacity?");
        int capacity = getCapacity();
        if(capacity == 0){
            logger.error("Wrong room capacity");
            return "Error";
        }
        //keyboard.nextLine();//consume new line from previous
        String building = getBuildingName();
        String location = getRoomLocation();
        //If room exists delete it and update it with the new one
        if(roomExists(name,roomList)){
		    logger.error("Room " + name + " already exists. An update will be performed");
		    /**
             * @TODO: 2/24/2018  Ask user if they want to perform this step
             *
             * NOTE: This removes the room silently
             */
		    roomList.remove(findRoomIndex(roomList,name));
        }
		Room newRoom = new Room(name, capacity, building, location);
		roomList.add(newRoom);
		return "Room '" + newRoom.getName() + "' added successfully!";
	}

	/**
     * exportRooms
     *
     * Export rooms to json. Files.write operation uses StandartOpenOption CREATE, TRUNCATE_EXISTING
     * So the existing file will be replace with the new file
     *
     * @param roomList a list of rooms
     * @return String
     * @throws IOException cannot export json files
     */
	protected static String exportRooms(ArrayList<Room> roomList) throws IOException{
	    if(!isListEmpty(roomList)) {
            Gson gs = new Gson();
            ArrayList<String> list = new ArrayList<>();
            String jsonRoom, roomName = "";
            for (Room room : roomList) {
                jsonRoom = gs.toJson(room, Room.class);
                //logger.info("Room to be added: "+jsonRoom);
                list.add(jsonRoom);
            }
            for (String jsonString : list) {
                roomName = gs.fromJson(jsonString, Room.class).getName();
                try {
                    if(Files.notExists(Paths.get("jsonfiles"))){
                        Path dirPath = Files.createDirectories(Paths.get("jsonfiles"));
                        logger.info("Directory " + dirPath.toString() +" created");
                    }
                    //Passing bytes to the Files.write fixes the problem with iterable argument that we need here
                    Files.write(Paths.get("jsonfiles/" + roomName + ".json"), jsonString.getBytes());
                } catch (IOException e) {
                    logger.error("Cannot write to JSON file...", e);
                    return "Error";
                }
            }
            logger.info("Rooms exported successfully");
            return "";
        }
        logger.error("No rooms to export.");
	    return "";
    }

    /**
     * importRooms
     *
     * Import rooms from json
     *
     * @param roomList the list of rooms
     * @return String
     * @throws IOException cannot import json file/s
     */
    protected static String importRooms(ArrayList<Room> roomList) throws IOException{
        Gson gson = new Gson();
        //Path pathToDir = Paths.get(jsonDir);//path to dir of Json files //original @see jsonDir field comment
        Path pathToDir = Paths.get("jsonFiles");//"jsonFiles" is directory of all Json files
        JsonFiles jsonFiles = new JsonFiles();//callback object for walking directories and files
        Files.walkFileTree(pathToDir , jsonFiles);
        List<Path> allJsonFilesInPath = JsonFiles.getPaths();//get all paths from JsonFiles
        //check if we have rooms to import
        if(!isListEmpty(allJsonFilesInPath)) {
            for (Path pth : allJsonFilesInPath) {
                try (BufferedReader reader = Files.newBufferedReader(pth)) {
                    Room room = gson.fromJson(reader, Room.class);
                    if(roomExists(room.getName(),roomList)){
                        logger.info("Room " + room.getName() + " already exists. An update will be performed");
                        //Delete existing room, to be replaced by the import
                        roomList.remove(findRoomIndex(roomList,room.getName()));
                    }
                    roomList.add(room);
                } catch (IOException e) {
                    logger.error("Error " + e);
                    return "Error";
                }
            }
        }else {
            logger.error("No rooms were imported. Missing import files");
            return "Error";
        }
        return "Rooms Imported";
    }

	/**
     * removeRoom
     *
     * Removing room from the list
     *
     * @param roomList a list of rooms
     * @return String
     */
	protected static String removeRoom(ArrayList<Room> roomList) {
	    String roomName = "";
	    if(!isListEmpty(roomList)) {
            System.out.println("Remove a room:");
            roomName = getRoomName();
            if(roomExists(roomName,roomList)) {
                roomList.remove(findRoomIndex(roomList, roomName));
                return "Room removed successfully!";
            }
            logger.error("No rooms with the name " + roomName + " found");
            return "Error";
        }
        logger.error("List of rooms is empty, no rooms to remove");
        return "Error";
	}

	/**
     * listRooms
     *
     * @param roomList the list of rooms
     * @return String
     */
	private static String listRooms(ArrayList<Room> roomList) {
		System.out.println("Room Name - Capacity - Building - Location");
		System.out.println(DEVIDER);
		for (Room room : roomList) {
			System.out.println(
			        room.getName() + " - " +
                    room.getCapacity() + " - " +
                    room.getBuilding() + " - " +
                    room.getLocation());
		}

		System.out.println(DEVIDER);

		return roomList.size() + " Room(s)";
	}

	/**
     * scheduleRoom
     *
     * Adding room schedule to the list
     *
     * @param roomList the list of rooms
     * @return String
     **/
	private static String scheduleRoom(ArrayList<Room> roomList) {
        //Compare interface for comparing timestamps
        compare = (start,end)-> start.after(end);
        String subject = "";
        if(!isListEmpty(roomList)) {
            System.out.println("Schedule a room:");
            //Check if room exists
            String name = getRoomName();
            if(!roomExists(name,roomList)){
                logger.error("Room " + name + " does not exists");
                return "Error";
            }
            //Start Date
            System.out.println("Start Date? (yyyy-mm-dd):");
            String startDate = keyboard.next();
            String dateString = getDate(startDate);
            //Check if date is correct
            if(SAME_STRING == dateString.compareTo("false")){
                return "Error";
            }
            //Start Time
            System.out.println("Start Time? (HH:mm)");
            String startTime = keyboard.next();
            String timeString = getTime(startTime);
            //Check if time is correct
            if(SAME_STRING == timeString.compareTo("false")){
                return "Error";
            }
            //End Date
            System.out.println("End Date? (yyyy-mm-dd):");
            String endDate = keyboard.next();
            String dateStringEnd = getDate(endDate);
            if(SAME_STRING == dateStringEnd.compareTo("false")){
                return "Error";
            }
            //End Time
            System.out.println("End Time? (HH:mm)");
            String endTime = keyboard.next();
            String timeStringEnd = getTime(endTime);
            if(SAME_STRING == timeStringEnd.compareTo("false")){
                return "Error";
            }
            //Current meeting timestamps
            Timestamp startTimestamp = Timestamp.valueOf(dateString + " " + timeString + ":00");//we need seconds for timestamp
            Timestamp endTimestamp = Timestamp.valueOf(dateStringEnd + " " + timeStringEnd + ":00");//we need seconds for timestamp

            //Adding subject
            System.out.println("Subject?");
            keyboard.nextLine();//consume "\n" left from previous next or nextInt
            if(keyboard.hasNextLine()) {
                //subject = keyboard.nextLine();//There is a problem here cannot get scanner for subject, fix it
                subject = keyboard.nextLine();
                if(subject.length() > MAX_SUBJECT_LENGTH){
                    logger.error("No more than 200 characters for the subject allowed");
                    return "Error";
                }
            }
            Room curRoom = getRoomFromName(roomList, name);
            //Get all already set meetings
            //@// TODO: 1/30/2018 Check for time conflict with current meeting to be added
            ArrayList<Meeting> me = curRoom.getMeetings();
            //Check if time is in the past
            if(timeInThePast(startTimestamp,endTimestamp)){
                logger.error("Time in the past");
                return "Error";
            }
            if(!me.isEmpty()){
                String answer = checkMeetingConflict(me,startTimestamp,endTimestamp);
                if(answer == "conflict"){
                    return "Error " + answer;
                }
            }
            Meeting meeting = new Meeting(startTimestamp, endTimestamp, subject);
            curRoom.addMeeting(meeting);
            return "Successfully scheduled meeting!";
	    }
	    logger.error("Cannot schedule meeting. No rooms available");
	    return "Error";
	}

	/**
     * roomExists
     *
     * @param name name of the room
     * @param roomList list of rooms to search
     * @return boolean
     */
    private static boolean roomExists(String name,ArrayList<Room> roomList) {
        for (Room rm : roomList) {
            if( SAME_STRING == name.compareTo(rm.getName())){
                return true;
            }
        }
        return false;
    }

    /**
     * timeInThePast
     *
     * @param start start timestamp
     * @param end end timestamp
     *
     * @return boolean true if time in the past, false otherwise
     */
	private static Boolean timeInThePast(Timestamp start, Timestamp end){
	    //set the interface
	    compare = (first,second)-> first.after(second);
        //Check start time is not in the past
        if(!compare.compare(start,new Timestamp(System.currentTimeMillis()))){
            logger.error("Start time cannot be in the past");
            return true;
        }
        //Check end time is not in the past
        if(!compare.compare(end,new Timestamp(System.currentTimeMillis()))){
            logger.error("End time cannot be in the past");
            return true;
        }
        return false;
    }

    /**
     * checkMeetingConflict
     *
     * Check if there is a conflict with before adding a meeting
     *
     * @param meetings a list of meetings for a room
     * @param startTimestamp start time of the meeting
     * @param endTimestamp end time of the meeting
     * @return String
     */
    private static String checkMeetingConflict(ArrayList<Meeting> me, Timestamp startTimestamp, Timestamp endTimestamp){
        String answer = "";
        int errorCounter = 0;
        for (Meeting mt : me) {
            //System.out.println(me.toString());
            if (startTimestamp.after(mt.getStartTime()) && startTimestamp.before(mt.getStopTime())) {
                //System.out.println("Cannot Book, Time is in between schedule");
                logger.error("Cannot Book, Time is in between schedule");
                errorCounter++;
            }else if(mt.getStartTime().after(startTimestamp) && mt.getStartTime().before(endTimestamp)){
                //System.out.println("Cannot book, Time Conflict");
                logger.error("Cannot book, Time Conflict");
                errorCounter++;
            }else if(startTimestamp.equals(mt.getStartTime()) || endTimestamp.equals(mt.getStopTime())){
                logger.error("Start or End time same as the schedule");
                errorCounter++;
            }
        }
        if(errorCounter > 0){
            return "conflict";
        }
        return "noconflict";
        //for (Meeting mt : me) {
        //        //System.out.println("StartMeeting: " + meetingStart.getTime() + " EndMeeting: " + meetingEnd.getTime());
        //    if (endTimestamp.before(startTimestamp)) {
        //        //error
        //        System.out.println("Stop time cannot be before start time");
        //        answer = "conflict";
        //        break;
        //    }else if(startTimestamp.equals(mt.getStartTime()) || endTimestamp.equals(mt.getStopTime())){
        //        System.out.println("Start time or end time equal with existing meeting");
        //        answer = "conflict";
        //        break;
        //    }else if (startTimestamp.after(mt.getStartTime()) && startTimestamp.before(mt.getStopTime())) {
        //        //conflict
        //        System.out.println("Start time is in the middle of existing");
        //        answer="conflict";
        //        break;
        //    } else if (endTimestamp.after(mt.getStartTime()) && endTimestamp.before(mt.getStopTime())) {
        //        //conflict
        //        System.out.println("End time is in the middle of existing");
        //        answer="conflict";
        //    } else if (startTimestamp.before(mt.getStartTime()) && endTimestamp.after(mt.getStopTime())) {
        //        //conflict
        //        System.out.println("Start time is good but End time is bigger than existing stop time");
        //        answer="conflict";
        //    }else{
        //        System.out.println("No Conflict");
        //        answer= "noconflict";
        //        break;
        //    }
        //}
    }

	/**
     * getDate
     *
     * Get the date and check for error
     *
     * @param input date
     * @return String
     */
	public static String getDate(String input){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(input, formatter);
            return date.format(formatter);
        }
        catch (DateTimeParseException exc) {
            logger.error("String " + input + " is not parsable!" + exc);
            return "false";
        }
    }

    /**
     * getTime
     *
     * Get the time and check for error
     *
     * @param input time as string
     * @return String
     */
    public static String getTime(String input){
        try {
            LocalTime time = LocalTime.parse(input);
            //System.out.println(time.toString());
            return time.toString();
        }
        catch (DateTimeParseException exc) {
            logger.error("String " + input + " is not parsable!" + exc);
            return "false";
        }
    }

    /**
     * getTimeStamp
     *
     * Get the timeStamp and check for error
     *
     * @param input datetime formated as string
     * @return String
     *
     * @TODO 2/18/2018 Finish this
     */
    protected static String getTimeStamp(String input){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime date = LocalDateTime.parse(input, formatter);
            Timestamp ts = Timestamp.valueOf(date);
            return ts.toString();
        }
        catch (DateTimeParseException exc) {
            logger.error("String " + input + " is not parsable!");
            return "false";
        }
    }

	/**
     * getRoomFromName
     *
     * Get a specific room, searching by name
     *
     * @param roomList the list of rooms
     * @param  name name of the room
     * @return Room
     */
	protected static Room getRoomFromName(ArrayList<Room> roomList, String name) {
		return roomList.get(findRoomIndex(roomList, name));
	}

	/**
     * findRoomIndex
     *
     * Get specific room, searching by the index in the list
     *
     * @param roomList the list of rooms
     * @param roomName room name
     * @return int
     */
	protected static int findRoomIndex(ArrayList<Room> roomList, String roomName) {
		int roomIndex = 0;

		for (Room room : roomList) {
			if (room.getName().compareTo(roomName) == SAME_STRING) {
				break;
			}
			roomIndex++;
		}
		return roomIndex;
	}

	/**
     * getRoomName
     *
     * Get the room name
     *
     * @return String
     */
	protected static String getRoomName() {
		System.out.println("Room Name?");
		return keyboard.nextLine();
	}

	/**
     * getBuildingName
     *
     * Get the name of the building
     * @return String
     */
	protected static String getBuildingName(){
	    System.out.println("Building Name?");
		return keyboard.nextLine();
    }

    protected static int getCapacity(){
	    int capacity = 0;
        if(keyboard.hasNextInt()){
            capacity = keyboard.nextInt();
            keyboard.nextLine();//consume new line
            if(capacity < MINIMUM_ROOM_CAPACITY || capacity > Integer.MAX_VALUE){
                logger.error("Capacity should be a positive integer bigger than zero and not bigger than max capacity");
                return 0;
            }else{
                return capacity;
            }
        }
        keyboard.nextLine();//consume new line
        return capacity;
    }

    /**
     * getRoomLocation
     *
     * Get the location for the room
     * @return String
     */
	protected static String getRoomLocation(){
	    System.out.println("Location?");
		return keyboard.nextLine();
    }

	/**
     * checkForRoom
     *
     * This function checks if there is at least one room
     * in a list of rooms
     *
     * @param roomList A list of Rooms
     *
     * @return boolean true or false
     */
    private static Boolean isListEmpty(List list){
        if (list.isEmpty()){
            logger.info("List is empty");
            return true;
        }
        return false;
    }
}