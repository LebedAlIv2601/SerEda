package com.disgust.sereda.recipe.screens.search.model

data class FiltersRecipe(
    val ingredientsList: List<IngredientFilter>?,
    val dietsList: List<Diet>?
) {
    data class Builder(
        var ingredientsList: MutableList<IngredientFilter> = mutableListOf(),
        var dietsList: MutableList<Diet> = mutableListOf()
    ) {

        fun setIngredientsList(ingredientsList: MutableList<IngredientFilter>) =
            apply { this.ingredientsList = ingredientsList }

        fun addIngredient(ingredient: IngredientFilter) =
            apply { ingredientsList.add(ingredient) }

        fun deleteIngredient(ingredient: IngredientFilter) =
            apply { ingredientsList.remove(ingredient) }

        fun setDietsList(dietsList: MutableList<Diet>) =
            apply { this.dietsList = dietsList }

        fun addDiet(diet: Diet) =
            apply { dietsList.add(diet) }

        fun deleteDiet(diet: Diet) =
            apply { dietsList.remove(diet) }

        fun build() = FiltersRecipe(ingredientsList, dietsList)
    }
}

data class IngredientFilter(
    val id: Int,
    val name: String,
    val image: String?,
    val isInclude: Boolean
)

enum class Diet(val value: String) {
    GLUTEN_FREE("Gluten free"),
    KETOGENIC("Ketogenic"),
    VEGETARIAN("Vegetarian"),
    LACTO_VEGETARIAN("Lacto-Vegetarian"),
    VEGAN("Vegan"),
    PESCETARIAN("Pescetarian"),
    PALEO("Paleo"),
    PRIMAL("Primal")
}