package com.example.dan.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    TextView results;
    TextView word_was;
    Button again;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("Hang-Man-Results");

        Intent intent = getIntent();
        String val = intent.getStringExtra("key");
        String word_val = intent.getStringExtra("key2");
        results = (TextView) findViewById(R.id.result1);
        word_was = (TextView) findViewById(R.id.word_is);
        results.setText(val);
        word_was.setText("The word was:  " + word_val);

        again = (Button) findViewById(R.id.btnAgain);





    }


    public void onClick(View v) {

        Intent myIntent = new Intent(Main2Activity.this, MainActivity.class);
        //myIntent.putExtra("key3", score); // could add score for instance
        Main2Activity.this.startActivity(myIntent);
    }

}
