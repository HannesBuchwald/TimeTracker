package classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.scene.control.ListView;

/**

 */
public class WorkerRunnable implements Runnable {

	protected Socket clientSocket = null;
	protected String serverText = null;
	private String message = "";

	public WorkerRunnable(Socket clientSocket, String serverText) {
		this.clientSocket = clientSocket;
		this.serverText = serverText;
	}

	public void run() {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
					clientSocket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader); // get the client message
			message = bufferedReader.readLine();

			if (message.length() > 0) {

			}

			System.out.println(message);
			SaveFile(message, null);
			inputStreamReader.close();
			clientSocket.close();
		} catch (IOException e) {
			// report exception somewhere.
			e.printStackTrace();
		}
	}

	private void SaveFile(String content, File file) {
		try {
			String desktopDirectory = System.getProperty("user.home")
					+ "/Desktop";
			String saveFolder = desktopDirectory + "/TimeTrackerLogs";
			String uniqueID = UUID.randomUUID().toString();

			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Calendar cal = Calendar.getInstance();
			String dateTime = dateFormat.format(cal.getTime());

			String fileName = saveFolder + "/savelog (" + dateTime + ") id - "
					+ uniqueID + ".csv";

			FileWriter fileWriter = null;
			//fileWriter = new FileWriter(fileName);
			jsonToCSV(content, fileName);
			// fileWriter.write(content);
			fileWriter.close();
		} catch (IOException ex) {
		}

	}

	private void jsonToCSV(String json, String file) {
		JsonFlattener parser = new JsonFlattener();
		CSVWriter writer = new CSVWriter();

		List<Map<String, String>> flatJson;
		try {
			flatJson = parser.parseJson(json);
			writer.writeAsCSV(flatJson, file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}