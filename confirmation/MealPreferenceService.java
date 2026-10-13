package confirmation;

public class MealPreferenceService {
    private String selectedMeal = "STANDART";

    public void setMealPref(String pref) {
        if (pref != null) {
            String upperPref = pref.toUpperCase();
            if (upperPref.equals("VEGETARIAN") || upperPref.equals("VEGAN") || 
                upperPref.equals("GLUTEN-FREE") || upperPref.equals("STANDARD")) {
                    this.selectedMeal = upperPref;
                    System.out.println("Meal preference: " + this.selectedMeal);
                } else {
                    System.out.println("Invalid meal code");
            }
        }
    }

    public String getMealPref() {
        return this.selectedMeal;
    }

    public double calculateMealPrice() {
        double extraCost = 0.0;

        if (selectedMeal.equals("VEGAN")) {
            extraCost = 10.0;
        } else if (selectedMeal.equals("GLUTEN-FREE")) {
            extraCost = 12.0;
        } else {
            extraCost = 0.0;
        }
        return extraCost;
    }
}
