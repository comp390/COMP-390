import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.Year;
import java.util.*;
import java.util.List;

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
    private static final String DRIVER_REQ = "driverRequests";
    private static final String SIGNUP = "signup";
    private static final String CARS = "cars";
    private static final String EDIT_CAR = "editCars";
    private static final String ADD_CAR = "addCars";


    private int currentUserID; // for now keep until correct login is implemented
    private int currentCarID;
    private JLabel[] profileViewLabels;
    private JPanel carListPanel;

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
    private JComboBox<String> carMakeT;
    private JComboBox<String> carModelT;
    private JComboBox<String> carYearT;
    private JTextField carPriceT;
    private JComboBox<String> carConditionT;
    private JTextField carExtColorT;
    private JTextField carIntColorT;
    private JTextField carIntMatT;
    private JTextField carLicensePlateT;

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
        setSize(1400, 800);
        setLocationRelativeTo(null);

        // build each page, within their own methods (see GeeksforGeeks tutorial, jp1, jp2 ...)
        JPanel loginPage = buildLoginPage();

        JPanel bookPage = buildBookingPage();
        JPanel profPage = buildEditProfilePage();
        JPanel histPage = buildHistoryPage();
        JPanel viewProf = buildProfileOverviewPage();
        JPanel driverPage = buildDriverRequestsPage();
        JPanel signUpPage = buildSignUpPage();
        JPanel carsPage = buildCarOverviewPage();
        JPanel editCarPage = buildEditCarPage();
        JPanel addCarPage = buildAddCarPage();

        // add pages to CardLayout with our keys (see GeeksforGeeks tutorial "1", "2" ... cards)
        cards.add(loginPage, LOGIN);

        cards.add(bookPage, BOOK);
        cards.add(profPage, PROF);
        cards.add(histPage, HIST);
        cards.add(viewProf, VIEW_PROF);
        cards.add(driverPage, DRIVER_REQ);
        cards.add(signUpPage, SIGNUP);
        cards.add(carsPage, CARS);
        cards.add(editCarPage, EDIT_CAR);
        cards.add(addCarPage, ADD_CAR);

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

        g.gridy = 7;
        g.insets = new Insets(0, 0, 0, 0);

        JPanel signUpLink = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signUpLink.setBackground(Style.CARD_BACKGROUD);

        JLabel noAcc = new JLabel("Don't have an account?");
        noAcc.setFont(Style.FONT_SMALL);
        noAcc.setForeground(Style.TEXT_GRAY);

        JButton signUpBtn = new JButton("Sign up");
        signUpBtn.setFont(Style.FONT_LABEL);
        signUpBtn.setForeground(Style.BLUE);
        signUpBtn.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        signUpBtn.setContentAreaFilled(false);
        signUpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        signUpBtn.addActionListener(e -> c1.show(cards, SIGNUP)); // Go to Sign Up

        signUpLink.add(noAcc);
        signUpLink.add(signUpBtn);
        card.add(signUpLink, g);

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
     * Builds the Sign-Up page for new users.
     * @return JPanel for the sign-up page
     */
    private JPanel buildSignUpPage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Style.APP_BACKGROUD);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Style.APP_BACKGROUD);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Style.CARD_BACKGROUD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_GRAY),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        g.weightx = 1.0;
        g.gridx = 0;
        g.gridy = 0;

        // --- Header ---
        g.insets = new Insets(0, 0, 25, 0);
        JLabel title = new JLabel("Create your account");
        title.setFont(Style.FONT_HEADER);
        title.setForeground(Style.TEXT_DARK);
        card.add(title, g);

        // --- Fields ---
        // Helper to keep code clean
        JTextField userT = new JTextField(); styleTextField(userT);
        JPasswordField passT = new JPasswordField(); styleTextField(passT);
        JTextField firstT = new JTextField(); styleTextField(firstT);
        JTextField lastT = new JTextField(); styleTextField(lastT);
        JTextField emailT = new JTextField(); styleTextField(emailT);
        JTextField phoneT = new JTextField(); styleTextField(phoneT);

        // Role Selection
        String[] roles = {"Select Role", "Rider", "Driver"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setBackground(Color.WHITE);
        roleCombo.setFont(Style.FONT_REGULAR);

        addEditField(card, g, "Username", userT);
        addEditField(card, g, "Password", passT);
        addEditField(card, g, "First Name", firstT);
        addEditField(card, g, "Last Name", lastT);
        addEditField(card, g, "Email", emailT);
        addEditField(card, g, "Phone Number", phoneT);

        // Role UI
        g.gridy++;
        g.insets = new Insets(15, 0, 5, 0);
        JLabel roleL = new JLabel("I want to be a...");
        roleL.setFont(Style.FONT_LABEL);
        roleL.setForeground(Style.TEXT_GRAY);
        card.add(roleL, g);

        g.gridy++;
        g.insets = new Insets(0, 0, 0, 0);
        card.add(roleCombo, g);

        // --- Actions ---
        g.gridy++;
        g.insets = new Insets(30, 0, 0, 0);

        JButton signUpBtn = new JButton("Create Account");
        styleButton(signUpBtn);
        signUpBtn.setBackground(Style.BLUE);

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn);
        cancelBtn.setBackground(Style.APP_BACKGROUD);
        cancelBtn.setForeground(Style.TEXT_DARK);
        cancelBtn.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));

        JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 0));
        buttons.setBackground(Style.CARD_BACKGROUD);
        buttons.add(cancelBtn);
        buttons.add(signUpBtn);

        card.add(buttons, g);

        // Wrap in scroll pane
        content.add(card);
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        p.add(scroll, BorderLayout.CENTER);

        // --- Logic ---
        cancelBtn.addActionListener(e -> c1.show(cards, LOGIN));

        signUpBtn.addActionListener(e -> {
            String u = userT.getText().trim();
            String pwd = new String(passT.getPassword()).trim();
            String f = firstT.getText().trim();
            String l = lastT.getText().trim();
            String em = emailT.getText().trim();
            String ph = phoneT.getText().trim();
            String role = ((String) roleCombo.getSelectedItem()).toLowerCase();

            // Basic Validation
            if (u.isEmpty() || pwd.isEmpty() || l.isEmpty() || em.isEmpty() || ph.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.\n(Username, Password, Last Name, Email, Phone)");
                return;
            }
            if (roleCombo.getSelectedItem() == null || roleCombo.getSelectedIndex() == 0){
                JOptionPane.showMessageDialog(this, "Please select a role.");
                return;
            }

            try {
                UserDAOSQLite dao = new UserDAOSQLite();

                // Check if user exists (Optional but good)
                // For now, we rely on the DB throwing a Unique Constraint error if email/user is taken

                // Create User Object
                // We fill optional fields with empty strings to satisfy the constructor/DB
                User newUser = new User(
                        u, pwd, f, l, em, ph,
                        "", // License (Optional)
                        "", // DOB (Optional)
                        "", // Street
                        "", // City
                        "", // State
                        "", // Country
                        "", // Zip
                        role,
                        "active" // Status
                );

                dao.insert(newUser);

                JOptionPane.showMessageDialog(this, "Account Created! Please Log In.");
                c1.show(cards, LOGIN);

            } catch (Exception ex) {
                // Handle duplicate username/email errors
                if (ex.getMessage().contains("UNIQUE constraint failed")) {
                    JOptionPane.showMessageDialog(this, "Username or Email already taken.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error creating account: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        return p;
    }

    /**
     * Builds the Home page after user logs in
     * @return JPanel for the Home page
     */
    private JPanel buildHomePage() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Style.APP_BACKGROUD);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Style.APP_BACKGROUD);
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // get data based on role
        String userName = getCurrentUserName();
        String userRole = "rider"; // default

        int totalTrips = 0;
        double totalMoney = 0.0;
        String lastActivity = "No rides yet";
        List<History> recentTrips = null;

        try {
            UserDAOSQLite userDAO = new UserDAOSQLite();
            Optional<User> uOpt = userDAO.findById(currentUserID);
            if(uOpt.isPresent()) userRole = uOpt.get().getRole();

            HistoryDAO historyDAO = new HistoryDAOSQLite();
            List<History> userHistory;

            if ("driver".equalsIgnoreCase(userRole)) {
                userHistory = historyDAO.findDriverHistory(currentUserID);
            } else {
                userHistory = historyDAO.findUserHistory(currentUserID);
            }

            recentTrips = userHistory;
            totalTrips = userHistory.size();
            totalMoney = 0.0;

            for (History trip : userHistory) {
                if (trip.getFare() != null && "completed".equalsIgnoreCase(trip.getStatus())) {
                    totalMoney += trip.getFare();
                }
            }

            if (!userHistory.isEmpty()) {

                History last = userHistory.get(0);
                if ("driver".equalsIgnoreCase(userRole)) {
                    lastActivity = String.format("$%.2f", last.getFare());
                } else {
                    lastActivity = trimLocation(last.getDropoffLoc());
                }
            }
        } catch (Exception ex) {
            System.err.println("Error loading stats: " + ex.getMessage());
        }

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0, 0, 0, 0);
        g.gridx = 0;
        g.anchor = GridBagConstraints.NORTH;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.weighty = 0.0;

        // header
        g.gridy = 0;
        JLabel welcome = new JLabel("Welcome back, " + userName);
        welcome.setFont(Style.FONT_HEADER);
        welcome.setForeground(Style.TEXT_DARK);
        content.add(welcome, g);

        // stats grid
        g.gridy = 1;
        g.insets = new Insets(20, 0, 0, 0);
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(Style.APP_BACKGROUD);

        if ("driver".equalsIgnoreCase(userRole)) {
            statsPanel.add(createStatCard("Trips Delivered", String.valueOf(totalTrips)));
            statsPanel.add(createStatCard("Total Earned", String.format("$%.2f", totalMoney)));
            statsPanel.add(createStatCard("Last Earnings", lastActivity));
        } else {
            statsPanel.add(createStatCard("Total Trips", String.valueOf(totalTrips)));
            statsPanel.add(createStatCard("Total Spent", String.format("$%.2f", totalMoney)));
            statsPanel.add(createStatCard("Last Drop-off", lastActivity));
        }
        content.add(statsPanel, g);

        // section eader
        g.gridy = 2;
        content.add(createSectionHeader("Quick Actions"), g);

        // action bttn
        g.gridy = 3;
        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        actionPanel.setBackground(Style.APP_BACKGROUD);

        JButton mainActionBtn;
        if ("driver".equalsIgnoreCase(userRole)) {
            mainActionBtn = new JButton("Find Requests");
            mainActionBtn.addActionListener(e -> {
                JPanel reqPage = buildDriverRequestsPage();
                cards.add(reqPage, DRIVER_REQ);
                c1.show(cards, DRIVER_REQ);
            });
        } else {
            mainActionBtn = new JButton("Book a Ride");
            mainActionBtn.addActionListener(e -> {
                JPanel booking = buildBookingPage();
                cards.add(booking, BOOK);
                c1.show(cards, BOOK);
            });
        }
        styleButton(mainActionBtn);
        mainActionBtn.setBackground(Style.GREEN);

        JButton profileBtn = new JButton("My Profile");
        styleButton(profileBtn);
        profileBtn.setBackground(Style.TEXT_DARK);

        // NEW: only for drivers
        JButton carsBtn = null;
        if ("driver".equalsIgnoreCase(userRole)) {
            carsBtn = new JButton("My Cars");
            styleButton(carsBtn);
            carsBtn.setBackground(Style.BLUE);
            carsBtn.addActionListener(e -> {
                JPanel carsPage = buildCarOverviewPage();
                cards.add(carsPage, CARS);
                c1.show(cards, CARS);
            });
        }

        JButton logoutBtn = new JButton("Log Out");
        styleButton(logoutBtn);
        logoutBtn.setBackground(Style.DARK_GRAY);

        profileBtn.addActionListener(e -> {
            loadUserIntoViewProf();
            c1.show(cards, VIEW_PROF);
        });
        logoutBtn.addActionListener(e -> {
            Component loginPage = cards.getComponent(0);
            if (loginPage instanceof JPanel) resetLoginFields((JPanel) loginPage);
            c1.show(cards, LOGIN);
        });

        if("driver".equalsIgnoreCase(userRole)) {
            actionPanel = new JPanel(new GridLayout(1, 4, 15, 0));
            actionPanel.add(mainActionBtn);
            actionPanel.add(profileBtn);
            actionPanel.add(carsBtn);
            actionPanel.add(logoutBtn);
        } else {
            actionPanel.add(mainActionBtn);
            actionPanel.add(profileBtn);
            actionPanel.add(logoutBtn);
        }
        content.add(actionPanel, g);

        // history header with view all bttn
        g.gridy = 4;

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Style.APP_BACKGROUD);

        headerPanel.add(createSectionHeader("Recent Activity"), BorderLayout.CENTER);

        // create the small "View All" button
        JButton viewAllBtn = new JButton("View All History");
        viewAllBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        viewAllBtn.setForeground(Style.BLUE);
        viewAllBtn.setContentAreaFilled(false);
        viewAllBtn.setBorderPainted(false);
        viewAllBtn.setFocusPainted(false);
        viewAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllBtn.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // rebuild history page
        viewAllBtn.addActionListener(e -> {
            JPanel histPage = buildHistoryPage();
            cards.add(histPage, HIST);
            c1.show(cards, HIST);
        });

        headerPanel.add(viewAllBtn, BorderLayout.EAST);
        content.add(headerPanel, g);

        // history list
        g.gridy = 5;
        g.weighty = 1.0;
        g.fill = GridBagConstraints.BOTH;

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
            int max = Math.min(3, recentTrips.size());
            for (int i = 0; i < max; i++) {
                History h = recentTrips.get(i);

                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Style.CARD_BACKGROUD);
                row.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

                String routeText = trimLocation(h.getPickupLoc()) + " \u2192 " + trimLocation(h.getDropoffLoc());
                JLabel route = new JLabel(routeText);
                route.setFont(Style.FONT_REGULAR);
                route.setForeground(Style.TEXT_DARK);

                JLabel fare = new JLabel(String.format("$%.2f", h.getFare()));
                fare.setFont(Style.FONT_LABEL);
                fare.setForeground(Style.TEXT_GRAY);

                row.add(route, BorderLayout.CENTER);
                row.add(fare, BorderLayout.EAST);

                historyList.add(row);
                if (1 < max -1) {
                    JSeparator sep = new JSeparator();
                    sep.setForeground(Style.BORDER_GRAY);
                    sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                    historyList.add(sep);
                }
            }
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Style.APP_BACKGROUD);
        wrapper.add(historyList, BorderLayout.NORTH);

        content.add(wrapper, g);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        homePanel.add(scroll, BorderLayout.CENTER);

        return homePanel;
    }

    /**
     *  Builds the book page to displace book trip and cost
     * @return JPanel of the booking page
     */
    private JPanel buildBookingPage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Style.APP_BACKGROUD);

        // content Wrapper (Center Card)
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Style.APP_BACKGROUD);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //White Card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Style.CARD_BACKGROUD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_GRAY),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        g.weightx = 1.0;
        g.gridx = 0;

        // header
        g.gridy = 0;
        g.insets = new Insets(0, 0, 25, 0);
        JLabel title = new JLabel("Book a Trip");
        title.setFont(Style.FONT_HEADER);
        title.setForeground(Style.TEXT_DARK);
        card.add(title, g);

        // form fields
        // Pickup
        g.gridy = 1;
        g.insets = new Insets(0, 0, 5, 0);
        JLabel pickupL = new JLabel("Pickup Location");
        pickupL.setFont(Style.FONT_LABEL);
        pickupL.setForeground(Style.TEXT_GRAY);
        card.add(pickupL, g);

        g.gridy = 2;
        g.insets = new Insets(0, 0, 15, 0);
        AutoCompleteTextField pickUpT = new AutoCompleteTextField(20);
        styleTextField(pickUpT); // Reuse our style helper!
        textHelper(pickUpT, "Enter pickup address...");
        card.add(pickUpT, g);

        // drop-off
        g.gridy = 3;
        g.insets = new Insets(0, 0, 5, 0);
        JLabel dropL = new JLabel("Drop-off Destination");
        dropL.setFont(Style.FONT_LABEL);
        dropL.setForeground(Style.TEXT_GRAY);
        card.add(dropL, g);

        g.gridy = 4;
        g.insets = new Insets(0, 0, 25, 0);
        AutoCompleteTextField dropoffT = new AutoCompleteTextField(20);
        styleTextField(dropoffT);
        textHelper(dropoffT, "Enter destination...");
        card.add(dropoffT, g);

        // map placeholder (Visual Flair)
        g.gridy = 5;
        g.insets = new Insets(0, 0, 25, 0);

        JPanel mapPlaceholder = new JPanel(new BorderLayout());
        mapPlaceholder.setBackground(new Color(240, 242, 245)); // Light Gray Map Color
        mapPlaceholder.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));
        mapPlaceholder.setPreferredSize(new Dimension(400, 200)); // Fixed height for map area

        JLabel mapLabel = new JLabel("Map Preview", SwingConstants.CENTER);
        mapLabel.setForeground(Style.TEXT_GRAY);
        mapPlaceholder.add(mapLabel, BorderLayout.CENTER);

        // Try to load image, fallback to text if missing
        try {
            ImageIcon img = new ImageIcon("images/basicTrip.png");
            if (img.getImageLoadStatus() == MediaTracker.COMPLETE) {
                // Resize logic could go here, but simple icon set is fine for now
                mapLabel.setText("");
                mapLabel.setIcon(img);
            }
        } catch (Exception e) { /* Ignore missing image */ }

        card.add(mapPlaceholder, g);

        // action buttons
        g.gridy = 6;
        g.insets = new Insets(10, 0, 0, 0);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 15, 0));
        buttons.setBackground(Style.CARD_BACKGROUD);

        JButton backBtn = new JButton("Cancel");
        styleButton(backBtn);
        backBtn.setBackground(Style.APP_BACKGROUD); // Light background for secondary action
        backBtn.setForeground(Style.TEXT_DARK);
        backBtn.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));

        JButton reqBtn = new JButton("Request Ride");
        styleButton(reqBtn);
        reqBtn.setBackground(Style.GREEN); // Primary action

        buttons.add(backBtn);
        buttons.add(reqBtn);
        card.add(buttons, g);

        // for card to wrapper
        content.add(card);
        p.add(content, BorderLayout.CENTER);

        // Logic-Preserved
        backBtn.addActionListener(e -> c1.show(cards, HOME));

        reqBtn.addActionListener(e -> {
            String pickUp = pickUpT.getText().trim();
            String dropOff = dropoffT.getText().trim();

            // basic validation to check against placeholders (first)
            if (pickUp.isEmpty() || pickUp.equals("Enter pickup address...")) {
                JOptionPane.showMessageDialog(this, "Please enter a pickup location.");
                return;
            }
            if (dropOff.isEmpty() || dropOff.equals("Enter destination...")) {
                JOptionPane.showMessageDialog(this, "Please enter a drop-off destination.");
                return;
            }

            // check validation from AutoCompleteTextField class
            if (!pickUpT.hasValidSelection()) {
                JOptionPane.showMessageDialog(this, "Invalid pickup location.");
                return;
            }
            if (!dropoffT.hasValidSelection()) {
                JOptionPane.showMessageDialog(this, "Invalid dropoff location.");
                return;
            }
            try {
                // Hardcoded distance for demo purposes
                Random random = new Random();
                double distMiles = random.nextInt(50);
                int durMin = 12;
                double fare = FareCalculator.calculateStandardFare(distMiles, durMin);

                History newTrip = new History(
                        currentUserID,
                        1, // placeholder Car ID (will be updated when driver accepts)
                        java.time.LocalDateTime.now().toString(),
                        pickUp,
                        dropOff,
                        fare,
                        "requested", // Crucial ofr status is 'requested'
                        "N/A"
                );

                HistoryDAO histDao = new HistoryDAOSQLite();
                histDao.insert(newTrip);

                JOptionPane.showMessageDialog(this,
                        "Ride Requested Successfully!\nEst. Fare: $" + String.format("%.2f", fare),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // return to dashboard to see the request in "Recent Activity"
                JPanel homePage = buildHomePage();
                cards.add(homePage, HOME);
                c1.show(cards, HOME);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        return p;
    }

    /**
     * Builds the read-only cars page.
     * @return JPanel car page containing current user car info
     */
    private JPanel buildCarOverviewPage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Style.APP_BACKGROUD);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Style.APP_BACKGROUD);
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Style.CARD_BACKGROUD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_GRAY),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTHWEST;
        g.weightx = 1.0;
        g.gridx = 0;

        // header
        g.gridy = 0;
        g.gridwidth = 2;
        g.insets = new Insets(0, 0, 25, 0);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Style.CARD_BACKGROUD);

        JLabel title = new JLabel("My Cars");
        title.setFont(Style.FONT_HEADER);
        title.setForeground(Style.TEXT_DARK);

        JButton addBtn = new JButton("Add Car");
        styleButton(addBtn);
        addBtn.setPreferredSize(new Dimension(120, 35));
        addBtn.setBackground(Style.TEXT_DARK);

        header.add(title, BorderLayout.WEST);
        header.add(addBtn, BorderLayout.EAST);
        card.add(header, g);

        //Car list section
        g.gridy++;
        g.insets = new Insets(20, 0, 10, 0);
        JLabel listHeader = new JLabel("Active Cars");
        listHeader.setForeground(Style.TEXT_DARK);
        card.add(listHeader, g);

        // scrollable car list
        g.gridy++;
        g.insets = new Insets(0, 0, 0,0);
        g.fill = GridBagConstraints.BOTH;
        g.weighty = 1.0;

        carListPanel = new JPanel();
        carListPanel.setLayout(new BoxLayout(carListPanel, BoxLayout.Y_AXIS));
        carListPanel.setBackground(Style.APP_BACKGROUD);
        carListPanel.setBorder(BorderFactory.createEmptyBorder(10,40,10,40));

        JScrollPane scrollPane = new JScrollPane(carListPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        card.add(scrollPane, g);

        // bttn
        g.gridy++;
        g.gridwidth = 2;
        g.insets = new Insets(30, 0, 0, 0);
        g.fill = GridBagConstraints.HORIZONTAL;

        JButton backBtn = new JButton("Go Back");
        styleButton(backBtn);
        backBtn.setBackground(Style.APP_BACKGROUD);
        backBtn.setForeground(Style.TEXT_DARK);
        backBtn.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));
        card.add(backBtn, g);

        content.add(card);
        p.add(new JScrollPane(content), BorderLayout.CENTER);

        backBtn.addActionListener(e -> c1.show(cards,HOME));
        addBtn.addActionListener(e -> c1.show(cards, ADD_CAR));

        //cars load here!!
        loadCurrentUserIntoCarPage();

        return p;
    }

    /**
     * Builds the editable panel with nested layouts for perfect alignment.
     * @return JPanel edit car page containing current car info
     */
    private JPanel buildAddCarPage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Style.APP_BACKGROUD);

        // content wrapper
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Style.APP_BACKGROUD);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Style.CARD_BACKGROUD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_GRAY),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        g.weightx = 1.0;
        g.gridx = 0;
        g.gridy = 0;

        // title
        g.insets = new Insets(0, 0, 20, 0);
        JLabel title = new JLabel("Add New Car");
        title.setFont(Style.FONT_HEADER);
        title.setForeground(Style.TEXT_DARK);
        card.add(title, g);

        g.gridy++;

        // Initialize Fields (Reusing class variables)
        JTextField carLicensePlate = new JTextField(); styleTextField(carLicensePlate);
        JTextField carPrice = new JTextField(); styleTextField(carPrice);
        JTextField carExtColor = new JTextField(); styleTextField(carExtColor);
        JTextField carIntColor = new JTextField(); styleTextField(carIntColor);
        JTextField carIntMat = new JTextField(); styleTextField(carIntMat);

        // Dropboxes
        String[] conditions = {"Select Condition:", "fair", "good", "very good", "excellent"};
        JComboBox<String> carCondition = new JComboBox<>(conditions);

        Map<String, String[]> makeModel = new HashMap<>();
        makeModel.put("Toyota", new String[]{"4Runner", "Camry", "Corolla", "Corolla Cross", "Corolla Cross Hybrid",
                "Corolla Hatchback", "Corolla Hybrid", "GR Supra", "Highlander", "Highlander Hybrid", "Prius",
                "Rav4", "Sequoia", "Tacoma", "Other"});
        makeModel.put("Volkswagen", new String[]{"Arteon", "Atlas", "Atlas Cross Sport", "Golf", "Golf GTI", "Golf R",
                "ID. Buzz (electric)", "ID.4 (electric)", "ID.7 (electric)", "Jetta", "Jetta GLI", "Passat",
                "Polo", "Taos", "Tiguan", "Other"});
        makeModel.put("Honda", new String[]{"Accord", "Civic", "Civic Type R", "CR-V", "Element", "Fit", "HR-V",
                "Insight", "Integra", "Odyssey", "Passport", "Pilot", "Prelude", "Ridgeline", "S2000", " Other"});
        makeModel.put("Ford", new String[]{"Bronco", "Bronco Sport", "Edge", "Escape", "Expedition", "Explorer",
                "F-150", "F-250", "F-350", "Fusion", "Maverick", "Mustang", "Mustang Mach-E", "Ranger", "Transit", "Other"});
        makeModel.put("Hyundai", new String[]{"Elantra", "Ioniq 5", "Ioniq 6", "Kona", "Palisade", "Santa Cruz", "Santa Fe",
                "Santa Fe Sport", "Sonata", "Tucson", "Venue", "Veloster", "Azera", "Accent", "Genesis Coupe", "Nexo", "Other"});
        makeModel.put("Nissan", new String[]{"370Z", "Altima", "Armada", "Frontier", "GT-R", "Juke", "Kicks", "Leaf", "Maxima",
                "Murano", "Pathfinder", "Rogue", "Sentra", "Titan", "Versa", "Other"});
        makeModel.put("Suzuki", new String[]{"Aerio", "Alto", "Baleno", "Celerio", "Ciaz", "Ertiga", "Grand Vitara", "Ignis",
                "Jimny", "Kizashi", "S-Cross", "Swift", "SX4", "Vitara", "Wagon R", "Other"});
        makeModel.put("Kia", new String[]{"Carnival", "EV6", "EV9", "Forte", "K5", "Niro", "Optima", "Rio", "Sedona", "Sorento",
                "Soul", "Sportage", "Stinger", "Telluride", "Seltos", "Other"});
        makeModel.put("Chevrolet", new String[]{"Blazer", "Bolt EV", "Camaro", "Colorado", "Corvette", "Equinox", "Impala",
                "Malibu", "Silverado 1500", "Silverado 2500", "Suburban", "Tahoe", "Trailblazer", "Traverse", "Trax", "Other"});
        makeModel.put("BMW", new String[]{"1 Series", "2 Series", "3 Series", "4 Series", "5 Series", "7 Series", "8 Series",
                "i3", "i4", "i7", "i8", "M3", "M4", "X3", "X5", "Other"});
        makeModel.put("Mercedes-Benz", new String[]{"A-Class", "C-Class", "E-Class", "S-Class", "GLA", "GLB", "GLC", "GLE", "GLS",
                "G-Class", "CLA", "CLS", "EQB", "EQE", "EQS", "Other"});
        makeModel.put("Audi", new String[]{"A3", "A4", "A5", "A6", "A7", "A8", "e-tron", "Q3", "Q4 e-tron", "Q5", "Q7", "Q8",
                "RS3", "RS6 Avant", "TT", "Other"});
        makeModel.put("Tesla", new String[]{"Cybertruck", "Model 3", "Model S", "Model X", "Model Y", "Roadster", "Other"});
        makeModel.put("Other", new String[]{"Other"});

        String[] makes = new String[makeModel.size() +1];
        makes[0] = "Select Make:";
        int i = 1;
        for (String key : makeModel.keySet()){
            makes[i] = key;
            i++;
        }
        JComboBox<String> carMake = new JComboBox<>(makes);

        JComboBox<String> carModel = new JComboBox<>();
        carModel.setEnabled(false);

        int maxYear = Year.now().getValue();
        String[] years = new String[11];
        years[0] = "Select Year:";
        for (int j = 0; j < 10; j++){
            years[j+1] = String.valueOf(maxYear - j);
        }
        JComboBox<String> carYear = new JComboBox<>(years);

        // add field
        addEditField(card, g, "Year", carYear);
        addEditField(card, g, "Make", carMake);
        addEditField(card, g, "Model", carModel);
        addEditField(card, g, "License Plate", carLicensePlate);
        addEditField(card, g, "Value", carPrice);
        addEditField(card, g, "Condition", carCondition);
        addEditField(card, g, "Exterior Color", carExtColor);
        addEditField(card, g, "Interior Color", carIntColor);
        addEditField(card, g, "Interior Materials", carIntMat);

        // actions bttn
        g.gridy++;
        g.insets = new Insets(30, 0, 0, 0);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBackground(Style.CARD_BACKGROUD);

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn);
        cancelBtn.setBackground(Style.APP_BACKGROUD);
        cancelBtn.setForeground(Style.TEXT_DARK);
        cancelBtn.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));

        JButton saveBtn = new JButton("Save Changes");
        styleButton(saveBtn);
        saveBtn.setBackground(Style.BLUE);

        buttons.add(cancelBtn);
        buttons.add(saveBtn);
        card.add(buttons, g);

        content.add(card);

        // wrap in ScrollPane because this form is tall!
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);


        // listeners
        carMake.addActionListener(e -> {
            String currentMake = (String) carMake.getSelectedItem();
            String[] models = makeModel.get(currentMake);
            carModel.removeAllItems();
            carModel.addItem("Select Model:");
            for (String model : models) {
                carModel.addItem(model);
            }
            carModel.setEnabled(true);
        });

        cancelBtn.addActionListener(e -> {
            loadCurrentUserIntoCarPage();
            c1.show(cards, CARS);
        });

        saveBtn.addActionListener(event -> {
            CarDAOSQLite carDAO = new CarDAOSQLite();
            try {
                Car currentCar = new Car();

                List<JTextField> fields = List.of(
                        carLicensePlate, carPrice,
                        carExtColor, carIntColor, carIntMat
                );
                for (JTextField f : fields) {
                    if (f.getText().trim().isEmpty()){
                        JOptionPane.showMessageDialog(this, "All fields are required");
                        return;
                    }
                }
                List<JComboBox> dropdownFields = List.of(
                        carModel, carMake, carCondition, carYear
                );
                for (JComboBox f : dropdownFields) {
                    if (f.getSelectedItem() == null || f.getSelectedIndex() == 0){
                        JOptionPane.showMessageDialog(this, "All fields are required");
                        return;
                    }
                }

                String priceText = carPrice.getText().trim();
                String cleanedPriceText = priceText.replaceAll(",", "").replaceAll("\\$", "");
                if (!isInteger(cleanedPriceText) || !isDouble(cleanedPriceText)){
                    JOptionPane.showMessageDialog(this, "Value must be a number.");
                    return;
                }


                currentCar.setUserId(currentUserID);
                currentCar.setYear(Integer.parseInt((String) carYear.getSelectedItem()));
                currentCar.setMake((String) carMake.getSelectedItem());
                currentCar.setModel((String) carModel.getSelectedItem());
                currentCar.setLicensePlate(carLicensePlate.getText().trim());
                currentCar.setCondition((String) carCondition.getSelectedItem());
                currentCar.setPrice(Double.parseDouble(cleanedPriceText));
                currentCar.setExteriorColor(carExtColor.getText().trim());
                currentCar.setInteriorColor(carIntColor.getText().trim());
                currentCar.setInteriorMaterials(carIntMat.getText().trim());

                int rows = carDAO.insert(currentCar);
                if (rows > 0){
                    JOptionPane.showMessageDialog(this, "Car added successfully!");
                    loadCurrentUserIntoCarPage();
                    c1.show(cards, CARS);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save changes.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        });

        return p;
    }

    /**
     * Builds the editable panel with nested layouts for perfect alignment.
     * @return JPanel add car page
     */
    private JPanel buildEditCarPage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Style.APP_BACKGROUD);

        // content wrapper
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Style.APP_BACKGROUD);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Style.CARD_BACKGROUD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_GRAY),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        g.weightx = 1.0;
        g.gridx = 0;
        g.gridy = 0;

        // title
        g.insets = new Insets(0, 0, 20, 0);
        JLabel title = new JLabel("Edit Car Information");
        title.setFont(Style.FONT_HEADER);
        title.setForeground(Style.TEXT_DARK);
        card.add(title, g);

        g.gridy++;

        // Initialize Fields (Reusing class variables)
        carLicensePlateT = new JTextField(); styleTextField(carLicensePlateT);
        carPriceT = new JTextField(); styleTextField(carPriceT);
        carExtColorT = new JTextField(); styleTextField(carExtColorT);
        carIntColorT = new JTextField(); styleTextField(carIntColorT);
        carIntMatT = new JTextField(); styleTextField(carIntMatT);

        // Dropboxes
        String[] conditions = {"Select Condition:", "fair", "good", "very good", "excellent"};
        carConditionT = new JComboBox<>(conditions);

        Map<String, String[]> makeModel = new HashMap<>();
        makeModel.put("Toyota", new String[]{"4Runner", "Camry", "Corolla", "Corolla Cross", "Corolla Cross Hybrid",
                "Corolla Hatchback", "Corolla Hybrid", "GR Supra", "Highlander", "Highlander Hybrid", "Prius",
                "Rav4", "Sequoia", "Tacoma", "Other"});
        makeModel.put("Volkswagen", new String[]{"Arteon", "Atlas", "Atlas Cross Sport", "Golf", "Golf GTI", "Golf R",
                "ID. Buzz (electric)", "ID.4 (electric)", "ID.7 (electric)", "Jetta", "Jetta GLI", "Passat",
                "Polo", "Taos", "Tiguan", "Other"});
        makeModel.put("Honda", new String[]{"Accord", "Civic", "Civic Type R", "CR-V", "Element", "Fit", "HR-V",
                "Insight", "Integra", "Odyssey", "Passport", "Pilot", "Prelude", "Ridgeline", "S2000", " Other"});
        makeModel.put("Ford", new String[]{"Bronco", "Bronco Sport", "Edge", "Escape", "Expedition", "Explorer",
                "F-150", "F-250", "F-350", "Fusion", "Maverick", "Mustang", "Mustang Mach-E", "Ranger", "Transit", "Other"});
        makeModel.put("Hyundai", new String[]{"Elantra", "Ioniq 5", "Ioniq 6", "Kona", "Palisade", "Santa Cruz", "Santa Fe",
                "Santa Fe Sport", "Sonata", "Tucson", "Venue", "Veloster", "Azera", "Accent", "Genesis Coupe", "Nexo", "Other"});
        makeModel.put("Nissan", new String[]{"370Z", "Altima", "Armada", "Frontier", "GT-R", "Juke", "Kicks", "Leaf", "Maxima",
                "Murano", "Pathfinder", "Rogue", "Sentra", "Titan", "Versa", "Other"});
        makeModel.put("Suzuki", new String[]{"Aerio", "Alto", "Baleno", "Celerio", "Ciaz", "Ertiga", "Grand Vitara", "Ignis",
                "Jimny", "Kizashi", "S-Cross", "Swift", "SX4", "Vitara", "Wagon R", "Other"});
        makeModel.put("Kia", new String[]{"Carnival", "EV6", "EV9", "Forte", "K5", "Niro", "Optima", "Rio", "Sedona", "Sorento",
                "Soul", "Sportage", "Stinger", "Telluride", "Seltos", "Other"});
        makeModel.put("Chevrolet", new String[]{"Blazer", "Bolt EV", "Camaro", "Colorado", "Corvette", "Equinox", "Impala",
                "Malibu", "Silverado 1500", "Silverado 2500", "Suburban", "Tahoe", "Trailblazer", "Traverse", "Trax", "Other"});
        makeModel.put("BMW", new String[]{"1 Series", "2 Series", "3 Series", "4 Series", "5 Series", "7 Series", "8 Series",
                "i3", "i4", "i7", "i8", "M3", "M4", "X3", "X5", "Other"});
        makeModel.put("Mercedes-Benz", new String[]{"A-Class", "C-Class", "E-Class", "S-Class", "GLA", "GLB", "GLC", "GLE", "GLS",
                "G-Class", "CLA", "CLS", "EQB", "EQE", "EQS", "Other"});
        makeModel.put("Audi", new String[]{"A3", "A4", "A5", "A6", "A7", "A8", "e-tron", "Q3", "Q4 e-tron", "Q5", "Q7", "Q8",
                "RS3", "RS6 Avant", "TT", "Other"});
        makeModel.put("Tesla", new String[]{"Cybertruck", "Model 3", "Model S", "Model X", "Model Y", "Roadster", "Other"});
        makeModel.put("Other", new String[]{"Other"});

        String[] makes = new String[makeModel.size() +1];
        makes[0] = "Select Make:";
        int i = 1;
        for (String key : makeModel.keySet()){
            makes[i] = key;
            i++;
        }
        carMakeT = new JComboBox<>(makes);

        carModelT = new JComboBox<>();
        carModelT.setEnabled(false);

        int maxYear = Year.now().getValue();
        String[] years = new String[11];
        years[0] = "Select Year:";
        for (int j = 0; j < 10; j++){
            years[j+1] = String.valueOf(maxYear - j);
        }
        carYearT = new JComboBox<>(years);

        addEditField(card, g, "Year", carYearT);
        addEditField(card, g, "Make", carMakeT);
        addEditField(card, g, "Model", carModelT);
        addEditField(card, g, "License Plate", carLicensePlateT);
        addEditField(card, g, "Value", carPriceT);
        addEditField(card, g, "Condition", carConditionT);
        addEditField(card, g, "Exterior Color", carExtColorT);
        addEditField(card, g, "Interior Color", carIntColorT);
        addEditField(card, g, "Interior Materials", carIntMatT);

        // actions bttn
        g.gridy++;
        g.insets = new Insets(30, 0, 0, 0);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBackground(Style.CARD_BACKGROUD);

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn);
        cancelBtn.setBackground(Style.APP_BACKGROUD);
        cancelBtn.setForeground(Style.TEXT_DARK);
        cancelBtn.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));

        JButton saveBtn = new JButton("Save Changes");
        styleButton(saveBtn);
        saveBtn.setBackground(Style.BLUE);

        buttons.add(cancelBtn);
        buttons.add(saveBtn);
        card.add(buttons, g);

        content.add(card);

        // wrap in ScrollPane because this form is tall!
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);

        // listeners
        carMakeT.addActionListener(e -> {
            String currentMake = (String) carMakeT.getSelectedItem();
            String[] models = makeModel.get(currentMake);
            carModelT.removeAllItems();
            carModelT.addItem("Select Model:");
            for (String model : models) {
                carModelT.addItem(model);
            }
            carModelT.setEnabled(true);
        });

        cancelBtn.addActionListener(e -> {
            loadCurrentUserIntoCarPage();
            c1.show(cards, CARS);
        });

        saveBtn.addActionListener(event -> {
            CarDAOSQLite carDAO = new CarDAOSQLite();
            try {
                Optional<Car> opt = carDAO.findById(currentCarID);
                if (opt.isEmpty()) return;
                List<JTextField> fields = List.of(
                        carLicensePlateT, carPriceT,
                        carExtColorT, carIntColorT, carIntMatT
                );
                for (JTextField f : fields) {
                    if (f.getText().trim().isEmpty()){
                        JOptionPane.showMessageDialog(this, "All fields are required");
                        return;
                    }
                }
                List<JComboBox> dropdownFields = List.of(
                        carModelT, carMakeT, carConditionT, carYearT
                );
                for (JComboBox f : dropdownFields) {
                    if (f.getSelectedItem() == null || f.getSelectedIndex() == 0){
                        JOptionPane.showMessageDialog(this, "All fields are required");
                        return;
                    }
                }

                String priceText = carPriceT.getText().trim();
                String cleanedPriceText = priceText.replaceAll(",", "").replaceAll("\\$", "");
                if (!isInteger(cleanedPriceText) || !isDouble(cleanedPriceText)){
                    JOptionPane.showMessageDialog(this, "Value must be a number.");
                    return;
                }

                Car currentCar = opt.get();
                currentCar.setYear(Integer.parseInt((String) carYearT.getSelectedItem()));
                currentCar.setMake((String) carMakeT.getSelectedItem());
                currentCar.setModel((String) carModelT.getSelectedItem());
                currentCar.setLicensePlate(carLicensePlateT.getText().trim());
                currentCar.setCondition((String) carConditionT.getSelectedItem());
                currentCar.setPrice(Double.parseDouble(carPriceT.getText().trim()));
                currentCar.setExteriorColor(carExtColorT.getText().trim());
                currentCar.setInteriorColor(carIntColorT.getText().trim());
                currentCar.setInteriorMaterials(carIntMatT.getText().trim());

                int rows = carDAO.update(currentCar);
                if (rows > 0){
                    JOptionPane.showMessageDialog(this, "Car updated successfully!");
                    loadCurrentUserIntoCarPage();
                    c1.show(cards, CARS);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save changes.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        });

        return p;
    }

    /**
     * Builds the editable Profile Page panel with nested layouts for perfect alignment.
     * @return JPanel profile page containing current user info
     */
    private JPanel buildEditProfilePage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Style.APP_BACKGROUD);

        // content wrapper
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Style.APP_BACKGROUD);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Style.CARD_BACKGROUD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_GRAY),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        g.weightx = 1.0;
        g.gridx = 0;
        g.gridy = 0;

        // title
        g.insets = new Insets(0, 0, 20, 0);
        JLabel title = new JLabel("Edit Profile");
        title.setFont(Style.FONT_HEADER);
        title.setForeground(Style.TEXT_DARK);
        card.add(title, g);

        // personal info section
        g.gridy++;
        card.add(createSectionHeader("Personal Information"), g);

        // Initialize Fields (Reusing class variables)
        firstNameT = new JTextField(); styleTextField(firstNameT);
        lastNameT = new JTextField(); styleTextField(lastNameT);
        emailT = new JTextField(); styleTextField(emailT);
        phoneT = new JTextField(); styleTextField(phoneT);
        licenseT = new JTextField(); styleTextField(licenseT);
        dobT = new JTextField(); styleTextField(dobT);

        addEditField(card, g, "First Name", firstNameT);
        addEditField(card, g, "Last Name", lastNameT);
        addEditField(card, g, "Email", emailT);
        addEditField(card, g, "Phone", phoneT);
        addEditField(card, g, "License Plate", licenseT);
        addEditField(card, g, "Date of Birth (YYYY-MM-DD)", dobT);

        // address section
        g.gridy++;
        g.insets = new Insets(20, 0, 0, 0);
        card.add(createSectionHeader("Address Details"), g);

        streetT = new JTextField(); styleTextField(streetT);
        cityT = new JTextField(); styleTextField(cityT);
        stateT = new JTextField(); styleTextField(stateT);
        countryT = new JTextField(); styleTextField(countryT);
        zipT = new JTextField(); styleTextField(zipT);

        addEditField(card, g, "Street Address", streetT);
        addEditField(card, g, "City", cityT);

        JPanel splitRow = new JPanel(new GridLayout(1, 2, 15, 0));
        splitRow.setBackground(Style.CARD_BACKGROUD);

        JPanel stateP = new JPanel(new BorderLayout(0,5));
        stateP.setBackground(Style.CARD_BACKGROUD);
        stateP.add(new JLabel("State"), BorderLayout.NORTH);
        stateP.add(stateT, BorderLayout.CENTER);

        JPanel zipP = new JPanel(new BorderLayout(0,5));
        zipP.setBackground(Style.CARD_BACKGROUD);
        zipP.add(new JLabel("Zip Code"), BorderLayout.NORTH);
        zipP.add(zipT, BorderLayout.CENTER);

        splitRow.add(stateP);
        splitRow.add(zipP);

        g.gridy++;
        g.insets = new Insets(15, 0, 0, 0);
        card.add(splitRow, g);

        addEditField(card, g, "Country", countryT);

        g.gridy++;
        g.insets = new Insets(30, 0, 0, 0);
        card.add(createSectionHeader("Payment Methods"), g);

        // 1. Credit Card
        JTextField ccNum = new JTextField(); styleTextField(ccNum); textHelper(ccNum, "0000 0000 0000 0000");
        JTextField ccExp = new JTextField(); styleTextField(ccExp); textHelper(ccExp, "MM/YY");
        JTextField ccCvc = new JTextField(); styleTextField(ccCvc); textHelper(ccCvc, "CVC");

        addEditField(card, g, "Card Number", ccNum);

        // Split row for Exp/CVC
        JPanel ccSplit = new JPanel(new GridLayout(1, 2, 15, 0));
        ccSplit.setBackground(Style.CARD_BACKGROUD);

        JPanel expP = new JPanel(new BorderLayout(0,5));
        expP.setBackground(Style.CARD_BACKGROUD);
        expP.add(new JLabel("Expiration"), BorderLayout.NORTH);
        expP.add(ccExp, BorderLayout.CENTER);

        JPanel cvcP = new JPanel(new BorderLayout(0,5));
        cvcP.setBackground(Style.CARD_BACKGROUD);
        cvcP.add(new JLabel("CVC"), BorderLayout.NORTH);
        cvcP.add(ccCvc, BorderLayout.CENTER);

        ccSplit.add(expP);
        ccSplit.add(cvcP);

        g.gridy++;
        g.insets = new Insets(15, 0, 0, 0);
        card.add(ccSplit, g);

        // 2. Digital Wallet
        g.gridy++;
        g.insets = new Insets(15, 0, 5, 0);
        JLabel walletL = new JLabel("Digital Wallets");
        walletL.setFont(Style.FONT_LABEL);
        walletL.setForeground(Style.TEXT_GRAY);
        card.add(walletL, g);

        g.gridy++;
        g.insets = new Insets(0, 0, 0, 0);

        JButton paypalBtn = new JButton("Connect PayPal / Google Pay");
        styleButton(paypalBtn);
        paypalBtn.setBackground(new Color(255, 196, 57)); // PayPal Gold-ish color
        paypalBtn.setForeground(Style.TEXT_DARK);
        paypalBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        paypalBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Redirecting to Digital Wallet provider...");
        });
        card.add(paypalBtn, g);

        // actions bttn
        g.gridy++;
        g.insets = new Insets(30, 0, 0, 0);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBackground(Style.CARD_BACKGROUD);

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn);
        cancelBtn.setBackground(Style.APP_BACKGROUD);
        cancelBtn.setForeground(Style.TEXT_DARK);
        cancelBtn.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));

        JButton saveBtn = new JButton("Save Changes");
        styleButton(saveBtn);
        saveBtn.setBackground(Style.BLUE);

        buttons.add(cancelBtn);
        buttons.add(saveBtn);
        card.add(buttons, g);

        content.add(card);

        // wrap in ScrollPane because this form is tall!
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);

        // listeners
        cancelBtn.addActionListener(e -> {
            loadUserIntoViewProf();
            c1.show(cards, VIEW_PROF);
        });

        saveBtn.addActionListener(event -> {
            UserDAOSQLite user = new UserDAOSQLite();
            try {
                Optional<User> opt = user.findById(currentUserID);
                if (!opt.isPresent()) return;

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
                    JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                    loadUserIntoViewProf();
                    c1.show(cards, VIEW_PROF);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save changes.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        });

        return p;
    }

    /**
     * Helper to add a label + input pair to the Edit form.
     */
    private void addEditField(JPanel p, GridBagConstraints g, String label, JComponent field) {
        g.gridy++;
        g.insets = new Insets(15, 0, 5, 0); // Spacing above label
        JLabel l = new JLabel(label);
        l.setFont(Style.FONT_LABEL);
        l.setForeground(Style.TEXT_GRAY);
        p.add(l, g);

        g.gridy++;
        g.insets = new Insets(0, 0, 0, 0); // No spacing between label and input
        p.add(field, g);
    }

    /**
     * Builds the JPanel History page for displaying privious trips
     * @return JPanel holding information about the user's past travels
     */
    private JPanel buildHistoryPage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Style.APP_BACKGROUD);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Style.APP_BACKGROUD);
        header.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Trip History");
        title.setFont(Style.FONT_HEADER);
        title.setForeground(Style.TEXT_DARK);
        header.add(title, BorderLayout.WEST);
        p.add(header, BorderLayout.NORTH);

        // List Container
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Style.APP_BACKGROUD);

        // Add top spacing
        listPanel.add(Box.createVerticalStrut(10));

        try {
            UserDAOSQLite userDAO = new UserDAOSQLite();
            Optional<User> uOpt = userDAO.findById(currentUserID);
            String role = uOpt.isPresent() ? uOpt.get().getRole() : "rider";

            HistoryDAO histDao = new HistoryDAOSQLite();
            List<History> trips;

            // Fetch correct history based on role
            if ("driver".equalsIgnoreCase(role)) {
                trips = histDao.findDriverHistory(currentUserID);
            } else {
                trips = histDao.findUserHistory(currentUserID);
            }

            if (trips.isEmpty()) {
                JLabel empty = new JLabel("No trip history found.");
                empty.setFont(Style.FONT_REGULAR);
                empty.setForeground(Style.TEXT_GRAY);
                empty.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
                listPanel.add(empty);
            } else {
                for (History h : trips) {
                    // Card Container
                    JPanel card = new JPanel(new BorderLayout());
                    card.setBackground(Style.CARD_BACKGROUD);
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(5, 40, 5, 40), // Outer margin
                            BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(Style.BORDER_GRAY),
                                    BorderFactory.createEmptyBorder(15, 20, 15, 20) // Inner padding
                            )
                    ));
                    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

                    // Left: Route & Date
                    JPanel infoP = new JPanel(new GridLayout(2, 1, 0, 5));
                    infoP.setBackground(Style.CARD_BACKGROUD);

                    JLabel route = new JLabel(h.getPickupLoc() + " \u2192 " + h.getDropoffLoc());
                    route.setFont(Style.FONT_LABEL);
                    route.setForeground(Style.TEXT_DARK);

                    JLabel date = new JLabel("Requested: " + h.getRequestedAt());
                    date.setFont(Style.FONT_SMALL);
                    date.setForeground(Style.TEXT_GRAY);

                    infoP.add(route);
                    infoP.add(date);

                    // Right: Fare, Status, Button
                    JPanel statusP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
                    statusP.setBackground(Style.CARD_BACKGROUD);

                    JLabel fare = new JLabel(String.format("$%.2f", h.getFare()));
                    fare.setFont(Style.FONT_HEADER);
                    fare.setForeground(Style.TEXT_DARK);

                    // Status Badge
                    JLabel badge = createStatusBadge(h.getStatus());

                    // "Next Status" Button (Simulation Logic)
                    // Only show if trip is active (not completed/canceled)
                    String s = h.getStatus().toLowerCase();
                    if (!s.equals("completed") && !s.equals("canceled") && !s.equals("refunded")) {
                        JButton actionBtn = new JButton("Advance Status");
                        actionBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
                        actionBtn.setBackground(Style.APP_BACKGROUD);
                        actionBtn.setFocusPainted(false);
                        actionBtn.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));
                        actionBtn.setPreferredSize(new Dimension(100, 25));

                        actionBtn.addActionListener(e -> {
                            try {
                                String next = getNextStatus(h.getStatus());
                                h.setStatus(next);
                                histDao.update(h);

                                // Refresh page
                                JPanel refresh = buildHistoryPage();
                                cards.add(refresh, HIST);
                                c1.show(cards, HIST);
                            } catch (Exception ex) { ex.printStackTrace(); }
                        });
                        statusP.add(actionBtn);
                    }

                    statusP.add(badge);
                    statusP.add(fare);

                    card.add(infoP, BorderLayout.CENTER);
                    card.add(statusP, BorderLayout.EAST);

                    listPanel.add(card);
                    listPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);

        // Footer Back Button
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Style.APP_BACKGROUD);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        JButton backBtn = new JButton("Go Back");
        styleButton(backBtn);
        backBtn.setBackground(Style.TEXT_GRAY);
        backBtn.addActionListener(e -> {
            JPanel refreshHome = buildHomePage();
            cards.add(refreshHome, HOME);
            c1.show(cards, HOME);
        });
        bottom.add(backBtn);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    /**
     * Builds the read-only profile page.
     * @return JPanel profile page containing current user info
     */
    private JPanel buildProfileOverviewPage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Style.APP_BACKGROUD);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Style.APP_BACKGROUD);
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Style.CARD_BACKGROUD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_GRAY),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTHWEST;
        g.weightx = 1.0;
        g.gridx = 0;

        // header
        g.gridy = 0;
        g.gridwidth = 2;
        g.insets = new Insets(0, 0, 25, 0);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Style.CARD_BACKGROUD);

        JLabel title = new JLabel("My Profile");
        title.setFont(Style.FONT_HEADER);
        title.setForeground(Style.TEXT_DARK);

        JButton editBtn = new JButton("Edit Profile");
        styleButton(editBtn);
        editBtn.setPreferredSize(new Dimension(120, 35));
        editBtn.setBackground(Style.TEXT_DARK);

        header.add(title, BorderLayout.WEST);
        header.add(editBtn, BorderLayout.EAST);
        card.add(header, g);

        // data
        g.gridwidth = 1;
        g.gridy = 1;

        profileViewLabels = new JLabel[11];

        addProfileField(card, g, 0, "First Name", 0);
        addProfileField(card, g, 1, "Last Name", 1);

        g.gridy++;
        addProfileField(card, g, 0, "Email", 2);
        addProfileField(card, g, 1, "Phone", 3);

        g.gridy++;
        addProfileField(card, g, 0, "License Plate", 4);
        addProfileField(card, g, 1, "Date of Birth", 5);

        // address section
        g.gridy++;
        g.gridwidth = 2;
        // FIX: Explicitly force anchor to WEST (Left) so it doesn't float
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.NONE; // Don't stretch, just sit on the left
        g.insets = new Insets(20, 0, 15, 0);

        JLabel addrHeader = new JLabel("Address Information");
        addrHeader.setFont(Style.FONT_LABEL);
        addrHeader.setForeground(Style.BLUE);
        card.add(addrHeader, g);

        // reset constraints for fields
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTHWEST;
        g.gridwidth = 1;
        g.gridy++;
        g.insets = new Insets(0, 0, 15, 0);

        addProfileField(card, g, 0, "Street", 6);
        addProfileField(card, g, 1, "City", 7);

        g.gridy++;
        addProfileField(card, g, 0, "State", 8);
        addProfileField(card, g, 1, "Zip Code", 10);

        g.gridy++;
        addProfileField(card, g, 0, "Country", 9);

        // bttn
        g.gridy++;
        g.gridwidth = 2;
        g.insets = new Insets(30, 0, 0, 0);
        g.fill = GridBagConstraints.HORIZONTAL;

        JButton backBtn = new JButton("Go Back");
        styleButton(backBtn);
        backBtn.setBackground(Style.APP_BACKGROUD);
        backBtn.setForeground(Style.TEXT_DARK);
        backBtn.setBorder(BorderFactory.createLineBorder(Style.BORDER_GRAY));
        card.add(backBtn, g);

        content.add(card);
        p.add(new JScrollPane(content), BorderLayout.CENTER);

        editBtn.addActionListener(e -> {
            loadCurrentUserIntoProfile();
            c1.show(cards, PROF);
        });
        backBtn.addActionListener(e -> c1.show(cards, HOME));

        return p;
    }

    /**
     * Build the page for the Driver
     * @return the page to be shown
     */
    private  JPanel buildDriverRequestsPage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Style.APP_BACKGROUD);

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Style.APP_BACKGROUD);
        header.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Open Ride Requests");
        title.setFont(Style.FONT_HEADER);
        title.setForeground(Style.TEXT_DARK);
        header.add(title, BorderLayout.WEST);
        p.add(header, BorderLayout.NORTH);

        // content
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Style.APP_BACKGROUD);

        try {
            HistoryDAO dao = new HistoryDAOSQLite();
            // 1. fetch open requests
            List<History> requests = dao.findOpenRequests();

            // 2. Get Current Driver's Car ID (Required to accept a ride)
            CarDAO carDao = new CarDAOSQLite();
            List<Car> cars = carDao.findAll();
            int myCarId = -1;
            for(Car c : cars) {
                if(c.getUserId() == currentUserID) {
                    myCarId = c.getCarId();
                    break;
                }
            }

            if (requests.isEmpty()) {
                JLabel empty = new JLabel("No active requests at the moment.");
                empty.setFont(Style.FONT_REGULAR);
                empty.setForeground(Style.TEXT_GRAY);
                empty.setBorder(BorderFactory.createEmptyBorder(20,40,0,0));
                listPanel.add(empty);
            } else {
                for (History h : requests) {
                    JPanel card = new JPanel(new BorderLayout());
                    card.setBackground(Style.CARD_BACKGROUD);
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(10, 40, 10, 40),
                            BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(Style.BORDER_GRAY),
                                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                            )
                    ));
                    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                    JPanel routeInfo = new JPanel(new GridLayout(2, 1));
                    routeInfo.setBackground(Style.CARD_BACKGROUD);

                    JLabel route = new JLabel(h.getPickupLoc() + " \u2192 " + h.getDropoffLoc());
                    route.setFont(Style.FONT_LABEL);

                    JLabel time = new JLabel("Requested: " + h.getRequestedAt());
                    time.setFont(Style.FONT_SMALL);
                    time.setForeground(Style.TEXT_GRAY);

                    routeInfo.add(route);
                    routeInfo.add(time);

                    //Fare + Accept Button
                    JPanel action = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    action.setBackground(Style.CARD_BACKGROUD);

                    JLabel fare = new JLabel(String.format("$%.2f  ", h.getFare()));
                    fare.setFont(Style.FONT_HEADER);
                    fare.setForeground(Style.TEXT_DARK);

                    JButton acceptBtn = new JButton("Accept");
                    styleButton(acceptBtn);
                    acceptBtn.setPreferredSize(new Dimension(100, 35));

                    int finalMyCarId = myCarId;
                    acceptBtn.addActionListener(e -> {
                        if (finalMyCarId == -1) {
                            JOptionPane.showMessageDialog(this, "You need a registered car to accept rides!");
                            return;
                        }
                        try {
                            // Assign Driver (Car)
                            h.setCarID(finalMyCarId);
                            h.setStatus("accepted");
                            dao.update(h);

                            JOptionPane.showMessageDialog(this, "Ride Accepted! Head to pickup.");

                            // Reload page
                            c1.show(cards, HOME);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    action.add(fare);
                    action.add(acceptBtn);

                    card.add(routeInfo, BorderLayout.CENTER);
                    card.add(action, BorderLayout.EAST);

                    listPanel.add(card);
                    listPanel.add(Box.createVerticalStrut(10));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        p.add(scroll, BorderLayout.CENTER);

        // Back Button at bottom
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Style.APP_BACKGROUD);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        JButton backBtn = new JButton("Go Back");
        styleButton(backBtn);
        backBtn.setBackground(Style.TEXT_GRAY);
        backBtn.addActionListener(e -> c1.show(cards, HOME));
        bottom.add(backBtn);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    /**
     * Creates a colored "Pill" badge for trip status.
     */
    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel(status.toUpperCase());
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        badge.setHorizontalAlignment(SwingConstants.CENTER);

        // color based for status
        switch (status.toLowerCase()) {
            case "completed":
                badge.setBackground(new Color(232, 245, 233)); // light green
                badge.setForeground(new Color(46, 125, 50));   // dark green
                break;
            case "canceled":
            case "refunded":
                badge.setBackground(new Color(255, 235, 238)); // light red
                badge.setForeground(new Color(198, 40, 40));   // dark red
                break;
            case "accepted":
            case "enroute":
                badge.setBackground(new Color(227, 242, 253)); // light blue
                badge.setForeground(new Color(21, 101, 192));  // dark blue
                break;
            default: // "requested", etc.
                badge.setBackground(new Color(255, 248, 225)); // light yellow
                badge.setForeground(new Color(245, 127, 23));  // dark orange
        }
        return badge;
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
     * Call this when opening the Car page so field are populated from DB
     */
    private void loadCurrentUserIntoCarPage() {
        try {
            CarDAOSQLite carDAO = new CarDAOSQLite();
            List<Car> list = carDAO.findAllByUserId(currentUserID);
            carListPanel.removeAll();

            if (list.isEmpty()) {
                // clear fields or leave placeholders as-is
                JLabel none = new JLabel("No cars found.");
                none.setForeground(Color.BLACK);
                none.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                System.err.println("No cars found for id=" + currentUserID);
            } else {
                for (Car c : list) {
                    JPanel card = buildCarCard(c);
                    carListPanel.add(card);
                    carListPanel.add(Box.createVerticalStrut(10));
                }
            }

            carListPanel.revalidate();
            carListPanel.repaint();

        } catch (Exception e) {
            System.err.println("Error loading profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *  Call this when opening the Car page so field are populated from DB
     */
    private JPanel buildCarCard(Car c) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setOpaque(true);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel title = new JLabel(c.getYear() + " " + c.getMake() + " " + c.getModel());
        title.setFont(Style.FONT_LABEL);
        JLabel plate = new JLabel("Plate: " + c.getLicensePlate());
        plate.setFont(Style.FONT_SMALL);
        JLabel color = new JLabel("Ext. Color: " + c.getExteriorColor());
        color.setFont(Style.FONT_SMALL);
        JLabel condition = new JLabel("Condition: " + c.getCondition());
        condition.setFont(Style.FONT_SMALL);

        JPanel info = new JPanel(new GridLayout(4,1));
        info.setBackground(Style.CARD_BACKGROUD);
        info.add(title);
        info.add(plate);
        info.add(color);
        info.add(condition);

        //buttons
        JPanel actions = new JPanel();
        actions.setBackground(Style.CARD_BACKGROUD);
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));

        JButton editBtn = new JButton("Edit");
        styleButton(editBtn);
        editBtn.setPreferredSize(new Dimension(90, 35));
        editBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton deleteBtn = new JButton("Delete");
        styleButton(deleteBtn);
        deleteBtn.setPreferredSize(new Dimension(90, 35));
        deleteBtn.setBackground(Style.ERROR_RED);
        deleteBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);

        editBtn.addActionListener(e -> {
                    currentCarID = c.getCarId();
                    loadCarIntoEditForm();
                    c1.show(cards, EDIT_CAR);
                });

        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete this car?", "Confirm", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try {
                    CarDAOSQLite carDAO = new CarDAOSQLite();
                    carDAO.delete(c.getCarId());
                    loadCurrentUserIntoCarPage();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(this, "Error deleting car: " + exception.getMessage());
                }
            }
        });

        actions.add(editBtn);
        actions.add((Box.createVerticalStrut(5)));
        actions.add(deleteBtn);

        card.add(info, BorderLayout.CENTER);
        card.add(actions, BorderLayout.EAST);

        return card;
    }

    /**
     * Call this when opening the Car page so field are populated from DB
     */
    private void loadCarIntoEditForm() {
        if (currentCarID <= 0) return;
        try {
            CarDAOSQLite carDAO = new CarDAOSQLite();
            Optional<Car> opt = carDAO.findById(currentCarID);
            if (opt.isEmpty()) {
                // clear fields or leave placeholders as-is
                System.err.println("No car found for id=" + currentCarID);
                return;
            }
            Car c = opt.get();

            // setText but avoid setting placeholders unintentionally
            carMakeT.setForeground(Color.BLACK);
            carModelT.setForeground(Color.BLACK);
            carYearT.setForeground(Color.BLACK);
            carPriceT.setForeground(Color.BLACK);
            carConditionT.setForeground(Color.BLACK);
            carExtColorT.setForeground(Color.BLACK);
            carIntColorT.setForeground(Color.BLACK);
            carIntMatT.setForeground(Color.BLACK);
            carLicensePlateT.setForeground(Color.BLACK);

            carMakeT.setSelectedItem(c.getMake());
            carModelT.setSelectedItem(c.getModel());
            carYearT.setSelectedItem(Integer.toString(c.getYear()));
            carPriceT.setText(Double.toString(c.getPrice()));
            carConditionT.setSelectedItem(c.getCondition());
            carExtColorT.setText(c.getExteriorColor());
            carIntColorT.setText(c.getInteriorColor());
            carIntMatT.setText(c.getInteriorMaterials());
            carLicensePlateT.setText(c.getLicensePlate());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to trim location from DB so be displayed
     * @param location the String that holds the pickup/dropoff to be trimmed
     */
    private String trimLocation(String location){
        if (location == null) {
            return null; // Handle null input
        }
        String trimmedLoc;
        int firstCommaIndex = location.indexOf(',');

        if (firstCommaIndex != -1) {
            trimmedLoc = location.substring(0, firstCommaIndex);
        } else {
            trimmedLoc = location;
        }
        return trimmedLoc;
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
     *
     * @param current
     * @return
     */
    private String getNextStatus(String current){
        if (current == null) return "Completed";

        switch (current.toLowerCase()) {
            case "requested": return "accepted";
            case "accepted": return "enroute";
            case "enroute": return "completed";
            default: return "completed";
        }
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
     * Helper to add a read-only field to the profile card.
     * @param p Panel
     * @param g GridBagConstraints
     * @param col Column (0 or 1)
     * @param label Title
     * @param index Index in the profileViewLabels array
     */
    private void addProfileField(JPanel p, GridBagConstraints g, int col, String label, int index) {
        g.gridx = col;
        // right padding for col 0, Left padding for col 1
        g.insets = (col == 0) ? new Insets(0, 0, 15, 20) : new Insets(0, 20, 15, 0);
        g.weightx = 0.5;

        JPanel field = new JPanel(new BorderLayout(0, 5));
        field.setBackground(Style.CARD_BACKGROUD);

        JLabel l = new JLabel(label);
        l.setFont(Style.FONT_SMALL);
        l.setForeground(Style.TEXT_GRAY);

        JLabel v = new JLabel("Loading...");
        v.setFont(Style.FONT_REGULAR);
        v.setForeground(Style.TEXT_DARK);

        profileViewLabels[index] = v; // store

        field.add(l, BorderLayout.NORTH);
        field.add(v, BorderLayout.CENTER);
        p.add(field, g);
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

    /** helper method to check text inputs for integer
     *
     * @param str, String
     * @return boolean
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** helper method to check text inputs for double
     *
     * @param str, String
     * @return boolean
     */
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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