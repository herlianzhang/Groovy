package petros.efthymiou.groovy.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import petros.efthymiou.groovy.databinding.FragmentItemListBinding

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentItemListBinding
    private val viewModel: PlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemListBinding.inflate(inflater, container, false)

        viewModel.loader.observe(viewLifecycleOwner) { loader ->
            binding.loader.isVisible = loader
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            val mPlaylists = playlists.getOrNull()
            if (mPlaylists != null)
                setupList(mPlaylists)
            else {
                //TODO
            }
        }

        return binding.root
    }

    private fun setupList(playlists: List<Playlist>) {
        binding.playlistsList.apply {
            layoutManager = LinearLayoutManager(context)

            adapter = MyPlaylistRecyclerViewAdapter(playlists)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlaylistFragment()
    }
}