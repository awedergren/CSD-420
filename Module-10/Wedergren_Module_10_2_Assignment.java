// Amanda Wedergren
// October 1, 2025
// Module 10.2 Assignment

// Write a program that views and updates the fan information stored in a database.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Single-file standalone Java application that connects to a MySQL database to
 * view and update "fan" records. No Maven or external build is required;
 * compile with javac and run with java, supplying the MySQL Connector/J JAR on
 * the classpath.
 *
 * Features:
 * - A simple Swing UI with two buttons: Display (load by id) and Update (save changes)
 * - JDBC methods to load and update a fan row in the database
 * - An embedded, minimal test-runner mode (--test) which inserts/updates a
 *   fixture row (does NOT create/delete tables) and verifies load/update
 * - Lots of inline comments to explain how it works and where to adapt it
 *
 * IMPORTANT:
 * - This program assumes a MySQL database reachable at jdbc:mysql://localhost:3306/databasedb
 *   with a user 'student1' and password 'pass'. Adjust the DB_URL, DB_USER and
 *   DB_PASSWORD constants below if your environment is different.
 * - It also assumes there exists a table named `fans` with these columns:
 *     id (INT primary key),
 *     first_name (VARCHAR),
 *     last_name (VARCHAR),
 *     favorite_team (VARCHAR)
 *   If your table or column names differ, update the SQL statements in loadFan/updateFan accordingly.
 * - The program intentionally does NOT create or drop the table. Tests do insert/update rows only.
 *
 * How to compile (PowerShell / Windows):
 *  1) Download the MySQL Connector/J jar (for example mysql-connector-java-8.0.xx.jar)
 *     and note its path, e.g. C:\libs\mysql-connector-java-8.0.33.jar
 *  2) From this file's directory run:
 *     javac -cp .;C:\libs\mysql-connector-java-8.0.33.jar Wedergren_Module_10_2_Assignment.java
 *  3) Run the GUI:
 *     java -cp .;C:\libs\mysql-connector-java-8.0.33.jar Wedergren_Module_10_2_Assignment
 *  4) Run the embedded tests (non-GUI):
 *     java -cp .;C:\libs\mysql-connector-java-8.0.33.jar Wedergren_Module_10_2_Assignment --test
 */
public class Wedergren_Module_10_2_Assignment {

    // --- Database configuration (change if needed) --------------------------------
    private static final String DB_URL = "jdbc:mysql://localhost:3306/databasedb?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "student1";
    private static final String DB_PASSWORD = "pass";
    // -------------------------------------------------------------------------------

    // Simple data holder class for a fan record used by both UI and tests
    private static class Fan {
        int id;
        String firstName;
        String lastName;
        String favoriteTeam;

        Fan(int id, String firstName, String lastName, String favoriteTeam) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.favoriteTeam = favoriteTeam;
        }

        @Override
        public String toString() {
            return "Fan{" + "id=" + id + ", firstName='" + firstName + '\'' + 
                    ", lastName='" + lastName + '\'' + ", favoriteTeam='" + favoriteTeam + '\'' + '}';
        }
    }

    // --- JDBC helper methods ------------------------------------------------------
    // Column name detection:
    // The database schema used by some environments has columns named
    // `first_name` / `last_name` / `favorite_team`, while others use
    // `firstname` / `lastname` / `favoriteteam`. To support both without
    // altering the database, detect which column names exist at runtime
    // and build the SQL statements dynamically.
    private static String DB_SCHEMA = "databasedb"; // change if your schema (database) name differs

    // The actual column names that will be used at runtime. These are set
    // by detectColumnNames() during static initialization.
    private static String colFirstName = "first_name";
    private static String colLastName = "last_name";
    private static String colTeam = "favorite_team";

    // SQL strings will be constructed after detection
    private static String SELECT_BY_ID_SQL;
    private static String INSERT_OR_UPDATE_SQL;

    // Attempt to load the driver class; modern drivers usually auto-register but
    // this makes behavior explicit and works across JVMs.
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // If the driver is not found you'll get clear runtime errors later;
            // keep this message to help the user understand what's missing.
            System.err.println("MySQL JDBC driver not found on classpath. Ensure the Connector/J jar is provided.");
            // don't exit here; let code run so the GUI can show informative errors
        }
        // After loading the driver, try to detect which column names the
        // `fans` table exposes and build SQL statements accordingly. Failure
        // to detect will keep the defaults (snake_case names) and the app
        // will show helpful SQL errors if the schema doesn't match.
        try {
            detectColumnNames();
        } catch (Exception ex) {
            // Do not fail startup; we will surface helpful messages when an
            // actual DB operation is attempted.
            System.err.println("Warning: could not detect column names automatically: " + ex.getMessage());
        }
    }

    /**
     * Detects which column names exist in the `fans` table and sets the
     * column variables and SQL statements accordingly.
     */
    private static void detectColumnNames() {
        // Try to open a connection using the configured DB credentials. If
        // the database is not reachable this will throw and we fall back to
        // defaults.
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Query information_schema for the columns present in the table
            String sql = "SELECT COLUMN_NAME FROM information_schema.columns WHERE table_schema = ? AND table_name = 'fans'";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, DB_SCHEMA);
                try (ResultSet rs = ps.executeQuery()) {
                    java.util.Set<String> names = new java.util.HashSet<>();
                    while (rs.next()) {
                        names.add(rs.getString(1).toLowerCase());
                    }

                    // prefer snake_case if present, otherwise fallback to compact names
                    if (names.contains("first_name")) colFirstName = "first_name";
                    else if (names.contains("firstname")) colFirstName = "firstname";

                    if (names.contains("last_name")) colLastName = "last_name";
                    else if (names.contains("lastname")) colLastName = "lastname";

                    if (names.contains("favorite_team")) colTeam = "favorite_team";
                    else if (names.contains("favoriteteam")) colTeam = "favoriteteam";

                    // construct SQL strings using the detected columns
                    SELECT_BY_ID_SQL = "SELECT id, " + colFirstName + ", " + colLastName + ", " + colTeam + " FROM fans WHERE id = ?";
                    INSERT_OR_UPDATE_SQL =
                            "INSERT INTO fans (id, " + colFirstName + ", " + colLastName + ", " + colTeam + ") VALUES (?, ?, ?, ?) " +
                                    "ON DUPLICATE KEY UPDATE " + colFirstName + " = VALUES(" + colFirstName + "), " +
                                    colLastName + " = VALUES(" + colLastName + "), " +
                                    colTeam + " = VALUES(" + colTeam + ")";
                }
            }
        } catch (SQLException ex) {
            // Propagate as runtime warning; caller already logs warnings.
            System.err.println("detectColumnNames: " + ex.getMessage());
            // Use defaults (snake_case) and construct SQL accordingly
            SELECT_BY_ID_SQL = "SELECT id, " + colFirstName + ", " + colLastName + ", " + colTeam + " FROM fans WHERE id = ?";
            INSERT_OR_UPDATE_SQL =
                    "INSERT INTO fans (id, " + colFirstName + ", " + colLastName + ", " + colTeam + ") VALUES (?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE " + colFirstName + " = VALUES(" + colFirstName + "), " +
                            colLastName + " = VALUES(" + colLastName + "), " +
                            colTeam + " = VALUES(" + colTeam + ")";
        }
    }

    /**
     * Load a Fan by id. Returns null if the row does not exist.
     * This method opens a new Connection for each call and uses try-with-resources
     * to ensure proper close semantics.
     */
    public static Fan loadFan(int id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Read columns by name to avoid ordering mistakes
                    int rid = rs.getInt("id");
                    String first = rs.getString("first_name");
                    String last = rs.getString("last_name");
                    String team = rs.getString("favorite_team");
                    return new Fan(rid, first, last, team);
                } else {
                    return null; // not found
                }
            }
        }
    }

    /**
     * Insert or update a fan record. Returns true on success.
     * This method uses INSERT ... ON DUPLICATE KEY UPDATE to avoid deleting the row.
     */
    public static boolean saveFan(Fan fan) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(INSERT_OR_UPDATE_SQL)) {
            ps.setInt(1, fan.id);
            ps.setString(2, fan.firstName);
            ps.setString(3, fan.lastName);
            ps.setString(4, fan.favoriteTeam);
            int affected = ps.executeUpdate();
            // affected is 1 for insert, 2 for update in some MySQL versions; treat >0 as success
            return affected > 0;
        }
    }
    // -------------------------------------------------------------------------------

    // --- Simple Swing UI ----------------------------------------------------------
    // The UI is intentionally minimal: a text field for ID, fields for the columns,
    // and two buttons: Display and Update.
    private JFrame frame;
    private JTextField idField;
    private JTextField firstField;
    private JTextField lastField;
    private JTextField teamField;
    private JButton displayButton;
    private JButton updateButton;

    public Wedergren_Module_10_2_Assignment() {
        frame = new JFrame("Fan Viewer / Updater");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 220);
        frame.setLayout(new BorderLayout());

        // Create form on center
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(10);
        firstField = new JTextField(20);
        lastField = new JTextField(20);
        teamField = new JTextField(20);

        int row = 0;
        addLabelAndComponent(form, "ID:", idField, c, row++);
        addLabelAndComponent(form, "First name:", firstField, c, row++);
        addLabelAndComponent(form, "Last name:", lastField, c, row++);
        addLabelAndComponent(form, "Favorite team:", teamField, c, row++);

        frame.add(form, BorderLayout.CENTER);

        // Buttons on bottom
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        displayButton = new JButton("Display");
        updateButton = new JButton("Update");
        buttons.add(displayButton);
        buttons.add(updateButton);
        frame.add(buttons, BorderLayout.SOUTH);

        // Wire actions: use background tasks so UI doesn't freeze during DB access
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDisplay();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onUpdate();
            }
        });
    }

    private static void addLabelAndComponent(JPanel panel, String labelText, JComponent comp, GridBagConstraints c, int row) {
        c.gridy = row;

        c.gridx = 0;
        c.weightx = 0;
        panel.add(new JLabel(labelText), c);

        c.gridx = 1;
        c.weightx = 1;
        panel.add(comp, c);
    }

    private void onDisplay() {
        final String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter an ID.", "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "ID must be a number.", "Bad input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Use SwingWorker to perform DB access off the EDT
        displayButton.setEnabled(false);
        updateButton.setEnabled(false);
        SwingWorker<Fan, Void> worker = new SwingWorker<Fan, Void>() {
            @Override
            protected Fan doInBackground() throws Exception {
                return loadFan(id);
            }

            @Override
            protected void done() {
                displayButton.setEnabled(true);
                updateButton.setEnabled(true);
                try {
                    Fan fan = get();
                    if (fan == null) {
                        JOptionPane.showMessageDialog(frame, "No fan with ID " + id + " found.", "Not found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        firstField.setText(fan.firstName != null ? fan.firstName : "");
                        lastField.setText(fan.lastName != null ? fan.lastName : "");
                        teamField.setText(fan.favoriteTeam != null ? fan.favoriteTeam : "");
                    }
                } catch (Exception ex) {
                    // Show a helpful message including SQL exception message if present
                    JOptionPane.showMessageDialog(frame, "Error loading fan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void onUpdate() {
        final String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter an ID.", "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        final int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "ID must be a number.", "Bad input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final Fan fan = new Fan(id, firstField.getText().trim(), lastField.getText().trim(), teamField.getText().trim());

        displayButton.setEnabled(false);
        updateButton.setEnabled(false);
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return saveFan(fan);
            }

            @Override
            protected void done() {
                displayButton.setEnabled(true);
                updateButton.setEnabled(true);
                try {
                    boolean ok = get();
                    if (ok) {
                        JOptionPane.showMessageDialog(frame, "Update succeeded.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Update reported no affected rows.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error updating fan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public void show() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // -------------------------------------------------------------------------------

    // --- Minimal test runner -------------------------------------------------------
    // The tests are purposefully simple: they insert/update a fixture row and then
    // verify that loadFan and saveFan behave as expected. Tests DO NOT create or drop the table.
    private static boolean runTests() {
        System.out.println("Running embedded tests...");
        int fixtureId = 999999; // an unlikely id for a real student row; change if conflict
        Fan initial = new Fan(fixtureId, "TestFirst", "TestLast", "TestersFC");
        try {
            // Save initial fixture (insert or update)
            boolean firstOk = saveFan(initial);
            if (!firstOk) {
                System.err.println("Failed to insert/update fixture row. Aborting tests.");
                return false;
            }

            // Load it back
            Fan loaded = loadFan(fixtureId);
            if (loaded == null) {
                System.err.println("Fixture row not found after insert.");
                return false;
            }
            if (!initial.firstName.equals(loaded.firstName) || !initial.lastName.equals(loaded.lastName) || !initial.favoriteTeam.equals(loaded.favoriteTeam)) {
                System.err.println("Loaded values do not match inserted ones: " + loaded);
                return false;
            }

            // Update the fixture
            Fan updated = new Fan(fixtureId, "ChangedFirst", "ChangedLast", "ChangedUnited");
            boolean updOk = saveFan(updated);
            if (!updOk) {
                System.err.println("Failed to update fixture row.");
                return false;
            }
            Fan reloaded = loadFan(fixtureId);
            if (reloaded == null) {
                System.err.println("Fixture row missing after update.");
                return false;
            }
            if (!updated.firstName.equals(reloaded.firstName) || !updated.lastName.equals(reloaded.lastName) || !updated.favoriteTeam.equals(reloaded.favoriteTeam)) {
                System.err.println("Reloaded values don't match updated ones: " + reloaded);
                return false;
            }

            System.out.println("All tests passed.");
            return true;
        } catch (SQLException ex) {
            System.err.println("SQL error during tests: " + ex.getMessage());
            ex.printStackTrace(System.err);
            return false;
        }
    }
    // -------------------------------------------------------------------------------

    // --- Main ----------------------------------------------------------------------
    public static void main(String[] args) {
        // If the user passes --test run the non-GUI tests and exit with code 0/1
        if (args != null && args.length > 0 && "--test".equals(args[0])) {
            boolean ok = runTests();
            System.exit(ok ? 0 : 1);
            return;
        }

        // Otherwise start the Swing GUI
        SwingUtilities.invokeLater(() -> {
            Wedergren_Module_10_2_Assignment app = new Wedergren_Module_10_2_Assignment();
            app.show();
        });
    }
}
