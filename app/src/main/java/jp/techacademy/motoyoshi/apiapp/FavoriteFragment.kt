package jp.techacademy.motoyoshi.apiapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.techacademy.motoyoshi.apiapp.databinding.FragmentApiBinding


class FavoriteFragment: Fragment() {
    private var _binding: FragmentApiBinding? = null
    private val binding get() = _binding!!

    private val favoriteAdapter by lazy { FavoriteAdapter() }

    // FavoriteFragment -> MainActivity に削除を通知する
    private var fragmentCallback: FragmentCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentCallback) {
            fragmentCallback = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // ここから初期化処理を行う
        // FavoriteAdapterのお気に入り削除用のメソッドの追加を行う
        favoriteAdapter.apply {
            // Adapterの処理をそのままActivityに通知
            onClickDeleteFavorite = {
                fragmentCallback?.onDeleteFavorite(it.id)
            }
            // Itemをクリックしたとき
            onClickItem = {
                fragmentCallback?.onClickItem(it.id, it.imageUrl, it.name, it.url)
            }
        }
        // RecyclerViewの初期化
        binding.recyclerView.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(requireContext()) // 一列ずつ表示
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            updateData()
        }
        updateData()
    }
    override fun onResume() {
        super.onResume()
        // FavoriteFragmentの更新
        updateData()
        }


    fun updateData() {
        favoriteAdapter.submitList(FavoriteShop.findAll())
        binding.swipeRefreshLayout.isRefreshing = false
    }
}