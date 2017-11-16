package com.lingdian.mylogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edit_code= (EditText) findViewById(R.id.edit_code);
        float textSize= edit_code.getTextSize();
        Log.v("xhw","textSize "+textSize);
    }
    public void onMyClick(View view){
        startActivity(new Intent(this,Main2Activity.class));
    }
}
