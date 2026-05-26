package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.flow.Flow

class JfcRepository(private val dao: JfcDao) {

    // --- Profile ---
    val profileFlow: Flow<UserProfile?> = dao.getProfileFlow()

    suspend fun getProfile(): UserProfile? = dao.getProfile()

    suspend fun updateProfile(profile: UserProfile) {
        dao.insertProfile(profile)
    }

    // --- Prayer Requests ---
    val allPrayerRequests: Flow<List<PrayerRequest>> = dao.getAllPrayerRequests()

    suspend fun addPrayerRequest(request: PrayerRequest) {
        dao.insertPrayerRequest(request)
    }

    suspend fun addPastoralResponse(id: Int, response: String) {
        dao.updatePastoralResponse(id, response)
    }

    suspend fun removePrayerRequest(id: Int) {
        dao.deletePrayerRequest(id)
    }

    // --- Home Bookings ---
    val allHomeBookings: Flow<List<HomeBooking>> = dao.getAllHomeBookings()

    suspend fun addHomeBooking(booking: HomeBooking) {
        dao.insertHomeBooking(booking)
    }

    suspend fun updateHomeBookingStatus(id: Int, status: String) {
        dao.updateBookingStatus(id, status)
    }

    // --- Favorite Songs ---
    val favoriteSongs: Flow<List<FavoriteSong>> = dao.getFavoriteSongs()

    suspend fun addFavoriteSong(songId: String, title: String) {
        dao.insertFavoriteSong(FavoriteSong(songId, title))
    }

    suspend fun removeFavoriteSong(songId: String) {
        dao.removeFavoriteSong(songId)
    }

    // --- Bookmarked Verses ---
    val bookmarkedVerses: Flow<List<BookmarkedVerse>> = dao.getBookmarkedVerses()

    suspend fun addBookmark(ref: String, eng: String, kan: String) {
        dao.insertBookmarkedVerse(BookmarkedVerse(ref, eng, kan))
    }

    suspend fun removeBookmark(ref: String) {
        dao.removeBookmarkedVerse(ref)
    }

    // --- Testimonies ---
    val allTestimonies: Flow<List<Testimony>> = dao.getAllTestimonies()

    suspend fun submitTestimony(testimony: Testimony) {
        dao.insertTestimony(testimony)
    }

    suspend fun approveTestimony(id: Int, approved: Boolean) {
        dao.updateTestimonyApproval(id, approved)
    }

    suspend fun deleteTestimony(id: Int) {
        dao.deleteTestimony(id)
    }

    // --- Admin Written Song Posts ---
    val allUserSongs: Flow<List<UserSong>> = dao.getAllUserSongs()

    suspend fun addUserSong(song: UserSong) {
        dao.insertUserSong(song)
    }

    suspend fun removeUserSong(id: Int) {
        dao.deleteUserSong(id)
    }
}
