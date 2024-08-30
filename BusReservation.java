
// import java.sql.*;
// import java.util.Scanner;
// import java.io.FileWriter;
// import java.io.IOException;

// public class BusReservationSystem {

//     static final String DB_URL = "jdbc:mysql://localhost:3306/bus_reservation";
//     static final String USER = "root"; // Default MySQL username
//     static final String PASS = ""; // Default MySQL password

//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

//             System.out.println("1. Register\n2. Login\n3. Admin Login\n4. exit");
//             int choice = sc.nextInt();
// boolean x = true;
// while(x){
//  switch (choice) {
//                 case 1:
//                     registerUser(conn, sc);
//                     break;
//                 case 2:
//                     userLogin(conn, sc);
//                     break;
//                 case 3:
//                     adminLogin(conn, sc);
//                     break;
//                 case 4:
//                     x = false;    
//                 default:
//                     System.out.println("Invalid choice!");
//             }
// }
           

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     public static void registerUser(Connection conn, Scanner sc) throws SQLException {
//         System.out.print("Enter username: ");
//         String username = sc.next();
//         System.out.print("Enter password: ");
//         String password = sc.next();

//         String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
//         PreparedStatement pstmt = conn.prepareStatement(sql);
//         pstmt.setString(1, username);
//         pstmt.setString(2, password);
//         pstmt.executeUpdate();

//         System.out.println("User registered successfully!");
//     }

//     public static void userLogin(Connection conn, Scanner sc) throws SQLException {
//         System.out.print("Enter username: ");
//         String username = sc.next();
//         System.out.print("Enter password: ");
//         String password = sc.next();

//         String sql = "SELECT * FROM users WHERE username=? AND password=?";
//         PreparedStatement pstmt = conn.prepareStatement(sql);
//         pstmt.setString(1, username);
//         pstmt.setString(2, password);
//         ResultSet rs = pstmt.executeQuery();

//         if (rs.next()) {
//             System.out.println("Login successful!");
//             bookTicket(conn, sc, rs.getInt("user_id"));
//         } else {
//             System.out.println("Invalid credentials!");
//         }
//     }

//     public static void adminLogin(Connection conn, Scanner sc) throws SQLException {
//         System.out.print("Enter admin username: ");
//         String username = sc.next();
//         System.out.print("Enter admin password: ");
//         String password = sc.next();

//         String sql = "SELECT * FROM admins WHERE username=? AND password=?";
//         PreparedStatement pstmt = conn.prepareStatement(sql);
//         pstmt.setString(1, username);
//         pstmt.setString(2, password);
//         ResultSet rs = pstmt.executeQuery();

//         if (rs.next()) {
//             System.out.println("Admin login successful!");
//             adminPanel(conn, sc);
//         } else {
//             System.out.println("Invalid admin credentials!");
//         }
//     }

//     public static void adminPanel(Connection conn, Scanner sc) throws SQLException {
//         System.out.println("1. Add Bus Route\n2. View All Routes");
//         int choice = sc.nextInt();

//         switch (choice) {
//             case 1:
//                 addBusRoute(conn, sc);
//                 break;
//             case 2:
//                 viewAllRoutes(conn);
//                 break;
//             default:
//                 System.out.println("Invalid choice!");
//         }
//     }

//     public static void addBusRoute(Connection conn, Scanner sc) throws SQLException {
//         System.out.print("Enter route: ");
//         String route = sc.next();
//         System.out.print("Enter bus type: ");
//         String busType = sc.next();
//         System.out.print("Enter departure time: ");
//         String departureTime = sc.next();
//         System.out.print("Enter arrival time: ");
//         String arrivalTime = sc.next();

//         String sql = "INSERT INTO bus_routes (route, bus_type, departure_time, arrival_time) VALUES (?, ?, ?, ?)";
//         PreparedStatement pstmt = conn.prepareStatement(sql);
//         pstmt.setString(1, route);
//         pstmt.setString(2, busType);
//         pstmt.setString(3, departureTime);
//         pstmt.setString(4, arrivalTime);
//         pstmt.executeUpdate();

//         System.out.println("Bus route added successfully!");
//     }

//     public static void viewAllRoutes(Connection conn) throws SQLException {
//         String sql = "SELECT * FROM bus_routes";
//         Statement stmt = conn.createStatement();
//         ResultSet rs = stmt.executeQuery(sql);

//         System.out.println("Available Bus Routes:");
//         while (rs.next()) {
//             System.out.println("Route ID: " + rs.getInt("route_id") + ", Route: " + rs.getString("route") + ", Bus Type: " + rs.getString("bus_type") + ", Departure: " + rs.getString("departure_time") + ", Arrival: " + rs.getString("arrival_time"));
//         }
//     }

//     public static void bookTicket(Connection conn, Scanner sc, int userId) throws SQLException {
//         viewAllRoutes(conn);
//         System.out.print("Enter route ID to book: ");
//         int routeId = sc.nextInt();

//         System.out.println("Choose Payment Method: 1. Credit Card 2. Debit Card 3. UPI");
//         int paymentChoice = sc.nextInt();
//         String paymentMethod = "";
//         switch (paymentChoice) {
//             case 1:
//                 paymentMethod = "Credit Card";
//                 break;
//             case 2:
//                 paymentMethod = "Debit Card";
//                 break;
//             case 3:
//                 paymentMethod = "UPI";
//                 break;
//             default:
//                 System.out.println("Invalid payment method!");
//                 return;
//         }

//         System.out.print("Enter amount: ");
//         double amount = sc.nextDouble();

//         String sql = "INSERT INTO tickets (user_id, route_id, payment_method, amount) VALUES (?, ?, ?, ?)";
//         PreparedStatement pstmt = conn.prepareStatement(sql);
//         pstmt.setInt(1, userId);
//         pstmt.setInt(2, routeId);
//         pstmt.setString(3, paymentMethod);
//         pstmt.setDouble(4, amount);
//         pstmt.executeUpdate();

//         System.out.println("Ticket booked successfully!");
//         writeTicketToFile(conn, userId, routeId);
//     }

//     public static void writeTicketToFile(Connection conn, int userId, int routeId) {
//         String sql = "SELECT * FROM tickets INNER JOIN bus_routes ON tickets.route_id = bus_routes.route_id WHERE tickets.user_id=? AND tickets.route_id=?";
//         try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//             pstmt.setInt(1, userId);
//             pstmt.setInt(2, routeId);
//             ResultSet rs = pstmt.executeQuery();

//             if (rs.next()) {
//                 try (FileWriter fw = new FileWriter("ticket.txt")) {
//                     fw.write("Ticket ID: " + rs.getInt("ticket_id") + "\n");
//                     fw.write("User ID: " + rs.getInt("user_id") + "\n");
//                     fw.write("Route: " + rs.getString("route") + "\n");
//                     fw.write("Bus Type: " + rs.getString("bus_type") + "\n");
//                     fw.write("Departure: " + rs.getString("departure_time") + "\n");
//                     fw.write("Arrival: " + rs.getString("arrival_time") + "\n");
//                     fw.write("Payment Method: " + rs.getString("payment_method") + "\n");
//                     fw.write("Amount: " + rs.getDouble("amount") + "\n");
//                     System.out.println("Ticket details written to ticket.txt file.");
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//             }

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
// }








////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//CREATE DATABASE bus_reservation;

//USE bus_reservation;

// CREATE TABLE users (
//     user_id INT AUTO_INCREMENT PRIMARY KEY,
//     username VARCHAR(50) NOT NULL,
//     password VARCHAR(50) NOT NULL
// );

// CREATE TABLE admins (
//     admin_id INT AUTO_INCREMENT PRIMARY KEY,
//     username VARCHAR(50) NOT NULL,
//     password VARCHAR(50) NOT NULL
// );

// CREATE TABLE bus_routes (
//     route_id INT AUTO_INCREMENT PRIMARY KEY,
//     route VARCHAR(100) NOT NULL,
//     bus_type VARCHAR(50) NOT NULL,
//     departure_time VARCHAR(50) NOT NULL,
//     arrival_time VARCHAR(50) NOT NULL
// );

// CREATE TABLE tickets (
//     ticket_id INT AUTO_INCREMENT PRIMARY KEY,
//     user_id INT,
//     route_id INT,
//     payment_method VARCHAR(50),
//     amount DOUBLE,
//     FOREIGN KEY (user_id) REFERENCES users(user_id),
//     FOREIGN KEY (route_id) REFERENCES bus_routes(route_id)
// );


import java.sql.*;
import java.util.*;
import java.io.*;

public class BusReservation {
    static Connection connection;

    // Custom LinkedList
    static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    static class LinkedList<T> {
        private Node<T> head;

        public LinkedList() {
            head = null;
        }

        public void add(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = newNode;
            } else {
                Node<T> current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
        }

        public T get(int index) {
            Node<T> current = head;
            int count = 0;
            while (current != null) {
                if (count == index) {
                    return current.data;
                }
                count++;
                current = current.next;
            }
            return null;
        }

        public boolean isEmpty() {
            return head == null;
        }
    }

    // Custom Stack
    static class Stack<T> {
        private LinkedList<T> list = new LinkedList<>();

        public void push(T item) {
            list.add(item);
        }

        public T pop() {
            if (list.isEmpty()) {
                throw new EmptyStackException();
            }
            T data = list.get(0); // Assuming the last item is popped
            return data;
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

    // Custom Queue
    static class Queue<T> {
        private LinkedList<T> list = new LinkedList<>();

        public void enqueue(T item) {
            list.add(item);
        }

        public T dequeue() {
            if (list.isEmpty()) {
                throw new NoSuchElementException();
            }
            T data = list.get(0); // Assuming the first item is dequeued
            return data;
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

    // Custom BinarySearchTree
    static class TreeNode<T extends Comparable<T>> {
        T data;
        TreeNode<T> left, right;

        TreeNode(T data) {
            this.data = data;
            this.left = this.right = null;
        }
    }

    static class BinarySearchTree<T extends Comparable<T>> {
        private TreeNode<T> root;

        public BinarySearchTree() {
            root = null;
        }

        public void insert(T data) {
            root = insertRec(root, data);
        }

        private TreeNode<T> insertRec(TreeNode<T> root, T data) {
            if (root == null) {
                root = new TreeNode<>(data);
                return root;
            }
            if (data.compareTo(root.data) < 0) {
                root.left = insertRec(root.left, data);
            } else if (data.compareTo(root.data) > 0) {
                root.right = insertRec(root.right, data);
            }
            return root;
        }

        public boolean search(T data) {
            return searchRec(root, data) != null;
        }

        private TreeNode<T> searchRec(TreeNode<T> root, T data) {
            if (root == null || root.data.equals(data)) {
                return root;
            }
            if (data.compareTo(root.data) < 0) {
                return searchRec(root.left, data);
            }
            return searchRec(root.right, data);
        }
    }

    public static void main(String[] args) {
        try {
            // Connect to MySQL database using XAMPP
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus_reservation", "root", "");

            Scanner sc = new Scanner(System.in);
            LinkedList<String> busList = new LinkedList<>();
            Stack<String> userActions = new Stack<>();
            Queue<String> reservationQueue = new Queue<>();
            BinarySearchTree<Integer> busIdsTree = new BinarySearchTree<>();

            while (true) {
                System.out.println("1. Admin Login");
                System.out.println("2. User Signup");
                System.out.println("3. User Login");
                System.out.println("4. Exit");
                int choice = sc.nextInt();
                sc.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        adminLogin(sc);
                        break;
                    case 2:
                        userSignup(sc);
                        break;
                    case 3:
                        userLogin(sc);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void adminLogin(Scanner sc) throws SQLException {
        System.out.print("Enter admin username: ");
        String username = sc.nextLine();
        System.out.print("Enter admin password: ");
        String password = sc.nextLine();

        String query = "SELECT * FROM Admin WHERE username=? AND password=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("Admin login successful!");
            adminMenu(sc);
        } else {
            System.out.println("Incorrect username or password.");
        }
    }

    public static void adminMenu(Scanner sc) throws SQLException {
        while (true) {
            System.out.println("1. Add Bus");
            System.out.println("2. Add Bus Route");
            System.out.println("3. Add Bus Timing");
            System.out.println("4. Add Distance Between Stations");
            System.out.println("5. Add Available Seats");
            System.out.println("6. See Reservation Status");
            System.out.println("7. Logout");

            int choice = sc.nextInt();
            sc.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addBus(sc);
                    break;
                case 2:
                    addBusRoute(sc);
                    break;
                case 3:
                    addBusTiming(sc);
                    break;
                case 4:
                    addDistance(sc);
                    break;
                case 5:
                    addSeats(sc);
                    break;
                case 6:
                    seeReservationStatus(sc);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void addBus(Scanner sc) throws SQLException {
        System.out.print("Enter bus name: ");
        String busName = sc.nextLine();
        System.out.print("Enter start station: ");
        String startStation = sc.nextLine();
        System.out.print("Enter end station: ");
        String endStation = sc.nextLine();

        String query = "INSERT INTO Bus (bus_name, start_station, end_station) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, busName);
        pstmt.setString(2, startStation);
        pstmt.setString(3, endStation);
        pstmt.executeUpdate();

        System.out.println("Bus added successfully!");
    }

    public static void addBusRoute(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  // Consume newline
        System.out.print("Enter start station: ");
        String startStation = sc.nextLine();
        System.out.print("Enter end station: ");
        String endStation = sc.nextLine();

        String query = "UPDATE Bus SET start_station=?, end_station=? WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, startStation);
        pstmt.setString(2, endStation);
        pstmt.setInt(3, busId);
        pstmt.executeUpdate();

        System.out.println("Bus route updated successfully!");
    }

    public static void addBusTiming(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  // Consume newline
        System.out.print("Enter bus timing (HH:MM): ");
        String timing = sc.nextLine();

        String query = "UPDATE Bus SET timing=? WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, timing);
        pstmt.setInt(2, busId);
        pstmt.executeUpdate();

        System.out.println("Bus timing updated successfully!");
    }

    public static void addDistance(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  // Consume newline
        System.out.print("Enter distance in km: ");
        int distance = sc.nextInt();
        sc.nextLine();  // Consume newline

        String query = "UPDATE Bus SET distance_km=? WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, distance);
        pstmt.setInt(2, busId);
        pstmt.executeUpdate();

        System.out.println("Distance updated successfully!");
    }

    public static void addSeats(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  // Consume newline
        System.out.print("Enter number of available seats: ");
        int seats = sc.nextInt();
        sc.nextLine();  // Consume newline

        String query = "UPDATE Bus SET available_seats=? WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, seats);
        pstmt.setInt(2, busId);
        pstmt.executeUpdate();

        System.out.println("Available seats updated successfully!");
    }

    public static void seeReservationStatus(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  // Consume newline

        String query = "SELECT * FROM Reservation WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, busId);
        ResultSet rs = pstmt.executeQuery();

        System.out.println("Reservation Status for Bus ID: " + busId);
        while (rs.next()) {
            System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
            System.out.println("Passenger Name: " + rs.getString("passenger_name"));
            System.out.println("Seat Number: " + rs.getInt("seat_number"));
            System.out.println("---------------");
        }
    }

    public static void userSignup(Scanner sc) throws SQLException {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        String query = "INSERT INTO Users (username, password, email, phone) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, email);
        pstmt.setString(4, phone);
        pstmt.executeUpdate();

        System.out.println("User signup successful!");
    }

    public static void userLogin(Scanner sc) throws SQLException {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        String query = "SELECT * FROM Users WHERE username=? AND password=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("User login successful!");
            userMenu(sc, rs.getInt("user_id"));
        } else {
            System.out.println("Incorrect username or password.");
        }
    }

    public static void userMenu(Scanner sc, int userId) throws SQLException {
        while (true) {
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. View Bus Details");
            System.out.println("4. Logout");

            int choice = sc.nextInt();
            sc.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    bookTicket(sc, userId);
                    break;
                case 2:
                    cancelTicket(sc, userId);
                    break;
                case 3:
                    viewBusDetails(sc);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void bookTicket(Scanner sc, int userId) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  // Consume newline
        System.out.print("Enter passenger name: ");
        String passengerName = sc.nextLine();
        System.out.print("Enter seat number: ");
        int seatNumber = sc.nextInt();
        sc.nextLine();  // Consume newline

        String query = "INSERT INTO Reservation (user_id, bus_id, passenger_name, seat_number) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, userId);
        pstmt.setInt(2, busId);
        pstmt.setString(3, passengerName);
        pstmt.setInt(4, seatNumber);
        pstmt.executeUpdate();

        System.out.println("Ticket booked successfully!");
    }

    public static void cancelTicket(Scanner sc, int userId) throws SQLException {
        System.out.print("Enter reservation ID: ");
        int reservationId = sc.nextInt();
        sc.nextLine();  // Consume newline

        String query = "DELETE FROM Reservation WHERE reservation_id=? AND user_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, reservationId);
        pstmt.setInt(2, userId);
        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Ticket cancelled successfully!");
        } else {
            System.out.println("No such reservation found.");
        }
    }

    public static void viewBusDetails(Scanner sc) throws SQLException {
        String query = "SELECT * FROM Bus";
        PreparedStatement pstmt = connection.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            System.out.println("Bus ID: " + rs.getInt("bus_id"));
            System.out.println("Bus Name: " + rs.getString("bus_name"));
            System.out.println("Start Station: " + rs.getString("start_station"));
            System.out.println("End Station: " + rs.getString("end_station"));
            System.out.println("Timing: " + rs.getString("timing"));
            System.out.println("Distance (in km): " + rs.getInt("distance_km"));
            System.out.println("Available Seats: " + rs.getInt("available_seats"));
            System.out.println("---------------");
        }
    }
}
