package gr.greekbusses.activities;

import static gr.greekbusses.misc.Strings.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import gr.greekbusses.R;

public class ContactActivity extends AppCompatActivity
{
    EditText nameInputEditText;
    EditText emailInputEditText;
    EditText messageInputEditText;
    Spinner reasonSpinner;
    Button sendButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setTitle(getResources().getText(R.string.app_name) + " | " + getResources().getText(R.string.contact_us));
        initializeViewObjects();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String[] SPINNER_OPTIONS = {
                (String) getResources().getText(R.string.issue_select),
                (String) getResources().getText(R.string.problem1),
                (String) getResources().getText(R.string.problem2),
                (String) getResources().getText(R.string.problem3)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SPINNER_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonSpinner.setAdapter(adapter);

        sendButton.setOnClickListener(view ->
        {
            if (nameInputEditText.getText().toString().isEmpty() || emailInputEditText.getText().toString().isEmpty() || messageInputEditText.getText().toString().isEmpty() || reasonSpinner.getSelectedItemPosition() == 0)
            {
                Toast.makeText(getApplicationContext(), "Enter the data...\nAll fields are required", Toast.LENGTH_SHORT).show();

                if (nameInputEditText.getText().toString().isEmpty())
                {
                    nameInputEditText.setHintTextColor(Color.RED);
                }
                if (emailInputEditText.getText().toString().isEmpty())
                {
                    emailInputEditText.setHintTextColor(Color.RED);
                }
                if (messageInputEditText.getText().toString().isEmpty())
                {
                    messageInputEditText.setHintTextColor(Color.RED);
                }
                if (reasonSpinner.getSelectedItemPosition() == 0)
                {
                    TextView textView = (TextView) reasonSpinner.getChildAt(0);
                    textView.setTextColor(Color.RED);
                }
            }
            else
            {
                if (isEmailValid(emailInputEditText.getText().toString()))
                {
                    Map<String, Object> message = new HashMap<>();
                    message.put("name", nameInputEditText.getText().toString());
                    message.put("email", emailInputEditText.getText().toString());
                    message.put("message", messageInputEditText.getText().toString());
                    message.put("problem", reasonSpinner.getSelectedItem().toString());

                    firestore.collection("User-Messages").add(message)
                            .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                            .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

                    Toast.makeText(getApplicationContext(), "Thank you for your feedback...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    emailInputEditText.setTextColor(Color.RED);
                    emailInputEditText.addTextChangedListener(new TextWatcher()
                    {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
                        {

                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                        {
                            if (isEmailValid(charSequence.toString()))
                            {
                                emailInputEditText.setTextColor(Color.GREEN);
                            }
                            else
                            {
                                emailInputEditText.setTextColor(Color.RED);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable editable)
                        {

                        }
                    });
                    Toast.makeText(getApplicationContext(), "Please enter a valid email...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(ContactActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
    public void initializeViewObjects()
    {
        nameInputEditText = findViewById(R.id.nameInputTextView);
        emailInputEditText = findViewById(R.id.emailInputTextView);
        messageInputEditText = findViewById(R.id.messageInputTextView);
        reasonSpinner = findViewById(R.id.reasonSpinner);
        sendButton = findViewById(R.id.sendButton);
        cancelButton = findViewById(R.id.cancelButton);
    }
    public static boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) return false;
        return pat.matcher(email).matches();
    }
}