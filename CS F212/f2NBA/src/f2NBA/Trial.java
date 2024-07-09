package f2NBA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Trial {
	private static Connection connection;
	private static void createAndShowGUI() {
		// Create and set up the login window
		JFrame loginFrame = new JFrame("Login Page");
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setSize(300, 150);
		loginFrame.setLocationRelativeTo(null); // Center align the login window
		
		// Create username and password labels and text fields
		JLabel usernameLabel = new JLabel("Username:");
		JTextField usernameField = new JTextField(20);
		JLabel passwordLabel = new JLabel("Password:");
		JPasswordField passwordField = new JPasswordField(20);
		
		// Create login button
		JButton loginButton = new JButton("Login");
		
		// Add action listener to the login button
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Replace this with your actual login logic
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				if (username.equals("admin") && password.equals("admin")) {
					// Close login window
					loginFrame.dispose();
					// Open main window
					createMainWindow();
				} else {
					JOptionPane.showMessageDialog(loginFrame, "Invalid username or password. Please try again.");
				}
			}
		});
		
		// Create a panel and add components
		JPanel loginPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		loginPanel.add(usernameLabel, gbc);
		gbc.gridy++;
		loginPanel.add(passwordLabel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		loginPanel.add(usernameField, gbc);
		gbc.gridy++;
		loginPanel.add(passwordField, gbc);
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.CENTER;
		loginPanel.add(loginButton, gbc);
		
		// Add the panel to the login frame
		loginFrame.getContentPane().add(loginPanel);
		
		// Display the login window
		loginFrame.setVisible(true);
	}
	
	private static void createMainWindow() {
		// Create and set up the main window
		JFrame mainFrame = new JFrame("Sportify");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(600, 400);
		mainFrame.setLocationRelativeTo(null); // Center align the main window
		
		// Create panel for dividing the window into two areas
		JPanel mainPanel = new JPanel(new GridLayout(1, 2));
		ImageIcon originalIcon = new ImageIcon("C:\\Users\\siddh\\Downloads\\NBA_foto.png");
		
		Image img = originalIcon.getImage();
		Image newImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Specify new width and height
		ImageIcon resizedIcon = new ImageIcon(newImg);
		
		ImageIcon originalIcon2 = new ImageIcon("C:\\Users\\siddh\\Downloads\\F1-Logo.png");
		
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
				createBlankPage("F1");
			}
		});
		
		nbaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNBAPage();
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
	
	private static void displayResults(String query) {
		// Connect to the database and execute the query
		
		try{
			try (Statement statement = connection.createStatement();
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
	
	private static void displayResultsTable(String query) {
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
	
	private static void createBlankPage(String pageTitle) {
		
		String testQuery = "SELECT r.constructorID, d.name, SUM(r.points)\r\n"
		+ "FROM constructorResults r, constructor_details d\r\n"
		+ "WHERE r.constructorID = d.constructorID\r\n"
		+ "GROUP BY r.constructorID\r\n"
		+ "ORDER BY SUM(r.points) DESC\r\n"
		+ "LIMIT 10";
		
		String[] queries = {"SELECT r.constructorID, d.name, SUM(r.points)\r\n"
		+ "FROM constructorResults r, constructor_details d\r\n"
		+ "WHERE r.constructorID = d.constructorID\r\n"
		+ "GROUP BY r.constructorID\r\n"
		+ "ORDER BY SUM(r.points) DESC\r\n"
		+ "LIMIT 10","select d.forename, d.surname, count(*) as number_of_wins\r\n"
		+ "from driver_details d, driverStandings s\r\n"
		+ "where d.driverID = s.driverID and s.position in ('1', '2', '3')\r\n"
		+ "group by d.forename, d.surname \r\n"
		+ "order by number_of_wins desc limit 20","with final as\r\n"
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
		JFrame frame = new JFrame("Swing Example");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a custom JPanel with background image
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				try {
					Image backgroundImage = ImageIO.read(new File("C:\\Users\\siddh\\Downloads\\f1_background2.png")); // Change this to your image path
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		panel.setLayout(new GridBagLayout());
		
		// Create the JComboBox
		String[] options = {"Top 10 teams of all time and points", "Total number of first position finishes for all the drivers", "Fastest lap times of all circuits and drivers", "Total number of points won by drivers until the old points system","Total number of points won by drivers with the new points system","Drivers and the different constructors that they have paired with","Drivers, circuits and the number of wins there","Driver with most number of wins on a particular circuit"};
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
				displayResultsTable(queries[comboBox.getSelectedIndex()]);
			}
		});
		
		
		
	}
	
	private static void createNBAPage() {
		
		String testQuery = "SELECT r.constructorID, d.name, SUM(r.points)\r\n"
		+ "FROM constructorResults r, constructor_details d\r\n"
		+ "WHERE r.constructorID = d.constructorID\r\n"
		+ "GROUP BY r.constructorID\r\n"
		+ "ORDER BY SUM(r.points) DESC\r\n"
		+ "LIMIT 10";
		
		String[] queries;
		// Create and set up the JFrame
		JFrame frame = new JFrame("Swing Example");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a custom JPanel with background image
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				try {
					Image backgroundImage = ImageIO.read(new File("C:\\Users\\siddh\\Downloads\\sunshine.jpg")); // Change this to your image path
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    ButtonGroup group = new ButtonGroup();

    JCheckBox checkBox1 = new JCheckBox("Get any stat of player");
    JTextField textField1 = new JTextField(10);
    group.add(checkBox1);
    panel.add(checkBox1);
    panel.add(textField1);

    // Repeat for each checkbox and text field pair
    JCheckBox checkBox2 = new JCheckBox("Another option");
    JTextField textField2 = new JTextField(10);
    group.add(checkBox2);
    panel.add(checkBox2);
    panel.add(textField2);    
		
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing the login window
		String url = "jdbc:mysql://172.17.66.164:3306/formula1_final";
		String username = "winga7";
		String pass = "ashok";
		try{
			//Class.forName("com.mysql.cj.jdbc.Driver");
			
			connection = DriverManager.getConnection(url,username,pass);
			
			Statement statement = connection.createStatement();
			
			//ResultSet resultSet = statement.executeQuery("select * from dates");
			
			System.out.println("Successfully connected");
			
			//            while (resultSet.next()) {
				//                // Assuming your 'dates' table has columns named 'column1', 'column2', etc.
				//                // Replace these column names with your actual column names.
				//                // For example, if your 'dates' table has a 'date' column, you would use resultSet.getDate("date")
				//                int year = resultSet.getInt("year");
				//                // Print the values or process them as needed
				//                System.out.println(year);
				//            }
				
				
			}
			catch(Exception e){
				System.out.println(e);
			}
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			});
			//			finally {
				//				try {
					//					connection.close();
					//				} catch (SQLException e) {
						//					// TODO Auto-generated catch block
						//					e.printStackTrace();
						//				}
						//			}
						
					}
					
				}



