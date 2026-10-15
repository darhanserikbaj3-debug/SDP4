package test;

import facades.FlightReservationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FlightReservationFacadeTest {

    private FlightReservationFacade facade;

    @BeforeEach
    void setUp() throws IOException {
        facade = new FlightReservationFacade();
        Files.deleteIfExists(Path.of("seatmap.txt"));
        Files.deleteIfExists(Path.of("eticket.txt"));
        Files.deleteIfExists(Path.of("chain_status.txt"));
        // Очищаем файлы подтверждений для всех тестовых билетов
        Files.deleteIfExists(Path.of("confirmation_TKT-1001.txt"));
        Files.deleteIfExists(Path.of("confirmation_TKT-1002.txt"));
        Files.deleteIfExists(Path.of("confirmation_TKT-1003.txt"));
        Files.deleteIfExists(Path.of("confirmation_TKT-9999.txt"));
    }

    private void bookedFlightWithDetails(String seat, String flightClass, String route, int bags, double weight, String ticket, String meal, String level) {
        facade.completeBookingWorkflow(seat, flightClass, route, bags, weight, ticket, meal, level);
    }

    @Test
    void testSuccessfulBookingWorkflow() {
        bookedFlightWithDetails("12A", "ECONOMY", "DOMESTIC", 1, 15.0, "TKT-1001", "STANDARD", "THREE");

        String status = readChainStatus();
        assertEquals("COMPLETED", status);
    }

    @Test
    void testCompensationDepth1_BaggageFails() {
        bookedFlightWithDetails("14B", "BUSINESS", "INTERNATIONAL", 1, 35.0, "TKT-1002", "STANDARD", "THREE");

        assertEquals("COMPENSATED", readChainStatus());
    }

    @Test
    void testCompensationDepth2_MealFails() {
        bookedFlightWithDetails("16C", "FIRST", "DOMESTIC", 1, 20.0, "TKT-1003", "OUT_OF_STOCK", "TWO");

        assertEquals("COMPENSATED", readChainStatus());
    }

    @Test
    void testPersistenceReload() {
        bookedFlightWithDetails("20F", "ECONOMY", "DOMESTIC", 1, 10.0, "TKT-9999", "VEGAN", "ONE");
        assertEquals("COMPLETED", readChainStatus());

        FlightReservationFacade restartedFacade = new FlightReservationFacade();
        restartedFacade.loadAllStates("TKT-9999");

        assertEquals("COMPLETED", readChainStatus());
    }

    private String readChainStatus() {
        try {
            Path path = Path.of("chain_status.txt");
            if (!Files.exists(path)) {
                return "NONE";
            }
            return Files.readString(path).trim();
        } catch (IOException e) {
            return "ERROR";
        }
    }
}