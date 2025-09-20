package passwordEvaluationTestbed;

import app.auth.AuthSession;
import java.util.Scanner;

public class PasswordEvaluationTestingAutomation {

    // counters for the summary/footer
    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // start with a demo login so the menu is usable on first run
        AuthSession.get().login("demoUser", AuthSession.Role.USER);
        System.out.println("Logged in as: " + AuthSession.get().getUsername()
                + "  (role=" + AuthSession.get().getRole() + ")");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1) Run password tests");
            System.out.println("2) Logout (optionally login again)");
            System.out.println("3) Exit");
            System.out.println("4) Change role");
            System.out.print("Choose: ");
            String c = in.nextLine().trim();

            if ("1".equals(c)) {
                if (!AuthSession.get().isLoggedIn()) {
                    System.out.println("Please login first (use option 2 to login).");
                    continue;
                }
                if (AuthSession.get().getRole() == AuthSession.Role.GUEST) {
                    System.out.println("Guests can't run tests. Use option 4 to change role to USER/ADMIN.");
                    continue;
                }
                numPassed = 0;
                numFailed = 0;
                runAllTests();
            } else if ("2".equals(c)) {
                AuthSession.get().logout();
                System.out.println("You are now logged out.");
                System.out.print("Enter username to login again (or press Enter to stay logged out): ");
                String u = in.nextLine().trim();
                if (!u.isEmpty()) {
                    AuthSession.get().login(u, AuthSession.Role.USER);
                    System.out.println("Logged in as: " + AuthSession.get().getUsername()
                            + "  (role=" + AuthSession.get().getRole() + ")");
                }
            } else if ("3".equals(c)) {
                break; // exit
            } else if ("4".equals(c)) {
                System.out.print("Enter role (ADMIN/USER/GUEST): ");
                String r = in.nextLine().trim().toUpperCase();
                try {
                    AuthSession.Role role = AuthSession.Role.valueOf(r);
                    if (!AuthSession.get().isLoggedIn()) {
                        System.out.print("Enter username to login with this role: ");
                        String u = in.nextLine().trim();
                        if (u.isEmpty()) {
                            System.out.println("No username entered; staying logged out.");
                            continue;
                        }
                        AuthSession.get().login(u, role);
                    } else {
                        AuthSession.get().login(AuthSession.get().getUsername(), role);
                    }
                    System.out.println("Role set to: " + AuthSession.get().getRole());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid role.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    // header -> run all test cases -> footer
    private static void runAllTests() {
        System.out.println("\n************ Test cases semi-automation report header ************");
        System.out.println("\nTesting Automation");

        // positive (should pass)
        performTestCase(1,  "Mustafa9!",        true);
        performTestCase(2,  "LeoRocks9$",       true);
        performTestCase(6,  "Leo1225m",         true);

        // negative (should fail)
        performTestCase(3,  "A",                false);
        performTestCase(4,  "",                 false);
        performTestCase(5,  "password9",        false);
        performTestCase(7,  "Mustafa!",         false);
        performTestCase(8,  "LeoRocks9",        false);
        performTestCase(9,  "Leo<9",            false);
        performTestCase(10, "mustafa_leo_2025!",false);

        System.out.println("\n************ Test cases semi-automation report footer ************");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    // Minimal stub so this file compiles; replace with your real evaluator if you have it.
    private static void performTestCase(int testCase, String inputText, boolean expectedPass) {
        boolean actual = inputText != null && inputText.length() >= 8; // placeholder
        if (actual == expectedPass) numPassed++; else numFailed++;
        System.out.printf("Test case %d: input=\"%s\" -> expected=%s, actual=%s%n",
                testCase, inputText, expectedPass, actual);
    }
}
