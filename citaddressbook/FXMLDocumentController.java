/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citaddressbook;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * CSC 255
 * Mr.Osborne
 * @author Isaiah Jones & Mr.Osborne
 * 9-26-2017
 */
public class FXMLDocumentController implements Initializable {

    //Create static variables for the different parts of the address
    //Specify the size of five String fields in the record
    final static int NAME_SIZE = 32;
    final static int STREET_SIZE = 32;
    final static int CITY_SIZE = 20;
    final static int STATE_SIZE = 2;
    final static int ZIP_SIZE = 5;
    final static int RECORD_SIZE = (NAME_SIZE + STREET_SIZE + CITY_SIZE + STATE_SIZE + ZIP_SIZE);

    //Create a RandomAccessFile
    private RandomAccessFile raf;

    //Make Controllers for the labels and buttons
    private Label label;
    @FXML
    private TextField nameText;
    @FXML
    private TextField streetText;
    @FXML
    private TextField cityText;
    @FXML
    private TextField stateText;
    @FXML
    private TextField zipText;
    @FXML
    private Button addBtn;
    @FXML
    private Button firstBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button previousBtn;
    @FXML
    private Button lastBtn;
    @FXML
    private Button updateBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Access the file address.dat using the RandomAccessFile
        try {
            raf = new RandomAccessFile("address.dat", "rw");
        } catch (IOException ex) {
            System.out.print("Error " + ex);
            System.exit(1);
        }

        //Display the first record if it exists
        try {
            if (raf.length() > 0) {
                readAddress(0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //"Add" button method
    @FXML
    private void addBtnAction(ActionEvent event) {
        writeAddress();
    }

    //"First" button method
    @FXML
    private void firstBtnAction(ActionEvent event) {
        try {
            if (raf.length() > 0) {
                readAddress(0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //"Next" button method
    @FXML
    private void nextBtnAction(ActionEvent event) {
        try {
            long currentPosition = raf.getFilePointer();
            if (currentPosition < raf.length()) {
                readAddress(currentPosition);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //"Previous" button method
    @FXML
    private void previousBtnAction(ActionEvent event) {
        try {
            long currentPosition = raf.getFilePointer();
            if (currentPosition - 2 * RECORD_SIZE > 0) {
                readAddress(currentPosition - 2 * 2 * RECORD_SIZE);
            } else {
                readAddress(0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //"Last" button method
    @FXML
    private void lastBtnAction(ActionEvent event) {
        try {
            long lastPosition = raf.length();
            if (lastPosition > 0) {
                readAddress(lastPosition - 2 * RECORD_SIZE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //"Update" button method
    @FXML
    private void updateBtnAction(ActionEvent event) {
        try {
            long currentPosition = raf.getFilePointer();
            editAddress(currentPosition - 2 * RECORD_SIZE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    //Read a record at the specified position
    public void readAddress(long position) throws IOException {
        raf.seek(position);
        String name = readFixedLengthString(NAME_SIZE, raf);
        String street = readFixedLengthString(STREET_SIZE, raf);
        String city = readFixedLengthString(CITY_SIZE, raf);
        String state = readFixedLengthString(STATE_SIZE, raf);
        String zip = readFixedLengthString(ZIP_SIZE, raf);

        nameText.setText(name);
        streetText.setText(street);
        cityText.setText(city);
        stateText.setText(state);
        zipText.setText(zip);
    }

    public static String readFixedLengthString(int size,
            DataInput in) throws IOException {
        // Declare an array of characters
        char[] chars = new char[size];

        // Read fixed number of characters to the array
        for (int i = 0; i < size; i++) {
            chars[i] = in.readChar();
        }

        return new String(chars);
    }

    //Write a record at the end of the file
    public void writeAddress() {
        try {
            raf.seek(raf.length());
            writeFixedLengthString(nameText.getText(), NAME_SIZE, raf);
            writeFixedLengthString(streetText.getText(), STREET_SIZE, raf);
            writeFixedLengthString(cityText.getText(), CITY_SIZE, raf);
            writeFixedLengthString(stateText.getText(), STATE_SIZE, raf);
            writeFixedLengthString(zipText.getText(), ZIP_SIZE, raf);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Write fixed number of characters to a DataOutput stream
    public static void writeFixedLengthString(String s, int size,
            DataOutput out) throws IOException {
        char[] chars = new char[size];

        // Fill in string with characters
        s.getChars(0, Math.min(s.length(), size), chars, 0);

        // Fill in blank characters in the rest of the array
        for (int i = Math.min(s.length(), size); i < chars.length; i++) {
            chars[i] = ' ';
        }

        // Create and write a new string padded with blank characters
        out.writeChars(new String(chars));
    }

    //overwrite the record from the current position 
    private void editAddress(long currentPosition) {
        try {
            raf.seek(currentPosition);
            writeFixedLengthString(nameText.getText(), NAME_SIZE, raf);
            writeFixedLengthString(streetText.getText(), STREET_SIZE, raf);
            writeFixedLengthString(cityText.getText(), CITY_SIZE, raf);
            writeFixedLengthString(stateText.getText(), STATE_SIZE, raf);
            writeFixedLengthString(zipText.getText(), ZIP_SIZE, raf);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
