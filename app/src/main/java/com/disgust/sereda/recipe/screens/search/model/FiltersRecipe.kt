package com.disgust.sereda.recipe.screens.search.model

import com.disgust.sereda.utils.base.BaseChipsEnum

data class FiltersRecipe(
    val ingredientsList: List<IngredientFilter>?,
    val dietsList: List<Diet>?,
    val intolerancesList: List<Intolerance>?,
    val maxReadyTime: Int?,
    val minCalories: Int?,
    val maxCalories: Int?
) {
    constructor() : this(
        null, null, null, null, null, null
    )

    data class Builder(
        var ingredientsList: MutableList<IngredientFilter> = mutableListOf(),
        var dietsList: MutableList<Diet> = mutableListOf(),
        var intolerancesList: MutableList<Intolerance> = mutableListOf(),
        var maxReadyTime: Int? = null,
        var minCalories: Int? = null,
        var maxCalories: Int? = null
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

        fun setIntolerancesList(intolerancesList: MutableList<Intolerance>) =
            apply { this.intolerancesList = intolerancesList }

        fun addIntolerance(intolerance: Intolerance) =
            apply { intolerancesList.add(intolerance) }

        fun deleteIntolerance(intolerance: Intolerance) =
            apply { intolerancesList.remove(intolerance) }

        fun setMaxReadyTime(maxTime: Int?) =
            apply { maxReadyTime = maxTime }

        fun clearMaxReadyTime() =
            apply { maxReadyTime = null }

        fun setMinCalories(minCalories: Int?) =
            apply { this.minCalories = minCalories }

        fun clearMinCalories() =
            apply { minCalories = null }

        fun setMaxCalories(maxCalories: Int?) =
            apply { this.maxCalories = maxCalories }

        fun clearMaxCalories() =
            apply { maxCalories = null }

        fun build() = FiltersRecipe(
            ingredientsList, dietsList, intolerancesList, maxReadyTime, minCalories, maxCalories
        )
    }
}

data class IngredientFilter(
    val id: Int,
    val name: String,
    val image: String?,
    val isInclude: Boolean
)

enum class Diet(override val value: String) : BaseChipsEnum {
    GLUTEN_FREE("Gluten free"),
    KETOGENIC("Ketogenic"),
    VEGETARIAN("Vegetarian"),
    LACTO_VEGETARIAN("Lacto-Vegetarian"),
    VEGAN("Vegan"),
    PESCETARIAN("Pescetarian"),
    PALEO("Paleo"),
    PRIMAL("Primal")
}

enum class Intolerance(override val value: String) : BaseChipsEnum {
    DAIRY("Dairy"),
    EGG("Egg"),
    GLUTEN("Gluten"),
    GRAIN("Grain"),
    PEANUT("Peanut"),
    SEAFOOD("Seafood"),
    SESAME("Sesame"),
    SHELLFISH("Shellfish"),
    SOY("Soy"),
    SULFITE("Sulfite"),
    TREE_NUT("Tree Nut"),
    WHEAT("Wheat")
}