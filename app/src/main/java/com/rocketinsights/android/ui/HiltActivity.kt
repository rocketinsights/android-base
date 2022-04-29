package com.rocketinsights.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rocketinsights.android.ui.compose.CircularProgressBar
import com.rocketinsights.android.ui.compose.RecipeCard
import com.rocketinsights.android.viewmodels.HiltListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltActivity : AppCompatActivity() {

    private val viewModel: HiltListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val recipes = viewModel.recipes.value
            val searchQuery = viewModel.searchQuery.value
            val loading = viewModel.loading.value
            val focusManager = LocalFocusManager.current

            Column() {
                TextField(
                    value = searchQuery,
                    onValueChange = { newValue ->
                        viewModel.onSearchQueryChanged(newValue)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = {
                        Text(text = "Search")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "icon"
                        )
                    },
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.newSearch(searchQuery)
                            focusManager.clearFocus()
                        }
                    )
                )
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(content = {
                        itemsIndexed(
                            items = recipes
                        ) { index, recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onCardClick = {}
                            )
                        }
                    })

                    CircularProgressBar(isDisplayed = loading)
                }
            }
        }
    }
}
