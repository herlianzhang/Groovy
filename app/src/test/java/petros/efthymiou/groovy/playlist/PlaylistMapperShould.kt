package petros.efthymiou.groovy.playlist

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import petros.efthymiou.groovy.R
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlaylistMapperShould : BaseUnitTest() {

    private val playlistRaw = PlaylistRaw("1", "da name", "da category")
    private val playlistRockRaw = PlaylistRaw("1", "da name", "rock")

    private val mapper = PlaylistMapper()

    private val playlist = mapper.apply(listOf(playlistRaw))[0]

    private val playlistRock = mapper.apply(listOf(playlistRockRaw))[0]

    @Test
    fun `keep same id`() {
        assertThat(playlist.id).isEqualTo(playlistRaw.id)
    }

    @Test
    fun `keep same name`() {
        assertThat(playlist.name).isEqualTo(playlistRaw.name)
    }

    @Test
    fun `keep same category`() {
        assertThat(playlist.category).isEqualTo(playlistRaw.category)
    }

    @Test
    fun `map default image when not rock`() {
        assertThat(playlist.image).isEqualTo(R.mipmap.playlist)
    }

    @Test
    fun `map rock image when rock category`() {
        assertThat(playlistRock.image).isEqualTo(R.mipmap.rock)
    }
}