package petros.efthymiou.groovy.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlayServiceShould : BaseUnitTest() {

    private lateinit var service: PlaylistService
    private val playlists = mock<List<Playlist>>()
    private val api = mock<PlaylistAPI>()

    @Test
    fun getPlaylistsFromAPI() = runBlockingTest {
        service = PlaylistService(api)

        service.fetchPlaylists().first()

        verify(api, times(1)).fetchAllPlaylists()
    }

    @Test
    fun convertValuesToFlowResultAndEmitsThem() = runBlockingTest {
        mockSuccessfulCase()

        assertEquals(Result.success(playlists), service.fetchPlaylists().first())
    }

    @Test
    fun emitsErrorResultWhenNetworkFails() = runBlockingTest {
        mockErrorCase()

        assertEquals(
            "Something went wrong",
            service.fetchPlaylists().first().exceptionOrNull()?.message
        )
    }

    private fun mockErrorCase() = runBlockingTest {
        whenever(api.fetchAllPlaylists()).thenThrow(RuntimeException(""))

        service = PlaylistService(api)
    }

    private fun mockSuccessfulCase() = runBlockingTest {
        whenever(api.fetchAllPlaylists()).thenReturn(playlists)

        service = PlaylistService(api)
    }

}