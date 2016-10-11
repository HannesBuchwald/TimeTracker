package classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
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
			SaveFile(message);
			inputStreamReader.close();
			clientSocket.close();
		} catch (IOException e) {
			// report exception somewhere.
			e.printStackTrace();
		}
	}

	private void SaveFile(String content) {

		String desktopDirectory = System.getProperty("user.home") + "/Desktop";
		String saveFolder = desktopDirectory + "/TimeTrackerLogs";
		String uniqueID = UUID.randomUUID().toString();

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Calendar cal = Calendar.getInstance();
		String dateTime = dateFormat.format(cal.getTime());

		saveFolder += " (" + dateTime + ")";

		String fileNameJson = saveFolder + "/Json/savelog json (" + dateTime
				+ ") id - " + uniqueID + ".txt";

		String fileNameCSV = saveFolder + "/CSV/savelog (" + dateTime
				+ ") id - " + uniqueID + ".csv";

		String fileNameExcel = saveFolder + "/Excel/savelog (" + dateTime
				+ ") id - " + uniqueID + ".csv";

		// Write json directly to file:
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileNameJson);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		jsonToCSV(content, fileNameExcel, true);

		jsonToCSV(content, fileNameCSV, false);
	}

	private void jsonToCSV(String json, String file, boolean addSep) {
		JsonFlattener parser = new JsonFlattener();
		CSVWriter writer = new CSVWriter();

		List<Map<String, String>> flatJson;
		try {
			flatJson = parser.parseJson(json);
			writer.writeAsCSV(flatJson, file);
			if (addSep) {
				String content = "\"sep=,\"\n\r" + readFile(file);

				try (PrintWriter out = new PrintWriter(file)) {
					out.println(content);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

}