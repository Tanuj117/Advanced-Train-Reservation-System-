import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class User {
    private String username;
    private String password;
    private List<Ticket> bookedTickets;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.bookedTickets = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String enteredPassword) {
        return this.password.equals(enteredPassword);
    }

    public List<Ticket> getBookedTickets() {
        return bookedTickets;
    }

    public void bookTicket(Ticket ticket) {
        bookedTickets.add(ticket);
    }

    public void cancelTicket(Ticket ticket) {
        bookedTickets.remove(ticket);
    }
}

class Train {
    private String trainName;
    private int totalSeats;
    private int availableSeats;

    public Train(String trainName, int totalSeats) {
        this.trainName = trainName;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }

    public String getTrainName() {
        return trainName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void bookSeats(int numSeats) {
        if (numSeats <= availableSeats) {
            availableSeats -= numSeats;
        }
    }

    public void cancelSeats(int numSeats) {
        availableSeats += numSeats;
    }
}

class Ticket {
    private static int ticketCounter = 1;
    private int ticketNumber;
    private Train train;
    private int numSeats;

    public Ticket(Train train, int numSeats) {
        this.ticketNumber = ticketCounter++;
        this.train = train;
        this.numSeats = numSeats;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public Train getTrain() {
        return train;
    }

    public int getNumSeats() {
        return numSeats;
    }
}

public class AdvancedTrainReservationSystem {
    private static Map<String, User> users = new HashMap<>();
    private static List<Train> trains = new ArrayList<>();
    private static List<Ticket> tickets = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create sample trains
        trains.add(new Train("Train A", 100));
        trains.add(new Train("Train B", 150));
        trains.add(new Train("Train C", 200));

        while (true) {
            System.out.println("Train Reservation System");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    User user = users.get(username);

                    if (user != null && user.authenticate(password)) {
                        loggedInMenu(scanner, user);
                    } else {
                        System.out.println("Invalid username or password. Try again.");
                    }
                    break;
                case 2:
                    System.out.print("Enter a new username: ");
                    String newUsername = scanner.nextLine();
                    System.out.print("Enter a password: ");
                    String newPassword = scanner.nextLine();

                    User newUser = new User(newUsername, newPassword);
                    users.put(newUsername, newUser);
                    System.out.println("Registration successful!");
                    break;
                case 3:
                    System.out.println("Exiting Train Reservation System. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void loggedInMenu(Scanner scanner, User user) {
        while (true) {
            System.out.println("\nWelcome, " + user.getUsername() + "!");
            System.out.println("1. View Available Trains");
            System.out.println("2. Book a Ticket");
            System.out.println("3. Cancel a Ticket");
            System.out.println("4. View Booked Tickets");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int userChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (userChoice) {
                case 1:
                    System.out.println("Available Trains:");
                    for (Train train : trains) {
                        System.out.println(train.getTrainName() + " - Available Seats: " + train.getAvailableSeats());
                    }
                    break;
                case 2:
                    System.out.print("Enter the train name to book a ticket: ");
                    String trainToBook = scanner.nextLine();

                    System.out.print("Enter the number of seats to book: ");
                    int numSeats = scanner.nextInt();

                    Train selectedTrain = null;
                    for (Train train : trains) {
                        if (train.getTrainName().equalsIgnoreCase(trainToBook)) {
                            selectedTrain = train;
                            break;
                        }
                    }

                    if (selectedTrain != null) {
                        if (selectedTrain.getAvailableSeats() >= numSeats) {
                            Ticket ticket = new Ticket(selectedTrain, numSeats);
                            selectedTrain.bookSeats(numSeats);
                            user.bookTicket(ticket);
                            tickets.add(ticket);
                            System.out.println("Ticket booked successfully! Ticket Number: " + ticket.getTicketNumber());
                        } else {
                            System.out.println("Not enough seats available on " + selectedTrain.getTrainName());
                        }
                    } else {
                        System.out.println("Train not found.");
                    }
                    break;
                case 3:
                    System.out.println("Your Booked Tickets:");
                    List<Ticket> userTickets = user.getBookedTickets();
                    for (Ticket ticket : userTickets) {
                        System.out.println("Ticket Number: " + ticket.getTicketNumber() +
                                ", Train: " + ticket.getTrain().getTrainName() +
                                ", Seats: " + ticket.getNumSeats());
                    }

                    System.out.print("Enter the ticket number to cancel: ");
                    int ticketToCancel = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Ticket canceledTicket = null;
                    for (Ticket ticket : userTickets) {
                        if (ticket.getTicketNumber() == ticketToCancel) {
                            canceledTicket = ticket;
                            break;
                        }
                    }

                    if (canceledTicket != null) {
                        canceledTicket.getTrain().cancelSeats(canceledTicket.getNumSeats());
                        user.cancelTicket(canceledTicket);
                        tickets.remove(canceledTicket);
                        System.out.println("Ticket canceled successfully!");
                    } else {
                        System.out.println("Invalid ticket number.");
                    }
                    break;
                case 4:
                    System.out.println("Your Booked Tickets:");
                    List<Ticket> bookedTickets = user.getBookedTickets();
                    for (Ticket ticket : bookedTickets) {
                        System.out.println("Ticket Number: " + ticket.getTicketNumber() +
                                ", Train: " + ticket.getTrain().getTrainName() +
                                ", Seats: " + ticket.getNumSeats());
                    }
                    break;
                case 5:
                    System.out.println("Logged out successfully.");
                    return; // Go back to the main menu
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}