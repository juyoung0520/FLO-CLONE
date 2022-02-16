package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.adapter.AlbumRecyclerViewAdapter
import com.example.flo.adapter.BannerViewPagerAdapter
import com.example.flo.adapter.PanelRecyclerViewAdapter
import com.example.flo.data.Album
import com.example.flo.data.SongDatabase
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.service.AlbumService
import com.example.flo.view.AlbumView
import com.google.gson.Gson


class HomeFragment : Fragment(), AlbumView{
    private lateinit var binding: FragmentHomeBinding

    private lateinit var podcastAlbumAdapter: AlbumRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initTodayAlbumAdapter()

        initBannerAdapter()

        initPanelAdapter()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initPodcastAdapter()
        getAlbums()
    }

    private fun getAlbums() {
        val albumService = AlbumService()
        albumService.setAlbumView(this)

        albumService.getAlbums()
    }

    private fun initTodayAlbumAdapter() {
        // 데이터 리스트 생성
        val songDB = SongDatabase.getInstance(requireContext())!!
        val albums = songDB.albumDao().getAlbums() as ArrayList<Album>

        val adapter = AlbumRecyclerViewAdapter(requireContext())
        adapter.addAlbums(albums)

        binding.homeAlbumRecyclerView.adapter = adapter
        binding.homeAlbumRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        adapter.setOnItemClickListener(object : AlbumRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }
        })
    }

    private fun initPanelAdapter() {
        val panelAdapter = PanelRecyclerViewAdapter(5)
        binding.homePanelVp.adapter = panelAdapter
        binding.homePanelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.homeIndicatorCi.setViewPager(binding.homePanelVp)
    }

    private fun initBannerAdapter() {
        val bannerAdapter = BannerViewPagerAdapter(this)

        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeViewpagerVp.adapter = bannerAdapter
        binding.homeViewpagerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun initPodcastAdapter() {
        podcastAlbumAdapter = AlbumRecyclerViewAdapter(requireContext())

        binding.homePodcastRecyclerView.adapter = podcastAlbumAdapter
        binding.homePodcastRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        podcastAlbumAdapter.setOnItemClickListener(object : AlbumRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }
        })
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                    putString("fragment", "home")
                }
            })
            .commitAllowingStateLoss()
    }

    override fun onGetAlbumsLoading() {
        binding.homeLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetAlbumsSuccess(albums: ArrayList<Album>) {
        binding.homeLoadingPb.visibility = View.GONE

        podcastAlbumAdapter.addAlbums(albums)
    }

    override fun onGetAlbumsFailure(code: Int, message: String) {
        binding.homeLoadingPb.visibility = View.GONE

        when(code) {
            400 -> Log.d("ALBUM/API-ERROR", message)
        }
    }


}