package model;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

import model.database.DatabaseAccess;
import model.database.RoomDataAccess;
import model.exception.DatabaseException;
import model.exception.InvalidIdException;
import model.exception.InvalidInputException;
import util.FxUtils;

public class CityLodge {

	private static CityLodge _instance = null;
	// Using a HashMap for room, so that it will be easier to find the
	// Duplicates in RoomId while adding new Room
	// RoomId is the Key for the HashMap
	private HashMap<String, Room> rooms;

	public HashMap<String, Room> getRooms() {
		return rooms;
	}

	private CityLodge(HashMap<String, Room> rooms) {
		this.rooms = rooms;
	}

	public static CityLodge getInstance() {
		if (_instance == null) {

			try {
				DatabaseAccess.createTables();
			} catch (ClassNotFoundException | SQLException | DatabaseException e1) {
				// Tables already exists
				System.out.println("Tables already exists");
			}

			try {
				_instance = new CityLodge(RoomDataAccess.getAllRooms());
			} catch (ClassNotFoundException | SQLException | DatabaseException | ParseException e) {
				// If some error happens, run the application with empty rooms
				FxUtils.showErrorAlert(e.getLocalizedMessage(),
						"Application will continue, but rooms might not retrieved!");
				_instance = new CityLodge(new HashMap<String, Room>());
			}
		}
		return _instance;
	}

	public void performAddStandardRoom(String roomId, int noOfBeds, String featureSummary)
			throws InvalidInputException, InvalidIdException {

		if (!roomId.startsWith("R_")) {
			throw new InvalidIdException("Room ID should start with R_ for Standard Rooms");
		}

		if (rooms.containsKey(roomId)) {
			throw new InvalidIdException("This Room ID already exists, try with a different one!");
		}

		Room newRoom = new StandardRoom(roomId, noOfBeds, featureSummary);
		// Add the new Room to our Collection
		rooms.put(roomId, newRoom);
	}

	public void performAddSuiteRoom(String roomId, String featureSummary)
			throws InvalidInputException, InvalidIdException {
		if (!roomId.startsWith("S_")) {
			throw new InvalidIdException("Room ID should start with S_ for Suite Rooms");
		}

		if (rooms.containsKey(roomId)) {
			throw new InvalidIdException("This Room ID already exists, try with a different one!");
		}
		Room newRoom = new SuiteRoom(roomId, featureSummary);
		// Add the new Room to our Collection
		rooms.put(roomId, newRoom);
	}
}
