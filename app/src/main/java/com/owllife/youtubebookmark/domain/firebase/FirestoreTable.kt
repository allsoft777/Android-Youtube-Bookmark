package com.owllife.youtubebookmark.domain.firebase

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
object FirestoreTable {

    object UserProfile {
        const val COLLECTION_NAME = "user"
        const val DISPLAY_NAME = "display_name"
        const val EMAIL = "email"
        const val PHOTO_URL = "photo_url"
        const val CREATED_TS = "created_ts"
        const val MEMBERSHIP_LEVEL = "membership_level"
        const val ML_ADMIN = 999
    }
}