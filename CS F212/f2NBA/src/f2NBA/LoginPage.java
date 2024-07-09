package f2NBA;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage {

    public static void createMainWindow() {
        // Create and set up the main window
    	JFrame mainFrame = new JFrame("Sportify");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(600, 400);
		mainFrame.setLocationRelativeTo(null); // Center align the main window
		
		// Create panel for dividing the window into two areas
		JPanel mainPanel = new JPanel(new GridLayout(1, 2));
		String currentDirectory = System.getProperty("user.dir");
        // Specify the file name you want to load from the current directory
        String fileName = "NBA_foto.png";
        // Construct the file path
        String filePath = currentDirectory + File.separator + "src/f2NBA/" + fileName;
		ImageIcon originalIcon = new ImageIcon(filePath);
		
		Image img = originalIcon.getImage();
		Image newImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Specify new width and height
		ImageIcon resizedIcon = new ImageIcon(newImg);
		
		fileName = "F1-Logo.png";
		filePath = currentDirectory + File.separator + "src/f2NBA/" + fileName;
		ImageIcon originalIcon2 = new ImageIcon(filePath);
		
		// Resize the image
		Image img2 = originalIcon2.getImage();
		Image newImg2 = img2.getScaledInstance(150, 100, Image.SCALE_SMOOTH); // Specify new width and height
		ImageIcon resizedIcon2 = new ImageIcon(newImg2);
		
		// Create buttons for the two areas
		JButton f1Button = new JButton(resizedIcon2);
		JButton nbaButton = new JButton(resizedIcon);
		
		// Add action listeners to the buttons
		f1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					createBlankPage("F1");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		nbaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					createNBAPage();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		// Add buttons to the main panel
		mainPanel.add(f1Button);
		mainPanel.add(nbaButton);
		
		// Add the main panel to the main frame
		mainFrame.getContentPane().add(mainPanel);
		
		// Display the main window
		mainFrame.setVisible(true);
    }
    private static void createNBAPage() throws SQLException{
		// Create and set up the NBA page window
        Connection connection = DriverManager.getConnection(DatabaseConnection.JDBC_URL_NBA, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);

		try {
			connection.setCatalog("NBA");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String statName = "";
		String[] options = {"Get any stat of any player",
		"Which game happened on 31st March 2014?",
		"List all Players with 10 plus Points in a game and 2 plus Assists",
		"Arrange all the players in descending order on the basis of Assists stat",
		"List Most-Valuable Players(MVP) from all Teams in the Season"};
		String[] queries = {"SELECT\n"
		+ "	Player.PlayerName,\n"
		+ "	SUM(Actions.FieldGoalsMade) AS TotalFieldGoalsMade,\n"
		+ "	SUM(Actions.Steals) AS TotalSteals,\n"
		+ "	SUM(Actions.Points) AS TotalPoints,\n"
		+ "	SUM(Actions.Assist) AS TotalAssists\n"
		+ "FROM\n"
		+ "	Player\n"
		+ "INNER JOIN\n"
		+ "	Actions ON Player.PlayerId = Actions.PlayerId\n"
		+ "WHERE\n"
		+ "	Player.PlayerName like ?\n"
		+ "GROUP BY\n"
		+ "	Player.PlayerName;", "SELECT\n"
		+ "	g.Date,\n"
		+ "	t1.TeamName AS Team1Name,\n"
		+ "	t2.TeamName AS Team2Name,\n"
		+ "	gs.ResultOfTeam1\n"
		+ "FROM\n"
		+ "	Game AS g\n"
		+ "INNER JOIN\n"
		+ "	Team AS t1 ON g.Team1Id = t1.TeamId\n"
		+ "INNER JOIN\n"
		+ "	Team AS t2 ON g.Team2Id = t2.TeamId\n"
		+ "LEFT JOIN\n"
		+ "	GameStatus AS gs ON g.GameId = gs.GameId\n"
		+ "WHERE\n"
		+ "	g.Date LIKE ?;", "select Player.PlayerName, Actions.PlayerId,Actions.GameId,Actions.Assist,Actions.Points from Player,Actions where Actions.PlayerId = Player.PlayerId and Assist > ? and Points > ?",
		"select Player.PlayerName,\n"
		+ "Actions.PlayerId, sum("+statName+") as Total\n"
		+ "from Player,Actions\n"
		+ "where Player.PlayerId = Actions.PlayerId\n"
		+ "Group by Actions.PlayerId\n"
		+ "order by Total desc;", "WITH temp AS (\n"
		+ "    SELECT \n"
		+ "        Team.TeamName,\n"
		+ "        Player.PlayerName,\n"
		+ "        (SUM(Actions.Points) * 3) + (SUM(Actions.Assist) * 2) + (SUM(Actions.TotalRebounds) * 1) AS MVP_score,\n"
		+ "        ROW_NUMBER() OVER (PARTITION BY Team.TeamId ORDER BY (SUM(Actions.Points) * 3) + (SUM(Actions.Assist) * 2) + (SUM(Actions.TotalRebounds) * 1) DESC) AS 'rank_final'\n"
		+ "    FROM \n"
		+ "        Team, Player, Actions\n"
		+ "    WHERE \n"
		+ "        Player.PlayerId = Actions.PlayerId\n"
		+ "        AND Team.TeamId = Actions.TeamId\n"
		+ "    GROUP BY \n"
		+ "        Team.TeamId, Player.PlayerId\n"
		+ ")\n"
		+ "SELECT \n"
		+ "    TeamName,\n"
		+ "    PlayerName,\n"
		+ "    MVP_score\n"
		+ "FROM \n"
		+ "    temp\n"
		+ "WHERE \n"
		+ "    rank_final = 1"};
		// Create and set up the JFrame
		JFrame frame = new JFrame("NBA");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a custom JPanel with background image
		JPanel panel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				try {
					String currentDirectory = System.getProperty("user.dir");
		            // Specify the file name you want to load from the current directory
		            String fileName = "nbabg3.jpg";
		            // Construct the file path
		            String filePath = currentDirectory + File.separator + "src/f2NBA/" + fileName;

					Image backgroundImage = ImageIO.read(new File(filePath)); // Change this to your image path
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		// Main panel layout
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		panel.setOpaque(false);
		
		// Query 1
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel1.setOpaque(false);
		
		JTextField textField1 = new JTextField(10);
		JButton button1 = new JButton("Run");
		panel1.add(new JLabel("Get stats of player: "));
		panel1.add(textField1);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(panel1, gbc);
		gbc.gridx = 1;
		panel.add(button1, gbc);
		
		// Query 2
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel2.setOpaque(false);
		
		JTextField dateField = new JTextField(30);
		dateField.setColumns(10);
		JButton button2 = new JButton("Run");
		panel2.add(new JLabel("Get games that happened on: "));
		panel2.add(dateField);
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(panel2, gbc);
		gbc.gridx = 1;
		panel.add(button2, gbc);
		
		// Query 3
		JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel3.setOpaque(false);
		
		JTextField textField3a = new JTextField(10);
		JTextField textField3b = new JTextField(10);
		JButton button3 = new JButton("Run");
		panel3.add(new JLabel("Get all players with more than "));
		panel3.add(textField3a);
		panel3.add(new JLabel(" assists and "));
		panel3.add(textField3b);
		panel3.add(new JLabel(" points"));
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(panel3, gbc);
		gbc.gridx = 1;
		panel.add(button3, gbc);
		
		// Query 4
		JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel4.setOpaque(false);
		
		String[] stats = {"Points", "Assist", "TotalRebounds", "Steals", "BlockedShots", "BlocksAgainst", "PersonalFouls"};
		JComboBox<String> comboBox4 = new JComboBox<>(stats);
		JButton button4 = new JButton("Run");
		panel4.add(new JLabel("Arrange players in descending order based on the stat: "));
		panel4.add(comboBox4);
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(panel4, gbc);
		gbc.gridx = 1;
		panel.add(button4, gbc);
		
		// Query 5
		JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel5.setOpaque(false);
		
		JTextField textField5 = new JTextField(10);
		JButton button5 = new JButton("Run");
		panel5.add(new JLabel("List most valuable players from all teams in the season: "));
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(panel5, gbc);
		gbc.gridx = 1;
		panel.add(button5, gbc);
		
		frame.add(panel);
		frame.setVisible(true);
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = queries[0];
				System.out.println("Button pressed "+textField1.getText());
				System.out.println(query);
				
				try {
					PreparedStatement statement = connection.prepareStatement(query);

					statement.setString(1,"%"+textField1.getText()+"%");
					ResultSet resultSet = statement.executeQuery();
					
					displayResultsTable(resultSet);
				}
				catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = queries[1];
				//System.out.println("Button pressed "+"%"+dateField.getText()+"%");
				
				try {
					PreparedStatement statement = connection.prepareStatement(query);
					System.out.println(dateField.getText());

					statement.setString(1,"%"+dateField.getText()+"%");
					ResultSet resultSet = statement.executeQuery();
					
					displayResultsTable(resultSet);
				}
				catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = queries[2];
				//System.out.println("Button pressed "+"%"+dateField.getText()+"%");
				
				try {
					PreparedStatement statement = connection.prepareStatement(query);
					System.out.println(dateField.getText());

					statement.setInt(1,Integer.parseInt(textField3a.getText()));
					statement.setInt(2,Integer.parseInt(textField3b.getText()));

					ResultSet resultSet = statement.executeQuery();
					
					displayResultsTable(resultSet);
				}
				catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = "select Player.PlayerName,\n"
				+ "Actions.PlayerId, sum("+comboBox4.getSelectedItem().toString().trim()+") as Total_"+comboBox4.getSelectedItem().toString().trim()+"\n"
				+ "from Player,Actions\n"
				+ "where Player.PlayerId = Actions.PlayerId\n"
				+ "Group by Actions.PlayerId\n"
				+ "order by Total_"+comboBox4.getSelectedItem().toString().trim()+" desc;";
				//System.out.println("Button pressed "+"%"+dateField.getText()+"%");
				System.out.println(query);
				
				try {
					PreparedStatement statement = connection.prepareStatement(query);
					

					ResultSet resultSet = statement.executeQuery();
					
					displayResultsTable(resultSet);
				}
				catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		button5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = queries[4];
				
				try {
					PreparedStatement statement = connection.prepareStatement(query);

					ResultSet resultSet = statement.executeQuery();
					
					displayResultsTable(resultSet);
				}
				catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

    private static void displayResults(String query, Connection connection) {
        // Connect to the database and execute the query

        try{
            try (
            	Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                // Create a new frame to display the results
                JFrame resultFrame = new JFrame("Query Results");
                resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resultFrame.setSize(400, 300);

                // Create a JTextArea to display the results
                JTextArea textArea = new JTextArea();
                ResultSetMetaData rsmd = resultSet.getMetaData();
    			int columnsNumber = rsmd.getColumnCount();


                while (resultSet.next()) {
    				String result = "";
    			    for (int i = 1; i <= columnsNumber; i++) {
    			        if (i > 1) result+=",  ";
    			        String columnValue = resultSet.getString(i);
    			        result+=columnValue;
    			    }
    			    result+="\n";
    			    textArea.append(result);
    			}

                // Add the JTextArea to the frame
                resultFrame.add(new JScrollPane(textArea));
                resultFrame.setVisible(true);
                
    			
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private static void displayResultsTable(ResultSet resultSet) {
		try{
			System.out.println("Entered display table");
			// Create a new frame to display the results
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnsNumber = metaData.getColumnCount();
			
			// Create column names array
			String[] columnNames = new String[columnsNumber];
			for (int i = 1; i <= columnsNumber; i++) {
				columnNames[i - 1] = metaData.getColumnName(i);
			}
			
			// Create data array
			ArrayList<Object[]> data = new ArrayList<>();
			while (resultSet.next()) {
				Object[] row = new Object[columnsNumber];
				for (int i = 1; i <= columnsNumber; i++) {
					row[i - 1] = resultSet.getObject(i);
				}
				data.add(row);
			}
			
			// Create table model
			DefaultTableModel model = new DefaultTableModel(data.toArray(new Object[0][]), columnNames);
			
			// Create table with model
			JTable table = new JTable(model);
			table.setRowHeight(30);
			
			JTableHeader header = table.getTableHeader();
			header.setBackground(Color.BLUE);
			header.setForeground(Color.WHITE);
			
			// Create frame to display table
			JFrame resultFrame = new JFrame("Results");
			resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			resultFrame.add(new JScrollPane(table));
			resultFrame.pack();
			resultFrame.setVisible(true);
			
			
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
    private static void displayResultsTable(String query, Connection connection) {
		try{
			try (Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query)) {
				// Create a new frame to display the results
				ResultSetMetaData metaData = resultSet.getMetaData();
				int columnsNumber = metaData.getColumnCount();
				
				// Create column names array
				String[] columnNames = new String[columnsNumber];
				for (int i = 1; i <= columnsNumber; i++) {
					columnNames[i - 1] = metaData.getColumnName(i);
				}
				
				// Create data array
				ArrayList<Object[]> data = new ArrayList<>();
				while (resultSet.next()) {
					Object[] row = new Object[columnsNumber];
					for (int i = 1; i <= columnsNumber; i++) {
						row[i - 1] = resultSet.getObject(i);
					}
					data.add(row);
				}
				
				// Create table model
				DefaultTableModel model = new DefaultTableModel(data.toArray(new Object[0][]), columnNames);
				
				// Create table with model
				JTable table = new JTable(model);
				table.setRowHeight(30);
				
				JTableHeader header = table.getTableHeader();
				header.setBackground(Color.BLUE);
				header.setForeground(Color.WHITE);
				
				// Create frame to display table
				JFrame resultFrame = new JFrame("Results");
				resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				resultFrame.add(new JScrollPane(table));
				resultFrame.pack();
				resultFrame.setVisible(true);
				
				
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

    private static void createBlankPage(String pageTitle) throws SQLException {
        // Create and set up the blank page window
    	Connection connection = DriverManager.getConnection(DatabaseConnection.JDBC_URL_formula1_final, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
    	
    	String[] queries = {"SELECT r.constructorID, d.name, SUM(r.points)\r\n"
    			+ "FROM constructorResults r, constructor_details d\r\n"
    			+ "WHERE r.constructorID = d.constructorID\r\n"
    			+ "GROUP BY r.constructorID\r\n"
    			+ "ORDER BY SUM(r.points) DESC\r\n"
    			+ "LIMIT 10",
    			
    			"select d.forename, d.surname, count(*) as number_of_wins\r\n"
    					+ "from driver_details d, driverStandings s\r\n"
    					+ "where d.driverID = s.driverID and s.position in ('1', '2', '3')\r\n"
    					+ "group by d.forename, d.surname \r\n"
    					+ "order by number_of_wins desc limit 20",
    					"with final as\r\n"
    							+ "	(with fastestspeed as\r\n"
    							+ "		(select r.raceID, max(s.fastestLapSpeed) as FLS from race_results r, lap_statistics s where r.resultsID = s.resultsID group by r.raceID)\r\n"
    							+ "	select round.circuitID, max(f.FLS) as fastest from race_round round, fastestspeed f  where f.raceID = round.raceID group by round.circuitID)\r\n"
    							+ "select c.circuitID, res.driverID, c.name , d.forename, d.surname, final.fastest\r\n"
    							+ "from circuit_name c, final, race_results res, lap_statistics s, driver_details d\r\n"
    							+ "where c.circuitID = final.circuitID\r\n"
    							+ "and res.resultsID = s.resultsID\r\n"
    							+ "and s.fastestLapSpeed = final.fastest\r\n"
    							+ "and d.driverID = res.driverID","select driver_details.forename as Firstname,driver_details.surname as Lastname,sum(lap_statistics.points) as total_points\r\n"
    									+ "from lap_statistics,race_results,dates,race_round,driver_details\r\n"
    									+ "where race_results.resultsID=lap_statistics.resultsID and\r\n"
    									+ "race_results.raceID=race_round.raceID and\r\n"
    									+ "race_results.driverID=driver_details.driverID and\r\n"
    									+ "race_round.date=dates.date and\r\n"
    									+ "dates.year between '1960' AND '2009'\r\n"
    									+ "group by driver_details.driverID \r\n"
    									+ "order by total_points desc","select driver_details.forename as Firstname,driver_details.surname as Lastname,sum(lap_statistics.points) as total_points\r\n"
    											+ "from lap_statistics,race_results,dates,race_round,driver_details\r\n"
    											+ "where race_results.resultsID=lap_statistics.resultsID and\r\n"
    											+ "race_results.raceID=race_round.raceID and\r\n"
    											+ "race_results.driverID=driver_details.driverID and\r\n"
    											+ "race_round.date=dates.date and\r\n"
    											+ "dates.year between '2010' AND '2024'\r\n"
    											+ "group by driver_details.driverID \r\n"
    											+ "order by total_points desc","select driver_details.forename as Firstname,driver_details.surname as Lastname,driver_details.nationality as DriverNationality,\r\n"
    													+ "constructor_details.name as ConstructorName,constructor_ref.nationality as ConstructorNationality from\r\n"
    													+ "driver_details,constructor_details,constructor_ref,lap_statistics,race_results\r\n"
    													+ "where driver_details.driverID=race_results.driverID and\r\n"
    													+ "race_results.resultsID=lap_statistics.resultsID and\r\n"
    													+ "lap_statistics.constructorID=constructor_details.constructorID and\r\n"
    													+ "constructor_details.name=constructor_ref.name\r\n"
    													+ "group by driver_details.driverID,constructor_details.constructorID\r\n"
    													+ "order by driver_details.driverID asc","select driver_details.forename as FirstName,driver_details.surname as LastName,circuit_info.name as CircuitName,count(*) as NumberOfWins\r\n"
    															+ "from driver_details,circuit_info,lap_statistics,race_results,race_round\r\n"
    															+ "where driver_details.driverID=race_results.driverID and\r\n"
    															+ "race_results.resultsID=lap_statistics.resultsID and\r\n"
    															+ "race_round.raceID=race_results.raceID and\r\n"
    															+ "race_round.circuitID=circuit_info.circuitID and\r\n"
    															+ "lap_statistics.positionOrder=1\r\n"
    															+ "group by circuit_info.circuitID,driver_details.driverID","WITH temp AS (\r\n"
    																	+ "    SELECT \r\n"
    																	+ "		circuit_info.name AS CircuitName,\r\n"
    																	+ "        driver_details.forename AS FirstName,\r\n"
    																	+ "        driver_details.surname AS LastName,\r\n"
    																	+ "        COUNT(*) AS NumberOfWins,\r\n"
    																	+ "        ROW_NUMBER() OVER (PARTITION BY circuit_info.circuitID ORDER BY COUNT(*) DESC) AS rank_final\r\n"
    																	+ "    FROM \r\n"
    																	+ "        driver_details, circuit_info, lap_statistics, race_results, race_round\r\n"
    																	+ "    WHERE \r\n"
    																	+ "        driver_details.driverID = race_results.driverID\r\n"
    																	+ "        AND race_results.resultsID = lap_statistics.resultsID\r\n"
    																	+ "        AND race_round.raceID = race_results.raceID\r\n"
    																	+ "        AND race_round.circuitID = circuit_info.circuitID\r\n"
    																	+ "        AND lap_statistics.positionOrder = 1\r\n"
    																	+ "    GROUP BY \r\n"
    																	+ "        circuit_info.circuitID, driver_details.driverID\r\n"
    																	+ ")\r\n"
    																	+ "SELECT\r\n"
    																	+ "	CircuitName, \r\n"
    																	+ "    FirstName,\r\n"
    																	+ "    LastName,\r\n"
    																	+ "    NumberOfWins\r\n"
    																	+ "FROM \r\n"
    																	+ "    temp\r\n"
    																	+ "WHERE \r\n"
    																	+ "    rank_final = 1"};
        // Create and set up the JFrame
        JFrame frame = new JFrame("Formula 1");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a custom JPanel with background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                	String currentDirectory = System.getProperty("user.dir");
		            System.out.println(currentDirectory);
		            // Specify the file name you want to load from the current directory
		            String fileName = "f1_background2.png";
		            System.out.println(fileName);
		            // Construct the file path
		            String filePath = currentDirectory + File.separator + "src/f2NBA/" + fileName;
		            System.out.println(filePath);
                    Image backgroundImage = ImageIO.read(new File(filePath)); // Change this to your image path
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        panel.setLayout(new GridBagLayout());

        // Create the JComboBox
        String[] options = {"Top 10 teams of all time and points", 
        		"Total number of first position finishes for all the drivers",
        		"Fastest lap times of all circuits and drivers",
        		"Total number of points won by drivers until the old points system",
        		"Total number of points won by drivers with the new points system",
        		"Drivers and the different constructors that they have paired with",
        		"Drivers, circuits and the number of wins there",
        		"Driver with most number of wins on a particular circuit"
        		};
        JComboBox<String> comboBox = new JComboBox<>(options);
        // Set preferred size for the JComboBox
        comboBox.setPreferredSize(new Dimension(500, 50));

        // Create the JButton
        JButton button = new JButton("Run");
        // Set preferred size for the JButton (wider)
        button.setPreferredSize(new Dimension(150, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 50, 20, 50); // Reduce top and bottom insets
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(comboBox, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(button, gbc);

        // Add the custom panel to the frame
        frame.setContentPane(panel);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Display the frame
        frame.setVisible(true);    
        
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayResultsTable(queries[comboBox.getSelectedIndex()], connection);
            }
        });
    }
    
//    private static void createBlankPageAB(String pageTitle) {
//    	
//    	JFrame blankFrame = new JFrame(pageTitle);
//        blankFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        blankFrame.setSize(600, 400);
//        blankFrame.setLocationRelativeTo(null); // Center align the blank page window
//
//        // Create panel for components
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        blankFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        // Center align the blank page window
//        blankFrame.getContentPane().add(mainPanel);
//        blankFrame.setVisible(true);
//
//    }
//
//
//    private static void openBlankPageWithOption(String option) {
//        // Create and set up the blank page window
//        JFrame blankFrame = new JFrame(option);
//        blankFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        blankFrame.setSize(600, 400);
//        blankFrame.setLocationRelativeTo(null); // Center align the blank page window
//
//        // Create a label with the selected option displayed in bigger font size
//        JLabel label = new JLabel(option);
//        label.setFont(new Font("Arial", Font.BOLD, 24)); // Bigger font size
//
//        // Add the label to the blank page frame
//        blankFrame.getContentPane().add(label);
//
//        // Display the blank page window
//        blankFrame.setVisible(true);
//    }

//    public static void main(String[] args) {
//        // Schedule a job for the event-dispatching thread:
//        // creating and showing the login window
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGUI();
//            }
//        });
//    }
}
