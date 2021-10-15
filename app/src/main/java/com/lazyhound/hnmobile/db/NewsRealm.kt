package com.lazyhound.hnmobile.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class NewsRealm(
    @PrimaryKey
    var story_id: Int = 0,
    var story_title: String? = "",
    var author: String? = "",
    var created_at: String? = "",
    var story_url: String? = "",
    var title: String? = "",
    var active: Boolean? = true
): RealmObject()