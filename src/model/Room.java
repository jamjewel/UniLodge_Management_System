package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import model.database.HiringRecordDataAccess;
import model.database.RoomDataAccess;
import model.exception.DatabaseException;
import model.exception.InvalidInputException;
import model.exception.MaintenanceException;
import model.exception.RentException;
import model.exception.ReturnException;
import util.DateTime;
import util.FxUtils;

public abstract class Room {
	protected String roomId;
	protected int noOfBeds;
	protected String featureSummary;
	protected RoomStatus roomStatus;
	protected RoomType roomType;
	protected String roomImageName;
	protected List<HiringRecord> hiringRecords;

	public Room(String roomId, int noOfBeds, String featureSummary) throws InvalidInputException {
		if (roomId == null || roomId.isEmpty()) {
			throw new InvalidInputException("Room ID cannot be empty");
		}
		if (roomId.contains(":") || featureSummary.contains(":")) {
			throw new InvalidInputException("Room ID or Feature Summary cannot contain colon (:)");
		}
		this.roomId = roomId;
		this.noOfBeds = noOfBeds;
		this.featureSummary = featureSummary;
		this.roomStatus = RoomStatus.Available;
		this.hiringRecords = new ArrayList<HiringRecord>();
	}

	public Room(String roomId, int noOfBeds, String featureSummary, RoomStatus roomStatus, RoomType roomType,
			String roomImageName, List<HiringRecord> hiringRecords) {
		this.roomId = roomId;
		this.noOfBeds = noOfBeds;
		this.featureSummary = featureSummary;
		this.roomStatus = roomStatus;
		this.roomType = roomType;
		this.roomImageName = roomImageName;
		this.hiringRecords = hiringRecords;
		if (this.hiringRecords == null) {
			this.hiringRecords = new ArrayList<HiringRecord>();
		}
	}

	public String getRoomId() {
		return roomId;
	}

	public int getNoOfBeds() {
		return noOfBeds;
	}

	public String getFeatureSummary() {
		return featureSummary;
	}

	public RoomStatus getRoomStatus() {
		return roomStatus;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public String getRoomImageName() {
		return roomImageName;
	}

	public List<HiringRecord> getHiringRecords() {
		return hiringRecords;
	}

	public void rent(String customerId, DateTime rentDate, int numOfRentDay) throws RentException {

		// Common Validations
		if (this.roomStatus != RoomStatus.Available) {
			throw new RentException("This room is currently not available. The room status is " + this.roomStatus);
		}

		// Successfully Rented
		DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDay);
		String hiringRecordId = this.roomId + "_" + customerId + "_" + rentDate.getEightDigitDate();
		HiringRecord h = new HiringRecord(hiringRecordId, this.roomId, rentDate, estimatedReturnDate);
		this.hiringRecords.add(h);

		this.roomStatus = RoomStatus.Rented;

		this.updateRoomInDatabase();
		this.insertHiringRecordInDatabase(h);
	}

	public void returnRoom(DateTime returnDate) throws ReturnException {
		if (this.roomStatus != RoomStatus.Rented) {
			throw new ReturnException("This room is not currently Rented");
		}

		HiringRecord currentHiringRecord = this.hiringRecords.get(this.hiringRecords.size() - 1);

		int actualDays = DateTime.diffDays(returnDate, currentHiringRecord.getRentDate());
		if (actualDays <= 0) {
			throw new ReturnException("Return Date should be after the Rent Date");
		}

		int plannedDays = DateTime.diffDays(currentHiringRecord.getEstimatedReturnDate(),
				currentHiringRecord.getRentDate());

		float rentalFee;
		float lateFee;
		if (actualDays <= plannedDays) {
			// Late Fee is not applicable
			rentalFee = actualDays * getRentalRate();
			lateFee = 0f;
		} else {
			// Late Fee is applicable
			rentalFee = plannedDays * getRentalRate();
			lateFee = (actualDays - plannedDays) * getLateFeeRate();
		}

		currentHiringRecord.setActualReturnDate(returnDate);
		currentHiringRecord.setRentalFee(rentalFee);
		currentHiringRecord.setLateFee(lateFee);

		this.roomStatus = RoomStatus.Available;

		this.updateRoomInDatabase();
		this.updateHiringRecordInDatabase(currentHiringRecord);
	}

	public void performMaintenance() throws MaintenanceException {

		if (this.roomStatus != RoomStatus.Available) {
			throw new MaintenanceException(
					"Maintenance cannot be performed in this room. The room status is " + this.roomStatus);
		}

		// After the validations are passed, set the roomStatus to Maintenance
		this.roomStatus = RoomStatus.Maintenance;

		this.updateRoomInDatabase();
	}

	public void completeMaintenance(DateTime completionDate) throws MaintenanceException {
		if (this.roomStatus != RoomStatus.Maintenance) {
			throw new MaintenanceException(
					"This room is currently not under maintenance. The room status is " + this.roomStatus);
		}

		// After the validations are passed, set the roomStatus to Maintenance
		this.roomStatus = RoomStatus.Available;

		this.updateRoomInDatabase();
	}

	public String getDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append("Room ID:");
		sb.append(roomId);
		sb.append("\n");

		sb.append("Number of beds:");
		sb.append(noOfBeds);
		sb.append("\n");

		sb.append("Type:");
		sb.append(roomType);
		sb.append("\n");

		sb.append("Status:");
		sb.append(roomStatus);
		sb.append("\n");

		sb.append("Feature summary:");
		sb.append(featureSummary);
		sb.append("\n");

		return sb.toString();
	}

	public String getRentalRecordsDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append("RENTAL RECORD:");

		if (!hiringRecords.isEmpty()) {
			sb.append("\n");
			int count = 0;
			ListIterator<HiringRecord> li = hiringRecords.listIterator(hiringRecords.size());
			while (li.hasPrevious() && count < 10) {
				// Print only 10 recent HiringRecords
				count++;
				sb.append(li.previous().getDetails());
				if (li.hasPrevious()) {
					sb.append("\n--------------------------------------------------\n");
				}
			}
		} else {
			sb.append("empty");
		}
		return sb.toString();
	}

	protected abstract float getRentalRate();

	protected abstract float getLateFeeRate();

	protected void addRoomToDatabase() {
		try {
			RoomDataAccess.insertRoom(this);
		} catch (ClassNotFoundException | SQLException | DatabaseException e) {
			FxUtils.showErrorAlert(e.getLocalizedMessage(), "Application will continue, but Rooms might not be saved!");
		}
	}

	protected void updateRoomInDatabase() {
		try {
			RoomDataAccess.updateRoom(this);
		} catch (ClassNotFoundException | SQLException | DatabaseException e) {
			FxUtils.showErrorAlert(e.getLocalizedMessage(), "Application will continue, but Rooms might not be saved!");
		}
	}

	private void insertHiringRecordInDatabase(HiringRecord hiringRecord) {
		try {
			HiringRecordDataAccess.insertHiringRecord(hiringRecord);
		} catch (ClassNotFoundException | SQLException | DatabaseException e) {
			FxUtils.showErrorAlert(e.getLocalizedMessage(), "Application will continue, but Rooms might not be saved!");
		}
	}

	private void updateHiringRecordInDatabase(HiringRecord hiringRecord) {
		try {
			HiringRecordDataAccess.updateHiringRecord(hiringRecord);
		} catch (ClassNotFoundException | SQLException | DatabaseException e) {
			FxUtils.showErrorAlert(e.getLocalizedMessage(), "Application will continue, but Rooms might not be saved!");
		}
	}
}
