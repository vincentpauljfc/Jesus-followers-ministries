package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Single profile row
    val name: String = "",
    val phone: String = "",
    val familyDetails: String = "",
    val prayerInterests: String = "",
    val baptismStatus: String = "No",
    val ministryGroup: String = "General"
)

@Entity(tableName = "prayer_requests")
data class PrayerRequest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val request: String,
    val category: String, // Healing, Family, Youth, Emergency, General
    val isAnonymous: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val pastoralResponse: String? = null // Pastor response simulation
)

@Entity(tableName = "home_bookings")
data class HomeBooking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val purpose: String, // House blessing, Worship at home, counseling, fasting prayer
    val familyCount: Int,
    val date: String,
    val preferredTime: String,
    val phone: String,
    val address: String,
    val status: String = "Pending", // Pending, Scheduled, Completed
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_songs")
data class FavoriteSong(
    @PrimaryKey val songId: String,
    val title: String,
    val isFavorite: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarked_verses")
data class BookmarkedVerse(
    @PrimaryKey val verseRef: String, // e.g., "John 3:16"
    val contentEnglish: String,
    val contentKannada: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "testimonies")
data class Testimony(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val content: String,
    val category: String, // Healing, Saving, Miracle, General
    val isApproved: Boolean = false, // Mock admin can approve
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_songs")
data class UserSong(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val chordsKeys: String = "KEY: G Major | Tempo: 85 BPM",
    val lyricsKannada: String,
    val lyricsEnglish: String,
    val category: String = "Worship",
    val authorName: String,
    val authorPhone: String,
    val timestamp: Long = System.currentTimeMillis()
)

