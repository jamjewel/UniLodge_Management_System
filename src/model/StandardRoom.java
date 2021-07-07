package model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.exception.InvalidInputException;
import model.exception.RentException;
import model.exception.ReturnException;
import util.DateTime;

public class StandardRoom extends Room {

	public StandardRoom(String roomId, int noOfBeds, String featureSummary) throws InvalidInputException {
		super(roomId, noOfBeds, featureSummary);

		if (!(noOfBeds == 1 || noOfBeds == 2 || noOfBeds == 4)) {
			throw new InvalidInputException("Number of Beds for Standard Room should be either 1, 2 or 4");
		}

		this.roomType = RoomType.Standard;
		this.roomImageName = "standard_room_" + roomId + ".jpg";

		super.addRoomToDatabase();
	}

	public StandardRoom(String roomId, int noOfBeds, String featureSummary, RoomStatus roomStatus, String roomImageName,
			List<HiringRecord> hiringRecords) {
		super(roomId, noOfBeds, featureSummary, roomStatus, RoomType.Standard, roomImageName, hiringRecords);
	}

	@Override
	public void rent(String customerId, DateTime rentDate, int numOfRentDay) throws RentException {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(rentDate.getTime()));
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeek >= 2 && dayOfWeek <= 6 && numOfRentDay < 2) {
			// Monday to Friday and Rent Days is less than 2
			throw new RentException("Number of Rent Days should be atleast 2 if rented between Mon and Fri");

		} else if ((dayOfWeek == 1 || dayOfWeek == 7) && numOfRentDay < 3) {
			// Saturday/Sunday and Rent Days is less than 3
			throw new RentException("Number of Rent Days should be atleast 3 if rented on Saturday or Sunday");
		}

		if (numOfRentDay > 10) {
			// Rent Days is more than 10
			throw new RentException("Number of Rent Days cannot be more than 10 days");
		}

		// After All StandardRoom specific Validations are passed, call the
		// common rent() method to perform the steps required for renting the
		// room
		super.rent(customerId, rentDate, numOfRentDay);
	}

	@Override
	public void returnRoom(DateTime returnDate) throws ReturnException {
		// As there is no specific validation that is to be done for the
		// StandardRoom while returning the room, we are directly calling the
		// returnRoom() method in the super class Room
		super.returnRoom(returnDate);
	}

	@Override
	protected float getRentalRate() {
		switch (this.noOfBeds) {
		case 1:
			return 10f;
		case 2:
			return 20f;
		case 4:
			return 40f;
		}
		System.out.println("Invalid Number of Beds for the Standard Room [NumberOfBeds = " + this.noOfBeds + "]");
		return 0;
	}

	@Override
	protected float getLateFeeRate() {
		return 1.35f * getRentalRate();
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

		sb.append(super.getRentalRecordsDetails());

		return sb.toString();
	}
}
