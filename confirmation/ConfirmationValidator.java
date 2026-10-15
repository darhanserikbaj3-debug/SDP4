package confirmation;

public class ConfirmationValidator {

    public boolean validateLoyaltyTier(String tier) {
        if (tier == null) {
            return false;
        }
        String upperTier = tier.toUpperCase();
        return upperTier.equals("ONE") || upperTier.equals("TWO") || upperTier.equals("THREE");
    }

    public boolean validateMealOption(String meal) {
        if (meal == null) {
            return false;
        }
        String upperMeal = meal.toUpperCase();
        return upperMeal.equals("VEGETARIAN") || upperMeal.equals("VEGAN") || 
               upperMeal.equals("GLUTEN-FREE") || upperMeal.equals("STANDARD");
    }
}