import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import javax.swing.event.*;
import javax.swing.text.Document;

public class RideshareApp extends JFrame {

    // card names (keys for different screens)
    private static final String LOGIN = "login";
    private static final String HOME  = "home";
    private static final String BOOK  = "book";
    private static final String PROF  = "profile";
    private static final String HIST  = "history";

    // CardLayout and container
    private final CardLayout c1 = new CardLayout();
    private final JPanel cards = new JPanel(c1);

    public RideshareApp() {
        setTitle("Rideshare App"); // should we come up with a fun name?
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // build each page, within their own methods (see GeeksforGeeks tutorial, jp1, jp2 ...)
        JPanel loginPage = buildLoginPage();
        JPanel homePage = buildHomePage();
        JPanel bookPage = buildBookingPage();
        JPanel profPage = buildProfilePage();
        JPanel histPage = buildHistoryPage();

        // add pages to CardLayout with our keys (see GeeksforGeeks tutorial "1", "2" ... cards)
        cards.add(loginPage, LOGIN);
        cards.add(homePage, HOME);
        //cards.add(bookPage, BOOK);
        cards.add(profPage, PROF);
        //cards.add(histPage, HIST);

        setContentPane(cards);

        c1.show(cards, LOGIN);

    }

    private JPanel buildLoginPage() {
        JPanel loginPage = new JPanel(new GridBagLayout());

        JPanel p = new JPanel(new BorderLayout(8,10));
        //loginPage.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                BorderFactory.createEmptyBorder(14,14,14,14)
        ));
        p.setBackground(Color.WHITE);
        p.setPreferredSize(new Dimension(360, 240));
        JLabel title = new JLabel("Log in");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setHorizontalAlignment(JLabel.CENTER);
        p.add(title, BorderLayout.NORTH);

        // Constrain the text boxes from being too tall in the PII form
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,10,8,10);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        //username
        JLabel userL = new JLabel("Username");
        g.gridx = 0; g.gridy = 0; g.weightx =0;
        form.add(userL, g);

        JTextField userTF = new JTextField(18);
        textHelper(userTF, "user");
        g.gridx = 1; g.gridy = 0; g.weightx = 1;
        form.add(userTF, g);

        //password
        JLabel passL = new JLabel("Password");
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        form.add(passL, g);

        JPasswordField passPF = new JPasswordField(18);
        textHelper(passPF, "password");
        g.gridx = 1; g.gridy = 1; g.weightx = 1;
        form.add(passPF, g);

        //error display
        JLabel error = new JLabel(" ");
        error.setForeground(Color.RED);
        g.gridx = 0; g.gridy = 2; g.gridwidth = 2; g.weightx=1;
        form.add(error, g);

        p.add(form, BorderLayout.CENTER);

        // buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginButton = new JButton("Log in!");
        loginButton.setEnabled(false); // Initially disabled
        buttons.add(loginButton);
        p.add(buttons, BorderLayout.SOUTH);

        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                printIt(documentEvent);
            }

            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                printIt(documentEvent);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                printIt(documentEvent);
            }
            private void printIt(DocumentEvent documentEvent) {
                Document source = documentEvent.getDocument();
                int length = source.getLength();
                loginButton.setEnabled(length != 0);
            }
        };

        // enable only when text in both
        Runnable update = () -> {
            boolean ok = !userTF.getText().trim().isEmpty()
                    && passPF.getPassword().length > 0;
            loginButton.setEnabled(ok);
        };
        userTF.getDocument().addDocumentListener(documentListener);
        passPF.getDocument().addDocumentListener(documentListener);

        loginButton.addActionListener(e -> {
            if (userTF.getText().equals("user") &&
                    new String(passPF.getPassword()).equals("password")) {
                c1.show(cards, HOME);
            } else {
                error.setText("Invalid username or password");
            }
        });


        loginPage.add(p, new GridBagConstraints());
        return loginPage;
    }

    private JPanel buildHomePage() {
        //JPanel homePanel = new JPanel();

        JPanel p = new JPanel(new BorderLayout(8,10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Welcome to Rider Share");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setHorizontalAlignment(JLabel.CENTER);
        p.add(title, BorderLayout.NORTH);


        // center info (I need to think how to organize this page, but this will do for summition)
        JPanel summary = new JPanel(new GridLayout(0,1,3,3));
        summary.add(new JLabel("Something goes here"));
        summary.add(new JLabel("Something goes here"));
        p.add(summary, BorderLayout.WEST);

        // add buttons
        JPanel buttons = new  JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton reviewProfile = new JButton("Review Profile");
        buttons.add(reviewProfile);
        // add more bottoms here

        // change to other cards for demostrations
        JButton showPii = new JButton("Show Profile window");
        showPii.addActionListener(e -> c1.show(cards,PROF));
        buttons.add(showPii);
        JButton logout = new JButton("Log Out");
        logout.addActionListener(e -> c1.show(cards,LOGIN));
        buttons.add(logout);

        p.add(buttons, BorderLayout.SOUTH);

        return p;
    }

    private JPanel buildBookingPage() {
        return null;
    }



    private int addRowHelper(JPanel form, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, gbc);

        return row + 1;
    }

    // a helper (Profile) to set text gray & clear on focus, then restore if empty
    private void textHelper(JTextField tf, String placeHolder){
        tf.setText(placeHolder);
        tf.setForeground(Color.GRAY);
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                if (tf.getText().equals(placeHolder)){
                    tf.setText("");
                    tf.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent event){
                if (tf.getText().isEmpty()){
                    tf.setForeground(Color.GRAY);
                    tf.setText(placeHolder);
                }
            }
        });
    }

    /**
     * Builds the main Profile Page panel with nested layouts for perfect alignment.
     */
    JPanel buildProfilePage() {
        // needed to count rows in the grid
        int prow = 0;
        int adrow = 0;

        // Outer Border page to help with simetry
        JPanel profilePage = new JPanel(new BorderLayout(8,8));
        profilePage.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Constrain the text boxes from being too tall in the PII form
        JPanel gridPii = new JPanel(new GridBagLayout());
        gridPii.setBorder(BorderFactory.createTitledBorder("Personal Information"));
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(4,6,4,6);
        gbc1.anchor = GridBagConstraints.WEST;
        gbc1.fill = GridBagConstraints.HORIZONTAL;

        // better title
        JLabel title = new JLabel("<html><h1>Profile</h1></html>");
        profilePage.add(title, BorderLayout.NORTH);



        // add labes, text boxes & rows
        // row 0
        JTextField nameT =  new JTextField();
        nameT.setColumns(20);
        textHelper(nameT, "Ride Share");
        prow = addRowHelper(gridPii, gbc1, prow,"Full Name:", nameT);

        // row 1
        JTextField emailT = new JTextField();
        emailT.setColumns(11);
        textHelper(emailT, "rideshare@bridgew.com");
        prow = addRowHelper(gridPii,gbc1,prow, "Email:",emailT);


        // row 2
        JTextField phoneT = new JTextField();
        phoneT.setColumns(11);
        textHelper(phoneT, "###-###-####");
        prow = addRowHelper(gridPii, gbc1, prow, "Phone Number:", phoneT);

        // row 3
        JTextField licenseT = new JTextField();
        licenseT.setColumns(11);
        textHelper(licenseT, "ABC-123");
        prow = addRowHelper(gridPii, gbc1, prow, "License Plate:", licenseT);

        // row 4
        //JLabel dobL = new JLabel("Date of Birth:");
        JTextField dobT = new JTextField();
        dobT.setColumns(11);
        textHelper(dobT, "YYYY-MM-DD");
        prow = addRowHelper(gridPii, gbc1, prow, "Date of Birth:", dobT);


        JPanel gridAddr = new JPanel(new GridBagLayout());
        gridAddr.setBorder(BorderFactory.createTitledBorder("Address"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(4,6,4,6);
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.fill = GridBagConstraints.HORIZONTAL;


        // address info labels and text boxes
        // row 1
        //JLabel streetL = new JLabel("Street:");
        JTextField streetT = new JTextField();
        streetT.setColumns(15);
        textHelper(streetT, "123 Plymouth St");
        adrow = addRowHelper(gridAddr, gbc2, adrow, "Street:", streetT);

        // row 2
        //JLabel cityL = new JLabel("City:");
        JTextField cityT = new JTextField();
        cityT.setColumns(11);
        textHelper(cityT, "Bridgewater");
        adrow = addRowHelper(gridAddr, gbc2, adrow, "City:", cityT);

        //row 3
        //JLabel stateL = new JLabel("State:");
        JTextField stateT = new JTextField();
        stateT.setColumns(9);
        textHelper(stateT, "MA");
        adrow = addRowHelper(gridAddr, gbc2, adrow, "State:", stateT);

        // row 4
        //JLabel countryL = new JLabel("Country:");
        JTextField countryT = new JTextField();
        countryT.setColumns(8);
        textHelper(countryT, "USA");
        adrow = addRowHelper(gridAddr, gbc2, adrow, "Country:", countryT);

        // row 5
        //JLabel zipL = new JLabel("ZIP Code:");
        JTextField zipT = new JTextField();
        zipT.setColumns(8);
        textHelper(zipT, "#####");
        adrow = addRowHelper(gridAddr, gbc2, adrow, "ZIP Code:", zipT);



        // add Panel and addrPanel to center side-by-side
        JPanel center = new JPanel(new GridLayout(1,2,0,0)); // <===
        center.add(gridPii);
        center.add(gridAddr);

        // add 1x3 grid below the two forms and above Save bottom
        JPanel paymentRow = new JPanel(new GridBagLayout());
        GridBagConstraints payGBC = new GridBagConstraints();

        payGBC.insets = new Insets(2,6,2,6);
        payGBC.anchor = GridBagConstraints.WEST;
        payGBC.fill = GridBagConstraints.NONE;

        // Label
        payGBC.gridx = 0;
        payGBC.weightx = 0;
        paymentRow.add(new JLabel("Credit/Debit Card"), payGBC);

        // Text box
        payGBC.gridx = 1;
        payGBC.weightx = 0.1;
        payGBC.fill = GridBagConstraints.HORIZONTAL;
        JTextField cardNumT = new JTextField(10);
        textHelper(cardNumT, "**** **** **** ****");
        paymentRow.add(cardNumT, payGBC);


        payGBC.gridx = 2;
        payGBC.weightx = 0;
        payGBC.fill = GridBagConstraints.NONE;
        paymentRow.add(Box.createHorizontalStrut(8), payGBC);

        // Label
        payGBC.gridx = 3;
        payGBC.weightx = 0;
        paymentRow.add(new JLabel("Exp. Date"), payGBC);

        // Text box
        payGBC.gridx = 4;
        payGBC.weightx = 0.15;
        payGBC.fill = GridBagConstraints.HORIZONTAL;
        JTextField expDateT = new JTextField(6);
        textHelper(expDateT, "MM-YY");
        paymentRow.add(expDateT, payGBC);

        // Label
        payGBC.gridx = 5;
        payGBC.weightx = 0;
        payGBC.fill = GridBagConstraints.NONE;
        paymentRow.add(new JLabel("CVV"), payGBC);


        payGBC.gridx = 6;
        payGBC.weightx = 0.05;
        payGBC.fill = GridBagConstraints.HORIZONTAL;
        JTextField cvvNumT = new JTextField(4);
        textHelper(cvvNumT, "###");
        paymentRow.add(cvvNumT, payGBC);

        // need wrapper so the grid sit below the forms
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.add(center);
        centerWrapper.add(Box.createRigidArea(new Dimension(0,8)));
        centerWrapper.add(paymentRow);
        profilePage.add(centerWrapper, BorderLayout.CENTER);


        // buttons
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(event -> {
            //TODO I need to implement the storing info part

            // Go to the home card
            c1.show(cards, HOME);
        });

        // set bottom to the lower-right of the frame
        JPanel buttonPosition = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPosition.add(saveButton);
        profilePage.add(buttonPosition, BorderLayout.SOUTH);

        return profilePage;
    }

    private JPanel buildHistoryPage() {
        return null;
    }

    public static void main(String[] args) {
        String schemaFile = "schema.sql";
        String databaseURL = "jdbc:sqlite:rideshare.db";

        // establish connection through jdbc (.jar file in lib)
        // if no such database is found it will create locally for you, otherwise connect to db
        try (Connection c = DriverManager.getConnection(databaseURL);
             Statement s = c.createStatement()) {
            // Read the entire schema.sql file into a String
            String sql = Files.readString(Paths.get(schemaFile));
            // Execute each statement separated by semicolons
            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    s.execute(trimmed + ";");
                }
            }

            System.out.println("Schema loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading schema: " + e.getMessage());
        }

//      app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(() ->{
            RideshareApp app = new RideshareApp();
            app.setVisible(true);
        });

    }
}