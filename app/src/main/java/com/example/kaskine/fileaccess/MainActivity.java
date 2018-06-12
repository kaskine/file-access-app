package com.example.kaskine.fileaccess;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStorageState;

public class MainActivity extends AppCompatActivity {

    private static final String sFileName = "CSCI235.txt";
    private static final String sClearString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText userInputField = findViewById(R.id.editText_input);
        final Button inputButton = findViewById(R.id.button_save);
        inputButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String userInput = userInputField.getText().toString() + " ";
                Toast.makeText(getBaseContext(),
                               getResources().getString(R.string.toastMessage_dataWrite),
                               Toast.LENGTH_SHORT).show();

                FileData entry = new FileData(getBaseContext());
                entry.open();
                entry.push(userInput);
                entry.close();

                userInputField.setText(sClearString);
            }
        });

        final TextView outputField = findViewById(R.id.textView_output);
        final Button loadButton = findViewById(R.id.button_load);
        loadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String outputValues = "";

                try {
                    FileData fileData = new FileData(getBaseContext());
                    fileData.open();
                    outputValues = fileData.pull();
                    fileData.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                outputField.setText(outputValues);
                Toast.makeText(getBaseContext(),
                               getResources().getString(R.string.toastMessage_dataRead),
                               Toast.LENGTH_SHORT).show();
            }
        });

        final Button clearButton = findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                clearSaveData();
                outputField.setText(sClearString);
            }
        });
    }

    private void clearSaveData() {

        FileData fileData = new FileData(getBaseContext());
        fileData.delete();

        Toast.makeText(getBaseContext(),
                       getResources().getString(R.string.toastMessage_dataClear),
                       Toast.LENGTH_SHORT).show();
    }

    /**
     * Determines the state of external storage media
     *
     * @return - True if external media is available
     */
    private boolean hasExternalStorageAvailable() {

        return getExternalStorageState().equals(MEDIA_MOUNTED);
    }

    /**
     * Determines if there is an existing external output file
     *
     * @return - True if there is an existing external output file
     */
    private boolean hasExistingExternalOutputFile() {

        File file = new File(getExternalStorageDirectory(), sFileName);
        return file.exists();
    }

    /**
     * Determines if an existing internal output file
     *
     * @return - True if there is an existing internal output file
     */
    private boolean hasExistingInternalOutputFile() {

        return (Arrays.binarySearch(fileList(), sFileName) >= 0);
    }

    /**
     * Gets an external output file
     *
     * @return - A File Object with the location of the external output file
     */
    private File getExternalOutputFile() {

        return new File(getExternalStorageDirectory(), sFileName);
    }

    /**
     * Gets an external write output file if available, otherwise gets an internal output file.
     * Also determines whether a new file needs to be created or if an existing one can be appended to
     *
     * @return A FileOutputStream which will write to the appropriate location with preferred settings.
     * @throws IOException
     */
    private FileOutputStream getFileOutputStream() throws IOException {

        if (hasExternalStorageAvailable()) {
            return hasExistingExternalOutputFile() ?
                    new FileOutputStream(getExternalOutputFile(), true) :
                    new FileOutputStream(getExternalOutputFile());
        }
        else {
            return hasExistingInternalOutputFile() ?
                    openFileOutput(sFileName, MODE_APPEND) :
                    openFileOutput(sFileName, MODE_PRIVATE);
        }
    }

    /**
     * Reads previous user input from a file (selects an external output file by default, but switches to an internal file if
     * external media is not attached).
     *
     * @return A String containing all previously entered user input.
     * @throws IOException
     */
    private String readFromFile() throws IOException {

        return hasExternalStorageAvailable()
                ? getFileData(
                        new FileInputStream(
                        new File(getExternalStorageDirectory(), sFileName)))
                : getFileData(openFileInput(sFileName));
    }

    /**
     * Reads all previously entered user input from a file and returns it as a single String Object ready to be output to
     * the display.
     *
     * @param fileInputStream - The FileInputStream from which to read the data.
     * @return A String containing all previously entered user input.
     * @throws IOException
     */
    private String getFileData(FileInputStream fileInputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        Scanner scanner = new Scanner(fileInputStream);

        while (scanner.hasNextLine()) {
            output.append(scanner.nextLine());
        }

        scanner.close();
        fileInputStream.close();

        return output.toString();
    }
}
