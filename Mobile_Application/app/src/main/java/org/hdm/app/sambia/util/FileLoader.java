package org.hdm.app.sambia.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.hdm.app.sambia.datastorage.DataManager;
import org.hdm.app.sambia.datastorage.ActivityObject;
import org.hdm.app.sambia.main.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import static org.hdm.app.sambia.util.Consts.*;


/**
 * Created by Hannes on 09.05.2016.
 */
public class FileLoader {

    private static final String TAG = "FileLoader";


    String state = Environment.getExternalStorageState();
    private File enviroment = Environment.getExternalStorageDirectory();
    private Context context;
    private Properties properties;


    /**************************
     * Constructor
     *************************/
    public FileLoader(MainActivity mainActivity) {
        context = mainActivity;
    }


    /**************************
     * Init File Prozess
     *************************/

    public void initFiles() {

        initFolder();


        String fileName = JSONFILE;
        String path = enviroment + "/" + getPropertiesFromAssets(PROPERTIESFILE)
                .getProperty(CONFIGFOLDER);

        // Check if Json File is in External Folder
        // if not than copy Json file from Asset to external Folder
        if (!isExternalFileExists(path + JSONFILE)) {
            copyFileFromAssetToExternal(JSONFILE, path);
        }

        // Check if "temp-activities.json" is in External Folder
//        if(isExternalFileExists(path + TEMPACTIVITIES)) fileName =  TEMPACTIVITIES;

        loadActivityObjects(path, fileName);
    }


    /**************************
     * Assets
     *************************/


    public String readFromAssets(Context context, String filename) {

        BufferedReader reader = null;
        StringBuilder sb = null;
        String mLine = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(filename)));
            sb = new StringBuilder();
            mLine = reader.readLine();
            while (mLine != null) {
                sb.append(mLine); // process line
                mLine = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }


    private String readStringFromExternalFolder(String folderPath, String fileName) {

        if (!isExternalFileExists(folderPath + fileName)) return null;

        BufferedReader reader;
        StringBuilder sb;
        String mLine;
        File file = new File(folderPath, fileName);

        try {

            FileInputStream fileInputStream = new FileInputStream(file);
            reader = new BufferedReader(
                    new InputStreamReader(fileInputStream));
            sb = new StringBuilder();
            mLine = reader.readLine();
            while (mLine != null) {
                sb.append(mLine); // process line
                mLine = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();

    }



    public boolean copyFileFromAssetToExternal(String fileName, String path) {

        if (fileName != null && path != null) {
            InputStream in = null;
            OutputStream out = null;

            try {
                in = context.getAssets().open(fileName);
                File outFile = new File(path, fileName);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.d(TAG, "Failed to copy asset file: " + fileName, e);
                return false;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public boolean CopyImagesFromResourceToExternal(int[] resources) {

        if (resources.length != 0) {
            String imageFolder = getPropertiesFromAssets(PROPERTIESFILE)
                    .getProperty(IMAGEFOLDER);
            for (int i = 0; i < resources.length; i++) {
                String fileName = enviroment + "/" + imageFolder + "/" +
                        context.getResources().getResourceEntryName(resources[i]) +
                        ".png";
                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resources[i]);
                try {
                    FileOutputStream out = new FileOutputStream(fileName);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    public Drawable getDrawableFromPath(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        //Here you can make logic for decode bitmap for ignore oom error.
        return new BitmapDrawable(bitmap);
    }


    /**************************
     * Create External Folder
     *************************/
    public String createExternalFolder(String folderName) {

        if (!isExternalStorageWritable()) {
            Toast.makeText(context, " External Storage is not writeble -" +
                    "folder could not be created", Toast.LENGTH_SHORT).show();
            return null;
        }

        File f = new File(enviroment, folderName);
        if (!f.exists()) {
            f.mkdirs();
            if (f.exists()) return f.toString();
        }
//        Toast.makeText(context, folderName + " already exists", Toast.LENGTH_SHORT).show();
        return null;
    }


    /**************************
     * Property File
     *************************/


    public Properties getPropertiesFromAssets(String file) {

        properties = new Properties();

        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(file);
            properties.load(inputStream);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
        return properties;
    }


    public void initFolder() {

        Properties properties = getPropertiesFromAssets(PROPERTIESFILE);

        for (Map.Entry<Object, Object> x : properties.entrySet()) {
            createExternalFolder((String) x.getValue());
        }
    }


    // Load Content
    public void loadActivityObjects(String folderPath, String fileName) {

        if (DEBUGMODE) Log.d(TAG, "loadActivityObjects " + folderPath + fileName);


//        // Check if JsonFile is in External Folder if not copy them from Asset to External Folder
//        if (!isExternalFileExists(folderPath + fileName) && fileName.equals(JSONFILE)) {
//            copyFileFromAssetToExternal(fileName, folderPath);
//        }

        // Read out JsonFile from External Folder
        String jsonString = readStringFromExternalFolder(folderPath, fileName);
        if (DEBUGMODE) Log.d(TAG, "jasonString " + jsonString);

        MyJsonParser jParser = new MyJsonParser();
        ArrayList<ActivityObject> list = jParser.createObjectFromJson(ACTIVITIES, jsonString);
        if (DEBUGMODE) Log.d(TAG, "list " + list);

        if (list == null) {
            jsonString = readStringFromExternalFolder(folderPath, JSONFILE);
            if (DEBUGMODE) Log.d(TAG, "list == null" + jsonString);

            if(jsonString == null) {
                    jsonString = readFromAssets(context, JSONFILE);
                    if (DEBUGMODE) Log.d(TAG, "jasonString == null" + jsonString);
            }
            list = jParser.createObjectFromJson(ACTIVITIES, jsonString);
        }


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        options.inSampleSize = 2; //reduce quality


        String imgPath = enviroment.toString() + "/" + getPropertiesFromAssets(PROPERTIESFILE)
                .getProperty(IMAGEFOLDER);


        for (int i = 0; i < list.size(); i++) {
            ActivityObject activityObject = list.get(i);

            Log.d(TAG, "imageName " + activityObject.imageName);
            String objectImgPath = imgPath + activityObject.imageName;

            // check if Image is in externalFolder available
            // if not than save it from asset to external load again
            if (isExternalFileExists(objectImgPath)) {
                activityObject.image = BitmapFactory.decodeFile(objectImgPath, options);
            } else {
                // Save Image from Asset to External
                copyFileFromAssetToExternal(activityObject.imageName, imgPath);
                activityObject.image = BitmapFactory.decodeFile(objectImgPath, options);
            }
            DataManager.getInstance().setActivityObject(activityObject);
        }


    }


    /**************************
     * File Permission Check
     *************************/


    public boolean isExternalStorageWritable() {
        return (Environment.MEDIA_MOUNTED.equals(state));
    }


    public boolean isExternalFileExists(String filePath) {
        File f = new File(filePath);
        if (f.exists()) return true;
        return false;
    }


    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    public File getEnvironment() {
        return enviroment;
    }


    public void saveLogsOnExternal() {

        MyJsonParser parser = new MyJsonParser();
        String logFile = parser.createLogJsonFromActivityObjects();
        String path = enviroment.toString() + "/" + getPropertiesFromAssets(PROPERTIESFILE)
                .getProperty(LOGFOLDER);
        writeStringOnExternal(logFile, parser.logName, path);
    }


    public void saveActivityStateOnExternal() {
        MyJsonParser parser = new MyJsonParser();
        String activityState = parser.createActivityStateJson();
        String path = enviroment.toString() + "/" + getPropertiesFromAssets(PROPERTIESFILE)
                .getProperty(CONFIGFOLDER);
        writeStringOnExternal(activityState, parser.logName, path);
    }


    private void writeStringOnExternal(String stringFile, String fileName, String path) {

        if (stringFile != null && fileName != null && path != null) {

            File f = new File(path);
            if (!f.exists()) {
                initFolder();
            }


            File file = new File(path, fileName);
            FileOutputStream stream = null;

            try {
                stream = new FileOutputStream(file);
                stream.write(stringFile.getBytes());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
