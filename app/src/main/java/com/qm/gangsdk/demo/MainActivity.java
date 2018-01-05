package com.qm.gangsdk.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qm.gangsdk.ui.GangSDK;


public class MainActivity extends AppCompatActivity {

    Button btnStart;
    EditText editGameId;
    EditText editUserNick;

    String gameId = null;
    String userNick = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editGameId = (EditText) findViewById(R.id.editGameId);
        editUserNick = (EditText) findViewById(R.id.editUserNick);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameId = editGameId.getText().toString().trim();
                userNick = editUserNick.getText().toString().trim();
                GangSDK.getInstance().startUI(MainActivity.this, gameId, userNick);
            }
        });
    }
}
