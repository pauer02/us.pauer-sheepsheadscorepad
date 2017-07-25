package us.pauer.android.sheepsheadscorepad;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
        final DBAdapter dbAdapter = DBAdapter.getInstance(this.getApplicationContext());
        boolean doubleOnBump = dbAdapter.getSettingValue(Integer.toHexString(R.id.double_on_bump_checkbox))
                .equalsIgnoreCase("true");
        String doublerLimit = dbAdapter.getSettingValue(Integer.toHexString(R.id.double_limit_text));
        String emailDefault = dbAdapter.getSettingValue(Integer.toHexString(R.id.email_default_text));
        CheckBox dobCB = (CheckBox)this.findViewById(R.id.double_on_bump_checkbox);
        dobCB.setChecked(doubleOnBump);
        TextView dlText = (TextView)this.findViewById(R.id.double_limit_text);
        dlText.setText((CharSequence)doublerLimit);
        TextView emailText = (TextView)this.findViewById(R.id.email_default_text);
        emailText.setText((CharSequence)emailDefault);

	}


    public void onCancelClick(View view) {
        finish();
    }

    public void onOkClick(View view) {
        final DBAdapter dbAdapter = DBAdapter.getInstance(this.getApplicationContext());
        String[] entrySet = new String[6];
        CheckBox dobCB = (CheckBox)this.findViewById(R.id.double_on_bump_checkbox);
        entrySet[0] = Integer.toHexString(R.id.double_on_bump_checkbox);
        if (dobCB.isChecked()) {
            entrySet[1] = "true";
        } else {
            entrySet[1] = "false";
        }
        TextView dlText = (TextView)this.findViewById(R.id.double_limit_text);
        entrySet[2]=Integer.toHexString(R.id.double_limit_text);
        entrySet[3]=dlText.getText().toString();
        TextView emailText = (TextView)this.findViewById(R.id.email_default_text);
        entrySet[4]=Integer.toHexString(R.id.email_default_text);
        entrySet[5]=emailText.getText().toString();
        dbAdapter.updateSettings(entrySet);
        finish();
    }
}
