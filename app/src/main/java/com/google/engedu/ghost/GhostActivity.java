/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    TextView gt, txtlabel, xd;
    FastDictionary simpledictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            simpledictionary = new FastDictionary(assetManager.open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
    }

    public void challenge(View view)
    {
        gt = (TextView)findViewById(R.id.ghostText);
        txtlabel = (TextView)findViewById(R.id.gameStatus);
        String cwf = gt.getText().toString();
        if(cwf.length()>=4 && simpledictionary.isWord(cwf))
        {
            txtlabel.setText("User Wins!");
        }
        else
        {
            String completeword = simpledictionary.getAnyWordStartingWith(cwf);
            txtlabel.setText("Computer Wins");
            xd = (TextView) findViewById(R.id.tv);
            xd.setText(completeword);
        }
    }

    public void restart(View view)
    {
        onStart(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView xd = (TextView) findViewById(R.id.tv);
        gt = (TextView)findViewById(R.id.ghostText);
        if(gt.getText().length()>=4 && simpledictionary.isWord(gt.getText().toString()))
        {
            label.setText("Computer Wins!");
            xd.setText("You just created a word!");
        }
        else
        {
            String getAny = simpledictionary.getAnyWordStartingWith(gt.getText().toString());
            if(getAny == null || getAny.length()==gt.getText().toString().length())
                {label.setText("Computer Wins!"); xd.setText("No such word exists! Pela mat kha!");}

            else {
                gt.append("" + getAny.charAt(gt.getText().length()));
                userTurn = true;
                label.setText(USER_TURN);
            }
        }
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        gt = (TextView)findViewById(R.id.ghostText);
        TextView txtlabel = (TextView)findViewById(R.id.gameStatus);
        xd = (TextView) findViewById(R.id.tv);
        char ch = (char)event.getUnicodeChar();
        xd.setText("");
        if((ch<='z' && ch>='a')||(ch<='Z' && ch>='A'))
        {
            gt.append(""+ch);
            txtlabel.setText(COMPUTER_TURN);
            userTurn=false;
            computerTurn();
        }
        else
        {
            Toast.makeText(this,"Enter a valid character!",Toast.LENGTH_SHORT).show();
        }
        return super.onKeyUp(keyCode, event);
    }
}
