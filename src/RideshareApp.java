import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * This is the main app that builds the gui and create instances
 * of all the needed models
 */
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

    /**
     * Constructor to create instance of the class used in main.
     */
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
        JPanel profPage = buildEditProfilePage();
        JPanel histPage = buildHistoryPage();
        JPanel viewProf = buildProfileOverviewPage();

        // add pages to CardLayout with our keys (see GeeksforGeeks tutorial "1", "2" ... cards)
        cards.add(loginPage, LOGIN);

        cards.add(bookPage, BOOK);
        cards.add(profPage, PROF);
        cards.add(histPage, HIST);
        cards.add(viewProf, VIEW_PROF);

        setContentPane(cards);

        c1.show(cards, LOGIN);

    }

    /**
     * Builds the loging page and verify user Authentication
     * @return the JPanel for the login page
     */
    private JPanel buildLoginPage() {
        // main Background
        JPanel loginPage = new JPanel(new GridBagLayout());
        loginPage.setBackground(Style.APP_BACKGROUD);

        // card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Style.CARD_BACKGROUD);
        // Add subtle border + padding inside the card
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_GRAY),
                Style.PADDING_LARGE
        ));

        // layout card
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; // Make text fields fill the width
        g.weightx = 1.0; // Give components weight to stretch
        g.anchor = GridBagConstraints.WEST; // Align text to the left
        g.gridx = 0; // Everything is in one column

        // title "Log in
        g.gridy = 0;
        g.insets = new Insets(0, 0, 25, 0); // 25px Bottom Margin (Space before Username)
        JLabel title = new JLabel("Log in");
        title.setFont(Style.FONT_HEADER);
        title.setForeground(Style.TEXT_DARK);
        card.add(title, g);

        // username label
        g.gridy = 1;
        g.insets = new Insets(0, 0, 5, 0); // 5px Bottom Margin (Close to input)
        JLabel userL = new JLabel("Username");
        userL.setFont(Style.FONT_LABEL);
        userL.setForeground(Style.TEXT_GRAY);
        card.add(userL, g);

        // username input
        g.gridy = 2;
        g.insets = new Insets(0, 0, 20, 0); // 20px Bottom Margin (Separation from Password)
        JTextField userTF = new JTextField(20);
        styleTextField(userTF);
        textHelper(userTF, "user");
        card.add(userTF, g);

        // password label
        g.gridy = 3;
        g.insets = new Insets(0, 0, 5, 0); // 5px Bottom Margin (Close to input)
        JLabel passL = new JLabel("Password");
        passL.setFont(Style.FONT_LABEL);
        passL.setForeground(Style.TEXT_GRAY);
        card.add(passL, g);

        // password input
        g.gridy = 4;
        g.insets = new Insets(0, 0, 10, 0); // 10px Bottom Margin (Close to Error/Button)
        JPasswordField passPF = new JPasswordField(20);
        styleTextField(passPF);
        textHelper(passPF, "password");
        passPF.setEchoChar('●');
        card.add(passPF, g);

        // error msm
        g.gridy = 5;
        g.insets = new Insets(0, 0, 10, 0);
        JLabel error = new JLabel(" ");
        error.setFont(Style.FONT_SMALL);
        error.setForeground(Style.ERROR_RED);
        card.add(error, g);

        // signin bttn
        g.gridy = 6;
        g.insets = new Insets(10, 0, 0, 0); // 10px Top Margin
        JButton loginButton = new JButton("Sign in");
        styleButton(loginButton);
        loginButton.setEnabled(false);
        loginButton.setBackground(Color.LIGHT_GRAY);
        card.add(loginButton, g);

        loginPage.add(card);

        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) { updateButton(); }
            @Override
            public void insertUpdate(DocumentEvent e) { updateButton(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateButton(); }

            private void updateButton() {
                String user = userTF.getText();
                String pass = new String(passPF.getPassword());
                boolean hasUser = !user.isEmpty() && !user.equals("user");
                boolean hasPass = !pass.isEmpty() && !pass.equals("password");
                boolean valid = hasUser && hasPass;
                loginButton.setEnabled(valid);
                if (valid) {
                    loginButton.setBackground(Style.BLUE);
                } else {
                    loginButton.setBackground(Color.LIGHT_GRAY);
                }
            }
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
                for (User u : allUsers){
                    if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                        matched = u;
                        break;
                    }
                }
                if (matched != null) {
                    error.setText(" ");
                    currentUserID = matched.getId();
                    JPanel newHome = buildHomePage();
                    cards.add(newHome, HOME);
                    loadUserIntoViewProf();
                    c1.show(cards, HOME);
                } else {
                    error.setText("Invalid username or password");
                }
            } catch (Exception ex) {
                error.setText("Database connection failed");
                ex.printStackTrace();
            }
        });

        return loginPage;
    }

    /**
     * Get the current login username
     * @return Returns the current user's username
     * @exception Exception returns a fix value: Commander instead of username.
     */
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

    /**
     * Builds the Home page after user logs in
     * @return JPanel for the Home page
     */
    private JPanel buildHomePage() {
        // Main container background
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Style.APP_BACKGROUD);

        // 2. Content Wrapper
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Style.APP_BACKGROUD);
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- Logic: Fetch Data (Same as before) ---
        String userName = getCurrentUserName();
        int totalTrips = 0;
        double totalSpent = 0.0;
        String lastRideLoc = "No rides yet";
        List<History> recentTrips = null;

        try {
            HistoryDAO historyDAO = new HistoryDAOSQLite();
            List<History> userHistory = historyDAO.findUserHistory(currentUserID);
            recentTrips = userHistory;
            totalTrips = userHistory.size();
            for (History trip : userHistory) {
                if (trip.getFare() != null) totalSpent += trip.getFare();
            }
            if (!userHistory.isEmpty()) {
                lastRideLoc = userHistory.get(userHistory.size() - 1).getDropoffLoc();
            }
        } catch (Exception ex) {
            System.err.println("Error loading stats: " + ex.getMessage());
        }

        // --- Layout Constraints Setup ---
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0, 0, 0, 0);
        g.gridx = 0;

        // FIX: Crucial settings to stop stretching
        g.anchor = GridBagConstraints.NORTH; // Anchor to top
        g.fill = GridBagConstraints.HORIZONTAL; // Only stretch width, NOT height
        g.weightx = 1.0;
        g.weighty = 0.0; // Do not give vertical space to these rows

        // --- Row 0: Header ---
        g.gridy = 0;
        JLabel welcome = new JLabel("Welcome back, " + userName);
        welcome.setFont(Style.FONT_HEADER);
        welcome.setForeground(Style.TEXT_DARK);
        content.add(welcome, g);

        // --- Row 1: Stats Grid ---
        g.gridy = 1;
        g.insets = new Insets(20, 0, 0, 0);

        // GridLayout with 10px horizontal gap
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(Style.APP_BACKGROUD);

        statsPanel.add(createStatCard("Total Trips", String.valueOf(totalTrips)));
        statsPanel.add(createStatCard("Total Spent", String.format("$%.2f", totalSpent)));
        statsPanel.add(createStatCard("Last Drop-off", lastRideLoc));

        content.add(statsPanel, g);

        // --- Row 2: Section Header ---
        g.gridy = 2;
        content.add(createSectionHeader("Quick Actions"), g);

        // --- Row 3: Action Buttons ---
        g.gridy = 3;
        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        actionPanel.setBackground(Style.APP_BACKGROUD);

        JButton bookBtn = new JButton("Book a Ride");
        styleButton(bookBtn);
        bookBtn.setBackground(Style.BLUE);

        JButton profileBtn = new JButton("My Profile");
        styleButton(profileBtn);
        profileBtn.setBackground(Style.TEXT_DARK);

        JButton logoutBtn = new JButton("Log Out");
        styleButton(logoutBtn);
        logoutBtn.setBackground(Style.ERROR_RED);

        // (Action Listeners kept same)
        bookBtn.addActionListener(e -> {
            JPanel booking = buildBookingPage();
            cards.add(booking, BOOK);
            c1.show(cards, BOOK);
        });
        profileBtn.addActionListener(e -> {
            loadUserIntoViewProf();
            c1.show(cards, VIEW_PROF);
        });
        logoutBtn.addActionListener(e -> {
            Component loginPage = cards.getComponent(0);
            if (loginPage instanceof JPanel) resetLoginFields((JPanel) loginPage);
            c1.show(cards, LOGIN);
        });

        actionPanel.add(bookBtn);
        actionPanel.add(profileBtn);
        actionPanel.add(logoutBtn);
        content.add(actionPanel, g);

        // --- Row 4: History Header ---
        g.gridy = 4;
        content.add(createSectionHeader("Recent Activity"), g);

        // --- Row 5: History List ---
        // FIX: This is the ONLY element allowed to take up extra vertical space
        g.gridy = 5;
        g.weighty = 1.0;
        g.fill = GridBagConstraints.BOTH; // Fill remaining space

        JPanel historyList = new JPanel();
        historyList.setLayout(new BoxLayout(historyList, BoxLayout.Y_AXIS));
        historyList.setBackground(Style.CARD_BACKGROUD);
        historyList.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));

        if (recentTrips == null || recentTrips.isEmpty()) {
            JLabel empty = new JLabel("No recent trips found.");
            empty.setFont(Style.FONT_REGULAR);
            empty.setForeground(Style.TEXT_GRAY);
            empty.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            historyList.add(empty);
        } else {
            int count = 0;
            for (int i = recentTrips.size() - 1; i >= 0 && count < 3; i--) {
                History h = recentTrips.get(i);

                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Style.CARD_BACKGROUD);
                row.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                // Fix max height for rows
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

                JLabel route = new JLabel(h.getPickupLoc() + " \u2192 " + h.getDropoffLoc());
                route.setFont(Style.FONT_REGULAR);
                route.setForeground(Style.TEXT_DARK);

                JLabel fare = new JLabel(String.format("$%.2f", h.getFare()));
                fare.setFont(Style.FONT_LABEL);
                fare.setForeground(Style.TEXT_GRAY);

                row.add(route, BorderLayout.CENTER);
                row.add(fare, BorderLayout.EAST);

                historyList.add(row);
                if (count < 2 && i > 0) {
                    JSeparator sep = new JSeparator();
                    sep.setForeground(Style.BORDER_GRAY);
                    sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                    historyList.add(sep);
                }
                count++;
            }
        }

        // Spacer to push content to top if list is empty/short
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Style.APP_BACKGROUD);
        wrapper.add(historyList, BorderLayout.NORTH);

        content.add(wrapper, g);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // Faster scrolling
        homePanel.add(scroll, BorderLayout.CENTER);

        return homePanel;
    }

    /**
     * Call this when opening the Profile page so field are populated from DB
     */
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

    /**
     *  Call this when opening the Profile page so field are populated from DB
     */
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

    /**
     * Helper method to reset login fields on logout
     * @param panel the JPanel that hold the JTextFields to be reset
     */
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

    /**
     *  Builds the book page to displace book trip and cost
     * @return JPanel of the booking page
     */
    private JPanel buildBookingPage() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));


        JLabel title = new JLabel("<html><h1>Book a Trip</h1></html>");
        p.add(title, BorderLayout.NORTH);

        // the form panel
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        AutoCompleteTextField pickUpT = new AutoCompleteTextField(15);
        AutoCompleteTextField dropoffT = new AutoCompleteTextField(15);

        textHelper(pickUpT,"Pickup Address");
        textHelper(dropoffT, "Drop-off Address");

        JButton searchPickButton = new JButton("Search");
        JButton searchDropButton = new JButton("Search");

        searchPickButton.addActionListener(e -> pickUpT.searchNow());
        searchDropButton.addActionListener(e -> dropoffT.searchNow());

        // top row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        form.add(new JLabel("Pickup Location:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        form.add(pickUpT, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        form.add(searchPickButton, gbc);

        // lower row
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        form.add(new JLabel("Drop-off Location:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        form.add(dropoffT, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        form.add(searchDropButton, gbc);


        int row = 0;
        row = addRowHelper(form, gbc, row, "Pickup Location:", pickUpT);
        row = addRowHelper(form, gbc, row, "Drop-off Location:", dropoffT);

        p.add(form, BorderLayout.CENTER);

        // Place image of MAP here
        JPanel mapHolder = new JPanel(new BorderLayout());
        mapHolder.setPreferredSize(new Dimension(400,400));
        p.add(mapHolder,BorderLayout.EAST);


        // bottons for navigation
        JPanel lower = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBttn = new JButton("Back");
        JButton viewBttn = new JButton("Preview Map");
        JButton reqBttn = new JButton("Request Trip");

        lower.add(backBttn);
        lower.add(viewBttn);
        lower.add(reqBttn);
        p.add(lower, BorderLayout.SOUTH);

        //back -> home
        backBttn.addActionListener(e -> c1.show(cards, HOME));

        viewBttn.addActionListener(e -> {
        ImageIcon img = new ImageIcon("images/basicTrip.png");
        JLabel imgL = new JLabel(img);
        mapHolder.add(imgL, BorderLayout.CENTER);
        mapHolder.repaint();
        });

        // request botton
        reqBttn.addActionListener(e -> {
            String pickUp = pickUpT.getText().trim();
            String dropOff = dropoffT.getText().trim();

            if (!pickUpT.hasValidSelection()) {
                JOptionPane.showMessageDialog(this,
                        "Please choose a valid Pickup address from the suggestions.",
                        "Invalid Pickup Location",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!dropoffT.hasValidSelection()) {
                JOptionPane.showMessageDialog(this,
                        "Please choose a valid Drop-off address from the suggestions.",
                        "Invalid Drop-off Location",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }


            try {
                // geocode - placeholder for now
                double distMiles = 3.0;
                int durMin = 10;

                double fare = FareCalculator.calculateStandardFare(distMiles,durMin);

                History newTrip = new History(
                        currentUserID,
                        1, // <- this is annoying, think of another way
                        java.time.LocalDateTime.now().toString(),
                        pickUp,
                        dropOff,
                        fare,
                        "requested",
                        "N/A"
                );

                HistoryDAO histDao = new HistoryDAOSQLite();
                histDao.insert(newTrip);

                JOptionPane.showMessageDialog(this,
                        "Ride has been Requested!\nTrip # "+
                        newTrip.getHistoryID()+"\nFare: $"+
                        String.format("%.2f", fare), "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // refresh home page by rebuilding it
                JPanel homePage = buildHomePage();
                cards.add(homePage, HOME);
                c1.show(cards, HOME);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving your ride:\n"+
                        ex.getMessage(), "Database Errror",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        return p;
    }

    /**
     *
     * @param current
     * @return
     */
    private String getNextStatus(String current){
        switch (current){
            case "Requested": return "Accepted";
            case "Accepted": return "In-Progress";
            case "In-Progress": return "Completed";
            default: return "Completed";
        }
    }

    /**
     * Helper to correctly devide the panel for multiple JLables and JTextFields
     * @param form The JPanel that will be modified
     * @param gbc The created grids to be places in the JPanel
     * @param row Integer, The number of rows
     * @param labelText String, The label for the JTextField
     * @param field The JTextField to be added
     * @return Integer, Number of rows + 1 to move to the next row
     */
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

    /**
     * A helper (Profile) to set text gray & clear on focus, then restore if empty
     * @param tf The target JTextField
     * @param placeHolder String, The display message while not focus
     */
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
     * Styles a button to look like a primary action button.
     * @param btn The button to be modified
     */
    private void styleButton(JButton btn){
        btn.setFont(Style.FONT_LABEL);
        btn.setBackground(Style.BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Adjusted padding
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // FIX: Force a maximum height for the button
        btn.setPreferredSize(new Dimension(150, 45));

        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
    }

    /**
     * Create a metric card with label and value
     * @param title Small gray title (e.g. "Total")
     * @param value Large dark value (e.g. "$45.50")
     * @return A styled Jpanel
     */
    private  JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(Style.CARD_BACKGROUD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_GRAY),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // FIX: Force a reasonable height (e.g., 100px) so it doesn't stretch
        card.setPreferredSize(new Dimension(200, 100));

        JLabel titleL = new JLabel(title.toUpperCase());
        titleL.setFont(Style.FONT_SMALL);
        titleL.setForeground(Style.TEXT_GRAY);

        JLabel valueL = new JLabel(value);
        valueL.setFont(Style.FONT_HEADER);
        valueL.setForeground(Style.TEXT_DARK);

        card.add(titleL, BorderLayout.NORTH);
        card.add(valueL, BorderLayout.CENTER);
        return card;
    }

    /**
     * Create a uniform section header label
     * @param txt The string to apply to the lable
     * @return
     */
    private JLabel createSectionHeader(String txt) {
        JLabel label = new JLabel(txt);
        label.setFont(Style.FONT_LABEL);
        label.setForeground(Style.TEXT_DARK);
        label.setBorder(BorderFactory.createEmptyBorder(20,0,10,0));
        return label;
    }

    /**
     * Give a clear boders and padding to a TextField
     * @param tf TextField to be styles
     */
    private void styleTextField(JTextField tf){
        tf.setFont(Style.FONT_REGULAR);
        tf.setForeground(Style.TEXT_DARK);

        // line on the outside & padding on inside
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8,10,8,10)
        ));
    }

    /**
     * Builds the editable Profile Page panel with nested layouts for perfect alignment.
     * @return JPanel profile page containing current user info
     */
    private JPanel buildEditProfilePage() {
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

    /**
     * Builds the read-only profile page.
     * @return JPanel profile page containing current user info
     */
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

    /**
     * Builds the JPanel History page for displaying privious trips
     * @return JPanel holding information about the user's past travels
     */
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
                    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));

                    JLabel label = new JLabel(
                            String.format(
                                "Trip #%d  |  %s -> %s  |  $%.2f  |  %s",
                                h.getHistoryID(),
                                h.getPickupLoc(),
                                h.getDropoffLoc(),
                                h.getFare(),
                                h.getStatus()
                            )
                    );

                    JButton updateBttn = new JButton("Next Status");
                    updateBttn.addActionListener(e -> {
                        try {
                            String newStatus = getNextStatus(h.getStatus());
                            h.setStatus(newStatus);

                            HistoryDAO histDao2 = new HistoryDAOSQLite();
                            histDao2.update(h);

                            JOptionPane.showMessageDialog(this,
                                    "Trip status updated to: "+newStatus);

                            JPanel refreshed = buildHistoryPage();
                            cards.add(refreshed, HIST);
                            c1.show(cards, HIST);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    row.add(label);
                    row.add(updateBttn);
                    listPanel.add(row);
                }
            }
        } catch (Exception e) {
            listPanel.add(new JLabel("Error loading your travels history."));
            e.printStackTrace();
        }

        JScrollPane scrollP = new JScrollPane(listPanel);
        p.add(scrollP, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> c1.show(cards, HOME));

        JPanel lower = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lower.add(backButton);
        p.add(lower, BorderLayout.SOUTH);

        return p;
    }

    /**
     * Initializes the SQLite schema and seed data by checking if the database already
     * contains user defined tables, if not it loads the executes the AQL statements from schema.sql
     *
     * @param c
     * @throws SQLException
     */
    public static void initializeSchema(Connection c) throws Exception {
        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery("SELECT name FROM sqlite_schema WHERE type='table' AND name NOT LIKE 'sqlite_%';");
            if (!rs.next()) { // No user-defined tables found
                System.out.println("Database is empty. Running schema initialization.");
                String schemaFile;
                try {
                    schemaFile = Files.readString(Paths.get("schema.sql"));
                } catch (Exception e) {
                    throw new Exception("Failed to read schema.sql", e);
                }
                for (String statement : schemaFile.split(";")) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()){
                        s.execute(trimmed + ";");
                    }
                }
                System.out.println("Schema and seed successfully initialized.");
            } else {
                System.out.println("Database already contains tables. No schema initialization needed.");
            }
        }
    }

    /**
     * Main calls for the app
     * @param args -
     */
    public static void main(String[] args) {
        String databaseURL = "jdbc:sqlite:rideshare.db";

        // establish connection through jdbc (.jar file in lib)
        // if no such database is found it will create locally for you, otherwise connect to db
        try (Connection c = DriverManager.getConnection(databaseURL)) {
             try (Statement s = c.createStatement()) {
                 s.execute("PRAGMA foreign_keys = ON");
             }
            initializeSchema(c);
            System.out.println("Database loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading schema: " + e.getMessage());
            e.printStackTrace();
        }

//      app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(() ->{
            RideshareApp app = new RideshareApp();
            app.setVisible(true);
        });

    }
}