package petros.efthymiou.groovy.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData

class PlaylistViewModel(
    private val repository: PlaylistRepository
) : ViewModel() {
    val playlists = liveData {
        emitSource(repository.getPlaylists().asLiveData())
    }
}
