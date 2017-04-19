/**
*   <h1>Data DAO<h1>
*   Data DAO class that allows us to get create, delete and update: matches, players, stats and teams and get information of them from the database
*   @author Maria Garcia Fernandez
*   @version 1.0
*   @since 2017-04-07
*/

/**
 * Imported package and libraries
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import application.Match;
import application.Player;
import application.Stats;
import application.Team;
import application.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DataDAO {
	/**
	 * Class private variables
	 */
	private static final String user_table = "ss_user";
	private static final String player_table = "ss_player";
	private static final String team_table = "ss_team";
	private static final String match_table = "ss_match";
	private static final String stats_table = "ss_stat";
	private Connection connection;
	private Statement statement;
	private Connector connector = new Connector();

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private User adminUser;

	private Player p1;
	private Player p2;
	private Player p3;
	private Player p4;

	private Team t1;
	private Team t2;

	private Stats s1;
	private Stats s2;

	private Match m1;
	private Match m2;

	private static final int REAL_MADRID = 1;
	private static final int BARCELONA = 2;

	public static final int MYSQL_DUPLICATE_PK = 1062;
	public static final int MYSQL_TRUNCATION_PK = 1406;

	/**
	 * Create Tables method that generates all the tables in the database
	 */
	public void createTables() {
		// A try catch is needed in case the sql statement is invalid
		try {
			// Create connection and statement
			connection = connector.getConnection();
			statement = connection.createStatement();

			// Create tables if they do not exist already
			String user_sql = "CREATE TABLE IF NOT EXISTS " + user_table + " (id INTEGER NOT NULL AUTO_INCREMENT,"
					+ " username VARCHAR(10) UNIQUE NOT NULL," + " password VARCHAR(15) NOT NULL,"
					+ " name VARCHAR(20) NOT NULL," + " lastname VARCHAR(20) NOT NULL,"
					+ " email VARCHAR(40) UNIQUE NOT NULL," + " isAdmin BOOLEAN NOT NULL," + " PRIMARY KEY (id))";

			String player_sql = "CREATE TABLE IF NOT EXISTS " + player_table + " (id INTEGER NOT NULL AUTO_INCREMENT,"
					+ " idTeam INTEGER NOT NULL," + " name VARCHAR(20) NOT NULL," + " lastname VARCHAR(20) NOT NULL,"
					+ " dateOfBirth DATE NOT NULL," + " height INTEGER NOT NULL," + " PRIMARY KEY (id))";

			String team_sql = "CREATE TABLE IF NOT EXISTS " + team_table + " (id INTEGER NOT NULL AUTO_INCREMENT,"
					+ " name VARCHAR(20) UNIQUE NOT NULL," + " coach VARCHAR(20) UNIQUE NOT NULL,"
					+ " city VARCHAR(20) NOT NULL," + " dateFoundation DATE NOT NULL," + " PRIMARY KEY (id))";

			String match_sql = "CREATE TABLE IF NOT EXISTS " + match_table + " (id INTEGER NOT NULL AUTO_INCREMENT,"
					+ " idHome INTEGER NOT NULL," + " idAway INTEGER NOT NULL," + " matchDate DATE NOT NULL,"
					+ " referee VARCHAR(20) NOT NULL," + " PRIMARY KEY (id))";

			String stats_sql = "CREATE TABLE IF NOT EXISTS " + stats_table + " (id INTEGER NOT NULL AUTO_INCREMENT,"
					+ " idMatch INTEGER NOT NULL," + " goalsHome INTEGER NOT NULL," + " goalsAway INTEGER NOT NULL,"
					+ " numberOfCorners INTEGER NOT NULL," + " numberOfFouls INTEGER NOT NULL," + " PRIMARY KEY (id))";

			// Inform the user that the table has been created just the first
			// time
			if (statement.executeUpdate(user_sql) > 0) {
				System.out.println("User table succesfully created");
			}
			if (statement.executeUpdate(player_sql) > 0) {
				System.out.println("Player table succesfully created");
			}
			if (statement.executeUpdate(team_sql) > 0) {
				System.out.println("Team table succesfully created");
			}
			if (statement.executeUpdate(match_sql) > 0) {
				System.out.println("Match table succesfully created");
			}
			if (statement.executeUpdate(stats_sql) > 0) {
				System.out.println("Stats table succesfully created");
			}
			// Closing the connection
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Generate DB method, this method once the tables are created introduces
	 * some default values
	 */
	public void generateDB() {
		// Database tables creation
		createTables();

		// Try catch estructure in case there is an exception during the data
		// insertion
		try {
			// Create an admin user
			adminUser = new User("admin", "1234", "admin", "admin", "admin", 1);

			// Create some default players
			String p1DateString = "1988-05-12";
			String p2DateString = "1985-02-05";
			String p3DateString = "1987-06-24";
			String p4DateString = "1992-02-05";
			Date p1Date = sdf.parse(p1DateString);
			Date p2Date = sdf.parse(p2DateString);
			Date p3Date = sdf.parse(p3DateString);
			Date p4Date = sdf.parse(p4DateString);
			p1 = new Player("Marcelo", "Vieira", p1Date, 174);
			p2 = new Player("Cristiano", "Ronaldo", p2Date, 185);
			p3 = new Player("Lionel", "Messi", p3Date, 170);
			p4 = new Player("Neymar", "Silva", p4Date, 175);

			// Create some default teams
			String t1DateString = "1902-03-6";
			String t2DateString = "1985-02-05";
			Date t1Date = sdf.parse(t1DateString);
			Date t2Date = sdf.parse(t2DateString);
			List<Player> t1Players = formatPlayers(getPlayersByTeamId(REAL_MADRID));
			List<Player> t2Players = formatPlayers(getPlayersByTeamId(BARCELONA));
			t1 = new Team(t1Players, "Real Madrid C.F.", "Zinedine Zidane", "Madrid", t1Date);
			t2 = new Team(t2Players, "FC Barcelona", "Luis Enrique", "Barcelona", t2Date);

			// Create some default stats
			s1 = new Stats(1, 1, 6, 15);
			s2 = new Stats(3, 1, 8, 18);

			// Create some default matches
			String m1DateString = "2017-01-20";
			String m2DateString = "2017-03-06";
			Date m1Date = sdf.parse(m1DateString);
			Date m2Date = sdf.parse(m2DateString);
			m1 = new Match(t2, t1, s1, m1Date, "Alfonso Alvarez Izq");
			m2 = new Match(t1, t2, s2, m2Date, "Carlos Clos Gomez");

			// Fill the database tables only if they are empty
			if (!usersFilled()) {
				insertUser(adminUser);
			}
			if (!playersFilled()) {
				insertPlayer(p1, REAL_MADRID);
				insertPlayer(p2, REAL_MADRID);
				insertPlayer(p3, BARCELONA);
				insertPlayer(p4, BARCELONA);
			}
			if (!teamsFilled()) {
				insertTeam(t1);
				insertTeam(t2);
			}
			if (!matchesFilled() && !statsFilled()) {
				insertStat(s1);
				insertMatch(m1);
				insertStat(s2);
				insertMatch(m2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * dbLogin method that checks that the username and password introduce are
	 * stored in the database and are correct
	 *
	 * @param username
	 * @param password
	 * @return login true or false
	 */
	public boolean dbLogin(String username, String password) {
		PreparedStatement statement = null;
		boolean login = false;
		try {
			String login_sql = "SELECT username, password FROM " + user_table + " WHERE username = ? AND password = ?;";
			connection = connector.getConnection();
			statement = connection.prepareStatement(login_sql);
			statement.setString(1, username);
			statement.setString(2, password);
			statement.executeQuery();
			login = statement.getResultSet().first();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return login;
	}

	/**********************************
	 * Team Table Database Operations *
	 **********************************/

	/**
	 * getTeams method that returns all the teams in the table
	 *
	 * @return rs the teams resultset after the sql query
	 * @throws SQLException
	 */
	public ResultSet getTeams() throws SQLException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String teamRecords_sql = "SELECT * FROM " + team_table;
		connection = connector.getConnection();
		statement = connection.prepareStatement(teamRecords_sql);
		rs = statement.executeQuery();
		return rs;
	}

	/**
	 * teamsFilled method allows us to check if the table has been filled with
	 * the default values
	 *
	 * @return true or false
	 */
	public boolean teamsFilled() {
		try {
			if (getTeams().last() == true) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * insertTeam method that allows us to insert a new team in the database
	 * table
	 *
	 * @param team
	 */
	public void insertTeam(Team team) {
		try {
			Statement statement = null;
			connection = connector.getConnection();
			statement = connection.createStatement();
			java.sql.Date tDate = new java.sql.Date(team.getDateFoundation().getTime());
			String insertTeam_sql = "INSERT INTO " + team_table + " (id, name, coach, city, dateFoundation)"
					+ "VALUES (NULL, '" + team.getName() + "' , '" + team.getCoach() + "' , '" + team.getCity()
					+ "' , '" + tDate + "')";
			statement.executeUpdate(insertTeam_sql);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_DUPLICATE_PK) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Duplicity Error");
				alert.setContentText("This team is already in our database");
				alert.showAndWait();
			} else {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * updateTeam method allow us to update an existing team in the database
	 *
	 * @param t
	 *            the team
	 * @param id
	 * @return true or false if the team has been updated successfully
	 */
	public boolean updateTeam(Team t, int id) {
		boolean update = false;
		try {
			PreparedStatement statement = null;
			java.sql.Date tDate = new java.sql.Date(t.getDateFoundation().getTime());
			String updateRecords_sql = "UPDATE " + team_table + " SET name='" + t.getName() + "', coach='"
					+ t.getCoach() + "', city='" + t.getCity() + "', dateFoundation='" + tDate + "' WHERE id='" + id
					+ "'";
			connection = connector.getConnection();
			statement = connection.prepareStatement(updateRecords_sql);
			statement.executeUpdate();
			update = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return update;
	}

	/**
	 * deleteTeamById method that deleted a specific team by id
	 *
	 * @param id
	 * @return true or false if the team has been deleted successfully
	 */
	public boolean deleteTeamById(int id) {
		deleteMatchesByTeamId(id);
		ResultSet rsPlayers;
		try {
			rsPlayers = getPlayersByTeamId(id);
			while(rsPlayers.next()){
				deletePlayerById(rsPlayers.getInt(1));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		boolean result = true;
		PreparedStatement statement = null;
		String teamByIdRecords_sql = "DELETE FROM " + team_table + " WHERE id='" + id + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(teamByIdRecords_sql);
			statement.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	/**
	 * getTeamIdByPlayerName method that returns the team id of a specific
	 * player
	 *
	 * @param pName
	 * @return id
	 */
	public int getTeamIdByPlayerName(String pName) {
		int id = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String teamIdByPlayerNameRecords_sql = "SELECT idTeam FROM " + player_table + " WHERE name='" + pName + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(teamIdByPlayerNameRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return id;
	}

	/**
	 * getTeamNames method that returns all the team names stored in the
	 * database
	 *
	 * @return rs the resultset that contains all the team names
	 */
	public ResultSet getTeamNames() {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String teamNamesRecords_sql = "SELECT name FROM " + team_table;
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(teamNamesRecords_sql);
			rs = statement.executeQuery();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return rs;
	}

	/**
	 * getTeamIdByName method that gives us the team id of a specific team using
	 * its name
	 *
	 * @param name
	 * @return id
	 */
	public int getTeamIdByName(String name) {
		int id = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String teamIdByNameRecords_sql = "SELECT id FROM " + team_table + " WHERE name='" + name + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(teamIdByNameRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return id;
	}

	/**
	 * getTeamIdBySelection method return us a specific team id by list of data
	 * of a team like the name, coach, city and foundation date
	 *
	 * @param selection
	 * @return id
	 */
	public int getTeamIdBySelection(List<String> selection) {
		int id = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;

		java.sql.Date date = null;
		try {
			date = new java.sql.Date(sdf.parse(selection.get(3)).getTime());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String teamIdBySelectionRecords_sql = "SELECT id FROM " + team_table + " WHERE name='" + selection.get(0)
				+ "' AND coach='" + selection.get(1) + "' AND city='" + selection.get(2) + "' AND dateFoundation='"
				+ date + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(teamIdBySelectionRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return id;
	}

	/**
	 * getTeamNameById method returns the team name of a specific team using its
	 * id
	 *
	 * @param id
	 * @return t the team name
	 */
	public String getTeamNameById(int id) {
		String t = "";
		PreparedStatement statement = null;
		ResultSet rs = null;
		String teamByIdRecords_sql = "SELECT name FROM " + team_table + " WHERE id='" + id + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(teamByIdRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				t = rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return t;
	}

	/**
	 * getTeamId method returns the team id of a specific team
	 *
	 * @param team
	 * @return id
	 */
	public int getTeamId(Team team) {
		int teamId = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String teamIdRecords_sql = "SELECT id FROM " + team_table + " WHERE name='" + team.getName() + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(teamIdRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				teamId = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return teamId;
	}

	/**
	 * getTeamByName method returns a team from the database with the same name
	 * that the one given
	 *
	 * @param name
	 * @return t the team
	 */
	public Team getTeamByName(String name) {
		Team t = null;
		try {
			PreparedStatement statement = null;
			ResultSet rs = null;
			String teamRecords_sql = "SELECT * FROM " + team_table + " WHERE name='" + name + "'";
			connection = connector.getConnection();
			statement = connection.prepareStatement(teamRecords_sql);
			rs = statement.executeQuery();
			List<Player> players = new ArrayList<Player>();
			if (rs.next()) {
				players = formatPlayers(getPlayersByTeamId(rs.getInt(1)));
				t = new Team(players, rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return t;
	}

	/**
	 * getTeamById method returns the team from the database with the specific
	 * id
	 *
	 * @param id
	 * @param rsMatches
	 * @return t the team
	 */
	public Team getTeamById(int id, ResultSet rsMatches) {
		Team t = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String teamByIdRecords_sql = "SELECT * FROM " + team_table + " WHERE id='" + id + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(teamByIdRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				t = new Team(formatPlayers(getPlayersByTeamId(id)), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getDate(5));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return t;
	}

	/***********************************
	 * Stats Table Database Operations *
	 ***********************************/

	/**
	 * getStats method that returns all the stats in the table
	 *
	 * @return rs the stats resultset after the sql query
	 * @throws SQLException
	 */
	public ResultSet getStats() throws SQLException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String statsRecords_sql = "SELECT * FROM " + stats_table;
		connection = connector.getConnection();
		statement = connection.prepareStatement(statsRecords_sql);
		rs = statement.executeQuery();
		return rs;
	}

	/**
	 * statsFilled method allows us to check if the table has been filled with
	 * the default values
	 *
	 * @return true or false
	 */
	public boolean statsFilled() throws SQLException {
		try {
			if (getStats().last() == true) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * insertStat method that allows us to insert a new stat in the database
	 * table
	 *
	 * @param stat
	 */
	public void insertStat(Stats stat) {
		try {
			Statement statement = null;
			connection = connector.getConnection();
			statement = connection.createStatement();
			int idMatch = getMatchesRowNumber() + 1;
			String insertStat_sql = "INSERT INTO " + stats_table
					+ " (id, idMatch, goalsHome, goalsAway, numberOfCorners, numberOfFouls)" + " VALUES (NULL, '"
					+ idMatch + "', '" + stat.getGoalsHome() + "' , '" + stat.getGoalsAway() + "' , '"
					+ stat.getNumberOfCorners() + "' , '" + stat.getNumberOfFouls() + "')";
			statement.executeUpdate(insertStat_sql);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_DUPLICATE_PK) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Duplicity Error");
				alert.setContentText("This team is already in our database");
				alert.showAndWait();
			} else {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * updateStat method allow us to update an existing stat in the database
	 *
	 * @param s
	 *            the stat
	 * @param id
	 * @return true or false if the team has been updated successfully
	 */
	public boolean updateStat(Stats s, int id) {
		boolean update = false;
		try {
			PreparedStatement statement = null;
			String updateRecords_sql = "UPDATE " + stats_table + " SET goalsHome='" + s.getGoalsHome()
					+ "', goalsAway='" + s.getGoalsAway() + "', numberOfCorners='" + s.getNumberOfCorners()
					+ "', numberOfFouls='" + s.getNumberOfFouls() + "' WHERE idMatch='" + id + "'";
			connection = connector.getConnection();
			statement = connection.prepareStatement(updateRecords_sql);
			statement.executeUpdate();
			update = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return update;
	}

	/**
	 * deleteStatById method that deleted a specific stat by id
	 *
	 * @param id
	 * @return true or false if the stat has been deleted successfully
	 */
	public boolean deleteStatById(int id) {
		PreparedStatement statement = null;
		String statByIdRecords_sql = "DELETE FROM " + stats_table + " WHERE id='" + id + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(statByIdRecords_sql);
			statement.executeUpdate();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * getStatsByMatchId method selects a specific stat by its match id
	 *
	 * @param matchId
	 * @return s the stat
	 */
	public Stats getStatsByMatchId(int matchId) {
		Stats s = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String matchesByTeamRecords_sql = "SELECT * FROM " + stats_table + " WHERE idMatch='" + matchId + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(matchesByTeamRecords_sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				s = new Stats(rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * getStatsById method returns us a specific stat by id
	 *
	 * @param id
	 * @return s the stat
	 */
	public Stats getStatsById(int id) {
		Stats s = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String statByIdRecords_sql = "SELECT * FROM " + stats_table + " WHERE id='" + id + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(statByIdRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				s = new Stats(rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return s;
	}

	/**********************************
	 * Match Table Database Operations *
	 **********************************/

	/**
	 * getMatches method that returns all the matches in the table
	 *
	 * @return rs the matches resultset after the sql query
	 * @throws SQLException
	 */
	public ResultSet getMatches() throws SQLException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String matchRecords_sql = "SELECT * FROM " + match_table;
		connection = connector.getConnection();
		statement = connection.prepareStatement(matchRecords_sql);
		rs = statement.executeQuery();
		return rs;
	}

	/**
	 * matchesFilled method allows us to check if the table has been filled with
	 * the default values
	 *
	 * @return true or false
	 */
	public boolean matchesFilled() throws SQLException {
		try {
			if (getMatches().last() == true) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * insertMatch method that allows us to insert a new match in the database
	 * table
	 *
	 * @param match
	 */
	public void insertMatch(Match match) {
		try {
			Statement statement = null;
			connection = connector.getConnection();
			statement = connection.createStatement();
			java.sql.Date mDate = new java.sql.Date(match.getDate().getTime());
			String insertMatch_sql = "INSERT INTO " + match_table + " (id, idHome, idAway, matchDate, referee)"
					+ "VALUES (NULL, '" + getTeamId(match.getTeamHome()) + "' , '" + getTeamId(match.getTeamAway())
					+ "' , '" + mDate + "' , '" + match.getReferee() + "')";
			statement.executeUpdate(insertMatch_sql);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_DUPLICATE_PK) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Duplicity Error");
				alert.setContentText("This team is already in our database");
				alert.showAndWait();
			} else {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * updateMatch method allow us to update an existing match in the database
	 *
	 * @param m
	 *            the match
	 * @param id
	 * @return true or false if the team has been updated successfully
	 */
	public boolean updateMatch(Match m, int id) {
		boolean update = false;
		try {
			PreparedStatement statement = null;
			// (Team teamHome, Team teamAway, Stats stats, Date date, String
			// referee)
			java.sql.Date newDate = new java.sql.Date(m.getDate().getTime());
			int idHome = getTeamIdByName(m.getTeamHome().getName());
			int idAway = getTeamIdByName(m.getTeamAway().getName());
			String updateRecords_sql = "UPDATE " + match_table + " SET idHome='" + idHome + "', idAway='" + idAway
					+ "', matchDate='" + newDate + "', referee='" + m.getReferee() + "' WHERE id=" + id;
			connection = connector.getConnection();
			statement = connection.prepareStatement(updateRecords_sql);
			statement.executeUpdate();
			update = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return update;
	}

	/**
	 * deleteMatchById method that deletes a specific match by id
	 *
	 * @param id
	 * @return true or false if the match has been deleted successfully
	 */
	public boolean deleteMatchById(int id) {
		boolean result = false;
		PreparedStatement statement = null;
		String matchByIdRecords_sql = "DELETE FROM " + match_table + " WHERE id='" + id + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(matchByIdRecords_sql);
			statement.executeUpdate();
			result = deleteStatById(id);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	/**
	 * deleteMatchesByTeamId method that deletes all the matches with a specific team id
	 *
	 * @param id
	 * @return true or false if the matches have been deleted successfully
	 */
	public boolean deleteMatchesByTeamId(int id){
		boolean result = true;
		PreparedStatement statement = null;
		try {
			ResultSet rsMatches = getMatchesByTeamId(id);
			while(rsMatches.next()){
				result = result && deleteStatById(rsMatches.getInt(1));
			}
			String matchByIdRecords_sql = "DELETE FROM " + match_table + " WHERE idHome='" + id + "' OR idAway='" + id + "'";
			connection = connector.getConnection();
			statement = connection.prepareStatement(matchByIdRecords_sql);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	/**
	 * formatMatches method return a list with all the matches from a resultset
	 * @param rs the matches resultset
	 * @return mList the matches list
	 */
	public List<Match> formatMatches(ResultSet rs) {
		List<Match> mList = new ArrayList<Match>();
		try {
			while (rs.next()) {
				int matchId = rs.getInt(1);
				int homeTeamId = rs.getInt(2);
				int awayTeamId = rs.getInt(3);
				Date date = rs.getDate(4);
				Team homeTeam = getTeamById(homeTeamId, rs);
				Team awayTeam = getTeamById(awayTeamId, rs);
				Match m = new Match(homeTeam, awayTeam, getStatsByMatchId(matchId), date, rs.getString(5));
				mList.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mList;
	}

	/**
	 * getMatchIdBySelection method return us a specific match id by list of
	 * data of a match like the home team, local team, referee and the match
	 * date
	 *
	 * @param selection
	 * @return id
	 */
	public int getMatchIdBySelection(List<String> selection) {
		int id = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		java.sql.Date date = null;
		try {
			date = new java.sql.Date(sdf.parse(selection.get(2)).getTime());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		int idHome = getTeamIdByName(selection.get(0));
		int idAway = getTeamIdByName(selection.get(1));
		String matchIdBySelectionRecords_sql = "SELECT id FROM " + match_table + " WHERE referee='" + selection.get(3)
				+ "' AND idHome='" + idHome + "' AND idAway='" + idAway + "' AND matchDate='" + date + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(matchIdBySelectionRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return id;
	}

	/**
	 * getMatchById method returns a specific match by its id
	 * @param id
	 * @return m the match
	 */
	public Match getMatchById(int id) {
		Match m = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String matchByIdRecords_sql = "SELECT * FROM " + match_table + " WHERE id='" + id + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(matchByIdRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				m = new Match(getTeamById(rs.getInt(2), getMatchesByTeamId(rs.getInt(2))),
						getTeamById(rs.getInt(3), getMatchesByTeamId(rs.getInt(3))), getStatsById(id), rs.getDate(4),
						rs.getString(5));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return m;
	}

	/**
	 * getMatchesRowNumber method returns the number of matches in the database
	 * @return nMatches
	 */
	public int getMatchesRowNumber() {
		int nMatches = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String teamByIdRecords_sql = "SELECT COUNT(*) FROM " + match_table;
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(teamByIdRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				nMatches = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return nMatches;
	}

	/**
	 * getMatchesByTeamId method returns all the matches of a specific team by its id
	 * @param teamId
	 * @return rs the resulset with all the matches
	 * @throws SQLException
	 */
	public ResultSet getMatchesByTeamId(int teamId) throws SQLException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String matchesByTeamRecords_sql = "SELECT * FROM " + match_table + " WHERE idHome='" + teamId + "' OR idAway='"
				+ teamId + "'";
		connection = connector.getConnection();
		statement = connection.prepareStatement(matchesByTeamRecords_sql);
		rs = statement.executeQuery();
		return rs;
	}

	/**********************************
	 * User Table Database Operations *
	 **********************************/

	/**
	 * getUsers method that returns all the users in the table
	 *
	 * @return rs the users resultset after the sql query
	 * @throws SQLException
	 */
	public ResultSet getUsers() throws SQLException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String userRecords_sql = "SELECT * FROM " + user_table;
		connection = connector.getConnection();
		statement = connection.prepareStatement(userRecords_sql);
		rs = statement.executeQuery();
		return rs;
	}

	/**
	 * usersFilled method allows us to check if the table has been filled with
	 * the default values
	 *
	 * @return true or false
	 */
	public boolean usersFilled() throws SQLException {
		try {
			if (getUsers().last() == true) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * insertUser method that allows us to insert a new user in the database
	 * table
	 *
	 * @param user
	 */
	public void insertUser(User user) throws SQLException {
		try {
			Statement statement = null;
			connection = connector.getConnection();
			statement = connection.createStatement();
			String insertUser_sql = "INSERT INTO " + user_table
					+ " (id, username, password, name, lastname, email, isAdmin)" + "VALUES (NULL, '"
					+ user.getUsername() + "' , '" + user.getPassword() + "' , '" + user.getName() + "' , '"
					+ user.getLastname() + "' , '" + user.getEmail() + "' , '" + user.getIsAdmin() + "')";
			statement.executeUpdate(insertUser_sql);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_DUPLICATE_PK) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Duplicity Error");
				alert.setContentText(
						"This username or email is already in our database please try again with another one");
				alert.showAndWait();
			} else if (e.getErrorCode() == MYSQL_TRUNCATION_PK) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Data Truncated Error");
				alert.setContentText("One of the fields was too long please try again with shorter words");
				alert.showAndWait();
			} else {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * getUser method that returns a specific user by username and password
	 * @param username
	 * @param password
	 * @return user
	 * @throws SQLException
	 */
	public User getUser(String username, String password) throws SQLException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String userRecord_sql = "SELECT * FROM " + user_table + " WHERE username='" + username + "' AND password='"
				+ password + "'";
		connection = connector.getConnection();
		statement = connection.prepareStatement(userRecord_sql);
		rs = statement.executeQuery();
		User user = null;
		while (rs.next()) {
			try {
				user = new User(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
						rs.getInt(7));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return user;
	}

	/**********************************
	 * Player Table Database Operations *
	 **********************************/

	/**
	 * getPlayers method that returns all the players in the table
	 *
	 * @return rs the players resultset after the sql query
	 * @throws SQLException
	 */
	public ResultSet getPlayers() throws SQLException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String playerRecords_sql = "SELECT * FROM " + player_table;
		connection = connector.getConnection();
		statement = connection.prepareStatement(playerRecords_sql);
		rs = statement.executeQuery();
		return rs;
	}

	/**
	 * playersFilled method allows us to check if the table has been filled with
	 * the default values
	 *
	 * @return true or false
	 */
	public boolean playersFilled() throws SQLException {
		try {
			if (getPlayers().last() == true) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * insertPlayer method that allows us to insert a new player in the database
	 * table
	 *
	 * @param player
	 * @param idTeam
	 */
	public void insertPlayer(Player player, int idTeam) {
		try {
			Statement statement = null;
			connection = connector.getConnection();
			statement = connection.createStatement();
			java.sql.Date pDate = new java.sql.Date(player.getDateOfBirth().getTime());
			String insertPlayer_sql = "INSERT INTO " + player_table
					+ " (id, idTeam, name, lastname, dateOfBirth, height)" + "VALUES (NULL, " + idTeam + ", '"
					+ player.getName() + "' , '" + player.getLastname() + "' , '" + pDate + "' , '" + player.getHeight()
					+ "')";
			statement.executeUpdate(insertPlayer_sql);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_DUPLICATE_PK) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Duplicity Error");
				alert.setContentText("This player is already in our database");
				alert.showAndWait();
			} else {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * updatePlayer method allow us to update an existing player in the database
	 *
	 * @param p the player
	 * @param idTeam
	 * @param id
	 * @return true or false if the team has been updated successfully
	 */
	public boolean updatePlayer(Player p, int idTeam, int id) {
		boolean update = false;
		try {
			PreparedStatement statement = null;
			java.sql.Date newDate = new java.sql.Date(p.getDateOfBirth().getTime());
			String updateRecords_sql = "UPDATE " + player_table + " SET idTeam='" + idTeam + "', name='" + p.getName()
					+ "', lastname='" + p.getLastname() + "', dateOfBirth='" + newDate + "', height='" + p.getHeight()
					+ "' WHERE id=" + id;
			connection = connector.getConnection();
			statement = connection.prepareStatement(updateRecords_sql);
			statement.executeUpdate();
			update = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return update;
	}

	/**
	 * deletePlayerById method that deleted a specific player by id
	 *
	 * @param id
	 * @return true or false if the player has been deleted successfully
	 */
	public boolean deletePlayerById(int id) {
		PreparedStatement statement = null;
		String playerByIdRecords_sql = "DELETE FROM " + player_table + " WHERE id='" + id + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(playerByIdRecords_sql);
			statement.executeUpdate();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * formatPlayers method return a list with all the players from a resultset
	 * @param rs the players resultset
	 * @return pList the player list
	 */
	public List<Player> formatPlayers(ResultSet rs) {
		List<Player> pList = new ArrayList<Player>();
		try {
			while (rs.next()) {
				Player p = new Player(rs.getString(3), rs.getString(4), rs.getDate(5), rs.getInt(6));
				pList.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pList;
	}

	/**
	 * getPlayersByTeamId method returns all the players of an specific team by its id
	 * @param teamId
	 * @return rs the resultset with the players
	 * @throws SQLException
	 */
	public ResultSet getPlayersByTeamId(int teamId) throws SQLException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String playersByTeamRecords_sql = "SELECT * FROM " + player_table + " WHERE idTeam='" + teamId + "'";
		connection = connector.getConnection();
		statement = connection.prepareStatement(playersByTeamRecords_sql);
		rs = statement.executeQuery();
		return rs;
	}

	/**
	 * getPlayerIdBySelection method return us a specific player id by list of data
	 * of a player like the name, lastname, height, date of birth and his team.
	 *
	 * @param selection
	 * @return id
	 */
	public int getPlayerIdBySelection(List<String> selection) {
		int id = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		java.sql.Date date = null;
		try {
			date = new java.sql.Date(sdf.parse(selection.get(2)).getTime());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		int idTeam = getTeamIdByName(selection.get(4));
		String playerIdBySelectionRecords_sql = "SELECT id FROM " + player_table + " WHERE name='" + selection.get(0)
				+ "' AND lastname='" + selection.get(1) + "' AND height='" + selection.get(3) + "' AND dateOfBirth='"
				+ date + "' AND idTeam='" + idTeam + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(playerIdBySelectionRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return id;
	}

	/**
	 * getPlayerById method returns a player by an id
	 * @param id
	 * @return p the player
	 */
	public Player getPlayerById(int id) {
		Player p = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String playerByIdRecords_sql = "SELECT * FROM " + player_table + " WHERE id='" + id + "'";
		connection = connector.getConnection();
		try {
			statement = connection.prepareStatement(playerByIdRecords_sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				p = new Player(rs.getString(3), rs.getString(4), rs.getDate(5), rs.getInt(6));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return p;
	}

}
