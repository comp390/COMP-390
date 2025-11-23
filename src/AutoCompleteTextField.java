import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * AutoCompleteTextField
 * version 0.1
 * 10/19/25
 * This Class provide addresses lookup services provided by nominatim
 */
public class AutoCompleteTextField extends JTextField {

    private JPopupMenu popupMenu;
    private boolean validSelection = false;
    private String selectedValue = "";

    /**
     * The constructor of the class
     * @param columns Integer, amount if char pass in
     */
    public AutoCompleteTextField(int columns) {
        super(columns);
        popupMenu = new JPopupMenu();

        // pay attentionm  -> dropdown while typing
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                validSelection = false;  //  must revalidate
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    triggerSearch();  // Only ENTER triggers dropdown
                }
            }
        });
    }

    /**
     * verify JTextFied are not empty
     * @return Boolean, true if address is provided
     */
    public boolean hasValidSelection() {
        return validSelection;
    }

    /**
     * Starts the query and calls showSuggestions
     */
    private void triggerSearch() {
        String query = getText().trim();
        if (query.length() < 3) {
            return;
        }

        showSuggestions(query);
    }

    /**
     * Used by buttons to indecate when to start the query
     */
    public void searchNow() {
        triggerSearch();
    }

    /**
     * Connects to the service and request for given string
     * @param query String, what is to be query
     */
    private void showSuggestions(String query) {
        popupMenu.removeAll();

        try {
            String url = "https://nominatim.openstreetmap.org/search?q=" +
                    URLEncoder.encode(query, StandardCharsets.UTF_8) +
                    "&format=json&addressdetails=1&limit=5";

            HttpURLConnection conn = (HttpURLConnection)
                    new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", "RideshareApp/1.0");

            try (InputStream in = conn.getInputStream()) {
                JSONArray results = new JSONArray(new String(in.readAllBytes()));

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    String display = obj.getString("display_name");

                    JMenuItem item = new JMenuItem(display);
                    item.addActionListener(e -> {
                        setText(display);
                        selectedValue = display;
                        validSelection = true;
                        popupMenu.setVisible(false);
                    });

                    popupMenu.add(item);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (popupMenu.getComponentCount() > 0) {
            popupMenu.show(this, 0, this.getHeight());
        }
    }
}
