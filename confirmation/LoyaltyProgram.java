package confirmation;

public class LoyaltyProgram {
    private String memberLevel = "THREE";
    private int availablePoints = 0;

    public void setMemberLevel(String level) {
        if (level != null) {
            String upperLevel = level.toUpperCase();
            if (upperLevel.equals("TWO") || upperLevel.equals("THREE") || upperLevel.equals("ONE")) {
                this.memberLevel = upperLevel;
                System.out.println("Level is set to: " + this.memberLevel);
            } else {
                System.out.println("Invalid level");
            }
        }
    }

    public double applyTierDiscount(double ticketPrice) {
        double discountRate = 0.0;
        
        if (memberLevel.equals("THREE")) {
            discountRate = 0.00; 
        } else if (memberLevel.equals("TWO")) {
            discountRate = 0.10;
        } else {
            discountRate = 0.15;
        } 
        
        return ticketPrice * (1.0 - discountRate);
    }

    public void earnPoints(double finalPaidAmount) {
        int earned = (int) (finalPaidAmount * 0.10); 
        this.availablePoints += earned;
        System.out.println("Earned " + earned + " loyalty points. Total balance: " + this.availablePoints);
    }

    public int getAvailablePoints() {
        return this.availablePoints;
    }
}
