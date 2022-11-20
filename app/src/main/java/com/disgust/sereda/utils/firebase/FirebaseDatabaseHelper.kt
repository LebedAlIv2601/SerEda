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

    private val auth = Firebase.auth
    private val database = FirebaseDatabase.getInstance()
    private val usersDatabaseReference: DatabaseReference = database.reference.child("users")
    private val userReference = auth.currentUser?.uid?.let { usersDatabaseReference.child(it) }

    fun addFavoriteRecipe(recipe: FavoriteRecipeFirebaseModel) {
        doIfUserExists {
            it.child("favoriteRecipes")
                .updateChildren(mapOf("/${recipe.id}" to recipe.toMap()))
        }
    }

    fun deleteFavoriteRecipe(recipe: FavoriteRecipeFirebaseModel) {
        doIfUserExists {
            it.child("favoriteRecipes").child(recipe.id).removeValue()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getFavoriteRecipes(doOnComplete: (List<FavoriteRecipeFirebaseModel>) -> Unit) {
        val list = mutableListOf<FavoriteRecipeFirebaseModel>()
        doIfUserExists {
            it.child("favoriteRecipes").get().addOnCompleteListener { task ->
                if (task.result.exists()) {
                    val result = task.result.value as HashMap<String, HashMap<String, Any?>>
                    result.forEach { map ->
                        list.add(
                            FavoriteRecipeFirebaseModel(
                                id = map.value["id"].toString(),
                                name = map.value["name"].toString(),
                                image = map.value["image"].toString()
                            )
                        )
                    }
                    doOnComplete(list)
                }
            }
        }
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

    private inline fun doIfUserExists(crossinline function: (DatabaseReference) -> Unit) {
        userReference?.let {
            it.get().addOnCompleteListener { task ->
                if (task.result.exists()) {
                    function.invoke(it)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getListOfFavoriteRecipes(userData: DataSnapshot?): List<FavoriteRecipeFirebaseModel> {
        val favoriteRecipes = mutableListOf<FavoriteRecipeFirebaseModel>()
        val userMap = userData?.value as HashMap<String, Any?>
        (userMap["favoriteRecipes"] as HashMap<String, HashMap<String, Any?>>).forEach {
            favoriteRecipes.add(
                FavoriteRecipeFirebaseModel(
                    id = it.value["id"].toString(),
                    name = it.value["name"].toString(),
                    image = it.value["image"].toString()
                )
            )
        }
        return favoriteRecipes
    }
}