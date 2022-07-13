package com.example.androidapp;

// reference:
// DIR and STEP is Conveyor Belt Motor
// DIR2 and STEP2 is PB dispenser Motor
// DIR3 and STEP3 is Jelly Motor
// DIR4 and STEP4 is Bread Motor

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import static com.example.androidapp.activity_1.ip_address;

public class activity_2 extends AppCompatActivity {

    private Button sandwichNow;
    private Button timedSandwich;
    private Button timedPB;
    private Button timedJelly;
    private Button PBNow;
    private Button JellyNow;
    TextView text;
    private EditText sandwichTimeText;
    private static String sandwichTimeInput;
    private LocalDateTime dateTime;
    private LocalDateTime parsedDateTime;
    private Date date;
    private DateTimeFormatter dtf;
    private static String localDateTimeFormat;
    private Instant instant;
    Timer sandwichTimer;
    Handler handler = new Handler();
    boolean statusdevice = true;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_2);

        //Creating all button functionality
        sandwichNow = (Button)findViewById(R.id.conveyor);
        timedSandwich = (Button)findViewById(R.id.sandwichLater);
        timedPB = (Button)findViewById(R.id.pbLater);
        timedJelly = (Button)findViewById(R.id.jellyLater);
        PBNow = (Button)findViewById(R.id.pbNow);
        JellyNow = (Button)findViewById(R.id.jellynow);


        sandwichNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // request information from esp32
                // full sandwich, all motors running
                request_to_url("STEP");
                request_to_url("DIR");
                /*request_to_url("STEP2");
                request_to_url("DIR2");
                request_to_url("STEP3");
                request_to_url("DIR3");
                request_to_url("STEP4");
                request_to_url("DIR4"); */
                request_to_url("ledRED");
                request_to_url("ledGREEN");

            }
        });

        PBNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request information from esp32
                // PB sandwich now, disable jelly motor
                request_to_url("STEP");
                request_to_url("DIR");
                /*request_to_url("STEP2");
                request_to_url("DIR2");
                request_to_url("STEP4");
                request_to_url("DIR4"); */
                request_to_url("ledRED");
                request_to_url("ledGREEN");
            }
        });


        JellyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request information from esp32
                // Jelly sandwich now, disable PB motor
                request_to_url("STEP");
                request_to_url("DIR");
                /*request_to_url("STEP3");
                request_to_url("DIR3");
                request_to_url("STEP4");
                request_to_url("DIR4"); */
                request_to_url("ledRED");
                request_to_url("ledGREEN");
            }
        });

        timedPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request information from esp32
                // Jelly sandwich now, disable PB motor
                request_to_url("STEP");
                request_to_url("DIR");
                /*request_to_url("STEP2");
                request_to_url("DIR2");
                request_to_url("STEP4");
                request_to_url("DIR4"); */
                request_to_url("ledRED");
                request_to_url("ledGREEN");
            }
        });

        timedJelly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request information from esp32
                // Jelly sandwich later, disable PB motor
                 ;
                /*request_to_url("STEP3");
                request_to_url("DIR3");
                request_to_url("STEP4");
                request_to_url("DIR4"); */
                request_to_url("ledRED");
                request_to_url("ledGREEN");
            }
        });


        //Creating the specific sandwich time functionality
        sandwichTimeText = (EditText)findViewById(R.id.enter_time);

        //Making a string to hold the specific time format we want, we can also change this to be more
        // specific date wise if we want.
        localDateTimeFormat = "yyyy-MM-dd-HH-mm-ss";
        //creating a DateTimeFormatter to correctly capture the string
        dtf = DateTimeFormatter.ofPattern(localDateTimeFormat);


        timedSandwich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Getting the input from the user and converting it into a string
                sandwichTimeInput = sandwichTimeText.getText().toString().trim();

                //Checking to see if the user input is empty (maybe more error checks later)
                if (sandwichTimeInput.isEmpty())
                {
                    sandwichTimeText.setError("Empty input.");
                }
                //This is to correctly parse the input using the specified date-time format earlier
                parsedDateTime = LocalDateTime.parse(sandwichTimeInput, dtf);
                //think this is to get it into the correct TimeZone
                instant = parsedDateTime.atZone(ZoneId.systemDefault()).toInstant();
                //Basically this now goes from a LocalDateTime object to a Date object
                date = Date.from(instant);

                //Creating a Timer with the thread having the name of sandwichTimeInput (not sure if needed)
                sandwichTimer = new Timer(sandwichTimeInput);

                //Creating a TimerTask object to run whatever code is in the run method whenever it is called
                TimerTask sandwichTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        // put motor control stuff here

                        // request information from esp32
                        // full timed sandwich, all motors run
                        request_to_url("STEP");
                        request_to_url("DIR");

                        // commented out temporarily till full maker is built
                        /*request_to_url("STEP2");
                        request_to_url("DIR2");
                        request_to_url("STEP3");
                        request_to_url("DIR3");
                        request_to_url("STEP4");
                        request_to_url("DIR4"); */
                        request_to_url("ledRED");
                        request_to_url("ledGREEN");
                    }
                };

                // call the timer task with the inputted time.
                sandwichTimer.schedule(sandwichTimerTask, date);

            }
        });
    }


    private Runnable status_data = new Runnable() {
        @Override
        public void run() {
            if (statusdevice) {

                request_to_url("");
                handler.postDelayed(this, 2000);
                Log.d("Status", "Connectivity_esp32");
            }else {
                handler.removeCallbacks(status_data);
                Log.d("Status","Finalize");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusdevice = false;
    }

    public void request_to_url (String command) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {

            new request_data().execute("http://" + ip_address + "/" + command);

        }else {
            Toast.makeText(activity_2.this, "Not connected  ", Toast.LENGTH_LONG).show();

        }
    }

    private class request_data extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url)
        {
            return Connectivity.geturl(url[0]);
        }

        @Override
        protected void onPostExecute(String result_data) {
            if(result_data != null)
            {

            }else{
                Toast.makeText(activity_2.this, "Null data", Toast.LENGTH_LONG).show();
            }
        }
    }


}