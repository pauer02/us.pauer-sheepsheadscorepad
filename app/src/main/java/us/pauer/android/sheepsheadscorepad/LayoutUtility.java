package us.pauer.android.sheepsheadscorepad;

import java.util.Arrays;

import android.app.Activity;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LayoutUtility {
	DBAdapter dbAdapter;
	Activity owningActivity;
	

	public LayoutUtility(Activity owningActivity, DBAdapter dbAdapter) {
		this.dbAdapter = dbAdapter;
		this.owningActivity = owningActivity;
	}

	public void setPlayerLayout() {
		String[] players = getPlayers();
		putPlayersInLayout(players);
		populatePenalties(players);
		if (dbAdapter.isGameActive()) {
			populateHands(players, dbAdapter.getAllHands());
		}
		populateNotes(players);
	}

	private void populatePenalties(String[] players) {
		LinearLayout playerPenaltyLayout = (LinearLayout)owningActivity.findViewById(R.id.playerActionLayout);
		TextView tv = null;
		for (int i=0; i<7; i++) {
			tv = (TextView)playerPenaltyLayout.getChildAt((i*2)+2);
			tv.setText("");
		}
		for (int i=0; players!=null && i<players.length; i++) {
			tv = (TextView)playerPenaltyLayout.getChildAt((i*2)+2);
			int penaltyCount = dbAdapter.getPenaltyCount(players[i]);
			tv.setText(Integer.toString(penaltyCount));
		}
	}
	
	private void populateHands(String[] players, Cursor hands) {
		LinearLayout handLayout = (LinearLayout)owningActivity.findViewById(R.id.linearScoreLayout);
		if (hands.getCount()==0) {  //no hands yet
			//handLayout.addView(getEmptyScoreView(1));
		} else {
			handLayout.removeAllViews();
			while (!hands.isLast()) {
				hands.moveToNext();
				handLayout.addView(getScoreView(hands));
			}
		}

	}
	
	private View getScoreView(Cursor hands) {
		LayoutInflater inflater = owningActivity.getLayoutInflater();
		LinearLayout scoreLayout = (LinearLayout)inflater.inflate(R.layout.hand_layout, null, false);

		int handNum = hands.getInt(hands.getColumnIndex(DBAdapter.COL_HAND_NUMBER));
		//default values
		boolean pickerWon = true;
		boolean gotSchnied = true;
		boolean gotTrick = true;
		boolean leastBool = false;
		boolean handScored = hands.getInt(hands.getColumnIndex(DBAdapter.COL_HAND_SCORED))==1;
		if (handScored) {
			pickerWon = hands.getInt(hands.getColumnIndex(DBAdapter.COL_HAND_PICKER_WIN))==1;
			gotSchnied = hands.getInt(hands.getColumnIndex(DBAdapter.COL_HAND_NO_SCHNEID))==0;
			gotTrick = hands.getInt(hands.getColumnIndex(DBAdapter.COL_HAND_NO_TRICK))==0;
			leastBool = hands.getInt(hands.getColumnIndex(DBAdapter.COL_HAND_LEASTER))==1;
		} 
		fillScoreColors(scoreLayout, handNum, handScored, pickerWon, gotSchnied, gotTrick);
		setButtonSettings(scoreLayout, handNum, handScored, pickerWon, gotSchnied, gotTrick, leastBool);
		
		return scoreLayout;
	}

	private void setButtonSettings(LinearLayout scoreLayout, int handNum, boolean handScored, 
			boolean pickerWon, boolean gotSchneid, boolean gotTrick, boolean leastBool) {
		Button pickerStatus = (Button)scoreLayout.findViewById(R.id.buttonPickerStatus);
		Button scoreHand = (Button)scoreLayout.findViewById(R.id.buttonScore);
		if (!handScored) {
			pickerStatus.setText(R.string.won);
			pickerStatus.setTag(R.string.won);
			scoreHand.setText(R.string.scoreHand);
			setScoresClickable(scoreLayout, true);
		} else {
			setScoresClickable(scoreLayout, false);
			scoreHand.setText(R.string.unlockHand);
			pickerStatus.setClickable(false);
			if (leastBool) {
				pickerStatus.setText(R.string.leaster);
				pickerStatus.setTag(R.string.leaster);
			}
			else {
				if (pickerWon) {
					if (gotSchneid) {
						pickerStatus.setText(R.string.won);
						pickerStatus.setTag(R.string.won);
					} else {
						if (gotTrick) {
							pickerStatus.setText(R.string.wonNoSchneid);
							pickerStatus.setTag(R.string.wonNoSchneid);
						} else {
							pickerStatus.setText(R.string.wonNoTrick);
							pickerStatus.setTag(R.string.wonNoTrick);
						}
					}
				} else {
					if (gotSchneid) {
						pickerStatus.setText(R.string.lost);
						pickerStatus.setTag(R.string.lost);
					} else {
						if (gotTrick) {
							pickerStatus.setText(R.string.lostNoSchneid);
							pickerStatus.setTag(R.string.lostNoSchneid);
						} else {
							pickerStatus.setText(R.string.LostNoTrick);
							pickerStatus.setTag(R.string.LostNoTrick);
						}
					}
				}
			}
		}
		Button doublerButton = (Button)scoreLayout.findViewById(R.id.buttonDoubler);
		doublerButton.setClickable(false);
		doublerButton.setTag(R.string.handKey, Integer.toString(handNum));
		int currentDblsThisHand = dbAdapter.getDoublersForHand(handNum);
		TextView doubleText = (TextView)scoreLayout.findViewById(R.id.buttonDoubler);
		String dbText = "X "+(int)Math.round(Math.pow(2, currentDblsThisHand));
		doubleText.setText(dbText);
		doublerButton.setClickable(true);
		
	}

	public void setScoresClickable(LinearLayout scoreLayout, boolean clickable) {
		scoreLayout.setClickable(false);
		int childCount = scoreLayout.getChildCount();
		for (int i=0; i<childCount; i++) {
			scoreLayout.getChildAt(i).setClickable(clickable);
		}
	}

	public void resetColorsToOriginalRoles(LinearLayout scoreLine, int handNum) {
		int[] colorArray = new int[7];
		int[] playerScores = new int[7];
		int[] playerRoles = dbAdapter.getRolesDescriptionsForHand(handNum);
		for (int i=0; i<colorArray.length; i++) {
			// here we fetch the default colors
			colorArray[i] = ScoreUtility.getInstance().getOriginalColors(playerRoles[i]);
		}
		playerScores = dbAdapter.getScoresForHandNumber(handNum);
		fillScoreColors(scoreLine, colorArray, playerScores, handNum, false);
		Button pickerStatus = (Button)scoreLine.findViewById(R.id.buttonPickerStatus);
		pickerStatus.setClickable(true);
		Button doublerButton = (Button)scoreLine.findViewById(R.id.buttonDoubler);
		doublerButton.setClickable(true);
		//setButtonSettings(scoreLine, handNum, false, true, true,
		//		true, false);
	}
	
	
	private void fillScoreColors(LinearLayout scoreLayout, int handNum, boolean handScored,
			boolean pickerWon, boolean gotSchneid, boolean gotTrick) {
		int[] colorArray = new int[7];
		int[] playerScores = new int[7];
		if (!handScored) {
			Arrays.fill(colorArray, ScoreUtility.getInstance().getCorrectColor(R.string.sittingOut));
		} else {
			int[] playerRoles = dbAdapter.getRolesDescriptionsForHand(handNum);
			for (int i=0; i<colorArray.length; i++) {
				colorArray[i] = ScoreUtility.getInstance().getCorrectColor(playerRoles[i], 
						pickerWon, gotSchneid, gotTrick);
			}
			playerScores = dbAdapter.getScoresForHandNumber(handNum);
		}
		fillScoreColors(scoreLayout, colorArray, playerScores, handNum, handScored);
		
	}

	private void fillScoreColors(LinearLayout handLayout, int[] colorArray, 
			int[] scores, int handNum, boolean handScored) {
		LinearLayout scoreLayout = (LinearLayout)handLayout.getChildAt(0);
		TextView num = (TextView)scoreLayout.getChildAt(0);
		num.setText(Integer.toString(handNum));
		
		//((TextView)(scoreLayout.getChildAt(0))).setText(Integer.valueOf(handNum));
		for (int i=0; i<colorArray.length; i++) {
			TextView tv = (TextView)(scoreLayout.getChildAt((i*2)+2));
			tv.setBackgroundResource(colorArray[i]);
			tv.setTag(colorArray[i]);
			if (handScored) {
				tv.setText(Integer.toString(scores[i]));
			}
		}
	}

	private void populateNotes(String[] players) {
		LinearLayout playerNotesLayout = (LinearLayout)owningActivity.findViewById(R.id.playerNotesLayout);
		TextView et = null;
		for (int i=0; players!=null && i<players.length; i++) {
			et = (TextView)playerNotesLayout.getChildAt((i*2)+2);
			String note = dbAdapter.getNote(players[i]);
			et.setText(note);
		}
		
	}

	private String[] getPlayers() {
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

	private void putPlayersInLayout(String[] players) {
		LinearLayout playerNames = (LinearLayout)owningActivity.findViewById(R.id.playerNameLayout);
		TextView tv = null;
		for (int i=0; i<7; i++) {
			tv = (TextView)playerNames.getChildAt((i*2)+2);
			tv.setText("");
		}
		for (int i=0; players!=null && i<players.length; i++) {
			tv = (TextView)playerNames.getChildAt((i*2)+2);
			tv.setText(players[i]);
		}
	}


}
