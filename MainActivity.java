package com.example.gpacalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText numCoursesEditText;
    LinearLayout coursesLayout;
    Button calculateBtn;
    TextView gpaResult;

    // Instance variables to store total credits and weighted GPA
    float weightedGPA = 0;
    float totalCredits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numCoursesEditText = findViewById(R.id.numCoursesEditText);
        coursesLayout = findViewById(R.id.coursesLayout);
        calculateBtn = findViewById(R.id.calculateBtn);
        gpaResult = findViewById(R.id.gpaResult);

        // When 'Calculate GPA' is pressed
        calculateBtn.setOnClickListener(v -> {
            // Clear previous input fields
            coursesLayout.removeAllViews();

            // Get number of courses entered by user
            int numCourses;
            try {
                numCourses = Integer.parseInt(numCoursesEditText.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Please enter a valid number of courses", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate if the number of courses is a positive integer
            if (numCourses <= 0) {
                Toast.makeText(MainActivity.this, "Please enter a valid number of courses", Toast.LENGTH_SHORT).show();
                return;
            }

            // Reset GPA calculation variables
            weightedGPA = 0;
            totalCredits = 0;

            // Dynamically create input fields for each course
            for (int i = 0; i < numCourses; i++) {
                // Create new LinearLayout to group each course inputs (marks and credit hours)
                LinearLayout courseLayout = new LinearLayout(this);
                courseLayout.setOrientation(LinearLayout.HORIZONTAL);
                courseLayout.setPadding(0, 10, 0, 10); // Add some padding for better spacing

                final EditText marksEditText = new EditText(this);
                marksEditText.setHint("Enter marks for course " + (i + 1));
                marksEditText.setHintTextColor(getResources().getColor(android.R.color.white));  // Set hint color to white
                marksEditText.setTextColor(getResources().getColor(android.R.color.white));  // Set text color to white
                marksEditText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)); // Give it flexible width
                marksEditText.setTypeface(null, android.graphics.Typeface.BOLD);  // Make hint text bold
                courseLayout.addView(marksEditText);

// Create and add credit hours EditText
                final EditText crhEditText = new EditText(this);
                crhEditText.setHint("Enter credit hours for course " + (i + 1));
                crhEditText.setHintTextColor(getResources().getColor(android.R.color.white));  // Set hint color to white
                crhEditText.setTextColor(getResources().getColor(android.R.color.white));  // Set text color to white
                crhEditText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)); // Give it flexible width
                crhEditText.setTypeface(null, android.graphics.Typeface.BOLD);  // Make hint text bold
                courseLayout.addView(crhEditText);


                // Add courseLayout to the main coursesLayout
                coursesLayout.addView(courseLayout);
            }

            // Handle GPA calculation after input fields are shown
            calculateBtn.setOnClickListener(v1 -> {
                // Loop through all the EditText fields, get marks and credit hours, and calculate GPA
                for (int i = 0; i < numCourses; i++) {
                    // Get the marks and credit hours for each course from the input fields
                    LinearLayout courseLayout = (LinearLayout) coursesLayout.getChildAt(i);
                    EditText marksEditText = (EditText) courseLayout.getChildAt(0); // Marks field
                    EditText crhEditText = (EditText) courseLayout.getChildAt(1); // Credit hours field

                    String marksString = marksEditText.getText().toString();
                    String crhString = crhEditText.getText().toString();

                    // Validate input and calculate GPA for each course
                    if (!marksString.isEmpty() && !crhString.isEmpty()) {
                        float marks = Float.parseFloat(marksString);
                        float crh = Float.parseFloat(crhString);

                        // Convert marks to GPA
                        float courseGPA = convertMarksToGPA(marks);

                        // Calculate weighted GPA
                        weightedGPA += courseGPA * crh;
                        totalCredits += crh;
                    } else {
                        // Show message if inputs are missing
                        Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Calculate the final GPA and display it
                if (totalCredits > 0) {
                    float finalGPA = weightedGPA / totalCredits;
                    gpaResult.setText("Your GPA: " + String.format("%.2f", finalGPA));
                } else {
                    Toast.makeText(MainActivity.this, "Please enter valid details for courses", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Method to convert marks to GPA based on the provided scale
    private float convertMarksToGPA(float marks) {
        if (marks >= 86) {
            return 4.00f; // A
        } else if (marks >= 80) {
            return 3.66f; // B+
        } else if (marks >= 75) {
            return 3.33f; // B
        } else if (marks >= 66) {
            return 3.00f; // C+
        } else if (marks >= 56) {
            return 2.66f; // C
        } else if (marks >= 46) {
            return 2.33f; // D+
        } else if (marks >= 36) {
            return 2.00f; // D
        } else {
            return 0.00f; // F
        }
    }
}
