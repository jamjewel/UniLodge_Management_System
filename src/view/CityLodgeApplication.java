package view;

import javafx.application.Application;
import javafx.stage.Stage;
import util.FxUtils;

public class CityLodgeApplication extends Application {

	/*
	 * The start method launches the City Lodge Application It displays an error
	 * message if it can not.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			// loading the main program window
			MainView mainView = new MainView();
			mainView.loadWindow(primaryStage);
		} catch (Exception e) {
			FxUtils.showErrorAlert(e.getLocalizedMessage(), null);
		}
	}

	public static void main(String a[]) {
		Application.launch(a);
	}
}
