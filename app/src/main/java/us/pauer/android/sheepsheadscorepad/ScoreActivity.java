package us.pauer.android.sheepsheadscorepad;



import java.util.Observable;
import java.util.Observer;


import android.os.Bundle;
import android.preference.EditTextPreference;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends Activity implements Observer  {
	
	DBAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbAdapter = DBAdapter.getInstance(this);
		dbAdapter.open();
		dbAdapter.registerObserver(this);
		setContentView(R.layout.activity_score);
		drawInterface();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_score, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return MenuChoice(item);
	}
	

	private boolean MenuChoice(MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.new_game:
				newGameDialog();
				return true;
			case R.id.add_player:
				nameEntryDialog();
				return true;
			case R.id.menu_settings:
				Intent i = new Intent(this, SettingsActivity.class);
				startActivity(i);
				return true;
	/*	case R.id.stats:
			Intent j = new Intent(this, StatsActivity.class);
			startActivity(j);
			return true;  */
			case R.id.email:
				sendResultsDialog();
				return true;
			case R.id.help:
				Intent k = new Intent(this, HelpActivity.class);
				startActivity(k);
				return true;
		}
		
		return false;
	}
	
	private void nameEntryDialog() {
		int numPlayers = dbAdapter.getPlayers().getCount();
		if (numPlayers==7) {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("     You already have 7 players. ");
            builder1.setCancelable(true);
            AlertDialog alert11 = builder1.create();
            alert11.show();
		} else {
			AddPlayerDialogFragment playerDialog = new AddPlayerDialogFragment();
			playerDialog.show(getFragmentManager(), "Add Player");
		}
	}

    private void sendResultsDialog() {
        EmailDialogFragment emailDialog = new EmailDialogFragment();
        emailDialog.show(getFragmentManager(), "Send Results");
    }

	private void newGameDialog() {
		StartNewGameDialogFragment newGameDialog = new StartNewGameDialogFragment();
		newGameDialog.show(getFragmentManager(), "New Game");
	}
	
	public void onNameClick(View nameView) {
		String name = ((TextView)nameView).getText().toString().trim();
		if (name.equalsIgnoreCase("")) {
			Toast.makeText(this, "No Player Selected",Toast.LENGTH_SHORT).show();
		} else {
			NameMenuDialogFragment nameMenuDialog = new NameMenuDialogFragment();
			nameMenuDialog.setAdapter(dbAdapter);
			nameMenuDialog.setName(name);
			nameMenuDialog.show(getFragmentManager(), "Player Menu");
		}		
	}
	
	public void onNotesClick(View notesView) {
		String notes = ((TextView)notesView).getText().toString();
		NotesDialogFragment notesDialog = new NotesDialogFragment();
		notesDialog.setAdapter(dbAdapter);
		notesDialog.setNote(notes);
		notesDialog.setNoteNum(Integer.valueOf((String)notesView.getTag()));
		notesDialog.show(getFragmentManager(), "Update Note");
	}
	
	public void onScoreClick(View scoreView) {
		int bgColor = (Integer)scoreView.getTag();
		try {
			bgColor = ScoreUtility.getInstance().getNextStateColor(this,bgColor);
		} catch (NoColorMatchException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}
		scoreView.setBackgroundResource(bgColor);
		scoreView.setTag(bgColor);
	}
	
	public void onScoreHandButtonClick(View scoreButton) {
		LinearLayout parentLayout = (LinearLayout)this.findViewById(R.id.linearScoreLayout);
		LinearLayout scoreLine = (LinearLayout)scoreButton.getParent().getParent().getParent();
		Button scoreHandButton = (Button)scoreButton;
		TextView handText = (TextView)scoreLine.getChildAt(0);
		int handNumber = Integer.parseInt((String)handText.getText());
		if (scoreHandButton.getText().toString().equalsIgnoreCase(
				getResources().getString(R.string.unlockHand))) {
			LayoutUtility lu = new LayoutUtility(this, dbAdapter);
			lu.setScoresClickable(scoreLine, true);
			lu.resetColorsToOriginalRoles((LinearLayout)(scoreLine.getParent()), handNumber);

			scoreHandButton.setText(R.string.scoreHand);
		} else {
	
			TextView doublerText = (TextView)scoreLine.findViewById(R.id.buttonDoubler);
			int timesValue = Integer.parseInt(((String) doublerText.getText()).trim().substring(2));
			int doublers = (int)Math.round(Math.log(timesValue)/Math.log(2.0d));
			//(LinearLayout)findViewById(R.id.masterlayout).findViewWithTag(R.string.handKey); 
			int[] scoresForPreviousHand = new int[7];
			if (handNumber>1) {
				scoresForPreviousHand = dbAdapter.getScoresForHandNumber(handNumber-1);
			} 
			try {
				int[][] scoresPlayers = ScoreUtility.getInstance().scoreHand(scoreLine, 
						scoresForPreviousHand, doublers);
				int handResult = (Integer)((Button)scoreLine.findViewById(R.id.buttonPickerStatus)).getTag();
				boolean pickerWon = handResult==R.string.won ||	handResult==R.string.wonNoSchneid
						|| handResult==R.string.wonNoTrick;
				boolean noTrick = handResult==R.string.wonNoTrick || handResult==R.string.LostNoTrick;
				boolean noSchneid = noTrick || handResult==R.string.wonNoSchneid || handResult==R.string.lostNoSchneid;
				dbAdapter.scoreHand(handNumber, doublers, pickerWon, noSchneid, noTrick, 
						scoresPlayers[1], scoresPlayers[0]);
			} catch (NoColorMatchException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			} catch (NoPickerException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			} catch (TooManyPickersException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			} catch (TooManyPartnersException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			}
			drawInterface();
		}
		
		
	}

	public void onPickerStatusButtonClick(View scoreButton) {
		Button pickerStatus = (Button)scoreButton;
		int currentStatus = (Integer)pickerStatus.getTag();
		try {
			currentStatus = ScoreUtility.getInstance().getNextPickerStatus(this, currentStatus);
		} catch (NoStatusMatchException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}
		pickerStatus.setText(currentStatus);
		pickerStatus.setTag(currentStatus);
	}
	
	public void onDoublerButtonClick(View doublerButton) {
		int doublerLimit = Integer.parseInt(dbAdapter.getSettingValue(Integer.toHexString(R.id.double_limit_text)));
		LinearLayout doublerLayout = (LinearLayout)doublerButton.getParent();
		int handNumber = Integer.parseInt((String)doublerButton.getTag(R.string.handKey));
		//int handNumber = ((Integer)doublerButton.getTag(DBAdapter.HAND_NUMBER_KEY)).intValue();
		TextView doubleText = (TextView)doublerLayout.findViewById(R.id.buttonDoubler);
		String thisText = doubleText.getText().toString();
		int  doublerTotal = Integer.parseInt(thisText.substring(2));

		int currentDblsThisHand = (int)Math.round(Math.log(doublerTotal)/Math.log(2.0d));
		int newDoubleValue = (currentDblsThisHand+1) % doublerLimit;
		doubleText.setText("X "+Math.round(Math.pow(2,newDoubleValue)));
		dbAdapter.setDoublersForHand(handNumber, newDoubleValue, false);
	}

	private void drawInterface() {
		// set overall view
		LayoutUtility lu = new LayoutUtility(this, dbAdapter);
		lu.setPlayerLayout();
	}
	
	@Override
	public void update(Observable observable, Object data) {
		drawInterface();
	}

	
}
