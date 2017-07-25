package us.pauer.android.sheepsheadscorepad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void onCancelClick(View view) {
        finish();
    }


}
