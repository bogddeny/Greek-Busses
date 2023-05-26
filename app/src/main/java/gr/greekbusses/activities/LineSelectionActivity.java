package gr.greekbusses.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import gr.greekbusses.R;
import gr.greekbusses.misc.Line;
import gr.greekbusses.misc.Strings;

public class LineSelectionActivity extends AppCompatActivity
{
    private String regionString;
    private String categoryString;

    LinearLayout lineSelLinearLayout;

    public static ArrayList<Line> lines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_selection);
        Bundle bundle = getIntent().getExtras();
        regionString = bundle.getString("region");
        categoryString = bundle.getString("category");

        initializeViewObjects();
        readLinesFromDatabase();

        String title = getResources().getString(R.string.app_name) + " " + regionString + " " + getResources().getString(R.string.title_category) + " " + categoryString;
        setTitle(title);

        int padding = convertDpToPx(12);
        lineSelLinearLayout.setPadding(padding, padding, padding, padding);
    }
    public void initializeViewObjects()
    {
        lineSelLinearLayout = findViewById(R.id.lineSelLinearLayout);
    }
    public void readLinesFromDatabase()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(Strings.REG).document(regionString).collection(Strings.LINES).whereEqualTo(Strings.CATEGORY, categoryString).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                lines.clear();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                {
                    Log.d(Strings.TAG, document.getId() + " => " + document.getData());
                    lines.add(new Line(document.get("category"), document.get("line"), document.get("description"), document.get("time"), document.get("timeSat"), document.get("timeSun")));
                }
                for (int i = 0; i < lines.size(); i++)
                {
                    String line = lines.get(i).getLine().toString();
                    Button button = new Button(this);
                    button.setText(line);
                    button.setHeight(convertDpToPx(80));
                    lineSelLinearLayout.addView(button);

                    int finalI = i;

                    button.setOnClickListener(view ->
                    {
                        Intent intent = new Intent(LineSelectionActivity.this, SelectedLineActivity.class);
                        intent.putExtra("region", regionString);
                        intent.putExtra("line", lines.get(finalI).getLine().toString());
                        startActivity(intent);
                    });
                }
            }
            else
            {
                Log.d(Strings.TAG, "Error getting documents: ", task.getException());
            }
        });
    }
    public int convertDpToPx(int dp)
    {
        float density = this.getResources().getDisplayMetrics().density;
        float pxValue = dp * density;
        return (int)pxValue;
    }
}