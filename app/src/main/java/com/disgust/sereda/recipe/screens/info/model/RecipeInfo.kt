package com.disgust.sereda.recipe.screens.info.model

data class RecipeInfo(
    val id: Int,
    val name: String,
    val image: String,
    val time: Int,
    val servings: Int,
    val instructions: List<StepRecipeInstruction>?,
    val diets: List<String>?,
    val ingredients: List<Ingredient>?,
    val calories: Nutrient?,
    val fat: Nutrient?,
    val carbohydrates: Nutrient?,
    val protein: Nutrient?,
    val nutrition: List<Nutrient>?,
)

data class StepRecipeInstruction(
    val number: Int,
    val step: String,
    val equipment: List<String>,
)

fun RecipeInfoResponse.toRecipeInfo(): RecipeInfo {
    val nutrients = nutrition?.nutrients?.toMutableList()
    val calories = nutrients?.find { it.name == "Calories" }
    val fat = nutrients?.find { it.name == "Fat" }
    val carbohydrates = nutrients?.find { it.name == "Carbohydrates" }
    val protein = nutrients?.find { it.name == "Protein" }
    nutrients?.removeAll(listOf(calories, fat, carbohydrates, protein))

    return RecipeInfo(
        id = id,
        name = name,
        image = image,
        time = time,
        servings = servings,
        instructions = instructions?.getOrNull(0)?.step?.map { it ->
            StepRecipeInstruction(
                number = it.number,
                step = it.step,
                equipment = it.equipment.map { it.name }
            )
        },
        diets = diets,
        ingredients = ingredients,
        calories = calories,
        fat = fat,
        carbohydrates = carbohydrates,
        protein = protein,
        nutrition = nutrients
    )
}

