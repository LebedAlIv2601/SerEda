package com.disgust.sereda.utils.firebase

import com.disgust.sereda.utils.firebase.model.FavoriteRecipeFirebaseModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class FirebaseDatabaseHelper {

    private val auth = Firebase.auth
    private val database = FirebaseDatabase.getInstance()
    private val usersDatabaseReference: DatabaseReference = database.reference.child("users")
    private val userReference = auth.currentUser?.uid?.let { usersDatabaseReference.child(it) }

    fun addFavoriteRecipe(recipe: FavoriteRecipeFirebaseModel, doOnComplete: () -> Unit) {
        doIfUserExists {
            it.child("favoriteRecipes")
                .updateChildren(mapOf("/${recipe.id}" to recipe.toMap())).addOnCompleteListener {
                    doOnComplete.invoke()
                }
        }

    }

    fun deleteFavoriteRecipe(recipe: FavoriteRecipeFirebaseModel, doOnComplete: () -> Unit) {
        doIfUserExists {
            it.child("favoriteRecipes").child(recipe.id).removeValue().addOnCompleteListener {
                doOnComplete.invoke()
            }
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

    private inline fun doIfUserExists(crossinline function: (DatabaseReference) -> Unit) {
        userReference?.let {
            it.get().addOnCompleteListener { task ->
                if (task.result.exists()) {
                    function.invoke(it)
                }
            }
        }
    }
}