import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException; 

public class VotingSystemAWT9 extends Frame implements ActionListener {
    private TextField aadhaarField, voterIdField, phoneField, otpField;
    private Label messageLabel;
    private Button verifyAadhaarButton, verifyVoterButton, sendOtpButton, verifyOtpButton, voteButton;
    private Choice candidateChoice;
    private String aadhaarNumber,voterId;
    private String generatedOtp;
    private Image backgroundImage;
    private Image passportPhoto;
    private static Map<String, String> aadhaarToVoterIdMap = new HashMap<>();
    private static Map<String, String> otpMap = new HashMap<>();
	 boolean flag=false;

    static {
        aadhaarToVoterIdMap.put("123456789012", "VOTER12345");
        aadhaarToVoterIdMap.put("987654321098", "VOTER67890");
		aadhaarToVoterIdMap.put("123456789013", "VOTER12346");
		aadhaarToVoterIdMap.put("123456789014", "VOTER12344");
		 
    }

    public VotingSystemAWT9()
	{
        setTitle("Voting System");
        setSize(900, 600);
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        // Load images
        backgroundImage = Toolkit.getDefaultToolkit().getImage("C:\\Users\\laksh\\Downloads\\WhatsApp Image 2025-02-28 at 22.06.36_07b9c1ff.jpg");
        passportPhoto = Toolkit.getDefaultToolkit().getImage("C:\\Users\\laksh\\Downloads\\swathi.jpg");
     

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        Font labelFont = new Font("ITALIC", Font.ITALIC, 25);
        Font buttonFont = new Font("ITALIC", Font.ITALIC, 28);

        gbc.gridx = 0; gbc.gridy = 0;
        add(createColoredLabel("Aadhaar Number:", labelFont, Color.BLUE), gbc);
        gbc.gridx = 1;
        aadhaarField = createColoredTextField(30, Color.CYAN);
        add(aadhaarField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(createColoredLabel("Voter ID:", labelFont, Color.BLUE), gbc);
        gbc.gridx = 1;
        voterIdField = createColoredTextField(30, Color.CYAN);
        add(voterIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(createColoredLabel("Phone Number:", labelFont, Color.BLUE), gbc);
        gbc.gridx = 1;
        phoneField = createColoredTextField(30, Color.CYAN);
        add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        add(createColoredLabel("Enter OTP:", labelFont, Color.BLUE), gbc);
        gbc.gridx = 1;
        otpField = createColoredTextField(30, Color.CYAN);
        add(otpField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        add(createColoredLabel("Select Candidate:", labelFont, Color.BLUE), gbc);
        gbc.gridx = 1;
        candidateChoice = new Choice();
        candidateChoice.add("Candidate A");
        candidateChoice.add("Candidate B");
        candidateChoice.add("Candidate C");
        add(candidateChoice, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        verifyAadhaarButton = createColoredButton("Verify Aadhaar", buttonFont, Color.GREEN);
        add(verifyAadhaarButton, gbc);
        gbc.gridx = 1;
        verifyVoterButton = createColoredButton("Verify Voter ID", buttonFont, Color.GREEN);
        add(verifyVoterButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        sendOtpButton = createColoredButton("Send OTP", buttonFont, Color.ORANGE);
        add(sendOtpButton, gbc);
        gbc.gridx = 1;
        verifyOtpButton = createColoredButton("Verify OTP", buttonFont, Color.ORANGE);
        add(verifyOtpButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        voteButton = createColoredButton("Vote", buttonFont, Color.RED);
        voteButton.setEnabled(false);
        add(voteButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 10;
        messageLabel = new Label("", Label.CENTER);
        messageLabel.setForeground(Color.BLUE);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 25));
        add(messageLabel, gbc);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        
        // Draw passport-size photo in the top-right corner
        int photoWidth = 200;  // Width of the passport photo
        int photoHeight = 190; // Height of the passport photo
        int xPosition = getWidth() - photoWidth - 20;  // Right margin
        int yPosition = 50;  // Top margin

        g.drawImage(passportPhoto, xPosition, yPosition, photoWidth, photoHeight, this);
    }

    private Label createColoredLabel(String text, Font font, Color color) {
        Label label = new Label(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private TextField createColoredTextField(int size, Color bgColor) {
        TextField textField = new TextField(size);
        textField.setBackground(bgColor);
        return textField;
    }

    private Button createColoredButton(String text, Font font, Color bgColor) {
        Button button = new Button(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.addActionListener(this);
        return button;
    }

    public void actionPerformed(ActionEvent e)  {
        if (e.getSource() == verifyAadhaarButton) {
            aadhaarNumber = aadhaarField.getText();
            if (aadhaarToVoterIdMap.containsKey(aadhaarNumber)) {
                messageLabel.setText("Aadhaar Verified.");
            } else {
                messageLabel.setText("Invalid Aadhaar.");
            }
        } else if (e.getSource() == verifyVoterButton) {
             voterId = voterIdField.getText();
            if (aadhaarToVoterIdMap.getOrDefault(aadhaarNumber, "").equals(voterId)) {
                messageLabel.setText("Voter ID Verified.");
            } else {
                messageLabel.setText("Invalid Voter ID.");
            }
        } else if (e.getSource() == sendOtpButton) {
            generatedOtp = generateOTP();
            otpMap.put(aadhaarNumber, generatedOtp);
            messageLabel.setText("OTP Sent: " + generatedOtp);
        } else if (e.getSource() == verifyOtpButton) {
            if (otpField.getText().equals(generatedOtp)) {
                messageLabel.setText("OTP Verified.");
                voteButton.setEnabled(true);
            } else {
                messageLabel.setText("Invalid OTP.");
            }
        } else if (e.getSource() == voteButton) {
			
			 try{
				  //Registering the Driver
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());

      //Getting the connection
      String mysqlUrl = "jdbc:mysql://localhost/voting system";
      Connection con = DriverManager.getConnection(mysqlUrl, "root", "");
      System.out.println("Connection established......");
				  String query1="SELECT Aadharno from voterdetails";
				  Statement stmt=con.createStatement();
				  ResultSet rset=stmt.executeQuery(query1);
				 
				  while(rset.next())
				  {
					if(aadhaarNumber.equals(rset.getString("Aadharno")))
					{
						flag=true;
					}			
				  }
				}
				  catch(SQLException se)
				{
					System.out.println(""+se);
				}
					  
      if(flag)
	  { 
            System.out.println("ALREADY  VOTED"+flag);
		  messageLabel.setText("You are already VOTED" + candidateChoice.getSelectedItem() + "!");
	  }
	  else
	  {  
	     System.out.println("NEW VOTE"+flag);
			try
			{
            messageLabel.setText("You voted for " + candidateChoice.getSelectedItem() + "!");
			//Registering the Driver
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());

      //Getting the connection
      String mysqlUrl = "jdbc:mysql://localhost:4306/voting system";
      Connection con = DriverManager.getConnection(mysqlUrl, "root", "");
      System.out.println("Connection established......");
	 
	
      
		  
      //Creating a Prepared Statement
      String query = "INSERT INTO voterdetails(Aadharno,Voterid,Phoneno,otp)VALUES (?, ?, ?, ?)";
	  
      PreparedStatement pstmt = con.prepareStatement(query);
      
	  pstmt.setString(1,aadhaarNumber);
      pstmt.setString(2,voterId);
      pstmt.setString(3,phoneField.getText());
	  pstmt.setInt(4,Integer.parseInt(generatedOtp));

      
      int num = pstmt.executeUpdate();
      System.out.println(num+"Rows inserted ...."); 
	  messageLabel.setText("You voted for " + candidateChoice.getSelectedItem() + "!");
	  }
        
	
	catch(SQLException se)
	{
		System.out.println(""+se);
	}
    }
		}
}
    private static String generateOTP() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public static void main(String[] args) {
        VotingSystemAWT9 votingSystem = new VotingSystemAWT9();
        votingSystem.setVisible(true);
		
    }
}