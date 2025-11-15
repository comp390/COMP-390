import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class RideshareApp extends JFrame {

    // card names (keys for different screens)
    private static final String LOGIN = "login";
    private static final String HOME  = "home";
    private static final String BOOK  = "book";
    private static final String PROF  = "profile";
    private static final String HIST  = "history";
    private static final String VIEW_PROF = "viewProf";


    private int currentUserID; // for now keep until correct login is implemented
    private JLabel[] profileViewLabels;

    // need these in the fiel area so we can load and make data persist
    private JTextField firstNameT;
    private JTextField lastNameT;
    private JTextField emailT;
    private JTextField phoneT;
    private JTextField licenseT;
    private JTextField dobT;
    private JTextField streetT;
    private JTextField cityT;
    private JTextField stateT;
    private JTextField countryT;
    private JTextField zipT;

    // CardLayout and container
    private final CardLayout c1 = new CardLayout();
    private final JPanel cards = new JPanel(c1);

    public RideshareApp() {
        setTitle("Rideshare App"); // should we come up with a fun name?
                                   // ^ Yes! I think we can do better!
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // build each page, within their own methods (see GeeksforGeeks tutorial, jp1, jp2 ...)
        JPanel loginPage = buildLoginPage();

        //NO NEEDED HERE!!! let's build it after user login, to show user info
//      JPanel homePage = buildHomePage();
//      cards.add(homePage, HOME);

        JPanel bookPage = buildBookingPage();
        JPanel profPage = buildProfilePage();
        JPanel histPage = buildHistoryPage();
        JPanel viewProf = buildProfileOverviewPage();

        // add pages to CardLayout with our keys (see GeeksforGeeks tutorial "1", "2" ... cards)
        cards.add(loginPage, LOGIN);

        //cards.add(bookPage, BOOK);
        cards.add(profPage, PROF);
        cards.add(histPage, HIST);
        cards.add(viewProf, VIEW_PROF);

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

            String username = userTF.getText().trim();
            String password = new String(passPF.getPassword()).trim();

            try {
                UserDAOSQLite userDao = new UserDAOSQLite();
                List<User> allUsers = userDao.findAll();

                User matched = null;

                //need protection (in-memory as plain text) Big no no!!
                for (User u : allUsers){
                    if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                        matched = u;
                        break;
                    }
                }

                if (matched != null) {
                    error.setText(" ");

                    // This is the key part to track the user ID
                     currentUserID = matched.getId();

                    // rebuild to display user info if login
                    JPanel newHome = buildHomePage();
                    cards.add(newHome, HOME);

                    // reload the data for the user
                    loadUserIntoViewProf();

                    c1.show(cards, HOME);
                }else {
                    error.setText("Invalid username or password");
                    resetLoginFields(loginPage);
                }
            } catch (Exception ex) {
                error.setText("User or password not match in database");
                ex.printStackTrace();
            }

            //
//            if (userTF.getText().equals("user") &&
//                    new String(passPF.getPassword()).equals("password")) {
//                error.setText(" ");
//                c1.show(cards, HOME);
//            } else {
//                error.setText("Invalid username or password");
//            }
        });


        loginPage.add(p, new GridBagConstraints());
        return loginPage;
    }

    // This will return the current user's username
    private String getCurrentUserName(){
        try {
            UserDAOSQLite dao = new UserDAOSQLite();
            Optional<User> opt = dao.findById(currentUserID);
            if (opt.isPresent()){
                User u = opt.get();
                return u.getUsername();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Commander";
    }

    private JPanel buildHomePage() {
        //JPanel homePanel = new JPanel();
        JPanel homePanel = new JPanel(new BorderLayout(10, 10));
        homePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10 ,10));

        String userName = getCurrentUserName();
        JLabel title = new JLabel("Hi "+userName.toUpperCase(Locale.ROOT)+ "!\n Where're you heading today?");

        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setHorizontalAlignment(JLabel.CENTER);
        homePanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        JPanel statsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Your Account"));

        int totalTrips = 0;
        double totalSpent = 0.0;
        String lastRide = "No rides yet";

        try {
            HistoryDAO historyDAO = new HistoryDAOSQLite();
            List<History> userHistory = historyDAO.findUserHistory(currentUserID);

            totalTrips = userHistory.size();

            for (History trip : userHistory) {
                if (trip.getFare() != null) {
                    totalSpent += trip.getFare();
                }
            }

            if (!userHistory.isEmpty()) {
                History last = userHistory.get(userHistory.size() - 1);
                lastRide = last.getPickupLoc() + " to " + last.getDropoffLoc();
            }

        } catch (Exception ex) {
            System.err.println("Error loading statistics: " + ex.getMessage());
        }

        statsPanel.add(new JLabel("Total Trips: " + totalTrips));
        statsPanel.add(new JLabel("Total Spent: $" + String.format("%.2f", totalSpent)));
        statsPanel.add(new JLabel("Last Ride: " + lastRide));

        JPanel tripsPanel = new JPanel(new BorderLayout());
        tripsPanel.setBorder(BorderFactory.createTitledBorder("Recent Trips"));

        JPanel tripsList = new JPanel();
        tripsList.setLayout(new BoxLayout(tripsList, BoxLayout.Y_AXIS));

        try {
            HistoryDAO historyDAO = new HistoryDAOSQLite();
            int currentUserID = 1;
            List<History> userHistory = historyDAO.findUserHistory(currentUserID);

            if (userHistory.isEmpty()) {
                tripsList.add(new JLabel("No trips yet"));
            } else {
                int displayCount = Math.min(5, userHistory.size());
                for (int i = userHistory.size() -1; i >= userHistory.size() - displayCount; i--){
                    History trip = userHistory.get(i);
                    String tripText = String.format("%s -> %s ($%.2f)",
                            trip.getPickupLoc(), trip.getDropoffLoc(), trip.getFare());
                    tripsList.add(new JLabel(tripText));
                    }
                }
            } catch (Exception ex) {
                tripsList.add(new JLabel("Error loading trips"));
            }

        JScrollPane scrollPane = new JScrollPane(tripsList);
        scrollPane.setPreferredSize(new Dimension( 300, 100));
        tripsPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(statsPanel);
        centerPanel.add(tripsPanel);
        homePanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton bookRideButton = new JButton("Book a Ride");
        bookRideButton.addActionListener(e -> {
            //TODO: Uncomment when booking page is implemented
            //c1.show(cards, BOOK);
            JOptionPane.showMessageDialog(this,
             "Booking page is under construction",
            "Coming Soon",
            JOptionPane.INFORMATION_MESSAGE);
        });

        JButton historyButton = new JButton("View History");
        historyButton.addActionListener(e -> {
            JPanel histPage = buildHistoryPage();
            cards.add(histPage, HIST);
            c1.show(cards, HIST);
        });

    JButton profileButton = new JButton("Profile");
    profileButton.addActionListener(e -> {
            // load current user into profile form each time user clicks Profile
            loadUserIntoViewProf();
            //loadCurrentUserIntoProfile();
            c1.show(cards, VIEW_PROF);
    });

    JButton logoutButton = new JButton("Log Out");
    logoutButton.addActionListener(e -> {
        Component loginPage = cards.getComponent(0);
        if (loginPage instanceof JPanel) {
            resetLoginFields((JPanel) loginPage);
        }
        c1.show(cards, LOGIN);
    });

    buttonsPanel.add(bookRideButton);
    buttonsPanel.add(historyButton);
    buttonsPanel.add(profileButton);
    buttonsPanel.add(logoutButton);

    homePanel.add(buttonsPanel, BorderLayout.SOUTH);

    return homePanel;
    
    }

    // Call this when opening the Profile page so field are populated from DB
    private void loadCurrentUserIntoProfile() {
        try {
            UserDAOSQLite userDAO = new UserDAOSQLite();
            Optional<User> opt = userDAO.findById(currentUserID);
            if (!opt.isPresent()) {
                // clear fields or leave placeholders as-is
                System.err.println("No user found for id=" + currentUserID);
                return;
            }
            User u = opt.get();

            // setText but avoid setting placeholders unintentionally
            firstNameT.setForeground(Color.BLACK);
            lastNameT.setForeground(Color.BLACK);
            emailT.setForeground(Color.BLACK);
            phoneT.setForeground(Color.BLACK);
            licenseT.setForeground(Color.BLACK);
            dobT.setForeground(Color.BLACK);
            streetT.setForeground(Color.BLACK);
            cityT.setForeground(Color.BLACK);
            stateT.setForeground(Color.BLACK);
            countryT.setForeground(Color.BLACK);
            zipT.setForeground(Color.BLACK);

            firstNameT.setText(u.getFirstName() == null ? "" : u.getFirstName());
            lastNameT.setText(u.getLastName() == null ? "" : u.getLastName());
            emailT.setText(u.getEmail() == null ? "" : u.getEmail());
            phoneT.setText(u.getPhone() == null ? "" : u.getPhone());
            licenseT.setText(u.getLicense() == null ? "" : u.getLicense());
            dobT.setText(u.getDob() == null ? "" : u.getDob());
            streetT.setText(u.getStreetAddress() == null ? "" : u.getStreetAddress());
            cityT.setText(u.getCity() == null ? "" : u.getCity());
            stateT.setText(u.getState() == null ? "" : u.getState());
            countryT.setText(u.getCountry() == null ? "" : u.getCountry());
            zipT.setText(u.getZipCode() == null ? "" : u.getZipCode());

        } catch (Exception e) {
            System.err.println("Error loading profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Call this when opening the Profile page so field are populated from DB
    private void loadUserIntoViewProf() {
        try {
            UserDAOSQLite user = new UserDAOSQLite();
            Optional<User> opt = user.findById(currentUserID);

            if (!opt.isPresent()) return;

            User curentUser = opt.get();

            profileViewLabels[0].setText(curentUser.getFirstName());
            profileViewLabels[1].setText(curentUser.getLastName());
            profileViewLabels[2].setText(curentUser.getEmail());
            profileViewLabels[3].setText(curentUser.getPhone());
            profileViewLabels[4].setText(curentUser.getLicense());
            profileViewLabels[5].setText(curentUser.getDob());
            profileViewLabels[6].setText(curentUser.getStreetAddress());
            profileViewLabels[7].setText(curentUser.getCity());
            profileViewLabels[8].setText(curentUser.getState());
            profileViewLabels[9].setText(curentUser.getCountry());
            profileViewLabels[10].setText(curentUser.getZipCode());
        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    //helper method to reset login fields on logout
    private void resetLoginFields(JPanel panel){
    for (Component comp : panel.getComponents()){
        if (comp instanceof JTextField && !(comp instanceof JPasswordField)){
            JTextField field = (JTextField) comp;
            field.setText("user");
            field.setForeground(Color.GRAY);
        } else if (comp instanceof JPasswordField){
            JPasswordField field = (JPasswordField) comp;
            field.setText("password");
            field.setForeground(Color.GRAY);
        } else if (comp instanceof JButton) {
            JButton button = (JButton) comp;
            if (button.getText().equals("Log in!")) {
                button.setEnabled(false); 
            }
        } else if (comp instanceof JPanel){
            resetLoginFields((JPanel) comp);
        }
    }
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
    private JPanel buildProfilePage() {
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
        gbc1.insets = new Insets(4,7,4,6);
        gbc1.anchor = GridBagConstraints.WEST;
        gbc1.fill = GridBagConstraints.HORIZONTAL;

        // better title
        JLabel title = new JLabel("<html><h1>Profile</h1></html>");
        profilePage.add(title, BorderLayout.NORTH);

        // Using the instance variable from the field area
        firstNameT =  new JTextField();
        firstNameT.setColumns(20);
        textHelper(firstNameT, "Ride");
        prow = addRowHelper(gridPii, gbc1, prow,"First Name:", firstNameT);

        lastNameT =  new JTextField();
        lastNameT.setColumns(20);
        textHelper(lastNameT, "Share");
        prow = addRowHelper(gridPii, gbc1, prow,"Last Name:", lastNameT);

        emailT = new JTextField();
        emailT.setColumns(11);
        textHelper(emailT, "rideshare@bridgew.com");
        prow = addRowHelper(gridPii,gbc1,prow, "Email:",emailT);

        phoneT = new JTextField();
        phoneT.setColumns(11);
        textHelper(phoneT, "###-###-####");
        prow = addRowHelper(gridPii, gbc1, prow, "Phone Number:", phoneT);

        licenseT = new JTextField();
        licenseT.setColumns(11);
        textHelper(licenseT, "ABC-123");
        prow = addRowHelper(gridPii, gbc1, prow, "License Plate:", licenseT);

        dobT = new JTextField();
        dobT.setColumns(11);
        textHelper(dobT, "YYYY-MM-DD");
        prow = addRowHelper(gridPii, gbc1, prow, "Date of Birth:", dobT);


        JPanel gridAddr = new JPanel(new GridBagLayout());
        gridAddr.setBorder(BorderFactory.createTitledBorder("Address"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(4,6,4,6);
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        // keep using the instance fiels
        streetT = new JTextField();
        streetT.setColumns(15);
        textHelper(streetT, "123 Plymouth St");
        adrow = addRowHelper(gridAddr, gbc2, adrow, "Street:", streetT);

        cityT = new JTextField();
        cityT.setColumns(11);
        textHelper(cityT, "Bridgewater");
        adrow = addRowHelper(gridAddr, gbc2, adrow, "City:", cityT);

        stateT = new JTextField();
        stateT.setColumns(9);
        textHelper(stateT, "MA");
        adrow = addRowHelper(gridAddr, gbc2, adrow, "State:", stateT);

        countryT = new JTextField();
        countryT.setColumns(8);
        textHelper(countryT, "USA");
        adrow = addRowHelper(gridAddr, gbc2, adrow, "Country:", countryT);

        zipT = new JTextField();
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
            UserDAOSQLite user = new UserDAOSQLite();

            try {
                //load existing user
                Optional<User> opt = user.findById(currentUserID);
                if (!opt.isPresent()){
                    JOptionPane.showMessageDialog(this, "User ID " +currentUserID+ " Not Found!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User currentUser = opt.get();

                currentUser.setFirstName(firstNameT.getText().trim());
                currentUser.setLastName(lastNameT.getText().trim());
                currentUser.setEmail(emailT.getText().trim());
                currentUser.setPhone(phoneT.getText().trim());
                currentUser.setLicense(licenseT.getText().trim());
                currentUser.setDob(dobT.getText().trim());
                currentUser.setStreetAddress(streetT.getText().trim());
                currentUser.setCity(cityT.getText().trim());
                currentUser.setState(stateT.getText().trim());
                currentUser.setCountry(countryT.getText().trim());
                currentUser.setZipCode(zipT.getText().trim());

                int rows = user.update(currentUser);
                if (rows > 0){
                    JOptionPane.showMessageDialog(this,"Profile updated!",
                            "Success",JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,"Changes were not saved.",
                            "Attention",JOptionPane.INFORMATION_MESSAGE);
                }

                loadUserIntoViewProf();
                // Go to the home card
                c1.show(cards, VIEW_PROF);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saving "+
                        e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // set bottom to the lower-right of the frame
        JPanel buttonPosition = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        profilePage.add(buttonPosition, BorderLayout.SOUTH);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e ->{
            loadCurrentUserIntoProfile();
            loadUserIntoViewProf();
            c1.show(cards, VIEW_PROF);
        });


        buttonPosition.add(saveButton);
        buttonPosition.add(backButton);
        return profilePage;
    }

    private JPanel buildProfileOverviewPage() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        JLabel title = new JLabel("<html><h1>My Profile<h1></html>");
        p.add(title, BorderLayout.NORTH);

        // pairs of panels
        JPanel info = new JPanel(new GridLayout(0,2,10,10));
        JLabel firstNameL = new JLabel();
        JLabel lastNameL = new JLabel();
        JLabel emailL = new JLabel();
        JLabel phoneL = new JLabel();
        JLabel licenseL = new JLabel();
        JLabel dobL = new JLabel();
        JLabel streetL = new JLabel();
        JLabel cityL = new JLabel();
        JLabel stateL = new JLabel();
        JLabel countryL = new JLabel();
        JLabel zipL = new JLabel();

        // store it to update
        this.profileViewLabels = new JLabel[]{
                firstNameL, lastNameL, emailL, phoneL,
                licenseL, dobL, streetL, cityL,stateL,
                countryL, zipL
        };

        // add to pannel
        info.add(new JLabel("First Name:")); info.add(firstNameL);
        info.add(new JLabel("Last Name:")); info.add(lastNameL);
        info.add(new JLabel("Email:")); info.add(emailL);
        info.add(new JLabel("Phone:")); info.add(phoneL);
        info.add(new JLabel("License Plate:")); info.add(licenseL);
        info.add(new JLabel("DoB:")); info.add(dobL);
        info.add(new JLabel("Street:")); info.add(streetL);
        info.add(new JLabel("City:")); info.add(cityL);
        info.add(new JLabel("State:")); info.add(stateL);
        info.add(new JLabel("Country:")); info.add(countryL);
        info.add(new JLabel("ZIP Code:")); info.add(zipL);

        p.add(info, BorderLayout.CENTER);

        JButton editButton = new JButton("Edit Profile");
        editButton.addActionListener(e -> {

            //loadUserIntoProfileView();
            loadCurrentUserIntoProfile();
            c1.show(cards, PROF);
        });

        JPanel lower = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton backB = new JButton("Back");
        backB.addActionListener(e -> {

            c1.show(cards, HOME);
        });

        lower.add(backB);
        lower.add(editButton);

        p.add(lower, BorderLayout.SOUTH);
        return  p;
    }


    private JPanel buildHistoryPage() {
        JPanel p = new JPanel(new BorderLayout(10,10));

        JLabel title = new JLabel("<html><h1>History of My Trips</h1></html>");
        p.add(title, BorderLayout.NORTH);

        //list panels
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        try {
            HistoryDAO histDao = new HistoryDAOSQLite();
            List<History> trips = histDao.findUserHistory(currentUserID);

            if (trips.isEmpty()) {
                listPanel.add(new JLabel("No Previous Trips."));
            } else {
                for (History h : trips) {
                    String text = String.format(
                            "Trip #%d  |  %s -> %s  |  $%.2f  |  %s",
                            h.getHistoryID(),
                            h.getPickupLoc(),
                            h.getDropoffLoc(),
                            h.getFare(),
                            h.getStatus()
                    );
                    listPanel.add(new JLabel((text)));
                }
            }
        } catch (Exception e) {
            listPanel.add(new JLabel("Error loading your travels history."));
            e.printStackTrace();
        }

        JScrollPane scrollP = new JScrollPane(listPanel);
        p.add(scrollP, BorderLayout.CENTER);

        JButton backButton = new JButton("Bakc");
        backButton.addActionListener(e -> c1.show(cards, HOME));

        JPanel lower = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lower.add(backButton);
        p.add(lower, BorderLayout.SOUTH);

        return p;
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