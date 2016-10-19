import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class LogFileMerger {

	private String fileNameJson = "";
	private String fileNameCSV = "";
	private String fileNameExcel = "";
	private String saveFolder = "";

	public LogFileMerger() {
		createFolderNames();
		createSaveFolder();
		saveFiles(readJsons(getFileList()));
	}

	public static void main(String[] args) {
		new LogFileMerger();
	}

	public File[] getFileList() {

		File file = null;

		file = new File("");

		System.out.println("Path: " + file.getAbsolutePath());

		File newFile = new File(file.getAbsolutePath());

		for (File f : newFile.listFiles()) {
			System.out.println("Files: " + f.getPath());
		}
		File[] files = newFile.listFiles();

		return newFile.listFiles();
	}

	private String readJsons(File[] files) {
		String mergedContent = "[";
		for (File f : files) {
			if (!f.getName().equals("TimeTrackerLogFileMerger.jar")) {
				String fileContent = readFile(f.getAbsolutePath());
				fileContent = fileContent.substring(1);
				fileContent = fileContent
						.substring(0, fileContent.length() - 1);
				mergedContent += fileContent + ",";
			}
		}
		mergedContent = mergedContent.substring(0, mergedContent.length() - 1);
		mergedContent += "]";

		System.out.println("MergedContent: " + mergedContent);

		return mergedContent;
	}

	private String readFile(String path) {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(encoded);
	}

	private void saveFiles(String content) {

		saveJsonFile(fileNameJson, content);
		jsonToCSV(content, fileNameCSV, false);
		jsonToCSV(content, fileNameExcel, true);
	}

	private void saveJsonFile(String fileName, String content) {

		// Write json directly to file:
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFolderNames() {
		String desktopDirectory = System.getProperty("user.home") + "/Desktop";
		saveFolder = desktopDirectory + "/Merged TimeTrackerLogs";

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Calendar cal = Calendar.getInstance();
		String dateTime = dateFormat.format(cal.getTime());

		saveFolder += " (" + dateTime + ")";

		fileNameJson = saveFolder + "/Json/merged savelog json.txt";

		fileNameCSV = saveFolder + "/CSV/merged savelog.csv";

		fileNameExcel = saveFolder + "/Excel/merged savelog.csv";
	}

	private void jsonToCSV(String json, String file, boolean addSep) {
		JsonFlattener parser = new JsonFlattener();
		CSVWriter writer = new CSVWriter();

		List<Map<String, String>> flatJson;

		try {
			flatJson = parser.parseJson(json);
			writer.writeAsCSV(flatJson, file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (addSep) {
			String content = "\"sep=,\"\n\r" + readFile(file);

			try (PrintWriter out = new PrintWriter(file)) {
				out.println(content);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void createSaveFolder() {
		File saveDir = new File(saveFolder);
		File jsonDir = new File(saveFolder + "/Json");
		File csvDir = new File(saveFolder + "/CSV");
		File excelDir = new File(saveFolder + "/Excel");

		// if the directory does not exist, create it
		if (!saveDir.exists()) {
			System.out.println("creating directory: " + saveFolder);
			boolean result = false;

			try {
				saveDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("Save DIR created");
			}
		}

		if (!jsonDir.exists()) {
			System.out.println("creating directory: " + saveFolder + "/Json");
			boolean result = false;

			try {
				jsonDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("JSon DIR created");
			}
		}

		if (!csvDir.exists()) {
			System.out.println("creating directory: " + saveFolder + "/CSV");
			boolean result = false;

			try {
				csvDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("CSV DIR created");
			}
		}
		if (!excelDir.exists()) {
			System.out.println("creating directory: " + saveFolder + "/Excel");
			boolean result = false;

			try {
				excelDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("Excel DIR created");
			}
		}
	}

}
