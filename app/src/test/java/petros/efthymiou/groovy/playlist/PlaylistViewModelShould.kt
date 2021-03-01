package petros.efthymiou.groovy.playlist

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import petros.efthymiou.groovy.utils.BaseUnitTest
import petros.efthymiou.groovy.utils.captureValues
import petros.efthymiou.groovy.utils.getValueForTest

class PlaylistViewModelShould : BaseUnitTest() {

    private val repository: PlaylistRepository = mock()
    private val playlists: List<Playlist> = mock()
    private val expected = Result.success(playlists)
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun `get playlists from repository`() = runBlockingTest {
        val viewModel = mockSuccesfulCase()

        viewModel.playlists.getValueForTest()

        verify(repository, times(1)).getPlaylists()
    }

    @Test
    fun `emits playlists from repository`() = runBlockingTest {
        val viewModel = mockSuccesfulCase()

        assertThat(viewModel.playlists.getValueForTest()).isEqualTo(expected)
    }

    @Test
    fun `emit error when receive error`() {
        val viewModel = mockErrorCase()

        assertThat(viewModel.playlists.getValueForTest()?.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `show spinner while loading`() = runBlockingTest {
        val viewModel = mockSuccesfulCase()

        viewModel.loader.captureValues {
            viewModel.playlists.getValueForTest()

            assertThat(values[0]).isTrue()
        }
    }

    @Test
    fun `close loader after playlists load`() = runBlockingTest {
        val viewModel = mockSuccesfulCase()

        viewModel.loader.captureValues {
            viewModel.playlists.getValueForTest()

            assertThat(values.last()).isFalse()
        }
    }

    @Test
    fun `close loader after error`() = runBlockingTest {
        val viewModel = mockErrorCase()

        viewModel.loader.captureValues {
            viewModel.playlists.getValueForTest()

            assertThat(values.last()).isFalse()
        }
    }

    private fun mockErrorCase(): PlaylistViewModel {
        runBlockingTest {
            whenever(repository.getPlaylists()).thenReturn(
                flow {
                    emit(Result.failure<List<Playlist>>(exception))
                }
            )
        }
        return PlaylistViewModel(repository)
    }

    private fun mockSuccesfulCase(): PlaylistViewModel {
        runBlockingTest {
            whenever(repository.getPlaylists()).thenReturn(
                flow {
                    emit(expected)
                }
            )
        }

        return PlaylistViewModel(repository)
    }
}