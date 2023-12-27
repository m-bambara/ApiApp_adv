package jp.techacademy.motoyoshi.apiapp

interface FragmentCallback {
    // Itemを押したときの処理
    //お店の情報すべてを渡すように切り替える
    fun onClickItem(id: String, imageUrl:String, name: String, url: String )

    // お気に入り追加時の処理
    fun onAddFavorite(shop: Shop)

    // お気に入り削除時の処理
    fun onDeleteFavorite(id: String)
}