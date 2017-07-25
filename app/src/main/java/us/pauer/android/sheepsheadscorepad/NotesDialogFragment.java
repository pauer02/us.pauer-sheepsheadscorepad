package us.pauer.android.sheepsheadscorepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class NotesDialogFragment extends DialogFragment {

	DBAdapter dbAdapter;
	String note;
	int noteNum;

	public  void setAdapter(DBAdapter dbAdapter) {
		this.dbAdapter = dbAdapter;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public void setNoteNum(int noteNum) {
		this.noteNum = noteNum;
	}

	   public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Enter Note");
			alert.setMessage("Enter the Note Text:");
			final EditText input = new EditText(alert.getContext());
			input.setText(note);
			alert.setView(input);
			final DBAdapter dbAdapter = DBAdapter.getInstance(alert.getContext());
	        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		String value = input.getText().toString().trim();
	        		// Set player active in player table
	        		dbAdapter.updateNote(noteNum, value);
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

