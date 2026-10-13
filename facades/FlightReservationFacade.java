package facades;

import confirmation.LoyaltyProgram;
import confirmation.MealPreferenceService;
import execution.BaggageRulesEngine;
import execution.E_TicketIssuer;
import reservation.FareCalculator;
import reservation.seatMap;

public class FlightReservationFacade {
    private seatMap seatManager;
    private FareCalculator fareCalculator;
    private BaggageRulesEngine baggageEngine;
    private E_TicketIssuer ticketIssuer;
    private MealPreferenceService mealService;
    private LoyaltyProgram loyaltyProgram;

    public FlightReservationFacade() {
        this.seatManager = new seatMap();
        this.fareCalculator = new FareCalculator();
        this.baggageEngine = new BaggageRulesEngine();
        this.ticketIssuer = new E_TicketIssuer();
        this.mealService = new MealPreferenceService();
        this.loyaltyProgram = new LoyaltyProgram();
    }

    public void completeBookingWorkflow(String seat, String flightClass, String routeType, int bagCount, double bagWeight, String ticketId, String meal, String level) {
        System.out.println("==== Starting Flight Reservation ====");

        System.out.println("\n ++ Reservation ++ ");
        seatManager.selectSeat(seat);
        double calculatedFare = fareCalculator.calculateBaseFare(flightClass, routeType);
        System.out.println("Calculated Base Fare: $" + calculatedFare);
        seatManager.saveSeat();

        System.out.println("\n ++ Execution ++ ");
        double bagFee = baggageEngine.calculateBaggageFee(bagCount, bagWeight);
        ticketIssuer.issueTicket(ticketId);
        ticketIssuer.saveState();

        System.out.println("\n ++ Confirmation ++ ");
        mealService.setMealPref(meal);
        loyaltyProgram.setMemberLevel(level);

        double totalCost = calculatedFare + bagFee + mealService.calculateMealPrice();
        double finalPrice = loyaltyProgram.applyTierDiscount(totalCost);
        loyaltyProgram.earnPoints(finalPrice);

        System.out.println("\n==== Reservation is done! Final Price: $" + finalPrice + " ====");
    } 

    public void saveAllStates() {
        seatManager.saveSeat();
        ticketIssuer.saveState();
        System.out.println("All subsystem states saved to text files.");
    }

    public void loadAllStates(String id) {
        seatManager.loadState(id);
        ticketIssuer.saveState();
        System.out.println("All subsystem states loaded from text files.");
    }
}