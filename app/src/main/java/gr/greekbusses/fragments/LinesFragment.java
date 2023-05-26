package gr.greekbusses.fragments;

import static gr.greekbusses.activities.MenuActivity.regionString;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import gr.greekbusses.R;
import gr.greekbusses.activities.LineSelectionActivity;
import gr.greekbusses.activities.MenuActivity;
import gr.greekbusses.databinding.FragmentLinesBinding;
import gr.greekbusses.misc.OnSwipeListener;

public class LinesFragment extends Fragment
{
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_lines, container, false);
        LinearLayout linesLinearLayout = view.findViewById(R.id.linesLinearLayout);

        linesLinearLayout.setOnTouchListener(new OnSwipeListener(getContext())
        {
            @Override
            public void onSwipeLeft()
            {
                Toast.makeText(getContext(), "Swipe Left", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeRight()
            {
                Toast.makeText(getContext(), "Swipe Right", Toast.LENGTH_SHORT).show();
            }
        });

        int padding = convertDpToPx(12);
        linesLinearLayout.setPadding(padding, padding, padding, padding);

        for (int i = 0; i < MenuActivity.categories.size(); i++)
        {
            String category = MenuActivity.categories.get(i);
            String buttonText = getResources().getString(R.string.line_cat) + " " + MenuActivity.categories.get(i);
            Button button = new Button(view.getContext());
            button.setText(buttonText);
            button.setHeight(convertDpToPx(80));
            linesLinearLayout.addView(button);

            button.setOnClickListener(viewBtn ->
            {
                Intent intent = new Intent(getContext(), LineSelectionActivity.class);
                intent.putExtra("region", regionString);
                intent.putExtra("category", category);
                startActivity(intent);
            });
        }
        return view;
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        FragmentLinesBinding binding = null;
    }
    public int convertDpToPx(int dp)
    {
        float density = this.getResources().getDisplayMetrics().density;
        float pxValue = dp * density;
        return (int)pxValue;
    }
}