package reservation;

public class ReservationValidator {

    public boolean validateSeatFormat(String seatNumber) {
        if (seatNumber == null || seatNumber.isEmpty()) {
            return false;
        }
        return seatNumber.matches("^[0-9]+[A-Z]$");
    }

    public boolean validateClassAndRoute(String flightClass, String routeType) {
        if (flightClass == null || routeType == null) {
            return false;
        }
        String upperClass = flightClass.toUpperCase();
        String upperRoute = routeType.toUpperCase();

        boolean isValidClass = upperClass.equals("ECONOMY") || upperClass.equals("BUSINESS") || upperClass.equals("FIRST");
        boolean isValidRoute = upperRoute.equals("DOMESTIC") || upperRoute.equals("INTERNATIONAL");

        return isValidClass && isValidRoute;
    }
}