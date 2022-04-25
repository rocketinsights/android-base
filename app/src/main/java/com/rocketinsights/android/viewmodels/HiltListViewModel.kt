package com.rocketinsights.android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.network.models.recipe_hilt.Recipe
import com.rocketinsights.android.repos.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HiltListViewModel
@Inject
constructor(
    private val repository: RecipeRepository,
    @Named("auth_token") private val token: String
) : ViewModel() {

    private val recipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    val liveRecipe: LiveData<List<Recipe>> = recipes

    init {
        viewModelScope.launch {
            val result = repository.search(
                token = token,
                page = 1,
                query = "beef carrot potato onion"
            )

            result?.apply {
                recipes.value = this
            }
        }
    }
}
