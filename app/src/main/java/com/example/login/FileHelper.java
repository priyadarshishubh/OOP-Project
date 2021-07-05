package com.example.login;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHelper {

    public static final String FILENAME = "ListInfo.dat";

    public static void writeData(ArrayList<String> KitchenItems, Context context) {
        try {
            FileOutputStream fos1 = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream obs1 = new ObjectOutputStream(fos1);
            obs1.writeObject(KitchenItems);
            obs1.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<String> readData(Context context){
        ArrayList<String> itemsList = null;
        try {
            FileInputStream fis1 = context.openFileInput(FILENAME);
            ObjectInputStream ois1 = new ObjectInputStream(fis1);
            itemsList = (ArrayList<String>) ois1.readObject();
        } catch (FileNotFoundException e) {
            itemsList = new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return itemsList;

    }

}
