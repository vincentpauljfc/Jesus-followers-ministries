package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JfcDao {

    // --- Profile ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    // --- Prayer Requests ---
    @Query("SELECT * FROM prayer_requests ORDER BY timestamp DESC")
    fun getAllPrayerRequests(): Flow<List<PrayerRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerRequest(request: PrayerRequest)

    @Query("UPDATE prayer_requests SET pastoralResponse = :response WHERE id = :id")
    suspend fun updatePastoralResponse(id: Int, response: String)

    @Query("DELETE FROM prayer_requests WHERE id = :id")
    suspend fun deletePrayerRequest(id: Int)

    // --- Home Bookings ---
    @Query("SELECT * FROM home_bookings ORDER BY timestamp DESC")
    fun getAllHomeBookings(): Flow<List<HomeBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeBooking(booking: HomeBooking)

    @Query("UPDATE home_bookings SET status = :status WHERE id = :id")
    suspend fun updateBookingStatus(id: Int, status: String)

    // --- Favorite Songs ---
    @Query("SELECT * FROM favorite_songs ORDER BY timestamp DESC")
    fun getFavoriteSongs(): Flow<List<FavoriteSong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteSong(song: FavoriteSong)

    @Query("DELETE FROM favorite_songs WHERE songId = :songId")
    suspend fun removeFavoriteSong(songId: String)

    // --- Bookmarked Verses ---
    @Query("SELECT * FROM bookmarked_verses ORDER BY timestamp DESC")
    fun getBookmarkedVerses(): Flow<List<BookmarkedVerse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkedVerse(verse: BookmarkedVerse)

    @Query("DELETE FROM bookmarked_verses WHERE verseRef = :verseRef")
    suspend fun removeBookmarkedVerse(verseRef: String)

    // --- Testimonies ---
    @Query("SELECT * FROM testimonies ORDER BY timestamp DESC")
    fun getAllTestimonies(): Flow<List<Testimony>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestimony(testimony: Testimony)

    @Query("UPDATE testimonies SET isApproved = :approved WHERE id = :id")
    suspend fun updateTestimonyApproval(id: Int, approved: Boolean)

    @Query("DELETE FROM testimonies WHERE id = :id")
    suspend fun deleteTestimony(id: Int)

    // --- Admin Written Song Posts ---
    @Query("SELECT * FROM user_songs ORDER BY timestamp DESC")
    fun getAllUserSongs(): Flow<List<UserSong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSong(song: UserSong)

    @Query("DELETE FROM user_songs WHERE id = :id")
    suspend fun deleteUserSong(id: Int)
}
