package gr.greekbusses.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import gr.greekbusses.R;
import gr.greekbusses.activities.MenuActivity;
import gr.greekbusses.databinding.FragmentHomeBinding;
import gr.greekbusses.misc.OnSwipeListener;

public class HomeFragment extends Fragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        TextView homeRegionTextView = view.findViewById(R.id.homeRegionTextView);
        homeRegionTextView.setText(MenuActivity.regionString);

        ConstraintLayout fHomeConstraintLayout = view.findViewById(R.id.fHomeConstraintLayout);
        fHomeConstraintLayout.setOnTouchListener(new OnSwipeListener(getContext())
        {
            @Override
            public void onSwipeLeft()
            {
                Toast.makeText(getContext(), "Swipe Left", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        FragmentHomeBinding binding = null;
    }
}