package petros.efthymiou.groovy.playlist

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlayServiceShould : BaseUnitTest() {

    private lateinit var service: PlaylistService
    private val playlists: List<PlaylistRaw> = mock()
    private val api: PlaylistAPI = mock()

    @Test
    fun `get playlists from API`() = runBlockingTest {
        service = PlaylistService(api)

        service.fetchPlaylists().first()

        verify(api, times(1)).fetchAllPlaylists()
    }

    @Test
    fun `convert values to flow result and emits them`() = runBlockingTest {
        mockSuccessfulCase()

        assertThat(service.fetchPlaylists().first()).isEqualTo(Result.success(playlists))
    }

    @Test
    fun `emits error result when network fails`() = runBlockingTest {
        mockErrorCase()

        assertThat(
            service.fetchPlaylists().first().exceptionOrNull()?.message
        ).isEqualTo("Something went wrong")
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