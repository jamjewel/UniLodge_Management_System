package model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.exception.InvalidInputException;
import model.exception.MaintenanceException;
import model.exception.RentException;
import model.exception.ReturnException;
import util.DateTime;

public class SuiteRoom extends Room {

	private DateTime lastMaintenanceDate;

	public SuiteRoom(String roomId, String featureSummary) throws InvalidInputException {
		// A Suite always has 6 beds
		super(roomId, 6, featureSummary);
		this.roomType = RoomType.Suite;
		Calendar c = Calendar.getInstance();
		// Set Current Date
		c.setTime(new Date());
		// Convert to our own DateTime format
		this.lastMaintenanceDate = new DateTime(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1,
				c.get(Calendar.YEAR));

		this.roomImageName = "suite_room_" + roomId + ".jpg";

		super.addRoomToDatabase();
	}

	public SuiteRoom(String roomId, int noOfBeds, String featureSummary, RoomStatus roomStatus, String roomImageName,
			List<HiringRecord> hiringRecords, DateTime lastMaintenanceDate) {
		super(roomId, noOfBeds, featureSummary, roomStatus, RoomType.Suite, roomImageName, hiringRecords);
		this.lastMaintenanceDate = lastMaintenanceDate;
	}

	public DateTime getLastMaintenanceDate() {
		return lastMaintenanceDate;
	}

	@Override
	public void rent(String customerId, DateTime rentDate, int numOfRentDay) throws RentException {

		// Suite Rooms has strict maintenance schedule
		if (DateTime.diffDays(rentDate, lastMaintenanceDate) + numOfRentDay > 10) {
			throw new RentException(
					"Cannot Rent this room as the time period exceeds the date on which maintenance operation must be done");
		}

		// After all Validations are passed, rent the room
		super.rent(customerId, rentDate, numOfRentDay);

	}

	@Override
	public void returnRoom(DateTime returnDate) throws ReturnException {
		// As there is no specific validation that is to be done for the
		// SuiteRoom while returning the room, we are directly calling the
		// returnRoom() method in the super class Room
		super.returnRoom(returnDate);
	}

	@Override
	protected float getRentalRate() {
		return 99f;
	}

	@Override
	protected float getLateFeeRate() {
		return 109f;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(roomId);
		sb.append(":");

		sb.append(noOfBeds);
		sb.append(":");

		sb.append(roomType);
		sb.append(":");

		sb.append(roomStatus);
		sb.append(":");

		sb.append(lastMaintenanceDate);
		sb.append(":");

		sb.append(featureSummary);
		sb.append(":");

		sb.append(roomImageName);

		return sb.toString();
	}

	@Override
	public String getDetails() {
		StringBuilder sb = new StringBuilder();

		// Reusing the super class method for the common Details
		sb.append(super.getDetails());

		// Last maintenance date is specific to SuiteRoom
		sb.append("Last maintenance date:");
		sb.append(lastMaintenanceDate);
		sb.append("\n");

		sb.append(super.getRentalRecordsDetails());

		return sb.toString();
	}

	@Override
	public void performMaintenance() throws MaintenanceException {
		super.performMaintenance();
	}

	@Override
	public void completeMaintenance(DateTime completionDate) throws MaintenanceException {
		super.completeMaintenance(completionDate);
		// If maintenance is completed successfully (if no exception was
		// thrown), store the completionDate as the lastMaintenanceDate
		this.lastMaintenanceDate = completionDate;
	}
}
