package com.rocketinsights.android.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.rocketinsights.android.R
import com.rocketinsights.android.models.recipe_hilt.Recipe
import com.rocketinsights.android.ui.compose.util.loadPicture

@Composable
fun RecipeCard(
    recipe: Recipe,
    onCardClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                bottom = 6.dp,
                top = 6.dp
            )
            .fillMaxHeight()
            .clickable(onClick = onCardClick),
        elevation = 8.dp
    ) {
        Column {
            recipe.featuredImage?.let { url ->
                loadPicture(
                    url = url,
                    defaultImage = R.drawable.ic_rocketinsights_logo
                ).value?.let { image ->
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = "desc",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            recipe.title?.let { title ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start),
                        style = MaterialTheme.typography.h5
                    )
                    Text(
                        text = recipe.rating.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
    }
}
