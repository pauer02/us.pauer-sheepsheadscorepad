package us.pauer.android.sheepsheadscorepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class AddPlayerDialogFragment extends DialogFragment {
	
	   public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Add Player to Game");
			alert.setMessage("Enter the Next Player's Name:");
			final EditText input = new EditText(alert.getContext());
			alert.setView(input);
			final DBAdapter dbAdapter = DBAdapter.getInstance(alert.getContext());
	        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		String value = input.getText().toString().trim();
	        		if (value.equalsIgnoreCase("")) {
	        			Toast.makeText(getActivity(), "Name cannot be blank.",Toast.LENGTH_SHORT).show();
	        		}
					else {
						// Set player active in player table
						dbAdapter.addPlayer(value);
					}
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

