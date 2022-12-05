package com.disgust.sereda.utils.firebase

import com.disgust.sereda.utils.firebase.model.FavoriteRecipeFirebaseModel
import com.disgust.sereda.utils.firebase.model.ProfileUserFirebaseModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseDatabaseHelper {

    private val database = FirebaseDatabase.getInstance()
    private val usersDatabaseReference: DatabaseReference = database.reference.child("users")
    private val userReference
        get() = Firebase.auth.currentUser?.uid?.let { usersDatabaseReference.child(it) }

    fun addFavoriteRecipe(recipe: FavoriteRecipeFirebaseModel) {
        userReference?.child("favoriteRecipes")
            ?.updateChildren(mapOf("/${recipe.id}" to recipe.toMap()))
    }

    fun deleteFavoriteRecipe(recipe: FavoriteRecipeFirebaseModel) {
        userReference?.child("favoriteRecipes")?.child(recipe.id)?.removeValue()
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun getFavoriteRecipes(): List<FavoriteRecipeFirebaseModel> {
        val userData = userReference?.get()?.await()
        return getListOfFavoriteRecipes(userData)
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun getUserData(): ProfileUserFirebaseModel {
        val userData = userReference?.get()?.await()
        val userMap = userData?.value as HashMap<String, Any?>
        return ProfileUserFirebaseModel(
            email = userMap["email"].toString(),
            favoriteRecipes = getListOfFavoriteRecipes(userData)
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun getListOfFavoriteRecipes(userData: DataSnapshot?): List<FavoriteRecipeFirebaseModel> {
        val favoriteRecipes = mutableListOf<FavoriteRecipeFirebaseModel>()
        val userMap = userData?.value as HashMap<String, Any?>
        val favoriteRecipesReference = userMap["favoriteRecipes"]
        if (favoriteRecipesReference != null) {
            (userMap["favoriteRecipes"] as HashMap<String, HashMap<String, Any?>>).forEach {
                favoriteRecipes.add(
                    FavoriteRecipeFirebaseModel(
                        id = it.value["id"].toString(),
                        name = it.value["name"].toString(),
                        image = it.value["image"].toString()
                    )
                )
            }
        }
        return favoriteRecipes
    }
}