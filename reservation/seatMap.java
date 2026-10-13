package reservation;

import java.io.*;

public class seatMap {
    private String seatNumber;
    private String status = "NEW"; //new, confirmed, cancelled

    public boolean isSeatAvailable(String seat) {
        return seatNumber == null;
    }

    public void selectSeat(String seat) {
        this.seatNumber = seat;
        System.out.println("Seat " + seat + " selected");
    }

    public void releaseSeat(String seat) {
        if (seat.equals(this.seatNumber)) {
            this.seatNumber = null;
            System.out.println("Seat " + seat + " released");
        }
    }

    public void setStatus(String newStatus) {
        if (newStatus.equals("NEW") || newStatus.equals("CONFIRMED") || newStatus.equals("CANCELLED")) {
            this.status = newStatus;
            System.out.println("Status updated to: " + status);
        } else {
            throw new IllegalArgumentException("No such status");
        }
    }

    public void saveSeat() {
        try (PrintWriter out = new PrintWriter("seatmap.txt")) {
            if (seatNumber != null) {
                out.println(seatNumber);
            } else {
                out.println("NONE");
            }
            out.println(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadState(String id) {
        File file = new File("seatmap.txt");
        if (!file.exists()) {
            return;
        } 
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String seat = reader.readLine();
            if (seat.equals("NONE")) {
                this.seatNumber = null;
            } else {
                this.seatNumber = seat;
            }
            this.status = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSeatNumber() {
        return seatNumber;
    }
}
