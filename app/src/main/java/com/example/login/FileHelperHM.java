package com.example.login;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHelperHM {

    public static final String FILENAME1 = "ListInfoHM.dat";

    public static void writeData(ArrayList<String> HMItems, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME1, Context.MODE_PRIVATE);
            ObjectOutputStream obs = new ObjectOutputStream(fos);
            obs.writeObject(HMItems);
            obs.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<String> readData(Context context){
        ArrayList<String> itemsListHM = null;
        try {
            FileInputStream fis = context.openFileInput(FILENAME1);
            ObjectInputStream ois = new ObjectInputStream(fis);
            itemsListHM = (ArrayList<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            itemsListHM = new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return itemsListHM;

    }

}
