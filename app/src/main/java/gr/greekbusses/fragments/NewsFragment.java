package gr.greekbusses.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import gr.greekbusses.R;
import gr.greekbusses.misc.OnSwipeListener;

public class NewsFragment extends Fragment
{
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        FrameLayout fNewsFrameLayout = view.findViewById(R.id.fNewsFrameLayout);
        fNewsFrameLayout.setOnTouchListener(new OnSwipeListener(getContext())
        {
            @Override
            public void onSwipeRight()
            {
                Toast.makeText(getContext(), "Swipe Right", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        gr.greekbusses.databinding.FragmentNewsBinding binding = null;
    }
}