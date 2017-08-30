package us.pauer.android.sheepsheadscorepad;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Observer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.Observable;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter extends Observable<Observer>  {
	static final String DATABASE_NAME = "SheepsDB";
	
	static final int TRUE = 1;
	static final int FALSE = 0;
	
	static final int HAND_NUMBER_KEY = 4783;  //not the hande number, and int key used to get the handnumber from objects in the view
	
	static final String TABLE_GAME = "game";
    static final String TABLE_SETTING = "setting";
	static final String TABLE_PLAYER = "player";
	static final String TABLE_HAND = "hand";
	static final String TABLE_SCORES = "score";
	static final String TABLE_HIST_PLAYER = "hist_player";
	
	static final String KEY_ROWID = "_id";
	
	static final String COL_GAME_ACTIVE = "active";
	static final String COL_GAME_DATE = "date";

    static final String COL_SETTING_KEY = "setKey";
    static final String COL_SETTING_VALUE = "setValue";

	public static final String COL_PLAYER_NAME = "name";
	public static final String COL_PLAYER_PENALTY = "penalty";
	public static final String COL_PLAYER_PLAYING = "playing";
	public static final String COL_PLAYER_POSITION = "position";
	public static final String COL_PLAYER_NOTES = "notes";
	
	
	static final String COL_HAND_GAME = "gameid";
	static final String COL_HAND_NUMBER = "handnumber";
	static final String COL_HAND_PICKER_WIN = "pickerresult";
	static final String COL_HAND_DOUBLERS = "doublers";
	static final String COL_HAND_WHO_PLAYED = "played";
	static final String COL_HAND_NO_SCHNEID = "noschneid";
	static final String COL_HAND_NO_TRICK = "notrick";
	static final String COL_HAND_SCORED = "scored";
	
	static final String COL_SCORE_GAME = "gameid";
	static final String COL_SCORE_HAND_NUMBER = "handnumber";
	static final String COL_SCORE_HAND_PLAYER = "player";
	static final String COL_SCORE_SCORE = "score";

	static final String COL_HIST_PLAYER_GAME = "gameid";
	static final String COL_HIST_PLAYER_NAME = "name";
	static final String COL_HIST_PLAYER_SCORE = "score";
	static final String COL_HIST_PLAYER_POSITION = "position";
	static final String COL_HIST_PLAYER_PENALTY = "penalty";
	static final String COL_HIST_PLAYER_NOTES = "notes";
	static final String COL_HIST_PLAYER_DATE = "hist_date";



	static final String TAG = "DBAdapter";
	
	static final int DATABASE_VERSION = 1;
	
	
	final Context context;
	static private DBAdapter instance;
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	int currentGameId = -1;

	
	static public DBAdapter getInstance(Context ctx) {
		if (instance==null) {
			instance = new DBAdapter(ctx);
		} 
		return instance;
	}
	
	private DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

    private static class DatabaseHelper extends SQLiteOpenHelper {
		
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			
		}
		
		public void onCreate(SQLiteDatabase db) {
			try {
				createGameTable(db);
                createSettingTable(db);
				createPlayerTable(db);
				createHandTable(db);
				createScoreTable(db);
				createHistPlayerTable(db);
				
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		

		private void createGameTable(SQLiteDatabase db) throws SQLException {
			String sql = "create table "+TABLE_GAME+" (" +
					KEY_ROWID + " integer primary key autoincrement, "
					+ COL_GAME_ACTIVE + " integer not null, "
					+ COL_GAME_DATE + " text not null);";
			db.execSQL(sql);
		}

        private void createSettingTable(SQLiteDatabase db) throws SQLException {
            String sql = "create table "+TABLE_SETTING+" (" +
                    KEY_ROWID + " integer primary key autoincrement, "
                    + COL_SETTING_KEY + " text not null, "
                    + COL_SETTING_VALUE + " text not null);";
            db.execSQL(sql);
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_SETTING_KEY, Integer.toHexString(R.id.double_on_bump_checkbox));
            initialValues.put(COL_SETTING_VALUE, "true");
            long success = db.insert(TABLE_SETTING, null, initialValues);
            initialValues = new ContentValues();
            initialValues.put(COL_SETTING_KEY, Integer.toHexString(R.id.double_limit_text));
            initialValues.put(COL_SETTING_VALUE, "6");
            success = db.insert(TABLE_SETTING, null, initialValues);
            initialValues = new ContentValues();
            initialValues.put(COL_SETTING_KEY, Integer.toHexString(R.id.email_default_text));
            initialValues.put(COL_SETTING_VALUE, "");
            success = db.insert(TABLE_SETTING, null, initialValues);
        }
		
		private void createPlayerTable(SQLiteDatabase db) throws SQLException {
			String sql = "create table "+TABLE_PLAYER+" (" +
					KEY_ROWID + " integer primary key autoincrement, "
					+ COL_PLAYER_NAME + " text not null, "
					+ COL_PLAYER_PLAYING + " numeric not null, "
					+ COL_PLAYER_POSITION + " integer not null, "
					+ COL_PLAYER_PENALTY + " integer not null, " 
					+ COL_PLAYER_NOTES + " text);"; 
			db.execSQL(sql);
		}

		private void createHandTable(SQLiteDatabase db) throws SQLException {
			String sql = "create table "+TABLE_HAND + " (" +
					KEY_ROWID + " integer primary key autoincrement, "
					+ COL_HAND_DOUBLERS + " integer not null default 0, "
					+ COL_HAND_GAME + " integer not null, " 
					+ COL_HAND_NO_SCHNEID + " numeric, "
					+ COL_HAND_NO_TRICK + " numeric, "
					+ COL_HAND_NUMBER + " integer not null, "
					+ COL_HAND_PICKER_WIN + " numeric, "
					+ COL_HAND_WHO_PLAYED + " text, "
					+ COL_HAND_SCORED + " numeric not null);";
			db.execSQL(sql);
		}
		
		private void createScoreTable(SQLiteDatabase db) throws SQLException {
			String sql = "create table "+TABLE_SCORES+ " (" +
					KEY_ROWID + " integer primary key autoincrement, "
					+ COL_SCORE_GAME + " integer not null, "
					+ COL_SCORE_HAND_NUMBER + " integer not null, "
					+ COL_SCORE_HAND_PLAYER + " integer not null, "
					+ COL_SCORE_SCORE + " integer not null);";
			db.execSQL(sql);
		}

		private void createHistPlayerTable(SQLiteDatabase db) throws SQLException {
			String sql = "create table "+TABLE_HIST_PLAYER+ " (" +
					KEY_ROWID + " integer primary key autoincrement, "
					+ COL_HIST_PLAYER_GAME + " integer not null, "
					+ COL_HIST_PLAYER_NAME + " text not null, "
					+ COL_HIST_PLAYER_POSITION + " integer not null, "
					+ COL_HIST_PLAYER_SCORE + " integer not null, "
					+ COL_HIST_PLAYER_PENALTY + " integer not null, "
					+ COL_HIST_PLAYER_NOTES + " text, "
					+ COL_HIST_PLAYER_DATE + " text not null);";
			db.execSQL(sql);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version "+ oldVersion + " to "
					+ newVersion + ", which will destroy old data");
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_GAME);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SETTING);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_HAND);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_PLAYER);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCORES);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_HIST_PLAYER);
		}
	}
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public boolean startNewGame() {
		updateHistory();
		newGame(db);
		insertHands(10);
		notifyObservers();
		return true;
	}
	

	private void notifyObservers() {
		for (Observer member:mObservers) {
			member.update(null, null);
		}
		
	}

	public long newGame(SQLiteDatabase db) {
		setCurrentGameAsInactive();
		ContentValues initialValues = new ContentValues();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String gameDate = dateFormat.format(date);
		initialValues.put(COL_GAME_ACTIVE, 1);
		initialValues.put(COL_GAME_DATE, gameDate);
		long success = db.insert(TABLE_GAME, null, initialValues);
		return success;
	}
	


	public long addPlayer(String playerName) {
		// will add in first open slot
		long rowId = -1;
		int position = 1;
		Cursor playerSet = getPlayers();
	    // find first open slot
		boolean foundSpot = false;
		if (playerSet!=null && playerSet.getCount()>0) {
			playerSet.moveToFirst();
			for (int i=0; i<playerSet.getCount(); i++) {
				if (playerSet.getInt(playerSet.getColumnIndex(COL_PLAYER_POSITION))>i+1) {
					position = i+1;
					foundSpot = true;
					break;
				}
				playerSet.moveToNext();
			}
		}
		if (!foundSpot) {
			position = playerSet.getCount()+1;
		}
		// see if player exists
		Cursor player = getPlayerRow(playerName);
		if (player==null || player.getCount()==0) {
		// if not, add player with playing = true
			ContentValues cv = new ContentValues();
			cv.put(COL_PLAYER_NAME, playerName);
			cv.put(COL_PLAYER_PENALTY, 0);
			cv.put(COL_PLAYER_POSITION, position);
			cv.put(COL_PLAYER_PLAYING, TRUE);
			cv.put(COL_PLAYER_NOTES, "");
			db.insert(TABLE_PLAYER, null, cv);
		} else {
			// if so, update player with playing = true
			rowId = player.getLong(1);
			ContentValues cv = new ContentValues();
			cv.put(COL_PLAYER_PLAYING, TRUE);
			cv.put(COL_PLAYER_POSITION, position);
			db.update(TABLE_PLAYER, cv, KEY_ROWID+"= ?", new String[] {Long.toString(rowId)});
		}
		notifyObservers();
		return rowId;
	}

	public void deletePlayer(int position) {
		long rowId = -1;
		// see if player exists
		Cursor player = getPlayerRowByPosition(position);
		if (player==null || player.getCount()==0) {
		} else {
			// if so, delete player with playing = true
			rowId = player.getLong(1);
			db.delete(TABLE_PLAYER, KEY_ROWID+"= ?", new String[] {Long.toString(rowId)});
		}
		notifyObservers();
	}

	
	
	public boolean removePlayerFromGame(String playerName) {
		//check if there are scores, and don't allow
		// removal of players if game has started
		if (!currentGameHasScoredHands()) {
			ContentValues cv = new ContentValues();
			cv.put(COL_PLAYER_PLAYING, FALSE);
			cv.put(COL_PLAYER_POSITION, 0);
			db.update(TABLE_PLAYER, cv, COL_PLAYER_NAME+"= ?", new String[]{playerName});
			notifyObservers();
			return true;
		} else {
			return false;
		}
	}
	

	private boolean currentGameHasScoredHands() {
		long gamerow = getActiveGameId();
		String[] selectRows = new String[1];
		selectRows[0] = KEY_ROWID;
		Cursor scoreCursor = db.query(TABLE_SCORES, selectRows, COL_SCORE_GAME+" = ? ", 
				new String[]{Long.toString(gamerow)}, "","", KEY_ROWID);
		return scoreCursor!=null && scoreCursor.getCount()!=0;
		
	}

	private void clearPlayer() {
		db.delete(TABLE_PLAYER, null, null);
	}

	private void clearHands() {
		db.delete(TABLE_HAND, null, null);
	}

	private void clearScores() {
		db.delete(TABLE_SCORES, null, null);
	}
	
	
	public void addPenalty(String playerName) {
		if (playerName==null || playerName.trim().equalsIgnoreCase("")) {
			return;
		} else {
			Cursor c = getPlayerRow(playerName);
			c.moveToFirst();
			long row = c.getLong(c.getColumnIndex(KEY_ROWID));
			int penaltyCount = c.getInt(c.getColumnIndex(COL_PLAYER_PENALTY));
			penaltyCount++;
			ContentValues cv = new ContentValues();
			cv.put(COL_PLAYER_PENALTY, penaltyCount);
			db.update(TABLE_PLAYER, cv, KEY_ROWID+"=?", new String[]{Long.toString(row)});
			notifyObservers();
		}
	}

	public void subtractPenalty(String playerName) {
		if (playerName==null || playerName.trim().equalsIgnoreCase("")) {
			return;
		} else {
			Cursor c = getPlayerRow(playerName);
			c.moveToFirst();
			long row = c.getLong(c.getColumnIndex(KEY_ROWID));
			int penaltyCount = c.getInt(c.getColumnIndex(COL_PLAYER_PENALTY));
			if (penaltyCount>0) {
				penaltyCount--;
			}
			ContentValues cv = new ContentValues();
			cv.put(COL_PLAYER_PENALTY, penaltyCount);
			db.update(TABLE_PLAYER, cv, KEY_ROWID+"=?", new String[]{Long.toString(row)});
			notifyObservers();
		}
	}

	public int getPenaltyCount(String playerName) {
		int penaltyCount = 0;
		if (playerName==null || playerName.trim().equalsIgnoreCase("")) {
			
		} else {
			Cursor c = getPlayerRow(playerName);
			c.moveToFirst();
			penaltyCount = c.getInt(c.getColumnIndex(COL_PLAYER_PENALTY));
		}
		return penaltyCount;
	}
	
	public void updateNote(int noteNum, String noteValue) {
		Cursor c = getPlayerRowByPosition(noteNum);
		if (c!=null && c.getCount()!=0) {
			c.moveToFirst();
			long row = c.getLong(c.getColumnIndex(KEY_ROWID));
			ContentValues cv = new ContentValues();
			cv.put(COL_PLAYER_NOTES, noteValue);
			db.update(TABLE_PLAYER, cv, KEY_ROWID+"=?", new String[]{Long.toString(row)});
			notifyObservers();
		}
	}

	public String getNote(String playerName) {
		String note = "";
		if (playerName==null || playerName.trim().equalsIgnoreCase("")) {
			
		} else {
			Cursor c = getPlayerRow(playerName);
			c.moveToFirst();
			note = c.getString(c.getColumnIndex(COL_PLAYER_NOTES));
		}
		return note;
	}
	
	
	private Cursor getPlayerRow(String playerName) {
		return db.query(TABLE_PLAYER, new String[] {KEY_ROWID, COL_PLAYER_PENALTY, COL_PLAYER_POSITION, COL_PLAYER_NOTES}, COL_PLAYER_NAME +" = ?", 
				new String[]{playerName.trim()}, "", "", "");
	}

	private Cursor getPlayerRowByPosition(int position) {
		return db.query(TABLE_PLAYER, new String[] {KEY_ROWID, COL_PLAYER_PENALTY, COL_PLAYER_POSITION, COL_PLAYER_NOTES}, COL_PLAYER_POSITION +" = ?", 
				new String[]{Integer.toString(position)}, "", "", "");
	}
	
	
	private void setCurrentGameAsInactive() {
		// get current game rowid
		int row = getActiveGameId();
		currentGameId = -1;
		if (row > -1) {
			ContentValues cv = new ContentValues();
			cv.put(COL_GAME_ACTIVE, FALSE);
			db.update(TABLE_GAME, cv, KEY_ROWID+"=?", new String[]{String.valueOf(row)});
			clearPlayer();
			clearHands();
			clearScores();
		}
	}
	
	public int getActiveGameId() {
		if (currentGameId==-1) {
			Cursor c = db.query(TABLE_GAME, new String[] {KEY_ROWID}, COL_GAME_ACTIVE +" =?", new String[]{"1"}, "", "", "");
			c.moveToFirst();
			int count = c.getCount();
			if (count==0) {
				return -1;
			}
			currentGameId = c.getInt(c.getColumnIndex(KEY_ROWID));
		}
		return currentGameId;
	}

	public String getCurrentGameDate() {
		String gameDate = "";
		Cursor c = db.query(TABLE_GAME, new String[] {KEY_ROWID, COL_GAME_DATE}, COL_GAME_ACTIVE +" =?", new String[]{"1"}, "", "", "");
		c.moveToFirst();
		int count = c.getCount();
		if (count==0) {
			return "";
		}
		gameDate = c.getString(c.getColumnIndex(COL_GAME_DATE));
		return gameDate;
	}



	public void scoreHand(int handNumber, int doublers, boolean pickerWin, 
		    boolean noSchneid, boolean noTrick, int[] playerArray, int[] scores) {
		updateHand(handNumber, doublers, pickerWin, 
			 noSchneid, noTrick, playerArray, TRUE, scores);
		setScoresForHandNumber(handNumber, scores);
		notifyObservers();
		
	}
	
	
	private void updateHand(int handNumber, int doublers, boolean pickerWin, 
			boolean noSchneid, boolean noTrick, int[] playerArray, int scoreHand,
			int[] scores) {
		ContentValues cv = new ContentValues();
		cv.put(COL_HAND_DOUBLERS, doublers);
		cv.put(COL_HAND_PICKER_WIN, pickerWin ? TRUE : FALSE);
		cv.put(COL_HAND_GAME, getActiveGameId());
		cv.put(COL_HAND_NO_SCHNEID, noSchneid ? TRUE : FALSE);
		cv.put(COL_HAND_NO_TRICK, noTrick ? TRUE : FALSE);
		cv.put(COL_HAND_NUMBER, handNumber);
		cv.put(COL_HAND_WHO_PLAYED, Arrays.toString(playerArray));
		cv.put(COL_HAND_SCORED, scoreHand);
		Cursor cursor = getHand(handNumber);
		// check if hand exists
		if (cursor!=null && cursor.getCount()!=0) {
			cursor.moveToFirst();
			Long key = cursor.getLong(cursor.getColumnIndex(DBAdapter.KEY_ROWID));
			db.update(TABLE_HAND, cv, KEY_ROWID+" = ?", new String[]{Long.toString(key)});
		} else {
			db.insert(TABLE_HAND, null, cv);
		}
	}

	public int[] getRolesDescriptionsForHand(int handNumber) {
		Cursor hand = getHand(handNumber);
		int[] returnArr = new int[7];
		if (hand!=null && hand.getCount()!=0) {
			hand.moveToLast();
			String ints = hand.getString(hand.getColumnIndex(COL_HAND_WHO_PLAYED));
			ints = ints.substring(1, ints.length()-1);
			String[] splitUp = ints.split(",");
			for (int i=0; i<splitUp.length; i++) {
				returnArr[i] = Integer.parseInt(splitUp[i].trim());
			}
		}
		return returnArr;
		
	}
	

	private Cursor getHand(int handNumber) {
		long gamerow = getActiveGameId();
		return db.query(TABLE_HAND, null, COL_HAND_NUMBER+"= ? AND "+COL_HAND_GAME+"= ?", 
				new String[]{Integer.toString(handNumber), Long.toString(gamerow)}, "","","");
	}
	
	public int getFurthestHandScored() {
		int handNum = 0;
		Cursor hands = db.query(TABLE_HAND, null, COL_HAND_SCORED + "= ?",
				new String[]{Integer.toString(TRUE)}, "", "", "");
		if (hands!=null && hands.getCount()!=0) {
			hands.moveToLast();
			handNum = hands.getInt(hands.getColumnIndex(COL_HAND_NUMBER));
		}
		return handNum;
	}

	public Cursor getAllHands() {
		long gamerow = getActiveGameId();
		String[] selectRows = new String[9];
		selectRows[0] = KEY_ROWID;
		selectRows[1] = COL_HAND_DOUBLERS;
		selectRows[2] = COL_HAND_GAME;
		selectRows[3] = COL_HAND_NO_SCHNEID;
		selectRows[4] = COL_HAND_NO_TRICK;
		selectRows[5] = COL_HAND_NUMBER;
		selectRows[6] = COL_HAND_PICKER_WIN;
		selectRows[7] = COL_HAND_WHO_PLAYED;
		selectRows[8] = COL_HAND_SCORED;
		return db.query(TABLE_HAND, selectRows, COL_HAND_GAME+"= ?", 
				new String[]{Long.toString(gamerow)}, "","",COL_HAND_NUMBER);
	}

	public void close() {
		DBHelper.close();
	}

	public Cursor getPlayers() {
		return db.query(TABLE_PLAYER, new String[]{KEY_ROWID, COL_PLAYER_NAME, COL_PLAYER_POSITION, 
				COL_PLAYER_PENALTY, COL_PLAYER_NOTES}, 
				COL_PLAYER_PLAYING + "= ?", new String[]{Integer.toString(TRUE)}, "", "", COL_PLAYER_POSITION);
	}



	public int[] getScoresForHandNumber(int handNum) {
		int[] scores = new int[7];
		long currentGame = getActiveGameId();
		String[] selectRows = new String[5];
		selectRows[0] = KEY_ROWID;
		selectRows[1] = COL_SCORE_GAME;
		selectRows[2] = COL_SCORE_HAND_NUMBER;
		selectRows[3] = COL_SCORE_HAND_PLAYER;
		selectRows[4] = COL_SCORE_SCORE;
		Cursor scoreCursor = db.query(TABLE_SCORES, selectRows, COL_SCORE_GAME+" = ? AND "+COL_SCORE_HAND_NUMBER+" = ? ", 
				new String[]{Long.toString(currentGame), Integer.toString(handNum)}, "","", COL_SCORE_HAND_PLAYER);
    	scoreCursor.moveToFirst();
		int j = 0;
		while (!scoreCursor.isAfterLast()) {
			scores[j] = scoreCursor.getInt(scoreCursor.getColumnIndex(COL_SCORE_SCORE));
			j++;
			scoreCursor.moveToNext();
		}
		return scores;
		
	}

	public void setScoresForHandNumber(int handNumber, int[] scores) {
		long gamerow = getActiveGameId();
		// if any scores for this hand exist, delete them
		db.delete(TABLE_SCORES, COL_SCORE_GAME+" = ? AND "+COL_SCORE_HAND_NUMBER+" = ?",
				new String[] {Long.toString(gamerow), Integer.toString(handNumber)});
		for (int i=0; i<scores.length; i++) {
			ContentValues cv = new ContentValues();
			cv.put(COL_SCORE_GAME, gamerow);
			cv.put(COL_SCORE_HAND_NUMBER, handNumber);
			cv.put(COL_SCORE_SCORE, scores[i]);
			cv.put(COL_SCORE_HAND_PLAYER, i);
			db.insert(TABLE_SCORES, null, cv);
		}
		//insert more blank hands at end of score sheet
		int startCount = handNumber;
		int gameHandCount=getGameHandCount();
		insertHands(10-(gameHandCount-handNumber));
		

	}

	public int getDoublersForHand(int handNumber) {
		Cursor handCursor = getHand(handNumber);
		handCursor.moveToFirst();
		int count = handCursor.getCount();
		if (count==0) {
			return -1;
		}
		return handCursor.getInt(handCursor.getColumnIndex(COL_HAND_DOUBLERS));
	}

	public void setDoublersForHand(int handNumber, int newDoubleValue, boolean updateView) {
		ContentValues cv = new ContentValues();
		cv.put(COL_HAND_DOUBLERS, newDoubleValue);
		db.update(TABLE_HAND, cv, KEY_ROWID+"=?", new String[]{String.valueOf(handNumber)});
		if (updateView) {
			notifyObservers();
		}
	}
	
	/**
	 * insertHands will compute the next i hands to be added after the last active game hand....
	 * @param i  number of hands to add...
	 */
	private void insertHands(int i) {
		int j = getGameHandCount();
		for (int k=j; k<j+i; k++) {
			insertHand(k+1);
		}
		
	}
	
	/**
	 * getGameHandCount() returns the full number of hands
	 * for the current active game.  This will include scored and unscored hands.
	 * @return  total number of hands existing for current game
	 */
	private int getGameHandCount() {
		return getAllHands().getCount();
	}

	private void insertHand(int handNumber) {
		if (handNumber>0) {
			ContentValues cv = new ContentValues();
			cv.put(COL_HAND_GAME, currentGameId);
			cv.put(COL_HAND_NUMBER, handNumber);
			cv.put(COL_HAND_SCORED, 0);
			db.insert(TABLE_HAND, null, cv);
		}
	}

	public boolean isGameActive() {
		return getActiveGameId()!=-1;
	}

    public void updateSettings(String[] entrySet) {
        db.execSQL("delete from "+TABLE_SETTING);
        for (int i=0; i<(entrySet.length/2); i++) {
            ContentValues cv = new ContentValues();
            cv.put(COL_SETTING_KEY, entrySet[i*2]);
            cv.put(COL_SETTING_VALUE, entrySet[(i*2)+1]);
            db.insert(TABLE_SETTING, null, cv);

        }

    }

    public String getSettingValue(String key) {
        String[] selectRows = new String[1];
        selectRows[0] = COL_SETTING_VALUE;
        Cursor valCursor = db.query(TABLE_SETTING, selectRows, COL_SETTING_KEY+" = ? ",
                new String[]{key}, "","", "");
        valCursor.moveToFirst();
        return valCursor.getString(valCursor.getColumnIndex(COL_SETTING_VALUE));
    }



	public void updateHistory() {
		// check if there is an active game
		if (currentGameHasScoredHands())
		{
			String gameDate = getCurrentGameDate();
			// if so  get players, scores, penatlies and notes
			Cursor players = getPlayers();
			int lastHand = getFurthestHandScored();
			int[] lastHandScores = getScoresForHandNumber(lastHand);
			players.moveToFirst();
			while (!players.isAfterLast()) {
				String name = players.getString(players.getColumnIndex(COL_PLAYER_NAME));
				String note = players.getString(players.getColumnIndex(COL_PLAYER_NOTES));
				int penalty = players.getInt(players.getColumnIndex(COL_PLAYER_PENALTY));
				int position = players.getInt(players.getColumnIndex(COL_PLAYER_POSITION));
				ContentValues cv = new ContentValues();
				cv.put(COL_HIST_PLAYER_GAME, currentGameId);
				cv.put(COL_HIST_PLAYER_NAME, name);
				cv.put(COL_HIST_PLAYER_POSITION, position);
				cv.put(COL_HIST_PLAYER_PENALTY, penalty);
				cv.put(COL_HIST_PLAYER_NOTES, note);
				cv.put(COL_HIST_PLAYER_SCORE, lastHandScores[position]);
				cv.put(COL_HIST_PLAYER_DATE, gameDate);
				db.insert(TABLE_HIST_PLAYER, null, cv);
				players.moveToNext();
			}
		}
	}

	public Cursor getHistory() {



			Cursor scoreCursor = db.query(TABLE_HIST_PLAYER, null,
				"", new String[]{}, "", "", COL_HIST_PLAYER_GAME + " DESC, "+COL_HIST_PLAYER_POSITION);

		int stuff = scoreCursor.getCount();
		return scoreCursor;
	}





}
