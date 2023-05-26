package gr.greekbusses.activities;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import gr.greekbusses.R;
import gr.greekbusses.databinding.ActivityMenuBinding;
import gr.greekbusses.misc.Line;
import gr.greekbusses.misc.Strings;

public class MenuActivity extends AppCompatActivity
{
    BottomNavigationView navView;
    public static String regionString;

    public static ArrayList<Line> lines = new ArrayList<>();
    public static ArrayList<String> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        regionString = bundle.getString("region");

        ActivityMenuBinding binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_lines, R.id.navigation_tickets, R.id.navigation_news).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        readLinesFromDatabase();

        initializeViewObjects();

        navView.setItemIconTintList(null);
    }
    public void initializeViewObjects()
    {
        navView = findViewById(R.id.nav_view);
    }
    public void readLinesFromDatabase()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(Strings.REG).document(regionString).collection(Strings.LINES).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                {
                    Log.d(Strings.TAG, document.getId() + " => " + document.getData());
                    lines.add(new Line(document.get("category"), document.get("line"), document.get("description"), document.get("time"), document.get("timeSat"), document.get("timeSun")));
                    categories.add(Objects.requireNonNull(document.get("category")).toString());
                }
                categories = removeDuplicates(categories);
                categories.sort(Comparator.naturalOrder());
            }
            else
            {
                Log.d(Strings.TAG, "Error getting documents: ", task.getException());
            }
        });
    }
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
        ArrayList<T> newList = new ArrayList<>();

        for (T element : list)
        {
            if (!newList.contains(element))
            {
                newList.add(element);
            }
        }
        return newList;
    }
}