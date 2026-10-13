package reservation;

public class FareCalculator {
    double basePrice = 100.0;

    public double calculateBaseFare(String classType, String route) {
        double basePrice = 100.0; 
        
        if (route.equals("INTERNATIONAL")) {
            basePrice = 500.0;
        } else if (route.equals("DOMESTIC")) {
            basePrice = 150.0;
        }
        
        if (classType.equalsIgnoreCase("BUSINESS")) {
            basePrice *= 2.5;
        } else if (classType.equalsIgnoreCase("FIRST")) {
            basePrice *= 4.0;
        }
        
        return basePrice;
    }

    public double calculateTaxesAndFees(double almtotal) {
        double taxRate = 0.15;
        return almtotal * taxRate;
    }

    public double Total(String classType, String route) {
        double base = calculateBaseFare(classType, route);

        return base + calculateTaxesAndFees(base);
    }
}
 