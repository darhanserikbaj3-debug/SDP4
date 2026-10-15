import java.util.Scanner;
import facades.AdminBookingFacade;
import facades.FlightReservationFacade;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FlightReservationFacade bookingFacade = new FlightReservationFacade();
        AdminBookingFacade adminFacade = new AdminBookingFacade();

        boolean running = true;
        System.out.println("==================================================");
        System.out.println(" Flight Reservation System ");
        System.out.println("==================================================");

        while (running) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Complete New Booking Workflow");
            System.out.println("2. Admin: Inspect Persisted State");
            System.out.println("3. Admin: Force Manual Rollback");
            System.out.println("4. Exit");
            System.out.print("Enter choice (1-4): ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter seat number (e.g., 12A): ");
                String seat = scanner.nextLine();
                
                System.out.print("Enter flight class (ECONOMY / BUSINESS / FIRST): ");
                String flightClass = scanner.nextLine();

                System.out.print("Enter route type (DOMESTIC / INTERNATIONAL): ");
                String routeType = scanner.nextLine();
                
                System.out.print("Enter amount of baggage (e.g., 2): ");
                int bagCount = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter baggage weight in kg (e.g., 22.5): ");
                double bagWeight = Double.parseDouble(scanner.nextLine());
                
                System.out.print("Enter Ticket ID (e.g., TKT-9988): ");
                String ticketId = scanner.nextLine();
                
                System.out.print("Enter meal preference (VEGETARIAN / VEGAN / GLUTEN-FREE / STANDARD): ");
                String meal = scanner.nextLine();
                
                System.out.print("Enter loyalty tier/level (THREE / TWO / ONE): ");
                String level = scanner.nextLine();

                bookingFacade.completeBookingWorkflow(seat, flightClass, routeType, bagCount, bagWeight, ticketId, meal, level);

            } else if (choice.equals("2")) {
                System.out.print("Enter Booking/Ticket ID to inspect from disk: ");
                String id = scanner.nextLine();
                
                adminFacade.inspectSystemState(id);

            } else if (choice.equals("3")) {
                System.out.print("Enter seat ID for manual rollback/cancellation: ");
                String seatId = scanner.nextLine();
                
                adminFacade.forceManualRollback(seatId);

            } else if (choice.equals("4")) {
                running = false;
                System.out.println("Exiting application. Have a great flight!");
            } else {
                System.out.println("Invalid option. Please enter a number between 1 and 4.");
            }
        }
        scanner.close();
    }
}