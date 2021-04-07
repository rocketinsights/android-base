package com.rocketinsights.android.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.rocketinsights.android.R
import com.rocketinsights.android.models.Player
import com.rocketinsights.android.viewmodels.PlayerViewModel
import java.util.Locale

@ExperimentalAnimationApi
@Composable
fun PlayerScreen(playerViewModel: PlayerViewModel) {
    val player = playerViewModel.player.observeAsState(initial = Player())
    val isLoading = playerViewModel.isLoading.observeAsState(initial = true)
    val isError = playerViewModel.isError.observeAsState(initial = false)
    PlayerScreenContent(
        player = player.value,
        isLoading = isLoading.value,
        isError = isError.value,
        onRefresh = playerViewModel::getPlayer
    )
}

@ExperimentalAnimationApi
@Composable
fun PlayerScreenContent(
    player: Player,
    isLoading: Boolean,
    isError: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val paddingNormal = dimensionResource(R.dimen.activity_vertical_margin)
    val paddingSmall = dimensionResource(R.dimen.margin_small)
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(all = paddingNormal)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = paddingNormal, bottom = paddingNormal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Player info
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(visible = !isLoading && !isError) {
                    // Player info labels
                    Column(horizontalAlignment = Alignment.End) {
                        val labelModifier = Modifier.padding(top = paddingSmall)
                        Text(
                            text = "${stringResource(R.string.first_name)}:",
                            modifier = labelModifier
                        )
                        Text(
                            text = "${stringResource(R.string.last_name)}:",
                            modifier = labelModifier
                        )
                        Text(
                            text = "${stringResource(R.string.position)}:",
                            modifier = labelModifier
                        )
                    }
                }
                Spacer(Modifier.width(paddingNormal))
                AnimatedVisibility(visible = !isLoading && !isError) {
                    // Player info values
                    Column(horizontalAlignment = Alignment.Start) {
                        val valueModifier = Modifier.padding(top = paddingSmall)
                        Text(text = player.firstName, modifier = valueModifier)
                        Text(text = player.lastName, modifier = valueModifier)
                        Text(text = player.position.stringValue, modifier = valueModifier)
                    }
                }
            }
            Spacer(Modifier.height(paddingNormal))
            // Refresh button
            Button(
                onClick = onRefresh,
                enabled = !isLoading
            ) {
                Text(text = stringResource(R.string.refresh).toUpperCase(Locale.ROOT))
            }
        }
        // Loading indicator
        AnimatedVisibility(visible = isLoading, modifier = Modifier.align(Alignment.Center)) {
            CircularProgressIndicator()
        }
        // Error message
        AnimatedVisibility(
            visible = isError && !isLoading, modifier = Modifier.align(Alignment.Center)
        ) {
            Text(stringResource(R.string.error_get_player))
        }
    }
}
