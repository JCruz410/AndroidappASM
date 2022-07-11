package com.example.androidapp;

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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import static com.example.androidapp.activity_1.ip_address;

public class activity_2 extends AppCompatActivity {

    private Button test_motor;
    private Button timedSandwich;
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

        //Creating the create sandwich button functionality
        test_motor = (Button)findViewById(R.id.test_motor);

        test_motor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // request information from esp32
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

        //Creating the specific sandwich time functionality
        sandwichTimeText = (EditText)findViewById(R.id.enter_time);
        // creating handle for "Create Sandwich Button
        timedSandwich = (Button)findViewById(R.id.sandwichLater);
        //Making a string to hold the specific time format we want, we can also change this to be more
        // specific date wise if we want.
        localDateTimeFormat = "dd hh:mm:ss";
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
                    //Maybe put motor control stuff here?
                    }
                };
                //This is where the execution hopefully happens, we use the TimerTask object and the date we grabbed and formatted
                //early as the arguments
                //Also, this is all happening whenever we click the button right? Not sure If I need to move some of this or now
                sandwichTimer.schedule(sandwichTimerTask, date);
                // request information from esp32
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

        handler.postDelayed(status_data,0);
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