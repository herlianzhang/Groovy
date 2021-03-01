package petros.efthymiou.groovy.playlist

import androidx.arch.core.util.Function
import petros.efthymiou.groovy.R
import javax.inject.Inject

class PlaylistMapper @Inject constructor() : Function<List<PlaylistRaw>, List<Playlist>> {
    override fun apply(input: List<PlaylistRaw>): List<Playlist> {
        return input.map {
            val image = when (it.category) {
                "rock" -> R.mipmap.rock
                else -> R.mipmap.playlist
            }
            Playlist(it.id, it.name, it.category, image)
        }
    }
}
