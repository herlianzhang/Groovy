package petros.efthymiou.groovy.playlist

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlaylistRepositoryShould : BaseUnitTest() {

    private val service: PlaylistService = mock()
    private val mapper: PlaylistMapper = mock()
    private val playlists: List<Playlist> = mock()
    private val playlistRaw: List<PlaylistRaw> = mock()
    private val exception: RuntimeException = mock()

    @Test
    fun `gets playlists from service`() = runBlockingTest {
        val repository = mockSuccessfulCase()

        repository.getPlaylists()

        verify(service, times(1)).fetchPlaylists()
    }

    @Test
    fun `emit mapped playlists from service`() = runBlockingTest {
        val repository = mockSuccessfulCase()

        assertThat(repository.getPlaylists().first().getOrNull()).isEqualTo(playlists)
    }

    @Test
    fun `propagate error`() = runBlockingTest {
        val repository = mockFailureCase()

        assertThat(repository.getPlaylists().first().exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `delegate business logic to mapper`() = runBlockingTest {
        val repository = mockSuccessfulCase()

        repository.getPlaylists().first()

        verify(mapper, times(1)).apply(playlistRaw)
    }

    private suspend fun mockFailureCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.failure<List<PlaylistRaw>>(exception))
            }
        )
        return PlaylistRepository(service, mapper)
    }

    private suspend fun mockSuccessfulCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.success(playlistRaw))
            }
        )

        whenever(mapper.apply(playlistRaw)).thenReturn(playlists)
        return PlaylistRepository(service, mapper)
    }
}