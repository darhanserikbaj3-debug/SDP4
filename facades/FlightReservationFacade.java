package facades;

import confirmation.ConfirmationValidator;
import confirmation.LoyaltyProgram;
import confirmation.MealPreferenceService;
import execution.FlightExecutionValidator;
import execution.BaggageRulesEngine;
import execution.E_TicketIssuer;
import reservation.ReservationValidator;
import reservation.FareCalculator;
import reservation.seatMap;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FlightReservationFacade {
    private seatMap seatManager;
    private FareCalculator fareCalculator;
    private BaggageRulesEngine baggageEngine;
    private E_TicketIssuer ticketIssuer;
    private MealPreferenceService mealService;
    private LoyaltyProgram loyaltyProgram;

    private ReservationValidator reservationValidator;
    private FlightExecutionValidator executionValidator;
    private ConfirmationValidator confirmationValidator;

    public FlightReservationFacade() {
        this.seatManager = new seatMap();
        this.fareCalculator = new FareCalculator();
        this.baggageEngine = new BaggageRulesEngine();
        this.ticketIssuer = new E_TicketIssuer();
        this.mealService = new MealPreferenceService();
        this.loyaltyProgram = new LoyaltyProgram();

        this.reservationValidator = new ReservationValidator();
        this.executionValidator = new FlightExecutionValidator();
        this.confirmationValidator = new ConfirmationValidator();
    }

    // 1. Главный метод запускает процесс и ловит возможные ошибки
    public void completeBookingWorkflow(String seat, String flightClass, String routeType, int bagCount, double bagWeight, String ticketId, String meal, String level) {
        System.out.println("==== Starting Flight Reservation ====");
        boolean bCommitted = false;

        try {
            executeReservationStep(seat, flightClass, routeType);
            
            executeExecutionStep(routeType, ticketId, bagCount, bagWeight);
            bCommitted = true; // Шаг B успешно выполнен
            
            executeConfirmationStep(meal, level, ticketId);

            writeChainStatus("COMPLETED");
            System.out.println("\n==== Reservation is done! ====");

        } catch (RuntimeException e) {
            handleFailure(e, bCommitted, seat);
        }
    }

    // 2. Шаг А: Резервирование места и расчет базовой цены
    private void executeReservationStep(String seat, String flightClass, String routeType) {
        System.out.println("\n ++ Reservation ++ ");
        if (!reservationValidator.validateSeatFormat(seat) || !reservationValidator.validateClassAndRoute(flightClass, routeType)) {
            throw new RuntimeException("ReservationValidationError: Invalid seat, class or route format!");
        }

        seatManager.selectSeat(seat);
        double calculatedFare = fareCalculator.calculateBaseFare(flightClass, routeType);
        System.out.println("Calculated Base Fare: $" + calculatedFare);
        seatManager.saveSeat();
        writeChainStatus("PENDING_B");
    }

    // 3. Шаг Б: Оформление билета и проверка багажа
    private void executeExecutionStep(String routeType, String ticketId, int bagCount, double bagWeight) {
        System.out.println("\n ++ Execution ++ ");
        if (!executionValidator.validateRouteExecution(routeType) || !executionValidator.validateTicketFormat(ticketId)) {
            throw new RuntimeException("ExecutionValidationError: Invalid ticket format or route execution!");
        }

        if (bagWeight > 32.0) {
            throw new RuntimeException("BaggageError: Weight exceeds absolute safety limit (32kg)! B-Step failed.");
        }

        baggageEngine.calculateBaggageFee(bagCount, bagWeight);
        ticketIssuer.issueTicket(ticketId);
        ticketIssuer.saveState();
        writeChainStatus("PENDING_C");
    }

    // 4. Шаг С: Подтверждение еды и лояльности
    private void executeConfirmationStep(String meal, String level, String ticketId) {
        System.out.println("\n ++ Confirmation ++ ");
        if (!confirmationValidator.validateMealOption(meal) || !confirmationValidator.validateLoyaltyTier(level)) {
            throw new RuntimeException("ConfirmationValidationError: Invalid meal option or loyalty tier!");
        }

        if ("OUT_OF_STOCK".equalsIgnoreCase(meal)) {
            throw new RuntimeException("MealError: Requested special meal is out of stock! C-Step failed.");
        }

        mealService.setMealPref(meal);
        loyaltyProgram.setMemberLevel(level);

        saveConfirmationState(ticketId, meal, level);
    }

    // Вспомогательный метод отката (компенсаций) при сбое
    private void handleFailure(RuntimeException e, boolean bCommitted, String seat) {
        System.out.println("\n[Chain Failure Detected]: " + e.getMessage());
        System.out.println("Initiating automated compensation...");

        if (bCommitted) {
            System.out.println("-> Compensation Depth 2: Undoing Subsystem B (Cancelling Ticket)...");
            ticketIssuer.cancelTicket();
            ticketIssuer.saveState();
        } else {
            System.out.println("-> Compensation Depth 1: Subsystem B failed after A committed.");
        }

        System.out.println("-> Undoing Subsystem A (Releasing Seat)...");
        seatManager.releaseSeat(seat);
        seatManager.setStatus("CANCELLED");
        seatManager.saveSeat();

        writeChainStatus("COMPENSATED");
        System.out.println("==== Chain successfully rolled back. Status: COMPENSATED. ====");
    }

    private void writeChainStatus(String status) {
        try (PrintWriter out = new PrintWriter("chain_status.txt")) {
            out.println(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveConfirmationState(String ticketId, String meal, String level) {
        try (PrintWriter out = new PrintWriter("confirmation_" + ticketId + ".txt")) {
            out.println(meal);
            out.println(level);
            out.println("CONFIRMED");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAllStates() {
        seatManager.saveSeat();
        ticketIssuer.saveState();
        System.out.println("All subsystem states saved to text files.");
    }

    public void loadAllStates(String id) {
        seatManager.loadState(id);
        ticketIssuer.loadState(id);
        
        File confFile = new File("confirmation_" + id + ".txt");
        if (confFile.exists()) {
            System.out.println("Confirmation file loaded successfully for ID: " + id);
        }
        
        System.out.println("All subsystem states loaded from text files.");
    }
}