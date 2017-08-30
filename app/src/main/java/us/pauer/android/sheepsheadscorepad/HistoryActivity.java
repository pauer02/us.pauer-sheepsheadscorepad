package us.pauer.android.sheepsheadscorepad;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static us.pauer.android.sheepsheadscorepad.R.id.editTextGameDate1;
import static us.pauer.android.sheepsheadscorepad.R.id.historyPlayerNamesLayout1;

public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        final DBAdapter dbAdapter = DBAdapter.getInstance(this.getApplicationContext());
        LinearLayout playNames1 = (LinearLayout)this.findViewById(R.id.historyPlayerNamesLayout1);
        LinearLayout playScores1 = (LinearLayout)this.findViewById(R.id.scorelayout1);
        LinearLayout playNames2 = (LinearLayout)this.findViewById(R.id.historyPlayerNamesLayout2);
        LinearLayout playScores2 = (LinearLayout)this.findViewById(R.id.scorelayout2);
        LinearLayout playNames3 = (LinearLayout)this.findViewById(R.id.historyPlayerNamesLayout3);
        LinearLayout playScores3 = (LinearLayout)this.findViewById(R.id.scorelayout3);
        TextView dtText1 = (TextView)this.findViewById(R.id.editTextGameDate1);
        TextView dtText2 = (TextView)this.findViewById(R.id.editTextGameDate2);
        TextView dtText3 = (TextView)this.findViewById(R.id.editTextGameDate3);
        Cursor c = dbAdapter.getHistory();
        int thisGame = -1;
        int holdGame = -1;
        if (c.moveToNext())
        {
            dtText1.setText(c.getString(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_DATE)));
            thisGame = c.getInt(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_GAME));
            holdGame = thisGame;
            int i = 1;
            while (thisGame == holdGame)
            {
                AddTextToLayout(playNames1.getChildAt(i),
                        c.getString(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_NAME)));
                AddTextToLayout(playScores1.getChildAt(i),
                        c.getString(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_SCORE)));
                i = i+2;
                if (c.moveToNext()) {
                    thisGame = c.getInt(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_GAME));
                }
                else
                {
                    break;
                }
            }
        }
        holdGame = thisGame;
        int i = 1;
        if (!c.isAfterLast()) {
            dtText2.setText(c.getString(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_DATE)));
            while (thisGame == holdGame) {
                AddTextToLayout(playNames2.getChildAt(i),
                        c.getString(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_NAME)));
                AddTextToLayout(playScores2.getChildAt(i),
                        c.getString(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_SCORE)));
                i = i + 2;
                if (c.moveToNext()) {
                    thisGame = c.getInt(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_GAME));
                } else {
                    break;
                }
            }
        }
        holdGame = thisGame;
        i = 1;
        if (!c.isAfterLast()) {
            dtText3.setText(c.getString(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_DATE)));
            while (thisGame == holdGame) {
                AddTextToLayout(playNames3.getChildAt(i),
                        c.getString(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_NAME)));
                AddTextToLayout(playScores3.getChildAt(i),
                        c.getString(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_SCORE)));
                i = i + 2;
                if (c.moveToNext()) {
                    thisGame = c.getInt(c.getColumnIndex(DBAdapter.COL_HIST_PLAYER_GAME));
                } else {
                    break;
                }
            }
        }

    }

    private void AddTextToLayout(View childAt, String string) {
        View stuff = childAt;
        ((TextView)childAt).setText(string);
    }

    public void onCancelClick(View view) {
        finish();
    }




}
