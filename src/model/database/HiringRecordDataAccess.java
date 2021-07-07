package model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import model.HiringRecord;
import model.exception.DatabaseException;
import util.DateTime;

public class HiringRecordDataAccess {

	public static void insertHiringRecord(HiringRecord hiringRecord)
			throws SQLException, DatabaseException, ClassNotFoundException {
		try (Connection con = DatabaseAccess.getConnection();
				PreparedStatement pstmt = con.prepareStatement(QueryConstants.INSERT_HIRING_RECORD)) {
			setParametersForInsert(pstmt, hiringRecord);
			int result = pstmt.executeUpdate();
			if (result <= 0) {
				throw new DatabaseException("Error occurred while inserting into Database Table: hiring_record");
			}
		}
	}

	private static void setParametersForInsert(PreparedStatement pstmt, HiringRecord hiringRecord) throws SQLException {
		int paramIndex = 0;
		pstmt.setString(++paramIndex, hiringRecord.getRecordId());
		pstmt.setString(++paramIndex, hiringRecord.getRoomId());
		pstmt.setString(++paramIndex, hiringRecord.getRentDate().getFormattedDate());
		pstmt.setString(++paramIndex, hiringRecord.getEstimatedReturnDate().getFormattedDate());
		pstmt.setString(++paramIndex, hiringRecord.getActualReturnDate() != null
				? hiringRecord.getActualReturnDate().getFormattedDate() : null);
		if (hiringRecord.getRentalFee() != null) {
			pstmt.setFloat(++paramIndex, hiringRecord.getRentalFee());
		} else {
			pstmt.setNull(++paramIndex, Types.NUMERIC);
		}

		if (hiringRecord.getLateFee() != null) {
			pstmt.setFloat(++paramIndex, hiringRecord.getLateFee());
		} else {
			pstmt.setNull(++paramIndex, Types.NUMERIC);
		}
	}

	public static void insertHiringRecords(List<HiringRecord> hiringRecords)
			throws SQLException, DatabaseException, ClassNotFoundException {
		if (hiringRecords == null || hiringRecords.isEmpty()) {
			return;
		}
		try (Connection con = DatabaseAccess.getConnection();
				PreparedStatement pstmt = con.prepareStatement(QueryConstants.INSERT_HIRING_RECORD)) {

			for (HiringRecord hiringRecord : hiringRecords) {
				setParametersForInsert(pstmt, hiringRecord);
				pstmt.addBatch();
			}

			int results[] = pstmt.executeBatch();
			for (int result : results) {
				if (result <= 0) {
					throw new DatabaseException("Error occurred while inserting into Database Table: hiring_record");
				}
			}
		}
	}

	public static void updateHiringRecord(HiringRecord hiringRecord)
			throws SQLException, DatabaseException, ClassNotFoundException {
		try (Connection con = DatabaseAccess.getConnection();
				PreparedStatement pstmt = con.prepareStatement(QueryConstants.UPDATE_HIRING_RECORD)) {
			setParametersForUpdate(pstmt, hiringRecord);
			int result = pstmt.executeUpdate();
			if (result <= 0) {
				throw new DatabaseException("Error occurred while updating Database Table: hiring_record");
			}
		}
	}

	private static void setParametersForUpdate(PreparedStatement pstmt, HiringRecord hiringRecord) throws SQLException {
		int paramIndex = 0;
		pstmt.setString(++paramIndex, hiringRecord.getRentDate().getFormattedDate());
		pstmt.setString(++paramIndex, hiringRecord.getEstimatedReturnDate().getFormattedDate());
		pstmt.setString(++paramIndex, hiringRecord.getActualReturnDate().getFormattedDate());
		pstmt.setFloat(++paramIndex, hiringRecord.getRentalFee());
		pstmt.setFloat(++paramIndex, hiringRecord.getLateFee());

		pstmt.setString(++paramIndex, hiringRecord.getRecordId());

	}

	public static void updateHiringRecords(List<HiringRecord> hiringRecords)
			throws SQLException, DatabaseException, ClassNotFoundException {

		try (Connection con = DatabaseAccess.getConnection();
				PreparedStatement pstmt = con.prepareStatement(QueryConstants.UPDATE_HIRING_RECORD)) {

			for (HiringRecord hiringRecord : hiringRecords) {
				setParametersForUpdate(pstmt, hiringRecord);
				pstmt.addBatch();
			}

			int results[] = pstmt.executeBatch();
			for (int result : results) {
				if (result <= 0) {
					throw new DatabaseException("Error occurred while inserting into Database Table: hiring_record");
				}
			}
		}
	}

	public static List<HiringRecord> getHiringRecordsForRoom(String roomId)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<HiringRecord> hiringRecords = new ArrayList<HiringRecord>();
		try (Connection con = DatabaseAccess.getConnection();
				PreparedStatement pstmt = con.prepareStatement(QueryConstants.SELECT_HIRING_RECORDS_FOR_ROOM);) {

			pstmt.setString(1, roomId);

			try (ResultSet rs = pstmt.executeQuery();) {
				while (rs.next()) {
					float rentalFee = rs.getFloat("rental_fee");
					float lateFee = rs.getFloat("late_fee");

					HiringRecord hr = new HiringRecord(rs.getString("id"), rs.getString("room_id"),
							DateTime.parseFormattedDate(rs.getString("rent_date")),
							DateTime.parseFormattedDate(rs.getString("estimated_return_date")),
							DateTime.parseFormattedDate(rs.getString("actual_return_date")),
							(rentalFee != 0) ? rentalFee : null, (lateFee != 0) ? lateFee : null);

					hiringRecords.add(hr);
				}
			}
		}
		return hiringRecords;
	}

}
