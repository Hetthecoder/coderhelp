


import java.sql.*;
import java.util.*;
import java.io.*;

public class bus {
    static Connection connection;

   
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
    
    static class Stack<T> {
        private LinkedList<T> list = new LinkedList<>();

        public void push(T item) {
            list.add(item);
        }

        public T pop() {
            if (list.isEmpty()) {
                throw new EmptyStackException();
            }
            T data = list.get(0);
            return data;
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

    static class Queue<T> {
        private LinkedList<T> list = new LinkedList<>();

        public void enqueue(T item) {
            list.add(item);
        }

        public T dequeue() {
            if (list.isEmpty()) {
                throw new NoSuchElementException();
            }
            T data = list.get(0);
            return data;
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

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
           
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus22", "root", "");

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
                sc.nextLine();  

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
            System.out.println("1. Add bus");
            System.out.println("2. Add bus Path");
            System.out.println("3. Add bus Timing");
            System.out.println("4. Add Distance Between Stations");
            System.out.println("5. Add Available Seats");
            System.out.println("6. See Reservation Status");
            System.out.println("7. Logout");

            int choice = sc.nextInt();
            sc.nextLine();  

            switch (choice) {
                case 1:
                    addbus(sc);
                    break;
                case 2:
                    addbusPath(sc);
                    break;
                case 3:
                    addbusTiming(sc);
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

    public static void addbus(Scanner sc) throws SQLException {
        System.out.print("Enter bus name: ");
        String busName = sc.nextLine();
        System.out.print("Enter start station: ");
        String startStation = sc.nextLine();
        System.out.print("Enter end station: ");
        String endStation = sc.nextLine();

        String query = "INSERT INTO bus (bus_name, start_station, end_station) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, busName);
        pstmt.setString(2, startStation);
        pstmt.setString(3, endStation);
        pstmt.executeUpdate();

        System.out.println("buss added successfully!");
    }

    public static void addbusPath(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  
        System.out.print("Enter start station: ");
        String startStation = sc.nextLine();
        System.out.print("Enter end station: ");
        String endStation = sc.nextLine();

        String query = "UPDATE bus SET start_station=?, end_station=? WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, startStation);
        pstmt.setString(2, endStation);
        pstmt.setInt(3, busId);
        pstmt.executeUpdate();

        System.out.println("bus path updated successfully!");
    }

    public static void addbusTiming(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  // Consume newline
        System.out.print("Enter bus timing (HH:MM): ");
        String timing = sc.nextLine();

        String query = "UPDATE bus SET timing=? WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, timing);
        pstmt.setInt(2, busId);
        pstmt.executeUpdate();

        System.out.println("bus timing updated successfully!");
    }

    public static void addDistance(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  
        System.out.print("Enter distance in km: ");
        int distance = sc.nextInt();
        sc.nextLine();  

        String query = "UPDATE bus SET distance_km=? WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, distance);
        pstmt.setInt(2, busId);
        pstmt.executeUpdate();

        System.out.println("Distance updated successfully!");
    }

    public static void addSeats(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  
        System.out.print("Enter number of available seats: ");
        int seats = sc.nextInt();
        sc.nextLine();  

        String query = "UPDATE bus SET available_seats=? WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, seats);
        pstmt.setInt(2, busId);
        pstmt.executeUpdate();

        System.out.println("Seats updated successfully!");
    }

    public static void seeReservationStatus(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  

        String query = "SELECT * FROM Reservations WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, busId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
            System.out.println("User ID: " + rs.getInt("user_id"));
            System.out.println("Date: " + rs.getDate("date"));
        }
    }

    public static void userSignup(Scanner sc) throws SQLException {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        String query = "INSERT INTO Users (username, password) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.executeUpdate();

        System.out.println("User signed up successfully!");
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
            userMenu(sc);
        } else {
            System.out.println("Incorrect username or password.");
        }
    }

    public static void userMenu(Scanner sc) throws SQLException {
        while (true) {
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. View Train Details");
            System.out.println("4. Logout");

            int choice = sc.nextInt();
            sc.nextLine();  

            switch (choice) {
                case 1:
                    bookTicket(sc);
                    break;
                case 2:
                    cancelTicket(sc);
                    break;
                case 3:
                    viewbusDetails(sc);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

   

    public static void bookTicket(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter user ID: ");
        int userId = sc.nextInt();
        sc.nextLine();
    
    
       
        String distanceQuery = "SELECT distance_km FROM bus WHERE bus_id = ?";
        PreparedStatement distanceStmt = connection.prepareStatement(distanceQuery);
        distanceStmt.setInt(1, busId);
        ResultSet rs = distanceStmt.executeQuery();
    
        int distance = 0;
        if (rs.next()) {
            distance = rs.getInt("distance_km");
        }
    
       
        int price = distance * 80;
    
        
        String query = "INSERT INTO Reservations (bus_id, user_id, date, price) VALUES (?, ?, NOW(), ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, busId);
        pstmt.setInt(2, userId);
        pstmt.setInt(3, price);
        pstmt.executeUpdate();
    
        System.out.println("Ticket booked successfully! Price: " + price);
    }
    

    public static void cancelTicket(Scanner sc) throws SQLException {
        System.out.print("Enter reservation ID: ");
        int reservationId = sc.nextInt();
        sc.nextLine();  

        String query = "DELETE FROM Reservations WHERE reservation_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, reservationId);
        pstmt.executeUpdate();

        System.out.println("Ticket canceled successfully!");
    }

    public static void viewbusDetails(Scanner sc) throws SQLException {
        System.out.print("Enter bus ID: ");
        int busId = sc.nextInt();
        sc.nextLine();  
        String query = "SELECT * FROM bus WHERE bus_id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, busId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("bus ID: " + rs.getInt("bus_id"));
            System.out.println("bus Name: " + rs.getString("bus_name"));
            System.out.println("Start Station: " + rs.getString("start_station"));
            System.out.println("End Station: " + rs.getString("end_station"));
            System.out.println("Available Seats: " + rs.getInt("available_seats"));
            System.out.println("Distance: " + rs.getInt("distance_km") + " km");
        } else {
            System.out.println("No details found for this train.");
        }
    }
}


// import java.sql.*;
// import java.util.*;
// import java.io.*;

//  class BusReservation {
//     static Connection connection;

//     // Custom LinkedList
//     static class Node<T> {
//         T data;
//         Node<T> next;

//         Node(T data) {
//             this.data = data;
//             this.next = null;
//         }
//     }

//     static class LinkedList<T> {
//         private Node<T> head;

//         public LinkedList() {
//             head = null;
//         }

//         public void add(T data) {
//             Node<T> newNode = new Node<>(data);
//             if (head == null) {
//                 head = newNode;
//             } else {
//                 Node<T> current = head;
//                 while (current.next != null) {
//                     current = current.next;
//                 }
//                 current.next = newNode;
//             }
//         }

//         public T get(int index) {
//             Node<T> current = head;
//             int count = 0;
//             while (current != null) {
//                 if (count == index) {
//                     return current.data;
//                 }
//                 count++;
//                 current = current.next;
//             }
//             return null;
//         }

//         public boolean isEmpty() {
//             return head == null;
//         }
//     }

//     // Custom Stack
//     static class Stack<T> {
//         private LinkedList<T> list = new LinkedList<>();

//         public void push(T item) {
//             list.add(item);
//         }

//         public T pop() {
//             if (list.isEmpty()) {
//                 throw new EmptyStackException();
//             }
//             T data = list.get(0); // Assuming the last item is popped
//             return data;
//         }

//         public boolean isEmpty() {
//             return list.isEmpty();
//         }
//     }

//     // Custom Queue
//     static class Queue<T> {
//         private LinkedList<T> list = new LinkedList<>();

//         public void enqueue(T item) {
//             list.add(item);
//         }

//         public T dequeue() {
//             if (list.isEmpty()) {
//                 throw new NoSuchElementException();
//             }
//             T data = list.get(0); // Assuming the first item is dequeued
//             return data;
//         }

//         public boolean isEmpty() {
//             return list.isEmpty();
//         }
//     }

//     // Custom BinarySearchTree
//     static class TreeNode<T extends Comparable<T>> {
//         T data;
//         TreeNode<T> left, right;

//         TreeNode(T data) {
//             this.data = data;
//             this.left = this.right = null;
//         }
//     }

//     static class BinarySearchTree<T extends Comparable<T>> {
//         private TreeNode<T> root;

//         public BinarySearchTree() {
//             root = null;
//         }

//         public void insert(T data) {
//             root = insertRec(root, data);
//         }

//         private TreeNode<T> insertRec(TreeNode<T> root, T data) {
//             if (root == null) {
//                 root = new TreeNode<>(data);
//                 return root;
//             }
//             if (data.compareTo(root.data) < 0) {
//                 root.left = insertRec(root.left, data);
//             } else if (data.compareTo(root.data) > 0) {
//                 root.right = insertRec(root.right, data);
//             }
//             return root;
//         }

//         public boolean search(T data) {
//             return searchRec(root, data) != null;
//         }

//         private TreeNode<T> searchRec(TreeNode<T> root, T data) {
//             if (root == null || root.data.equals(data)) {
//                 return root;
//             }
//             if (data.compareTo(root.data) < 0) {
//                 return searchRec(root.left, data);
//             }
//             return searchRec(root.right, data);
//         }
//     }

//     public static void main(String[] args) {
//         try {
//             // Connect to MySQL database using XAMPP
//             connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus", "root", "");

//             Scanner sc = new Scanner(System.in);
//             LinkedList<String> busList = new LinkedList<>();
//             Stack<String> userActions = new Stack<>();
//             Queue<String> reservationQueue = new Queue<>();
//             BinarySearchTree<Integer> busIdsTree = new BinarySearchTree<>();

//             while (true) {
//                 System.out.println("1. Admin Login");
//                 System.out.println("2. User Signup");
//                 System.out.println("3. User Login");
//                 System.out.println("4. Exit");
//                 int choice = sc.nextInt();
//                 sc.nextLine();  // Consume newline

//                 switch (choice) {
//                     case 1:
//                         adminLogin(sc);
//                         break;
//                     case 2:
//                         userSignup(sc);
//                         break;
//                     case 3:
//                         userLogin(sc);
//                         break;
//                     case 4:
//                         return;
//                     default:
//                         System.out.println("Invalid choice. Please try again.");
//                 }
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     public static void adminLogin(Scanner sc) throws SQLException {
//         System.out.print("Enter admin username: ");
//         String username = sc.nextLine();
//         System.out.print("Enter admin password: ");
//         String password = sc.nextLine();

//         String query = "SELECT * FROM Admin WHERE username=? AND password=?";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setString(1, username);
//         pstmt.setString(2, password);
//         ResultSet rs = pstmt.executeQuery();

//         if (rs.next()) {
//             System.out.println("Admin login successful!");
//             adminMenu(sc);
//         } else {
//             System.out.println("Incorrect username or password.");
//         }
//     }

//     public static void adminMenu(Scanner sc) throws SQLException {
//         while (true) {
//             System.out.println("1. Add Bus");
//             System.out.println("2. Add Bus Path");
//             System.out.println("3. Add Bus Timing");
//             System.out.println("4. Add Distance Between Stations");
//             System.out.println("5. Add Available Seats");
//             System.out.println("6. See Reservation Status");
//             System.out.println("7. Logout");

//             int choice = sc.nextInt();
//             sc.nextLine();  // Consume newline

//             switch (choice) {
//                 case 1:
//                     addBus(sc);
//                     break;
//                 case 2:
//                     addBusPath(sc);
//                     break;
//                 case 3:
//                     addBusTiming(sc);
//                     break;
//                 case 4:
//                     addDistance(sc);
//                     break;
//                 case 5:
//                     addSeats(sc);
//                     break;
//                 case 6:
//                     seeReservationStatus(sc);
//                     break;
//                 case 7:
//                     return;
//                 default:
//                     System.out.println("Invalid choice. Please try again.");
//             }
//         }
//     }

//     public static void addBus(Scanner sc) throws SQLException {
//         System.out.print("Enter bus name: ");
//         String busName = sc.nextLine();
//         System.out.print("Enter start station: ");
//         String startStation = sc.nextLine();
//         System.out.print("Enter end station: ");
//         String endStation = sc.nextLine();

//         String query = "INSERT INTO Bus (bus_name, start_station, end_station) VALUES (?, ?, ?)";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setString(1, busName);
//         pstmt.setString(2, startStation);
//         pstmt.setString(3, endStation);
//         pstmt.executeUpdate();

//         System.out.println("Bus added successfully!");
//     }

//     public static void addBusPath(Scanner sc) throws SQLException {
//         System.out.print("Enter bus ID: ");
//         int busId = sc.nextInt();
//         sc.nextLine();  // Consume newline
//         System.out.print("Enter start station: ");
//         String startStation = sc.nextLine();
//         System.out.print("Enter end station: ");
//         String endStation = sc.nextLine();

//         String query = "UPDATE Bus SET start_station=?, end_station=? WHERE bus_id=?";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setString(1, startStation);
//         pstmt.setString(2, endStation);
//         pstmt.setInt(3, busId);
//         pstmt.executeUpdate();

//         System.out.println("Bus path updated successfully!");
//     }

//     public static void addBusTiming(Scanner sc) throws SQLException {
//         System.out.print("Enter bus ID: ");
//         int busId = sc.nextInt();
//         sc.nextLine();  // Consume newline
//         System.out.print("Enter bus timing (HH:MM): ");
//         String timing = sc.nextLine();

//         String query = "UPDATE Bus SET timing=? WHERE bus_id=?";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setString(1, timing);
//         pstmt.setInt(2, busId);
//         pstmt.executeUpdate();

//         System.out.println("Bus timing updated successfully!");
//     }

//     public static void addDistance(Scanner sc) throws SQLException {
//         System.out.print("Enter bus ID: ");
//         int busId = sc.nextInt();
//         sc.nextLine();  // Consume newline
//         System.out.print("Enter distance in km: ");
//         int distance = sc.nextInt();
//         sc.nextLine();  // Consume newline

//         String query = "UPDATE Bus SET distance_km=? WHERE bus_id=?";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setInt(1, distance);
//         pstmt.setInt(2, busId);
//         pstmt.executeUpdate();

//         System.out.println("Distance updated successfully!");
//     }

//     public static void addSeats(Scanner sc) throws SQLException {
//         System.out.print("Enter bus ID: ");
//         int busId = sc.nextInt();
//         sc.nextLine();  // Consume newline
//         System.out.print("Enter number of available seats: ");
//         int seats = sc.nextInt();
//         sc.nextLine();  // Consume newline

//         String query = "UPDATE Bus SET available_seats=? WHERE bus_id=?";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setInt(1, seats);
//         pstmt.setInt(2, busId);
//         pstmt.executeUpdate();

//         System.out.println("Available seats updated successfully!");
//     }

//     public static void seeReservationStatus(Scanner sc) throws SQLException {
//         String query = "SELECT * FROM Reservations";
//         Statement stmt = connection.createStatement();
//         ResultSet rs = stmt.executeQuery(query);

//         while (rs.next()) {
//             int reservationId = rs.getInt("reservation_id");
//             int userId = rs.getInt("user_id");
//             int busId = rs.getInt("bus_id");
//             String date = rs.getString("date");

//             System.out.println("Reservation ID: " + reservationId);
//             System.out.println("User ID: " + userId);
//             System.out.println("Bus ID: " + busId);
//             System.out.println("Date: " + date);
//             System.out.println("---------------------------");
//         }
//     }

//     public static void userSignup(Scanner sc) throws SQLException {
//         System.out.print("Enter username: ");
//         String username = sc.nextLine();
//         System.out.print("Enter password: ");
//         String password = sc.nextLine();

//         String query = "INSERT INTO Users (username, password) VALUES (?, ?)";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setString(1, username);
//         pstmt.setString(2, password);
//         pstmt.executeUpdate();

//         System.out.println("User signed up successfully!");
//     }

//     public static void userLogin(Scanner sc) throws SQLException {
//         System.out.print("Enter username: ");
//         String username = sc.nextLine();
//         System.out.print("Enter password: ");
//         String password = sc.nextLine();

//         String query = "SELECT * FROM Users WHERE username=? AND password=?";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setString(1, username);
//         pstmt.setString(2, password);
//         ResultSet rs = pstmt.executeQuery();

//         if (rs.next()) {
//             System.out.println("User login successful!");
//             userMenu(sc);
//         } else {
//             System.out.println("Incorrect username or password.");
//         }
//     }

//     public static void userMenu(Scanner sc) throws SQLException {
//         while (true) {
//             System.out.println("1. Book Bus Ticket");
//             System.out.println("2. Cancel Bus Ticket");
//             System.out.println("3. View Bus Details");
//             System.out.println("4. Logout");

//             int choice = sc.nextInt();
//             sc.nextLine();  // Consume newline

//             switch (choice) {
//                 case 1:
//                     bookBusTicket(sc);
//                     break;
//                 case 2:
//                     cancelBusTicket(sc);
//                     break;
//                 case 3:
//                     viewBusDetails(sc);
//                     break;
//                 case 4:
//                     return;
//                 default:
//                     System.out.println("Invalid choice. Please try again.");
//             }
//         }
//     }

//     public static void bookBusTicket(Scanner sc) throws SQLException {
//         System.out.print("Enter bus ID to book: ");
//         int busId = sc.nextInt();
//         sc.nextLine();  // Consume newline
//         System.out.print("Enter user ID: ");
//         int userId = sc.nextInt();
//         sc.nextLine();  // Consume newline

//         String query = "INSERT INTO Reservations (user_id, bus_id, date) VALUES (?, ?, NOW())";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setInt(1, userId);
//         pstmt.setInt(2, busId);
//         pstmt.executeUpdate();

//         System.out.println("Bus ticket booked successfully!");
//     }

//     public static void cancelBusTicket(Scanner sc) throws SQLException {
//         System.out.print("Enter reservation ID to cancel: ");
//         int reservationId = sc.nextInt();
//         sc.nextLine();  // Consume newline

//         String query = "DELETE FROM Reservations WHERE reservation_id=?";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setInt(1, reservationId);
//         pstmt.executeUpdate();

//         System.out.println("Bus ticket canceled successfully!");
//     }

//     public static void viewBusDetails(Scanner sc) throws SQLException {
//         System.out.print("Enter bus ID to view details: ");
//         int busId = sc.nextInt();
//         sc.nextLine();  // Consume newline

//         String query = "SELECT * FROM Bus WHERE bus_id=?";
//         PreparedStatement pstmt = connection.prepareStatement(query);
//         pstmt.setInt(1, busId);
//         ResultSet rs = pstmt.executeQuery();

//         if (rs.next()) {
//             String busName = rs.getString("bus_name");
//             String startStation = rs.getString("start_station");
//             String endStation = rs.getString("end_station");
//             String timing = rs.getString("timing");
//             int distanceKm = rs.getInt("distance_km");
//             int availableSeats = rs.getInt("available_seats");

//             System.out.println("Bus Name: " + busName);
//             System.out.println("Start Station: " + startStation);
//             System.out.println("End Station: " + endStation);
//             System.out.println("Timing: " + timing);
//             System.out.println("Distance (km): " + distanceKm);
//             System.out.println("Available Seats: " + availableSeats);
//         } else {
//             System.out.println("No bus found with the given ID.");
//         }
//     }
// }

