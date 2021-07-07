package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityLodge;
import model.Room;
import model.RoomType;
import model.SuiteRoom;
import model.database.DatabaseAccess;
import model.database.RoomDataAccess;
import model.exception.DatabaseException;
import model.exception.ImportException;
import util.FxUtils;
import util.ImportExportUtils;
import view.RoomDetailView;
import view.StandardRoomFormView;
import view.SuiteRoomFormView;

/*
 * MainController is the controller associated with MainView.
 * All functionalities of Main Window are handled here.
 */
public class MainController implements Initializable {
	/*
	 * P.S. These are references of the layout in the MainProgramWindowView.fxml
	 * file. No new GUI component is created in this class.
	 */

	@FXML
	ListView<HBox> mainListView;

	@FXML
	AnchorPane mainAnchorPane;

	private ObservableList<HBox> guiRooms;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		guiRooms = FXCollections.observableArrayList();
		mainListView.setItems(guiRooms);
		populateRoomsListView();
	}

	public void populateRoomsListView() {
		guiRooms.clear();
		HashMap<String, Room> rooms = CityLodge.getInstance().getRooms();
		for (String roomId : rooms.keySet()) {
			Room room = rooms.get(roomId);
			guiRooms.add(createListItemNode(room));
		}
	}

	private HBox createListItemNode(Room room) {
		HBox hContainer = new HBox();
		VBox vContainer1 = createImageComponentForListItem(room);
		VBox vContainer2 = createDetailComponentForListItem(room);
		VBox vContainer3 = createButtonComponentForListItem(room);

		hContainer.getChildren().add(vContainer1);
		hContainer.getChildren().add(vContainer2);
		hContainer.getChildren().add(vContainer3);
		hContainer.setSpacing(10);
		return hContainer;
	}

	private VBox createImageComponentForListItem(Room room) {
		VBox imageContainer = new VBox();
		Image image;
		try {
			image = new Image("/view/images/" + room.getRoomImageName());
			if (image.isError()) {
				image = new Image("/view/images/image_not_available.png");
			}
		} catch (Exception ex) {
			image = new Image("/view/images/image_not_available.png");
		}
		ImageView imageview = new ImageView(image);
		imageview.setFitHeight(135);
		imageview.setFitWidth(180);
		imageContainer.getChildren().add(imageview);
		return imageContainer;
	}

	private VBox createButtonComponentForListItem(Room room) {
		VBox buttonContainer = new VBox();
		Button viewDetailsButton = new Button("View Details");
		viewDetailsButton.setMaxHeight(135);
		viewDetailsButton.setMinHeight(135);
		viewDetailsButton.setAlignment(Pos.CENTER);
		viewDetailsButton.setId(room.getRoomId());
		viewDetailsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("User Clicked on Room ID: " + room.getRoomId());
				RoomDetailView view = new RoomDetailView(room);
				view.loadWindow();
				populateRoomsListView();
			}
		});
		buttonContainer.getChildren().add(viewDetailsButton);
		return buttonContainer;
	}

	private VBox createDetailComponentForListItem(Room room) {
		VBox detailContainer = new VBox();

		GridPane grid = new GridPane();
		grid.add(new Label("ID:\t\t"), 0, 0);
		grid.add(new Label("Type:\t\t"), 0, 1);
		grid.add(new Label("No. of Beds:\t\t"), 0, 2);
		grid.add(new Label("Feature Summary:\t\t"), 0, 3);
		grid.add(new Label(room.getRoomId()), 1, 0);
		grid.add(new Label(room.getRoomType().toString()), 1, 1);
		grid.add(new Label(Integer.toString(room.getNoOfBeds())), 1, 2);
		grid.add(new Label(room.getFeatureSummary()), 1, 3);

		if (room.getRoomType() == RoomType.Suite) {
			grid.add(new Label("Last Maintenance Date:"), 0, 4);
			grid.add(new Label("Status:\t\t"), 0, 5);
			grid.add(new Label(((SuiteRoom) room).getLastMaintenanceDate().toString()), 1, 4);
			grid.add(new Label(room.getRoomStatus().toString()), 1, 5);
		} else {
			grid.add(new Label("Status:\t\t"), 0, 4);
			grid.add(new Label(room.getRoomStatus().toString()), 1, 4);
		}
		detailContainer.getChildren().add(grid);
		detailContainer.setMaxWidth(500);
		detailContainer.setMinWidth(500);
		return detailContainer;
	}

	/*
	 * The method handleImportDataFromFileClick is used to import data from a
	 * text file and load the data into the model.
	 */
	public void handleImportDataFromFileClick() {
		FxUtils.showAlert(AlertType.WARNING, "City Lodge - Data Import Warning",
				"On Successful Import, all existing Data will be cleared\nand Application will be loaded with imported Data",
				null);
		FileChooser fc = new FileChooser();
		String currentDirectory = System.getProperty("user.dir");

		fc.setInitialDirectory(new File(currentDirectory));
		File selectedFile = fc.showOpenDialog(null);

		if (selectedFile != null) {
			String path = selectedFile.getAbsolutePath();
			HashMap<String, Room> importedRooms;
			try {
				importedRooms = ImportExportUtils.importFromFile(path);
				CityLodge.getInstance().getRooms().clear();
				CityLodge.getInstance().getRooms().putAll(importedRooms);
				// Refresh the ListView
				populateRoomsListView();
				// Truncate the Database Tables
				DatabaseAccess.truncateTables();
				// Insert importedRooms into the Database Tables
				RoomDataAccess.insertRooms(importedRooms);

			} catch (FileNotFoundException | ParseException | ImportException | ClassNotFoundException | SQLException
					| DatabaseException e) {
				FxUtils.showErrorAlert(e.getLocalizedMessage(), null);
			}
		}
	}

	/*
	 * The method handleExportDataFromFileClick is used to export data from the
	 * model into a text file.
	 */

	public void handleExportDataToFileClick() {
		String currentDirectory = System.getProperty("user.dir");

		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setInitialDirectory(new File(currentDirectory));
		Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
		File file = dirChooser.showDialog(stage);

		if (file != null) {
			String path = file.getAbsolutePath() + System.getProperty("file.separator") + "city_lodge_export_data.txt";
			try {
				ImportExportUtils.exportToFile(CityLodge.getInstance().getRooms(), path);
			} catch (FileNotFoundException e) {
				FxUtils.showErrorAlert(e.getLocalizedMessage(), null);
			}
		}
	}

	public void handleAddStandardRoomClick(ActionEvent e) {
		StandardRoomFormView view = new StandardRoomFormView();
		view.loadWindow();
		populateRoomsListView();
	}

	/*
	 * The method handleAddVanClick is used to add a van using the menu bar
	 * option
	 */
	public void handleAddSuiteRoomClick(ActionEvent e) {
		SuiteRoomFormView view = new SuiteRoomFormView();
		view.loadWindow();
		populateRoomsListView();
	}

	public void handleCloseClick(ActionEvent event) {
		Stage window = (Stage) mainAnchorPane.getScene().getWindow();
		window.close();
	}
}
