package execution;

public class FlightExecutionValidator {

    public boolean validateRouteExecution(String routeType) {
        if (routeType == null) {
            return false;
        }
        String upperRoute = routeType.toUpperCase();
        return upperRoute.equals("DOMESTIC") || upperRoute.equals("INTERNATIONAL");
    }

    public boolean validateTicketFormat(String ticketId) {
        if (ticketId == null) {
            return false;
        }
        // Проверяем, что ID билета начинается с правильного префикса
        return ticketId.startsWith("TKT-") && ticketId.length() >= 8;
    }
}