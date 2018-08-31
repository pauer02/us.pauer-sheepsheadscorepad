package us.pauer.android.sheepsheadscorepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmailDialogFragment extends DialogFragment {

	private String[] getPlayers(DBAdapter dbAdapter) {
		String[] players = null;
		Cursor cPlayers = dbAdapter.getPlayers();
		if (cPlayers!=null && cPlayers.getCount()>0) {
			cPlayers.moveToFirst();
			players = new String[cPlayers.getCount()];
			int count = 0;
			do {
				players[count] = cPlayers.getString(cPlayers.getColumnIndex(DBAdapter.COL_PLAYER_NAME));
				count++;
			}
			while (cPlayers.moveToNext());
		}
		return players;
	}

	private String getEmailText(int[] scores, String[] players) {


		String text = "";
		for (int i=0; i<players.length; i++) {
			text = text + " " + players[i] + ":";
     		text = text + " " + scores[i] + System.getProperty("line.separator");
		}
		return text;
	}


	public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("E-Mail Results");
			alert.setMessage("Enter E-Mail addresses to send results to:");
			final EditText input = new EditText(alert.getContext());
       	    final DBAdapter dbAdapter = DBAdapter.getInstance(alert.getContext());
 		    char[] defaultEmail = dbAdapter.getSettingValue(Integer.toHexString(R.id.email_default_text)).toCharArray();
		    input.setText(defaultEmail, 0, defaultEmail.length);
			alert.setView(input);
		    int furthestHand = dbAdapter.getFurthestHandScored();
		    int[] scoreArray = new int[5];
			if (furthestHand>0) {
				scoreArray = dbAdapter.getScoresForHandNumber(furthestHand);
			}
			String[] playersArray = getPlayers(dbAdapter);
			final String emailText = getEmailText(scoreArray, playersArray);
	        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		String value = input.getText().toString().trim();
                    if (value.equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Addresses cannot be blank.",Toast.LENGTH_SHORT).show();
                    }
                    String[] addresses = value.trim().split("[,; ]");
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String strDate = sdf.format(cal.getTime());
                    final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/html");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Game Results:"+strDate);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
                    alert.getContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	        	}
	        });

	       alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		// Canceled.
	        	}
	        });

	       return alert.create();
	    }

	}

