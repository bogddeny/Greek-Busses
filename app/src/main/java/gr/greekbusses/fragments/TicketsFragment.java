package gr.greekbusses.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import gr.greekbusses.R;
import gr.greekbusses.activities.MenuActivity;
import gr.greekbusses.databinding.FragmentTicketsBinding;
import gr.greekbusses.misc.OnSwipeListener;

public class TicketsFragment extends Fragment
{
    LinearLayout ticketsLinearLayout;

    String[] ticketTitle = new String[]{"Zone A Simple Ticket | 1.20€", "Zone A Reduced Ticket | 0.60€", "Zone B Simple Ticket | 1.70€", "Zone B Reduced Ticket | 0.80€"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);
        ticketsLinearLayout = view.findViewById(R.id.ticketsLinearLayout);

        ticketsLinearLayout.setOnTouchListener(new OnSwipeListener(getContext())
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

        if (MenuActivity.regionString.equals("Achaea"))
        {
            createTickedCard(R.drawable.ticket_a_simple, ticketTitle[0], view);
            createTickedCard(R.drawable.ticket_a_reduced, ticketTitle[2], view);
            createTickedCard(R.drawable.ticket_b_simple, ticketTitle[1], view);
            createTickedCard(R.drawable.ticket_b_reduced, ticketTitle[3], view);
        }

        return view;
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        FragmentTicketsBinding binding = null;
    }
    public void createTickedCard(int resInt, String str, View view)
    {
        int dp_8 = convertDpToPx(8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp_8, dp_8, dp_8, dp_8);
        params.gravity = Gravity.CENTER;

        CardView cardView = new CardView(view.getContext());
        ImageView imageView = new ImageView(view.getContext());
        TextView textView = new TextView(view.getContext());

        cardView.setLayoutParams(params);
        cardView.setRadius(dp_8);
        cardView.setCardBackgroundColor(Color.WHITE);
        cardView.setCardElevation(dp_8);
        cardView.setPreventCornerOverlap(true);
        cardView.setUseCompatPadding(true);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp_8, dp_8, dp_8, dp_8);
        params2.width = convertDpToPx(200);
        params2.height = convertDpToPx(175);
        params2.gravity = Gravity.CENTER;

        imageView.setImageResource(resInt);
        imageView.setLayoutParams(params2);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp_8, dp_8, dp_8, dp_8);
        params3.gravity = Gravity.CENTER_HORIZONTAL;

        textView.setLayoutParams(params3);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setText(str);
        textView.setTextColor(Color.BLACK);

        cardView.addView(imageView);
        cardView.addView(textView);
        ticketsLinearLayout.addView(cardView);
    }
    public int convertDpToPx(int dp)
    {
        float density = this.getResources().getDisplayMetrics().density;
        float pxValue = dp * density;
        return (int)pxValue;
    }
}