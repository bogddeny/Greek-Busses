package gr.greekbusses.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.security.KeyStore;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import gr.greekbusses.R;
import gr.greekbusses.misc.DatePicker;
import gr.greekbusses.misc.Line;
import gr.greekbusses.misc.Strings;
import gr.greekbusses.misc.TimePicker;

public class SelectedLineActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    Button selDateButton;
    Button selTimeButton;
    LinearLayout linearLayoutA;
    LinearLayout linearLayoutB;
    TextView titleATextView;
    TextView titleBTextView;
    TextView descriptionTextView;
    Button mapButton;

    String weekDay;
    boolean isDaySet = false;
    boolean isTimeSet = false;
    String regionString;
    String lineString;
    Line line;
    List<String> timeBusiness;
    List<String> timeSaturday;
    List<String> timeSunday;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_line);
        initializeViewObjects();

        Bundle bundle = getIntent().getExtras();
        regionString = bundle.getString("region");
        lineString = bundle.getString("line");
        setTitle(lineString);

        readLineFromDatabase();

        selDateButton.setOnClickListener(view ->
        {
            DatePicker datePicker = new DatePicker();
            datePicker.show(getSupportFragmentManager(), "Pick Date");
        });
        selTimeButton.setOnClickListener(view ->
        {
            TimePicker timePicker = new TimePicker();
            timePicker.show(getSupportFragmentManager(), "Pick Time");
        });
        mapButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(SelectedLineActivity.this, MapActivity.class);
            startActivity(intent);
        });
        String[] words = lineString.split("-");
        String titleA = titleATextView.getText().toString() + ": " + words[0].substring(4);
        titleATextView.setText(titleA);
        String titleB = titleBTextView.getText().toString() + ": " + words[1].substring(5);
        titleBTextView.setText(titleB);
    }
    public void initializeViewObjects()
    {
        selDateButton = findViewById(R.id.selDateButton);
        selTimeButton = findViewById(R.id.selTimeButton);
        linearLayoutA = findViewById(R.id.linearLayoutA);
        linearLayoutB = findViewById(R.id.linearLayoutB);
        titleATextView = findViewById(R.id.titleATextView);
        titleBTextView = findViewById(R.id.titleBTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        mapButton = findViewById(R.id.mapButton);
    }
    public void readLineFromDatabase()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(Strings.REG).document(regionString).collection(Strings.LINES).whereEqualTo(Strings.LINE, lineString).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                {
                    Log.d(Strings.TAG, document.getId() + " => " + document.getData());
                    line = new Line(document.get("category"), document.get("line"), document.get("description"), document.get("time"), document.get("timeSat"), document.get("timeSun"));
                    timeBusiness = (List<String>) document.get("time");
                    timeSaturday = (List<String>) document.get("timeSat");
                    timeSunday = (List<String>) document.get("timeSun");
                }
                descriptionTextView.setText(line.getDescription().toString());
            }
            else
            {
                Log.d(Strings.TAG, "Error getting documents: ", task.getException());
            }
        });
    }
    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        weekDay = dayFormat.format(calendar.getTime());

        if (!weekDay.equals("Saturday") && !weekDay.equals("Sunday"))
        {
            weekDay = "Business";
        }
        selDateButton.setText(weekDay);
        isDaySet = true;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        fillTimes(hour);
    }
    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minute)
    {
        String text = hour + ":" + minute;
        selTimeButton.setText(text);
        isTimeSet = true;

        if (isDaySet)
        {
            fillTimes(hour);
        }
    }
    public void fillTimes(int hour)
    {
        linearLayoutA.removeAllViews();
        linearLayoutB.removeAllViews();

        if (weekDay.equals("Business"))
        {
            addTimeToLayouts(timeBusiness, hour);
        }
        if (weekDay.equals("Saturday"))
        {
            addTimeToLayouts(timeSaturday, hour);
        }
        if (weekDay.equals("Sunday"))
        {
            addTimeToLayouts(timeSunday, hour);
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void addTimeToLayouts(List<String> times, int hour)
    {
        for (int i = 0; i < times.size(); i = i + 2)
        {
            int textPadding = convertDpToPx(4);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            Button textViewA = new Button(this);
            textViewA.setText(times.get(i));
            textViewA.setTextColor(Color.WHITE);
            textViewA.setBackgroundColor(Color.BLACK);
            textViewA.setClickable(false);
            params.setMargins(textPadding, textPadding, textPadding, textPadding);
            textViewA.setLayoutParams(params);

            Button textViewB = new Button(this);
            textViewB.setText(times.get(i + 1));
            textViewB.setTextColor(Color.WHITE);
            textViewB.setBackgroundColor(Color.BLACK);
            textViewB.setClickable(false);
            params.setMargins(textPadding, textPadding, textPadding, textPadding);
            textViewB.setLayoutParams(params);

            if (!isTimeSet)
            {
                linearLayoutA.addView(textViewA);
                linearLayoutB.addView(textViewB);
            }
            else
            {
                String[] hourA = textViewA.getText().toString().split(":");
                int textViewATime = Integer.parseInt(hourA[0]);

                if (textViewATime == hour - 2 || textViewATime == hour - 1 || textViewATime == hour || textViewATime == hour + 1 || textViewATime == hour + 2)
                {
                    linearLayoutA.addView(textViewA);
                }
                String[] hourB = textViewB.getText().toString().split(":");
                int textViewBTime = Integer.parseInt(hourB[0]);

                if (textViewBTime == hour - 2 || textViewBTime == hour - 1 || textViewBTime == hour || textViewBTime == hour + 1 || textViewBTime == hour + 2)
                {
                    linearLayoutB.addView(textViewB);
                }
            }
        }
    }
    public int convertDpToPx(int dp)
    {
        float density = this.getResources().getDisplayMetrics().density;
        float pxValue = dp * density;
        return (int)pxValue;
    }
}