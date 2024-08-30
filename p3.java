import java.sql.*;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Stack;


public class p3 {
    static Connection conn = DatabaseConnection.connect();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Check if the connection was successful
        if (conn == null) {
            System.err.println("Failed to connect to the database. Exiting...");
            System.exit(1);
        }

        // Main loop
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void registerUser() {
        System.out.print("Enter First Name: ");
        String firstName = sc.next();
        System.out.print("Enter Last Name: ");
        String lastName = sc.next();
        String mobileNumber;
        do {
            System.out.print("Enter Mobile Number (10 digits): ");
            mobileNumber = sc.next();
        } while (!isValidMobileNumber(mobileNumber));
        
        String email;
        do {
            System.out.print("Enter Email: ");
            email = sc.next();
        } while (!isValidEmail(email));
        
        System.out.print("Enter Password: ");
        String password = sc.next();

        try {
            String query = "INSERT INTO Users (firstName, lastName, mobileNumber, email, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, mobileNumber);
            pstmt.setString(4, email);
            pstmt.setString(5, password);
            pstmt.executeUpdate();
            System.out.println("Registration Successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loginUser() {
        System.out.print("Enter Email: ");
        String email = sc.next();
        System.out.print("Enter Password: ");
        String password = sc.next();

        try {
            String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("mobileNumber"), rs.getString("email"), rs.getString("password"));
                System.out.println("Login Successful! Welcome " + user.getFirstName());
                displayUserMenu(user);
            } else {
                System.out.println("Invalid Credentials!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayUserMenu(User user) {
        while (true) {
            System.out.println("1. Book Bus");
            System.out.println("2. View Booking History");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Update Booking");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    bookBus(user);
                    break;
                case 2:
                    viewBookingHistory(user);
                    break;
                case 3:
                    cancelBooking(user);
                    break;
                case 4:
                    updateBooking(user);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void bookBus(User user) {
        showAvailableBuses();
        System.out.print("Enter Bus Number: ");
        String busNumber = sc.next();
        System.out.print("Enter Seat Number: ");
        String seatNumber = sc.next();
        System.out.print("Enter Payment Status (Paid/Unpaid): ");
        String paymentStatus = sc.next();

        try {
            String query = "INSERT INTO Bookings (userId, busNumber, seatNumber, paymentStatus) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user.getId());
            pstmt.setString(2, busNumber);
            pstmt.setString(3, seatNumber);
            pstmt.setString(4, paymentStatus);
            pstmt.executeUpdate();
            System.out.println("Booking Successful!");

            // Show ticket
            Booking booking = new Booking(user.getId(), busNumber, seatNumber, paymentStatus);
            showTicket(user, booking);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showTicket(User user, Booking booking) {
        System.out.println("\n===============================");
        System.out.println("        BUS TICKET");
        System.out.println("===============================");
        System.out.println("Passenger: " + user.getFirstName() + " " + user.getFirstName());
        System.out.println("Mobile: " + user.getMobileNumber());
        System.out.println("-------------------------------");
        System.out.println("Bus Number: " + booking.getBusNumber());
        System.out.println("Seat Number: " + booking.getSeatNumber());
        System.out.println("Payment Status: " + booking.getPaymentStatus());
        System.out.println("===============================");
        System.out.println("  THANK YOU FOR YOUR JOURNEY!");
        System.out.println("===============================\n");
    }

    public static void viewBookingHistory(User user) {
        System.out.println("Your Booking History:");
        try {
            String query = "SELECT * FROM Bookings WHERE userId = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("Bus Number: " + rs.getString("busNumber") + ", Seat Number: " + rs.getString("seatNumber") + 
                                   ", Payment Status: " + rs.getString("paymentStatus"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cancelBooking(User user) {
        System.out.print("Enter Booking ID to Cancel: ");
        int bookingId = sc.nextInt();

        try {
            String query = "DELETE FROM Bookings WHERE id = ? AND userId = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, bookingId);
            pstmt.setInt(2, user.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Booking Cancelled Successfully!");
            } else {
                System.out.println("Booking ID not found or you don't have permission to cancel this booking.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBooking(User user) {
        System.out.print("Enter Booking ID to Update: ");
        int bookingId = sc.nextInt();

        System.out.print("Enter New Seat Number: ");
        String newSeatNumber = sc.next();

        System.out.print("Enter New Payment Status (Paid/Unpaid): ");
        String newPaymentStatus = sc.next();

        try {
            String query = "UPDATE Bookings SET seatNumber = ?, paymentStatus = ? WHERE id = ? AND userId = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, newSeatNumber);
            pstmt.setString(2, newPaymentStatus);
            pstmt.setInt(3, bookingId);
            pstmt.setInt(4, user.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Booking Updated Successfully!");
            } else {
                System.out.println("Booking ID not found or you don't have permission to update this booking.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showAvailableBuses() {
        System.out.println("Available Buses:");
        try {
            String query = "SELECT DISTINCT busNumber FROM Bookings";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println("Bus Number: " + rs.getString("busNumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidMobileNumber(String mobileNumber) {
        // Mobile number should be exactly 10 digits
        return mobileNumber.matches("\\d{10}");
    }

    public static boolean isValidEmail(String email) {
        // Basic email validation
        return email != null && email.contains("@") && email.indexOf("@") < email.lastIndexOf(".");
    }
}
