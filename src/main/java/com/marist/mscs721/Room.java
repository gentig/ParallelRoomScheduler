package com.marist.mscs721;

import com.sun.javafx.beans.IDProperty;

import java.util.ArrayList;
import java.util.UUID;

public class Room {
    /**
     * Instance fields
     */
	private String ID;
	private String name;
	private int capacity;
	private ArrayList<Meeting> meetings;
	
	/**
     * Constructor
     */
	public Room(String newName, int newCapacity) {
		setName(newName);
		setCapacity(newCapacity);
		setMeetings(new ArrayList<Meeting>());
		this.ID = UUID.randomUUID().toString();
	}

	/**
     * addMeeting
     *
     * Adds new meeting to the room instance
     *
     * @param newMeeting meeting for this room
     */
	public void addMeeting(Meeting newMeeting) {
		this.getMeetings().add(newMeeting);
	}

	/**
     * getName
     *
     * Gets and return the name of the room as string
     *
     * @return String
     */
	public String getName() {
		return name;
	}

	/**
     * setName
     *
     * Sets the name of the room instance
     *
     * @param name name for the room
     */
	public void setName(String name) {
		this.name = name;
	}

	/**
     * getCapacity
     *
     * Gets and return the capacity of the room
     *
     * @return int
     */
	public int getCapacity() {
		return capacity;
	}

	/**
     * setCapacity
     *
     * Sets the capacity of the room instance
     *
     * @param capacity capacity for the room
     */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

    /**
     * getMeetings
     *
     * Gets and returns the meetings for the room
     *
     * @return List
     */
	public ArrayList<Meeting> getMeetings() {
		return meetings;
	}


	/**
     * setMeetings
     *
     * Set meetings for room instance
     *
     * @param meetings meetings to be added
     */
	public void setMeetings(ArrayList<Meeting> meetings) {
		this.meetings = meetings;
	}

	/**
     * getID
     *
     * Gets and returns the unique ID for the room instance
     *
     * @return String
     */
	public String getID(){
		return ID;
	}
}
