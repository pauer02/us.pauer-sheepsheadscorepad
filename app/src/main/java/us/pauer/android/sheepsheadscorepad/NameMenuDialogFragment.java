package us.pauer.android.sheepsheadscorepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class NameMenuDialogFragment extends DialogFragment {
	int checkedItem = -1;
	DBAdapter dbAdapter;
	String name;
	
	public int getChoice() {
		return checkedItem;
	}
	
	public  void setAdapter(DBAdapter dbAdapter) {
		this.dbAdapter = dbAdapter;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
	    final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("Player Action");
		alert.setPositiveButton("Perform Action", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (checkedItem==0) {
					String removeMessage = "Player Removed";
					if (!dbAdapter.removePlayerFromGame(name)) {
						removeMessage = "Player has scores and cannot be removed.";
					}
					Toast.makeText(alert.getContext(), removeMessage, Toast.LENGTH_SHORT).show();

				} else if (checkedItem==1) {
					Toast.makeText(alert.getContext(), R.string.addedpenalty, Toast.LENGTH_SHORT).show();
					dbAdapter.addPenalty(name);
				} else {
					Toast.makeText(alert.getContext(), R.string.removedpenalty, Toast.LENGTH_SHORT).show();
					dbAdapter.subtractPenalty(name);
				}
			}
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Toast.makeText(alert.getContext(), "Action Canceled", Toast.LENGTH_SHORT).show();
			}
		});
		alert.setSingleChoiceItems(R.array.NameMenuItems, -1, 
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					checkedItem = which;
				}
		});

		return alert.create();
    }
}

