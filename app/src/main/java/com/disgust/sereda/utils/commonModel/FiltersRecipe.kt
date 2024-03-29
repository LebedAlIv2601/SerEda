package com.disgust.sereda.utils.commonModel

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
        var ingredientsList: List<IngredientFilter> = listOf(),
        var dietsList: List<Diet> = listOf(),
        var intolerancesList: List<Intolerance> = listOf(),
        var maxReadyTime: Int? = null,
        var minCalories: Int? = null,
        var maxCalories: Int? = null
    ) {

        fun setIngredientsList(ingredientsList: List<IngredientFilter>?) =
            apply {
                if (ingredientsList != null) {
                    this.ingredientsList = ingredientsList
                }
            }

        fun addIngredient(ingredient: IngredientFilter) =
            apply { ingredientsList = ingredientsList + ingredient }

        fun deleteIngredient(ingredient: IngredientFilter) =
            apply { ingredientsList = ingredientsList - ingredient }

        fun setDietsList(dietsList: List<Diet>?) =
            apply {
                if (dietsList != null) {
                    this.dietsList = dietsList
                }
            }

        fun addDiet(diet: Diet) =
            apply { dietsList = dietsList + diet }

        fun deleteDiet(diet: Diet) =
            apply { dietsList = dietsList - diet }

        fun setIntolerancesList(intolerancesList: List<Intolerance>?) =
            apply {
                if (intolerancesList != null) {
                    this.intolerancesList = intolerancesList
                }
            }

        fun addIntolerance(intolerance: Intolerance) =
            apply { intolerancesList = intolerancesList + intolerance }

        fun deleteIntolerance(intolerance: Intolerance) =
            apply { intolerancesList = intolerancesList - intolerance }

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

        fun clearAll() =
            apply {
                ingredientsList = listOf()
                dietsList = listOf()
                intolerancesList = listOf()
                maxReadyTime = null
                minCalories = null
                maxCalories = null
            }

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
    PRIMAL("Primal");

    companion object {
        infix fun from(value: String): Diet? = Diet.values().firstOrNull { it.value == value }
    }
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
    WHEAT("Wheat");

    companion object {
        infix fun from(value: String): Intolerance? =
            Intolerance.values().firstOrNull { it.value == value }
    }
}