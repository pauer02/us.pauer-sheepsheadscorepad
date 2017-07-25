package us.pauer.android.sheepsheadscorepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class StartNewGameDialogFragment extends DialogFragment {
	
	   public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Start a New Game?");
			alert.setMessage("Are you sure? (All current hands scored will become unavailable.)");
			final DBAdapter dbAdapter = DBAdapter.getInstance(alert.getContext());
	        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		// Set player active in player table
	        		boolean newGame = dbAdapter.startNewGame();
	        	}
	        });

	       alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        	}
	        });

	       return alert.create();
	    }

	}

