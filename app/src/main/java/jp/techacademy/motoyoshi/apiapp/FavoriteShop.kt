package jp.techacademy.motoyoshi.apiapp

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class FavoriteShop(id: String, imageUrl: String, name: String, url: String, favFlg: Boolean) : RealmObject {
    @PrimaryKey
    var id: String = ""
    var imageUrl: String = ""
    var name: String = ""
    var url: String = ""
    var favFlg: Boolean

    // 初期化処理
    init {
        this.id = id
        this.imageUrl = imageUrl
        this.name = name
        this.url = url
        this.favFlg = favFlg
    }

    // realm内部呼び出し用にコンストラクタを用意
    constructor() : this("", "", "", "", favFlg = false)

    //companion object : RealmObject {}
        companion object {
        /**
         * お気に入りのShopを全件取得
         */
        fun findAll(): List<FavoriteShop> {
            // Realmデータベースとの接続を開く
            val config = RealmConfiguration.create(schema = setOf(FavoriteShop::class))
            val realm = Realm.open(config)

            // Realmデータベースからお気に入り情報を取得
            // mapでディープコピーしてresultに代入する
            val result = realm.query<FavoriteShop>().find()
                .map { FavoriteShop(it.id, it.imageUrl, it.name, it.url, it.favFlg) }

            // Realmデータベースとの接続を閉じる
            realm.close()

            return result
        }

        /**
         * お気に入りされているShopをidで検索して返す
         * お気に入りに登録されていなければnullで返す
         */
        fun findBy(id: String): FavoriteShop? {
            // Realmデータベースとの接続を開く
            val config = RealmConfiguration.create(schema = setOf(FavoriteShop::class))
            val realm = Realm.open(config)

            val result = realm.query<FavoriteShop>("id == $0 AND favFlg == true", id).first().find()

            // Realmデータベースとの接続を閉じる
            realm.close()

            return result
        }

        /**
         * お気に入り追加
         */
        fun insert(favoriteShop: FavoriteShop) {
            // Realmデータベースとの接続を開く
            val config = RealmConfiguration.create(schema = setOf(FavoriteShop::class))
            val realm = Realm.open(config)

            realm.writeBlocking {
                // 既に同じIDのお気に入りが存在するかチェック
                val existingShop = query<FavoriteShop>("id == $0", favoriteShop.id).first().find()

                if (existingShop != null) {
                    // 既に存在する場合は、favFlgをtrueに更新
                    existingShop.favFlg = true
                } else {
                    // 存在しない場合は新しいオブジェクトを追加
                    // favFlgがtrueに設定された状態で渡されることを前提としている
                    copyToRealm(favoriteShop)
                }
            }

            // Realmデータベースとの接続を閉じる
            realm.close()
        }

        /**
         * idでお気に入りから削除する
         */
        fun delete(id: String) {
            // Realmデータベースとの接続を開く
            val config = RealmConfiguration.create(schema = setOf(FavoriteShop::class))
            val realm = Realm.open(config)

            // 削除処理
            realm.writeBlocking {
                val favoriteShops = query<FavoriteShop>("id=='$id'").find()
                favoriteShops.forEach {
                    //deleteではなくupdateで処理
                    it.favFlg = false
                    //delete(it)
                }
            }

            // Realmデータベースとの接続を閉じる
            realm.close()
        }
    }
}


