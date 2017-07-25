package us.pauer.android.sheepsheadscorepad;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void onCancelClick(View view) {
        finish();
    }


}
