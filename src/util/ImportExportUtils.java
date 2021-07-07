package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import model.HiringRecord;
import model.Room;
import model.RoomStatus;
import model.RoomType;
import model.StandardRoom;
import model.SuiteRoom;
import model.exception.ImportException;

public class ImportExportUtils {
	public static void exportToFile(HashMap<String, Room> rooms, String path) throws FileNotFoundException {

		PrintWriter writer = new PrintWriter(path);

		for (String roomId : rooms.keySet()) {
			Room room = rooms.get(roomId);
			writer.println("!" + room.toString());
			int x = 0;
			for (int i = room.getHiringRecords().size() - 1; i >= 0; i--) {
				if (x == 10) {
					break;
				}
				writer.println(room.getHiringRecords().get(i).toString());
				x++;
			}
		}
		writer.close();
	}

	public static HashMap<String, Room> importFromFile(String path)
			throws FileNotFoundException, ParseException, ImportException {

		HashMap<String, Room> rooms = new HashMap<>();
		try (Scanner fileScanner = new Scanner(new FileInputStream(path));) {
			if (!fileScanner.hasNextLine()) {
				throw new ImportException("Selected File is empty");
			}
			String line = fileScanner.nextLine();
			if (!line.startsWith("!")) {
				throw new ImportException("Selected File is not in Expected Format");
			}
			while (true) {
				// If starts with !, then Room Record - Remove ! and parse
				line = line.substring(1);
				Room r = parseRoom(line);
				rooms.put(r.getRoomId(), r);
				if (!fileScanner.hasNextLine()) {
					break;
				}
				while (fileScanner.hasNextLine()) {
					line = fileScanner.nextLine();
					if (line.startsWith("!")) {
						break;
					}
					// If not starts with !, then HiringRecord - Adding the
					// Hiring Records in Reverse Order as it is saved in Reverse
					// Order in the file during export
					r.getHiringRecords().add(0, parseHiringRecord(line));

					if (!fileScanner.hasNextLine()) {
						return rooms;
					}
				}
			}
		}
		return rooms;
	}

	private static Room parseRoom(String line) throws ImportException, ParseException {
		String[] tokens = line.split(":");
		if (tokens.length != 6 && tokens.length != 7) {
			throw new ImportException("Room Record is not in Expected Format");
		}

		int i = 0;

		String roomId = tokens[i++];
		int noOfBeds = Integer.parseInt(tokens[i++]);
		RoomType roomType = RoomType.valueOf(tokens[i++]);
		RoomStatus roomStatus = RoomStatus.valueOf(tokens[i++]);
		DateTime lastMaintenanceDate = roomType == RoomType.Suite ? DateTime.parseFormattedDate(tokens[i++]) : null;
		String featureSummary = tokens[i++];
		String imageName = tokens[i++];

		Room r = null;
		if (roomType == RoomType.Standard) {
			r = new StandardRoom(roomId, noOfBeds, featureSummary, roomStatus, imageName,
					new ArrayList<HiringRecord>());
		} else if (roomType == RoomType.Suite) {
			r = new SuiteRoom(roomId, noOfBeds, featureSummary, roomStatus, imageName, new ArrayList<HiringRecord>(),
					lastMaintenanceDate);
		}
		return r;
	}

	private static HiringRecord parseHiringRecord(String line) throws ParseException, ImportException {

		String[] tokens = line.split(":");

		if (tokens.length != 7) {
			// Invalid Rental Record
			throw new ImportException("Hiring Record is not in Expected Format");
		}

		int i = -1;
		String recordId = tokens[++i];
		String roomId = tokens[++i];
		DateTime rentDate = DateTime.parseFormattedDate(tokens[++i]);
		DateTime estimatedReturnDate = !tokens[++i].equals("none") ? DateTime.parseFormattedDate(tokens[i]) : null;
		DateTime actualReturnDate = !tokens[++i].equals("none") ? DateTime.parseFormattedDate(tokens[i]) : null;

		Float rentalFee = !tokens[++i].equals("none") ? Float.parseFloat(tokens[i]) : null;
		Float lateFee = !tokens[++i].equals("none") ? Float.parseFloat(tokens[i]) : null;
		return new HiringRecord(recordId, roomId, rentDate, estimatedReturnDate, actualReturnDate, rentalFee, lateFee);
	}
}
