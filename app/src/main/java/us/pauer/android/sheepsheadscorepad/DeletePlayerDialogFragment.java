package us.pauer.android.sheepsheadscorepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

public class DeletePlayerDialogFragment extends DialogFragment {
	
	   public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Delete Player from Game");
			alert.setMessage("Are you sure?");
			final DBAdapter dbAdapter = DBAdapter.getInstance(alert.getContext());
	        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		// Set player active in player table
	        		int pCount = dbAdapter.getPlayers().getCount();
	        		dbAdapter.deletePlayer(pCount);
	        	}
	        });

	       alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		// Canceled.
	        	}
	        });

	       return alert.create();
	    }

	}

