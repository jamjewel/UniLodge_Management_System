package model;

import util.DateTime;

public class HiringRecord {
	private String recordId;
	private String roomId;
	private DateTime rentDate;
	private DateTime estimatedReturnDate;
	private DateTime actualReturnDate;
	private Float rentalFee;
	private Float lateFee;

	public HiringRecord(String recordId, String roomId, DateTime rentDate, DateTime estimatedReturnDate) {
		this.recordId = recordId;
		this.roomId = roomId;
		this.rentDate = rentDate;
		this.estimatedReturnDate = estimatedReturnDate;
	}

	public HiringRecord(String recordId, String roomId, DateTime rentDate, DateTime estimatedReturnDate,
			DateTime actualReturnDate, Float rentalFee, Float lateFee) {
		super();
		this.recordId = recordId;
		this.roomId = roomId;
		this.rentDate = rentDate;
		this.estimatedReturnDate = estimatedReturnDate;
		this.actualReturnDate = actualReturnDate;
		this.rentalFee = rentalFee;
		this.lateFee = lateFee;
	}

	public void setActualReturnDate(DateTime actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	public void setRentalFee(Float rentalFee) {
		this.rentalFee = rentalFee;
	}

	public void setLateFee(Float lateFee) {
		this.lateFee = lateFee;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getRoomId() {
		return roomId;
	}

	public DateTime getRentDate() {
		return rentDate;
	}

	public DateTime getEstimatedReturnDate() {
		return estimatedReturnDate;
	}

	public DateTime getActualReturnDate() {
		return actualReturnDate;
	}

	public Float getRentalFee() {
		return rentalFee;
	}

	public Float getLateFee() {
		return lateFee;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(recordId);
		sb.append(":");

		sb.append(roomId);
		sb.append(":");

		sb.append(rentDate);
		sb.append(":");

		sb.append(estimatedReturnDate);
		sb.append(":");

		sb.append(actualReturnDate != null ? actualReturnDate : "none");
		sb.append(":");

		sb.append(rentalFee != null ? rentalFee : "none");
		sb.append(":");

		sb.append(lateFee != null ? lateFee : "none");

		return sb.toString();
	}

	public String getDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append("Record ID:");
		sb.append(recordId);
		sb.append("\n");

		sb.append("Rent Date:");
		sb.append(rentDate);
		sb.append("\n");

		sb.append("Estimated Return Date:");
		sb.append(estimatedReturnDate);
		sb.append("\n");

		if (actualReturnDate != null) {
			sb.append("Actual Return Date:");
			sb.append(actualReturnDate);
			sb.append("\n");
		}

		if (rentalFee != null) {
			sb.append("Rental Fee:");
			sb.append(String.format("%.2f", rentalFee));
			sb.append("\n");
		}

		if (lateFee != null) {
			sb.append("Late Fee:");
			sb.append(String.format("%.2f", lateFee));
		}

		return sb.toString();
	}
}
