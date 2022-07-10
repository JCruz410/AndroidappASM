package com.example.androidapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import androidx.appcompat.app.AppCompatActivity;
import static com.example.androidapp.activity_1.ip_address;

public class activity_2 extends AppCompatActivity {

    private Button test_motor;
    TextView text;
    TextView txvalue;
    private EditText sandwichTime;

    Handler handler = new Handler();
    boolean statusdevice = true;

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
                request_to_url("STEP");
                request_to_url("DIR");
            }
        });

        //Creating the specific sandwich time functionality
        sandwichTime = (EditText)findViewById(R.id.enter_time);

        /*sandwichTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void on
        });*/
        //txvalue =(TextView)findViewById(R.id.tx_value);

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
            if(result_data != null) {

                //txvalue.setText(result_data);

            }else{
                Toast.makeText(activity_2.this, "Null data", Toast.LENGTH_LONG).show();
            }
        }
    }


}