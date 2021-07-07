package model.database;

public class QueryConstants {
	public static final String CREATE_ROOM = "CREATE TABLE room (" + "id VARCHAR(20) NOT NULL,"
			+ "type VARCHAR(20) NOT NULL," + "no_of_beds NUMERIC NOT NULL," + "feature_summary VARCHAR(20) NOT NULL,"
			+ "status VARCHAR(20) NOT NULL," + "last_maintenance_date VARCHAR(20)," + "image_name VARCHAR(40),"
			+ "PRIMARY KEY (id))";

	public static final String TRUNCATE_ROOM = "TRUNCATE TABLE room";

	public static final String CREATE_HIRING_RECORD = "CREATE TABLE hiring_record (" + "id VARCHAR(50) NOT NULL,"
			+ "room_id VARCHAR(20) NOT NULL," + "rent_date VARCHAR(20) NOT NULL,"
			+ "estimated_return_date VARCHAR(20) NOT NULL," + "actual_return_date VARCHAR(20)," + "rental_fee NUMERIC,"
			+ "late_fee NUMERIC," + "PRIMARY KEY (id)," + "FOREIGN KEY(room_id) REFERENCES room(id))";

	public static final String TRUNCATE_HIRING_RECORD = "TRUNCATE TABLE hiring_record";

	public static final String INSERT_ROOM = "INSERT INTO room (id, type, no_of_beds, feature_summary, status, last_maintenance_date, image_name) "
			+ " VALUES(?,?,?,?,?,?,?)";

	public static final String UPDATE_ROOM = "UPDATE room SET type=?, no_of_beds=?, feature_summary=?, status=?, last_maintenance_date=?, image_name=? "
			+ " WHERE id=?";

	public static final String SELECT_ALL_ROOMS = "SELECT id, type, no_of_beds, feature_summary, status, last_maintenance_date, image_name "
			+ " FROM room";

	public static final String INSERT_HIRING_RECORD = "INSERT INTO hiring_record (id, room_id, rent_date, estimated_return_date, actual_return_date, rental_fee, late_fee) "
			+ " VALUES(?,?,?,?,?,?,?)";

	public static final String UPDATE_HIRING_RECORD = "UPDATE hiring_record SET rent_date=?, estimated_return_date=?, actual_return_date=?, rental_fee=?, late_fee=? "
			+ " WHERE id=?";

	public static final String SELECT_HIRING_RECORDS_FOR_ROOM = "SELECT id, room_id, rent_date, estimated_return_date, actual_return_date, rental_fee, late_fee "
			+ " FROM hiring_record" + " WHERE room_id=?";

}
