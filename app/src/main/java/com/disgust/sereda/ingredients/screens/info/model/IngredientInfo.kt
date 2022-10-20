package com.disgust.sereda.ingredients.screens.info.model

data class IngredientInfo(
    val id: Int,
    val name: String,
    val image: String,
    val calories: NutritionItem,
    val fat: NutritionItem,
    val carbohydrates: NutritionItem,
    val protein: NutritionItem,
    val caloricBreakdown: CaloricBreakdown,
    val minerals: List<NutritionItem>,
    val vitamins: List<NutritionItem>,
    val nutritionElements: List<NutritionItem>
)

fun IngredientInfoResponse.toIngredientInfo(): IngredientInfo {
    val nutrients = nutrition.nutrients
    return IngredientInfo(
        id = id,
        name = name,
        image = image,
        calories = nutrients.find { it.name == "Calories" } ?: NutritionItem(),
        fat = nutrients.find { it.name == "Fat" } ?: NutritionItem(),
        carbohydrates = nutrients.find { it.name == "Carbohydrates" } ?: NutritionItem(),
        protein = nutrients.find { it.name == "Protein" } ?: NutritionItem(),
        caloricBreakdown = nutrition.caloricBreakdown,
        minerals = nutrients.filter {
            when (it.name) {
                "Sodium", "Selenium",
                "Manganese", "Copper",
                "Potassium", "Magnesium",
                "Iron", "Calcium",
                "Zinc", "Phosphorus" -> true
                else -> false
            }
        },
        vitamins = nutrients.filter { it.name.contains("Vitamin") || it.name == "Folate" },
        nutritionElements = nutrients.filter {
            when (it.name) {
                "Saturated Fat", "Net Carbohydrates",
                "Sugar", "Cholesterol",
                "Fiber", "Caffeine",
                "Lycopene", "Folic Acid",
                "Fluoride", "Choline",
                "Alcohol" -> true
                else -> false
            }
        }
    )
}
