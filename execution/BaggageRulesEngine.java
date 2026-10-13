package execution;

public class BaggageRulesEngine {
    
    public boolean validateBaggage(double weight, double size) {
        boolean isWeightValid = weight <= 23.0;
        boolean isSizeValid = size <= 167.00;
        return isWeightValid && isSizeValid;
    }

    public double calculateBaggageFee(int bagCount, double weight) {
        double fee = 0.0;

        if (bagCount > 1) {
            fee += bagCount * 30.0;
        }

        if (weight > 23.0) {
            fee += 30;
        }

        return fee;
    }
}
