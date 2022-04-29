package com.rocketinsights.android.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.models.recipe_hilt.Recipe
import com.rocketinsights.android.repos.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HiltListViewModel
@Inject
constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(emptyList())
    val searchQuery = mutableStateOf("")
    val loading = mutableStateOf(false)

    init {
        newSearch(searchQuery.value)
    }

    fun newSearch(searchQuery: String) {
        viewModelScope.launch {
            loading.value = true
            clearSearch()

            val result = repository.search(
                page = 1,
                query = searchQuery
            )

            result?.apply {
                recipes.value = this
                loading.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        this.searchQuery.value = query
    }

    private fun clearSearch() {
        recipes.value = listOf()
    }
}
