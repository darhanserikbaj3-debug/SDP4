package execution;

import java.io.*;

public class E_TicketIssuer {
    private String ticketNumber;
    private String status = "NEW";

    public void issueTicket(String ticketId) {
        this.ticketNumber = ticketId;
        this.status = "CONFIRMED";
        System.out.println("E-Ticket " + ticketId + " issued succsess");
    }

    public void cancelTicket() {
        this.status = "CANCELLED";
        this.ticketNumber = null;
        System.out.println("E-Ticket has been cancelled/revoked.");
    }

    public void setStatus(String newStatus) {
        if (newStatus.equals("NEW") || newStatus.equals("CONFIRMED") || newStatus.equals("CANCELLED")) {
            this.status = newStatus;
            System.out.println("E-Ticket status updated to: " + status);
        } else {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }
    }

    public void saveState() {
        try (PrintWriter out = new PrintWriter("eticket.txt")) {
            if (ticketNumber != null) {
                out.println(ticketNumber);
            } else {
                out.println("NONE");
            } 
            out.println(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadState(String id) {
        File file = new File("eticket.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String ticket = reader.readLine();
            if (ticket.equals("NONE")) {
                this.ticketNumber = null;
            } else {
                this.ticketNumber = ticket;
            }
            this.status = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
}
