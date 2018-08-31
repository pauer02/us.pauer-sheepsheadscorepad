/**
 * ColorUtility hosts static methods to handle the state transitions as the score person clicks on the score
 * TextView to set what role the player has in the hand (Picker, Partner, Player, Sitting Out)
 */
package us.pauer.android.sheepsheadscorepad;

import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author cpauer
 *
 */
public class ScoreUtility {
	
	private HashMap<String, Integer> colorMap = new HashMap<String, Integer>(20);
	
	private static ScoreUtility instance = null;
	
	private int[] colorStack = new int[]{R.color.sit_out_clr,
			R.color.player_clr,
			R.color.picker_clr,
			R.color.partner_clr};
	
	private int[] stateStack = new int[]{
		R.string.sittingOut,
		R.string.player,
		R.string.picker,
		R.string.partner
	};

	private int[] correctColorStack = new int[]{
   	 R.color.picker_won_clr, R.color.picker_won_no_schneid_clr, R.color.picker_won_no_trick_clr,
   	 R.color.picker_lost_clr, R.color.picker_lost_no_schneid_clr, R.color.picker_lost_no_trick_clr,
   	 R.color.partner_won_clr, R.color.partner_won_no_schneid_clr, R.color.partner_won_no_trick_clr,
   	 R.color.partner_lost_clr, R.color.partner_lost_no_schneid_clr, R.color.partner_lost_no_trick_clr,
   	 R.color.player_won_clr, R.color.player_won_no_schneid_clr, R.color.player_won_no_trick_clr,
   	 R.color.player_lost_clr, R.color.player_lost_no_schneid_clr, R.color.player_lost_no_trick_clr};
	
	private int[] pickerStatusStringStack = new int[] {R.string.won,
			R.string.wonNoSchneid,
			R.string.wonNoTrick,
			R.string.lost,
			R.string.lostNoSchneid,
			R.string.LostNoTrick,
			R.string.leaster};
	
	private ScoreUtility() {
		super();
		initialize();
	}
	
	public static ScoreUtility getInstance() {
		if (instance==null) {
			instance = new ScoreUtility();
			
		} 
		return instance;
		
	}

	public  int getNextStateColor(Context context, int bgColor) throws NoColorMatchException {
		for (int i=0; i<colorStack.length; i++) {
			if (colorStack[i]==bgColor) {
				return colorStack[(i+1) % colorStack.length];
			}
		}
		throw new NoColorMatchException("Color not found in color stack.");
	}

	public  int getNextPickerStatus(Context context, int currentStatus) throws NoStatusMatchException {
		for (int i=0; i<pickerStatusStringStack.length; i++) {
			if (pickerStatusStringStack[i]==currentStatus) {
				return pickerStatusStringStack[(i+1) % pickerStatusStringStack.length];
			}
		}
		throw new NoStatusMatchException("Status not found in status stack.");
	}
	
	
	private void initialize() {
		for (int i=0; i<colorStack.length; i++) {
			colorMap.put(Integer.toString(stateStack[i]), Integer.valueOf(colorStack[i]));
		}
	
		String pickerwonyesyes = Integer.toString(R.string.picker)+"true"+"true"+"true";
		colorMap.put(pickerwonyesyes, Integer.valueOf(correctColorStack[0]));
		
		String pickerwonnoyes = Integer.toString(R.string.picker)+"true"+"false"+"true";
		colorMap.put(pickerwonnoyes, Integer.valueOf(correctColorStack[1]));
		
		String pickerwonnono = Integer.toString(R.string.picker)+"true"+"false"+"false";
		colorMap.put(pickerwonnono, Integer.valueOf(correctColorStack[2]));

		String pickerlostyesyes = Integer.toString(R.string.picker)+"false"+"true"+"true";
		colorMap.put(pickerlostyesyes, Integer.valueOf(correctColorStack[3]));
		
		String pickerlostnoyes = Integer.toString(R.string.picker)+"false"+"false"+"true";
		colorMap.put(pickerlostnoyes, Integer.valueOf(correctColorStack[4]));
		
		String pickerlostnono = Integer.toString(R.string.picker)+"false"+"false"+"false";
		colorMap.put(pickerlostnono, Integer.valueOf(correctColorStack[5]));

		String partnerwonyesyes = Integer.toString(R.string.partner)+"true"+"true"+"true";
		colorMap.put(partnerwonyesyes, Integer.valueOf(correctColorStack[6]));
		
		String partnerwonnoyes = Integer.toString(R.string.partner)+"true"+"false"+"true";
		colorMap.put(partnerwonnoyes, Integer.valueOf(correctColorStack[7]));
		
		String partnerwonnono = Integer.toString(R.string.partner)+"true"+"false"+"false";
		colorMap.put(partnerwonnono, Integer.valueOf(correctColorStack[8]));

		String partnerlostyesyes = Integer.toString(R.string.partner)+"false"+"true"+"true";
		colorMap.put(partnerlostyesyes, Integer.valueOf(correctColorStack[9]));
		
		String partnerlostnoyes = Integer.toString(R.string.partner)+"false"+"false"+"true";
		colorMap.put(partnerlostnoyes, Integer.valueOf(correctColorStack[10]));
		
		String partnerlostnono = Integer.toString(R.string.partner)+"false"+"false"+"false";
		colorMap.put(partnerlostnono, Integer.valueOf(correctColorStack[11]));
		
		String playerwonyesyes = Integer.toString(R.string.player)+"true"+"true"+"true";
		colorMap.put(playerwonyesyes, Integer.valueOf(correctColorStack[12]));
		
		String playerwonnoyes = Integer.toString(R.string.player)+"true"+"false"+"true";
		colorMap.put(playerwonnoyes, Integer.valueOf(correctColorStack[13]));
		
		String playerwonnono = Integer.toString(R.string.player)+"true"+"false"+"false";
		colorMap.put(playerwonnono, Integer.valueOf(correctColorStack[14]));

		String playerlostyesyes = Integer.toString(R.string.player)+"false"+"true"+"true";
		colorMap.put(playerlostyesyes, Integer.valueOf(correctColorStack[15]));
		
		String playerlostnoyes = Integer.toString(R.string.player)+"false"+"false"+"true";
		colorMap.put(playerlostnoyes, Integer.valueOf(correctColorStack[16]));
		
		String playerlostnono = Integer.toString(R.string.player)+"false"+"false"+"false";
		colorMap.put(playerlostnono, Integer.valueOf(correctColorStack[17]));
	}

	public int getCorrectColor(int roleResourceDesc) {
		String roleString = Integer.toString(roleResourceDesc);
		return ((Integer)colorMap.get(roleString)).intValue();
	}
	
	private int getStatusBasedOnColor(int colorResId) throws NoColorMatchException {
		for (int i=0; i<colorStack.length; i++) {
			if (colorStack[i]==colorResId) {
				return stateStack[i];
			}
		}
		throw new NoColorMatchException("Color not found in color stack.");
	}
	
	/**
	 * getCorrectColor will take a roleResourceDesc (for example R.string.partner), the other boolean
	 *  arguments representing the state of the hand, and return the appropriate single color for that
	 *  player display for that hand.
	 * @param roleResourceDesc String resource representing the player role in the hand
	 * @param won  boolean, did picker win
	 * @param schneid   boolean, was schneider achieved
	 * @param trick  boolean, was a trick gotten
	 * @return  color resource indicator in this apps color resource listings
	 */
	public int getCorrectColor(int roleResourceDesc, boolean won, boolean schneid, boolean trick) {
		if (roleResourceDesc==R.string.sittingOut) {
			return colorMap.get(Integer.toString(roleResourceDesc));
		}
		String stuff = Integer.toString(roleResourceDesc)+Boolean.toString(won)+
				Boolean.toString(schneid)+Boolean.toString(trick);
		return Integer.valueOf(colorMap.get(Integer.toString(roleResourceDesc)+Boolean.toString(won)+
				Boolean.toString(schneid)+Boolean.toString(trick)));
	}

	public int getOriginalColors(int roleResourceDesc) {
    	return colorMap.get(Integer.toString(roleResourceDesc));
	}

	
	
	/**
	 * scoreHand will calculate the new score values and also the player roles from the layout and the
	 *   previous hand scores.  Returns an 2x7 int array.  The first 7 are the new score values,
	 *   the second 7 are the player roles.
	 * @param handLayout  UI view of the hand, used to calculate player roles and scoring
	 * @param previousScores   add scores to previous scores
	 * @param doublers   how many doublers to be applied
	 * @return  2x7 int array: 7 score values, 7 player role indicators
	 * @throws NoColorMatchException
	 * @throws NoPickerException
	 * @throws TooManyPickersException
	 * @throws TooManyPartnersException
	 */
	public int[][] scoreHand(LinearLayout handLayout, int[] previousScores, int doublers) throws 
			NoColorMatchException, NoPickerException, TooManyPickersException, TooManyPartnersException {
		int[] newScores = new int[7];
		int[] playerArray = new int[7];
		int doublerMult = 1;
		for (int i=0; i<doublers; i++) {
			doublerMult = doublerMult*2;
		}

		int bump = 2;
		int handResult = (Integer)((Button)handLayout.findViewById(R.id.buttonPickerStatus)).getTag();
		int baseScoreStatus = handResult==R.string.won || handResult==R.string.lost || handResult==R.string.leaster ? 1 :
			handResult==R.string.wonNoSchneid || handResult==R.string.lostNoSchneid ? 2 : 3;
		baseScoreStatus = baseScoreStatus * doublerMult;
		boolean won = handResult==R.string.won || handResult==R.string.wonNoSchneid || handResult==R.string.wonNoTrick
				|| handResult == R.string.leaster;
		boolean noTrick = handResult==R.string.wonNoTrick || handResult==R.string.LostNoTrick;
		boolean noSchneid = handResult==R.string.wonNoSchneid || handResult==R.string.lostNoSchneid || noTrick; 
		//get total number of players and whether partner
		for (int i=0; i<7; i++) {
			TextView tv = (TextView)handLayout.getChildAt((i*2)+2); 
			playerArray[i] = getStatusBasedOnColor((Integer) tv.getTag());
		}
		//get partners, picker, players
		int numPlayers = 0;
		int numPartners = 0;
		boolean havePicker = false;
		for (int i=0; i<7; i++) {
			
			switch (playerArray[i]) {
			case R.string.picker:
				if (havePicker) {
					throw new TooManyPickersException("You are only allowed one picker.");
				}
				havePicker = true;
				numPlayers++;
				break;
			case R.string.partner:
				numPartners++;
				numPlayers++;
				break;
			case R.string.player:
				numPlayers++;
				break;
			default:
				break;
			}
		}
		if (!havePicker) {
			throw new NoPickerException("You must choose a picker (in 2 handed, or Leaster, Picker=Winner)");
		}
		if (numPartners>2) {
			throw new TooManyPartnersException("Limit of 2 partners max.");
		}
		if ((numPlayers==2) && (numPartners>0)) {
			throw new TooManyPartnersException("No partners in 2-handed.");
		}
		if ((numPlayers - (numPartners+1))<0) {
			throw new TooManyPartnersException("Your number of partners plus picker exceeds the number of other players.");
		}
		if ((handResult==R.string.leaster) && (numPartners > 0)) {
			throw new TooManyPartnersException("No partner should be chosen for Leaster");
		}

		int playerMultiplier = 1;
		int pickerModifier = 2;
		int partnerModifier = 1;
		if (numPartners==0) {
			pickerModifier = numPlayers-1;
			//partnerModifer ignored, since no partners will be scored
		}
		if (numPartners==1) {
			if ((numPlayers-2)%2==0) {
				//even number of players
				pickerModifier = (numPlayers-2)/2;
				partnerModifier = (numPlayers-2)/2;
			} else {
				if (numPlayers==5) {
					pickerModifier = 2;
					partnerModifier = 1;
				} else {
					pickerModifier = 3;
					partnerModifier = 2;
				}
			}
		}
		if (numPartners==2) {
			if ((numPlayers-2)%2==0) {
				//even number of players
				pickerModifier = 1;
				partnerModifier = 1;
			} else {
				pickerModifier = 2;
				partnerModifier = 1;
			}
		}
		for (int i=0; i<7; i++) {
			switch (playerArray[i]) {
			case R.string.picker:
				if (handResult==R.string.won || handResult==R.string.wonNoSchneid ||
						handResult==R.string.wonNoTrick || handResult==R.string.leaster) {
				 playerMultiplier = pickerModifier;	
				} else {
					playerMultiplier = -1*pickerModifier*bump;
				}
				break;
			case R.string.partner:
				if (handResult==R.string.won || handResult==R.string.wonNoSchneid || handResult==R.string.wonNoTrick) {
				 playerMultiplier = partnerModifier;	
				} else {
					playerMultiplier = -1*partnerModifier*bump;
				}
				break;
			case R.string.player:
				if (handResult==R.string.won || handResult==R.string.wonNoSchneid ||
						handResult==R.string.wonNoTrick || handResult==R.string.leaster) {
				   playerMultiplier = -1;	
				} else {
					playerMultiplier = 1*bump;
				}
				break;
			case R.string.sittingOut:
				 playerMultiplier = 0;	
				break;
			default:
				break;
			}
			newScores[i] = previousScores[i]+ (baseScoreStatus*playerMultiplier);
			
		}
		int[][] returnArray = new int[2][7];
		returnArray[0] = newScores;
		returnArray[1] = playerArray;
		return returnArray;
	}

	private boolean isThereAPartner(LinearLayout hand) {
		boolean partner = false;
		for (int i=0; i<7; i++) {
			TextView tv = (TextView) hand.getChildAt((i*2)+2);
			if ((Integer)tv.getTag()==getCorrectColor(R.string.partner)) {
				partner = true;
				break;
			}
		}
		return partner;
	}
	

}
