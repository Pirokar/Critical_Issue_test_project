package vladimir.ru.critical_issue_test_project.helpers;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import vladimir.ru.critical_issue_test_project.model.entites.ListItem;

/**
 * Created by Vladimir on 17.10.2016.
 * Allow to serialize/deserialize list
 */

public class SerializationHelper {
    private static final String LIST_FILE_NAME = "/critical_ussue_test_project_list.ser";

    @SuppressWarnings("all")
    public static boolean serializeList(List<ListItem> list, Context context) {
        File file;
        try {
            file = new File(context.getFilesDir() + LIST_FILE_NAME);
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
        FileOutputStream fileOut;
        ObjectOutputStream out;
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            fileOut = new FileOutputStream(file, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try {
            out = new ObjectOutputStream(fileOut);
            out.writeObject(list);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @SuppressWarnings("all")
    public static List<ListItem> deserealizeList(Context context) {
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        List<ListItem> result;
        try {
            fileIn = new FileInputStream(context.getFilesDir() + LIST_FILE_NAME);
            in = new ObjectInputStream(fileIn);
            result = (List<ListItem>)in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            return getDefaultList();
        } catch (IOException e) {
            e.printStackTrace();
            return getDefaultList();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return getDefaultList();
        } catch (ClassCastException e) {
            removeFile(context, LIST_FILE_NAME);
            return getDefaultList();
        } finally {
            try {
                if(in != null) {in.close();}
                if (fileIn != null) {fileIn.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static boolean removeFile(Context context, String what) {
        File file;

        try {
            file = new File(context.getFilesDir() + what);
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }

        return !file.exists() || file.delete();
    }

    private static List<ListItem> getDefaultList() {
        List<ListItem> list = new ArrayList<>(10);
        for(int i = 1; i < 11; i++) {
            list.add(new ListItem("This is " + i + " element", false));
        }
        return list;
    }
}
