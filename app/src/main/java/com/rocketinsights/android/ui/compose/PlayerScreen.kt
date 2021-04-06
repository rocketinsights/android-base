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
import androidx.compose.ui.unit.dp
import com.rocketinsights.android.models.Player
import com.rocketinsights.android.viewmodels.PlayerViewModel
import java.util.*

@ExperimentalAnimationApi
@Composable
fun PlayerScreen(playerViewModel: PlayerViewModel) {
    val player = playerViewModel.player.observeAsState(initial = Player())
    val isLoading = playerViewModel.isLoading.observeAsState(initial = true)
    PlayerScreenContent(
        player = player.value,
        isLoading = isLoading.value,
        onRefresh = playerViewModel::getPlayer
    )
}

@ExperimentalAnimationApi
@Composable
fun PlayerScreenContent(
    player: Player,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(visible = !isLoading) {
                    // // Player info labels
                    Column(horizontalAlignment = Alignment.End) {
                        Row(
                            Modifier.padding(top = 8.dp)
                        ) {
                            Text(text = "First name:")
                        }
                        Row(
                            Modifier.padding(top = 8.dp)
                        ) {
                            Text(text = "Last name:")
                        }
                        Row(
                            Modifier.padding(top = 8.dp)
                        ) {
                            Text(text = "Position:")
                        }
                    }
                }
                Spacer(Modifier.width(16.dp))
                AnimatedVisibility(visible = !isLoading) {
                    // Player info values
                    Column(horizontalAlignment = Alignment.Start) {
                        Row(
                            Modifier.padding(top = 8.dp)
                        ) {
                            Text(player.firstName)
                        }
                        Row(
                            Modifier.padding(top = 8.dp)
                        ) {
                            Text(player.lastName)
                        }
                        Row(
                            Modifier.padding(top = 8.dp)
                        ) {
                            Text(player.position.stringValue)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Row {
                Button(
                    onClick = onRefresh,
                    enabled = !isLoading
                ) {
                    Text(text = "Refresh".toUpperCase(Locale.ROOT))
                }
            }
        }
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}
