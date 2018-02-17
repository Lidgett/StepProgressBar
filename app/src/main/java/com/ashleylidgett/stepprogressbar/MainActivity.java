package com.ashleylidgett.stepprogressbar;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{

    private StepProgressBar mStepProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStepProgressBar = findViewById(R.id.progressBar);
        mStepProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        mStepProgressBar.setCompleteColor(ContextCompat.getColor(this, R.color.green));
        mStepProgressBar.setCurrentColor(ContextCompat.getColor(this, R.color.orange));

        final EditText addPointInput = findViewById(R.id.add_point_input);
        final EditText addPointCompletedInput = findViewById(R.id.add_point_completed_input);

        Button setPoints = findViewById(R.id.set_points);
        setPoints.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mStepProgressBar.setProgress(Integer.parseInt(addPointInput.getText().toString()));
                mStepProgressBar.setProgressComplete(Integer.parseInt(addPointCompletedInput.getText().toString()));
            }
        });

    }
}
