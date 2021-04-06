package com.rocketinsights.android.repos

import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.models.Player
import com.rocketinsights.android.models.Position
import kotlinx.coroutines.withContext

class PlayerRepository(private val dispatcher: DispatcherProvider) {

    suspend fun getPlayer(): Player = withContext(dispatcher.io()) {
        getCroatianFootballPlayers().random()
    }

    private fun getCroatianFootballPlayers() = listOf(
        Player("Luka", "Modrić", Position.MF),
        Player("Ivan", "Rakitić", Position.MF),
        Player("Vedran", "Ćorluka", Position.DF),
        Player("Dario", "Šimić", Position.DF),
        Player("Ivan", "Perišić", Position.MF),
        Player("Mario", "Mandžukić", Position.FW),
        Player("Domagoj", "Vida", Position.DF),
        Player("Robert", "Jarni", Position.MF),
        Player("Davor", "Šuker", Position.FW),
        Player("Mateo", "Kovačić", Position.MF),
        Player("Dejan", "Lovren", Position.DF),
        Player("Aljoša", "Asanović", Position.MF),
        Player("Zvonimir", "Soldo", Position.DF),
        Player("Dražen", "Ladić", Position.GK),
        Player("Marcelo", "Brozović", Position.MF),
        Player("Igor", "Tudor", Position.DF),
        Player("Milan", "Badelj", Position.MF),
        Player("Igor", "Štimac", Position.DF),
        Player("Goran", "Vlaović", Position.FW),
        Player("Andrej", "Kramarić", Position.FW),
        Player("Zvonimir", "Boban", Position.MF),
        Player("Robert", "Prosinečki", Position.MF),
        Player("Mario", "Stanić", Position.MF),
        Player("Ivan", "Strinić", Position.DF),
        Player("Šime", "Vrsaljko", Position.DF),
        Player("Slaven", "Bilić", Position.DF),
        Player("Danijel", "Subašić", Position.GK),
        Player("Nikola", "Kalinić", Position.FW),
        Player("Ante", "Rebić", Position.FW),
        Player("Tin", "Jedvaj", Position.DF),
        Player("Josip", "Pivarić", Position.DF),
        Player("Marko", "Pjaca", Position.MF),
        Player("Krunoslav", "Jurčić", Position.MF)
    )
}
