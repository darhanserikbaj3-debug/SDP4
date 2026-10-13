package facades;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import execution.E_TicketIssuer;
import reservation.seatMap;

public class AdminBookingFacade {
    private seatMap seatManager;
    private E_TicketIssuer ticketIssuer;

    public AdminBookingFacade() {
        this.seatManager = new seatMap();
        this.ticketIssuer = new E_TicketIssuer();
    }

    public void inspectSystemState(String bookingId) {
        System.out.println("=== ADMIN INSPECTION: Booking ID " + bookingId + " ===");
        
        seatManager.loadState(bookingId);
        ticketIssuer.loadState(bookingId);

        System.out.println("Seat Map File Status: Loaded successfully.");
        System.out.println("Current Seat Number: " + seatManager.getSeatNumber());
        
        System.out.println("E-Ticket File Status: Loaded successfully.");
        
        try (BufferedReader reader = new BufferedReader(new FileReader("chain_status.txt"))) {
            String currentChainState = reader.readLine();
            System.out.println("[Chain Progress Status]: " + currentChainState);
        } catch (IOException e) {
            System.out.println("[Chain Progress Status]: No active chain record found.");
        }
    }

    public void forceManualRollback(String seatId) {
        System.out.println("=== ADMIN ROOLBACK ===");

        seatManager.releaseSeat(seatId);
        seatManager.setStatus("CANCELLED");
        seatManager.saveSeat();

        ticketIssuer.cancelTicket();
        ticketIssuer.saveState();

        try (PrintWriter out = new PrintWriter("Chain_status.txt")) {
            out.println("COMPENSATED");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("=== MANUAL ROOLBACK IS DONE ===");
    }
}
