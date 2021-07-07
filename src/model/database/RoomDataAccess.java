package model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.HiringRecord;
import model.Room;
import model.RoomStatus;
import model.RoomType;
import model.StandardRoom;
import model.SuiteRoom;
import model.exception.DatabaseException;
import util.DateTime;

public class RoomDataAccess {

	public static void insertRoom(Room room) throws SQLException, DatabaseException, ClassNotFoundException {
		try (Connection con = DatabaseAccess.getConnection();
				PreparedStatement pstmt = con.prepareStatement(QueryConstants.INSERT_ROOM)) {
			setParametersForInsert(pstmt, room);
			int result = pstmt.executeUpdate();

			if (result <= 0) {
				throw new DatabaseException("Error occurred while inserting into Database Table: room");
			}

			// After Successful insert of Room, insert the HiringRecords
			HiringRecordDataAccess.insertHiringRecords(room.getHiringRecords());
		}
	}

	private static void setParametersForInsert(PreparedStatement pstmt, Room room) throws SQLException {
		int paramIndex = 0;
		pstmt.setString(++paramIndex, room.getRoomId());
		pstmt.setString(++paramIndex, room.getRoomType().toString());
		pstmt.setInt(++paramIndex, room.getNoOfBeds());
		pstmt.setString(++paramIndex, room.getFeatureSummary());
		pstmt.setString(++paramIndex, room.getRoomStatus().toString());
		if (room.getRoomType() == RoomType.Suite) {
			pstmt.setString(++paramIndex, ((SuiteRoom) room).getLastMaintenanceDate().getFormattedDate());
		} else {
			pstmt.setString(++paramIndex, null);
		}
		pstmt.setString(++paramIndex, room.getRoomImageName());
	}

	public static void insertRooms(HashMap<String, Room> rooms)
			throws SQLException, DatabaseException, ClassNotFoundException {
		if (rooms == null || rooms.isEmpty()) {
			return;
		}
		try (Connection con = DatabaseAccess.getConnection();
				PreparedStatement pstmt = con.prepareStatement(QueryConstants.INSERT_ROOM)) {
			List<HiringRecord> allHiringRecords = new ArrayList<>();
			for (String roomId : rooms.keySet()) {
				Room room = rooms.get(roomId);
				setParametersForInsert(pstmt, room);
				pstmt.addBatch();
				// Adding the HiringRecords of this room to the allHiringRecords
				// list, so that if all rooms are inserted successfully, all
				// hiring records can be inserted (hiring records cannot be
				// inserted before the rooms because of Foreign key constraint)
				allHiringRecords.addAll(room.getHiringRecords());
			}

			// Execute the batch - This is where rooms are inserted into the DB
			int results[] = pstmt.executeBatch();
			for (int result : results) {
				if (result <= 0) {
					throw new DatabaseException("Error occurred while inserting into Database Table: room");
				}
			}

			// After Successful insert of all Rooms, insert the HiringRecords of
			// all Rooms
			HiringRecordDataAccess.insertHiringRecords(allHiringRecords);
		}
	}

	public static void updateRoom(Room room) throws SQLException, DatabaseException, ClassNotFoundException {
		try (Connection con = DatabaseAccess.getConnection();
				PreparedStatement pstmt = con.prepareStatement(QueryConstants.UPDATE_ROOM)) {
			setParametersForUpdate(pstmt, room);
			int result = pstmt.executeUpdate();

			if (result <= 0) {
				throw new DatabaseException("Error occurred while updating in Database Table: room");
			}
		}
	}

	private static void setParametersForUpdate(PreparedStatement pstmt, Room room) throws SQLException {
		int paramIndex = 0;
		pstmt.setString(++paramIndex, room.getRoomType().toString());
		pstmt.setInt(++paramIndex, room.getNoOfBeds());
		pstmt.setString(++paramIndex, room.getFeatureSummary());
		pstmt.setString(++paramIndex, room.getRoomStatus().toString());
		if (room.getRoomType() == RoomType.Suite) {
			pstmt.setString(++paramIndex, ((SuiteRoom) room).getLastMaintenanceDate().getFormattedDate());
		} else {
			pstmt.setString(++paramIndex, null);
		}
		pstmt.setString(++paramIndex, room.getRoomImageName());

		pstmt.setString(++paramIndex, room.getRoomId());

	}

	public static void updateRooms(List<Room> rooms) throws SQLException, DatabaseException, ClassNotFoundException {
		try (Connection con = DatabaseAccess.getConnection();
				PreparedStatement pstmt = con.prepareStatement(QueryConstants.UPDATE_ROOM)) {
			for (Room room : rooms) {
				setParametersForUpdate(pstmt, room);
				pstmt.addBatch();
			}

			// Execute the batch - This is where rooms are updated in the DB
			int results[] = pstmt.executeBatch();
			for (int result : results) {
				if (result <= 0) {
					throw new DatabaseException("Error occurred while updating in Database Table: room");
				}
			}
		}
	}

	public static HashMap<String, Room> getAllRooms()
			throws SQLException, DatabaseException, ClassNotFoundException, ParseException {
		HashMap<String, Room> rooms = new HashMap<String, Room>();
		try (Connection con = DatabaseAccess.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(QueryConstants.SELECT_ALL_ROOMS);) {
			while (rs.next()) {
				Room r = null;
				String roomId = rs.getString("id");
				RoomType roomType = RoomType.valueOf(rs.getString("type"));
				List<HiringRecord> hiringRecords = HiringRecordDataAccess.getHiringRecordsForRoom(roomId);
				if (roomType == RoomType.Suite) {
					r = new SuiteRoom(roomId, rs.getInt("no_of_beds"), rs.getString("feature_summary"),
							RoomStatus.valueOf(rs.getString("status")), rs.getString("image_name"), hiringRecords,
							DateTime.parseFormattedDate(rs.getString("last_maintenance_date")));
				} else if (roomType == RoomType.Standard) {
					r = new StandardRoom(roomId, rs.getInt("no_of_beds"), rs.getString("feature_summary"),
							RoomStatus.valueOf(rs.getString("status")), rs.getString("image_name"), hiringRecords);
				}
				if (r != null) {
					rooms.put(roomId, r);
				}
			}
		}

		return rooms;
	}

}
