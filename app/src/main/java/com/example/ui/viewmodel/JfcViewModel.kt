package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiClient
import com.example.data.local.*
import com.example.data.model.BibleVerse
import com.example.data.model.ChurchMediaData
import com.example.data.model.SermonMedia
import com.example.data.model.WorshipSong
import com.example.data.repository.JfcRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class JfcTab {
    HOME, BIBLE, SONGS, MEDIA, EVENTS, PRAYER
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val senderName: String = "Assistant",
    val timeLabel: String = "Now"
)

data class LiveChatComment(
    val user: String,
    val text: String,
    val avatarColorIndex: Int
)

class JfcViewModel(
    application: Application,
    private val repository: JfcRepository
) : AndroidViewModel(application) {

    // --- Tab Navigation Sizing ---
    var currentTab by mutableStateOf(JfcTab.HOME)

    // --- Admin / Pastor Mode ---
    var isPastorMode by mutableStateOf(false)

    // --- Authentication Profile State ---
    var isUserLoggedIn by mutableStateOf(false)
    val userProfile: StateFlow<UserProfile?> = repository.profileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // --- UI/UX Message Logs ---
    var snackbarMessage by mutableStateOf<String?>(null)

    // --- Bible Module State ---
    var bibleSearchQuery by mutableStateOf("")
    var selectedBibleLanguage by mutableStateOf("Both") // Kannada, English, Both
    val bookmarkedVerses: StateFlow<List<BookmarkedVerse>> = repository.bookmarkedVerses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Full Bible Explorer states (English / Kannada)
    var selectedExploreBook by mutableStateOf("John")
    var selectedExploreChapter by mutableStateOf(3)
    var showBibleExplorer by mutableStateOf(true) // Default to true so they immediately see the amazing full bible!

    // --- Songbook State ---
    var songSearchQuery by mutableStateOf("")
    var songCategoryFilter by mutableStateOf("All")
    var selectedSong by mutableStateOf<WorshipSong?>(null)
    var selectedUserSong by mutableStateOf<UserSong?>(null)
    val favoriteSongsList: StateFlow<List<FavoriteSong>> = repository.favoriteSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allUserSongs: StateFlow<List<UserSong>> = repository.allUserSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Post new song forms for Admin Writing Post
    var adminSongTitle by mutableStateOf("")
    var adminSongChords by mutableStateOf("G Major")
    var adminSongLyricsKannada by mutableStateOf("")
    var adminSongLyricsEnglish by mutableStateOf("")
    var adminSongCategory by mutableStateOf("Worship")
    var adminSongContributorName by mutableStateOf("")
    var adminSongContributorPhone by mutableStateOf("")

    // --- Songbook Audio Playback Simulator ---
    var isAudioPlaying by mutableStateOf(false)
    var audioProgressSec by mutableStateOf(0)
    var currentKaraokeLine by mutableStateOf(0)
    private var playbackJob: Job? = null

    // --- Sermons & Media state ---
    var sermonCategoryFilter by mutableStateOf("All")
    var selectedSermon by mutableStateOf<SermonMedia?>(null)
    var liveStreamingEnabled by mutableStateOf(false)
    val liveChatComments = mutableStateListOf<LiveChatComment>()
    private var liveChatJob: Job? = null

    // --- Prayer Request state ---
    val allPrayerRequests: StateFlow<List<PrayerRequest>> = repository.allPrayerRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    var newPrayerRequestName by mutableStateOf("")
    var newPrayerRequestText by mutableStateOf("")
    var newPrayerCategory by mutableStateOf("General")
    var newPrayerAnonymous by mutableStateOf(false)

    // --- Home Prayer Booking State ---
    val allHomeBookings: StateFlow<List<HomeBooking>> = repository.allHomeBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    var bookPurpose by mutableStateOf("House Blessing")
    var bookFamilyCount by mutableStateOf(4)
    var bookDate by mutableStateOf("")
    var bookTime by mutableStateOf("06:00 PM")
    var bookPhone by mutableStateOf("")
    var bookAddress by mutableStateOf("")

    // --- Testimonies State ---
    val allTestimoniesList: StateFlow<List<Testimony>> = repository.allTestimonies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    var newTestimonyTitle by mutableStateOf("")
    var newTestimonyContent by mutableStateOf("")
    var newTestimonyCategory by mutableStateOf("Healing")
    var newTestimonyAuthor by mutableStateOf("")

    // --- Events & QR System state ---
    val registeredEventsList = mutableStateSetOf<String>() // Set of event IDs
    var countdownText by mutableStateOf("Calculated...")
    private var countdownJob: Job? = null

    // --- Donation Panel state ---
    var customDonationAmount by mutableStateOf("500")
    var selectedDonationType by mutableStateOf("UPI") // Razorpay, QR, UPI
    var showDonationSuccessDialog by mutableStateOf(false)

    // --- AI Bible Assistant state ---
    val aiChatHistory = mutableStateListOf<ChatMessage>()
    var aiChatInput by mutableStateOf("")
    var isAiGenerating by mutableStateOf(false)

    // Initial Database Prepopulation & Triggers
    init {
        // Pre-fill profile check, if database is empty set default empty profile
        viewModelScope.launch {
            val existing = repository.getProfile()
            if (existing == null) {
                repository.updateProfile(UserProfile(1, "Brother Vincent", "9876543210", "4 members", "Worship & Youth ministry", "Yes", "Worship Choir"))
            }
        }
        
        // Start ticking timer metrics
        startEventCountdown()
        
        // Pre-configure some dummy comments for the live-stream chats
        liveChatComments.add(LiveChatComment("Sister Suma", "God bless JFC Church! Praise the Lord. 🙏", 0))
        liveChatComments.add(LiveChatComment("Brother David", "Watching from Davanagere. Touch of Holy Spirit is heavy here!", 1))
        liveChatComments.add(LiveChatComment("Joy Pastor", "Amen. Keep praying brothers.", 2))

        // Prepopulate standard default local testimonies if database is empty
        viewModelScope.launch {
            repository.allTestimonies.first().let { testimonies ->
                if (testimonies.isEmpty()) {
                    repository.submitTestimony(Testimony(
                        title = "Miraculous Chronic Knee Healing",
                        author = "Geetha Davanagere",
                        content = "During last Friday revival fasting prayer, Pastor Vincent prayed for limb restoration. I felt an immediate warm electrical current pass through my knees. Today I can run up the stairs completely pain free after 12 years of clinical arthritis! Praise Jesus!",
                        category = "Healing",
                        isApproved = true
                    ))
                    repository.submitTestimony(Testimony(
                        title = "Salvation of My Whole Family",
                        author = "Kiran Kumar",
                        content = "My family was broken and without peace. After visiting JFC Connect online and observing worship, we came to service. Joy filled our rooms. We took baptism last month together! Truly Christ saves.",
                        category = "Salvation",
                        isApproved = true
                    ))
                }
            }
        }
    }

    // Help Helper for State Lists & Sets mutability
    private fun <T> mutableStateListOf() = androidx.compose.runtime.mutableStateListOf<T>()
    private fun <T> mutableStateSetOf() = androidx.compose.runtime.mutableStateListOf<T>()

    // --- Authentication Toggles ---
    fun performLogin(mobile: String) {
        viewModelScope.launch {
            repository.updateProfile(UserProfile(1, "Brother Vincent", mobile, "3 members", "Worship and family", "Yes", "Worship Team"))
            isUserLoggedIn = true
            showSnackbar("Logged in successfully via secure Mobile OTP simulation!")
        }
    }

    fun performGoogleSignIn() {
        viewModelScope.launch {
            repository.updateProfile(UserProfile(1, "Brother Paul", "paul.jfc@gmail.com", "1 member", "Evangelism & Media", "Yes", "Youth Group"))
            isUserLoggedIn = true
            showSnackbar("Logged in successfully via secure Google Sign-In!")
        }
    }

    fun updateProfile(name: String, phone: String, family: String, interests: String, baptism: String, group: String) {
        viewModelScope.launch {
            repository.updateProfile(UserProfile(1, name, phone, family, interests, baptism, group))
            showSnackbar("Profile details saved securely!")
        }
    }

    fun handleLogout() {
        isUserLoggedIn = false
        showSnackbar("Logged out securely.")
    }

    // --- Snackbar Alert Launcher ---
    fun showSnackbar(message: String) {
        snackbarMessage = message
        viewModelScope.launch {
            delay(3000)
            if (snackbarMessage == message) {
                snackbarMessage = null
            }
        }
    }

    // --- Bible Bookmark toggles ---
    fun isVerseBookmarked(ref: String): Boolean {
        return bookmarkedVerses.value.any { it.verseRef == ref }
    }

    fun toggleVerseBookmark(verse: BibleVerse) {
        viewModelScope.launch {
            if (isVerseBookmarked("${verse.book} ${verse.chapter}:${verse.verse}")) {
                repository.removeBookmark("${verse.book} ${verse.chapter}:${verse.verse}")
                showSnackbar("Removed bookmark.")
            } else {
                repository.addBookmark(
                    "${verse.book} ${verse.chapter}:${verse.verse}",
                    verse.textEnglish,
                    verse.textKannada
                )
                showSnackbar("Saved bookmark to offline Bible tab!")
            }
        }
    }

    // --- Songbook Favorites toggles ---
    fun isSongFav(songId: String): Boolean {
        return favoriteSongsList.value.any { it.songId == songId }
    }

    fun toggleSongFavorite(song: WorshipSong) {
        viewModelScope.launch {
            if (isSongFav(song.id)) {
                repository.removeFavoriteSong(song.id)
                showSnackbar("Removed from favorites.")
            } else {
                repository.addFavoriteSong(song.id, song.title)
                showSnackbar("Added to worship favorites!")
            }
        }
    }

    // --- Audio Playback and Scrolling Karaoke Simulator ---
    fun selectWorshipSong(song: WorshipSong) {
        selectedSong = song
        selectedUserSong = null
        currentTab = JfcTab.SONGS
        // Stop previous audio simulation
        stopAudioPlayback()
    }

    fun selectUserWorshipSong(song: UserSong) {
        selectedUserSong = song
        selectedSong = null
        currentTab = JfcTab.SONGS
        stopAudioPlayback()
    }

    fun submitAdminSong() {
        val title = adminSongTitle.trim()
        val textKannada = adminSongLyricsKannada.trim()
        val name = adminSongContributorName.trim()
        val phone = adminSongContributorPhone.trim()
        if (title.isEmpty() || textKannada.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            showSnackbar("Please fill Title, Kannada Lyrics, Creator Name & Number.")
            return
        }
        viewModelScope.launch {
            repository.addUserSong(
                UserSong(
                    title = title,
                    chordsKeys = "KEY: $adminSongChords | Contributor: $name",
                    lyricsKannada = textKannada,
                    lyricsEnglish = adminSongLyricsEnglish.trim(),
                    category = adminSongCategory,
                    authorName = name,
                    authorPhone = phone
                )
            )
            // Clear inputs
            adminSongTitle = ""
            adminSongLyricsKannada = ""
            adminSongLyricsEnglish = ""
            adminSongContributorName = ""
            adminSongContributorPhone = ""
            showSnackbar("Worship song published successfully!")
        }
    }

    fun deleteUserSongAsAdmin(id: Int) {
        viewModelScope.launch {
            repository.removeUserSong(id)
            showSnackbar("Custom song deleted securely!")
        }
    }

    fun togglePlayWorshipSong() {
        if (isAudioPlaying) {
            stopAudioPlayback()
        } else {
            startAudioPlayback()
        }
    }

    private fun startAudioPlayback() {
        isAudioPlaying = true
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (isAudioPlaying && audioProgressSec < 272) {
                delay(1000)
                audioProgressSec += 1
                // Automatically scroll lyrics/karaoke every 12 seconds mockingly
                currentKaraokeLine = (audioProgressSec / 12) % 4
            }
            if (audioProgressSec >= 272) {
                stopAudioPlayback()
                audioProgressSec = 0
            }
        }
    }

    fun stopAudioPlayback() {
        isAudioPlaying = false
        playbackJob?.cancel()
    }

    // --- Prayer Request actions ---
    fun submitNewPrayerRequest() {
        val nameToSave = if (newPrayerAnonymous) "Anonymous Believer" else newPrayerRequestName.trim()
        val textToSave = newPrayerRequestText.trim()
        if (textToSave.isEmpty()) {
            showSnackbar("Please describe your prayer need.")
            return
        }
        viewModelScope.launch {
            repository.addPrayerRequest(
                PrayerRequest(
                    name = if (nameToSave.isEmpty()) "JFC Believer" else nameToSave,
                    request = textToSave,
                    category = newPrayerCategory,
                    isAnonymous = newPrayerAnonymous
                )
            )
            newPrayerRequestName = ""
            newPrayerRequestText = ""
            showSnackbar("Prayer request sent to Pastor. Heavens are listening!")
        }
    }

    fun deletePrayerAsAdmin(id: Int) {
        viewModelScope.launch {
            repository.removePrayerRequest(id)
            showSnackbar("Prayer request archived by pastor.")
        }
    }

    fun submitPastoralResponse(id: Int, text: String) {
        if (text.trim().isEmpty()) return
        viewModelScope.launch {
            repository.addPastoralResponse(id, text.trim())
            showSnackbar("Sent blessing note and responses!")
        }
    }

    // --- Home Prayer Booking Actions ---
    fun requestHomePrayerBooking() {
        val phoneNum = bookPhone.trim()
        val addr = bookAddress.trim()
        if (phoneNum.isEmpty() || addr.isEmpty()) {
            showSnackbar("Please enter contact phone and house address.")
            return
        }
        if (bookDate.isEmpty()) {
            showSnackbar("Please select a date.")
            return
        }
        viewModelScope.launch {
            repository.addHomeBooking(
                HomeBooking(
                    purpose = bookPurpose,
                    familyCount = bookFamilyCount,
                    date = bookDate,
                    preferredTime = bookTime,
                    phone = phoneNum,
                    address = addr
                )
            )
            bookPhone = ""
            bookAddress = ""
            showSnackbar("Home prayer booked! Church office will schedule soon.")
        }
    }

    fun updateBookingStatusAsPastor(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateHomeBookingStatus(id, status)
            showSnackbar("Status updated to '$status'")
        }
    }

    // --- Testimony Module actions ---
    fun submitNewTestimony() {
        val title = newTestimonyTitle.trim()
        val text = newTestimonyContent.trim()
        val author = newTestimonyAuthor.trim()
        if (title.isEmpty() || text.isEmpty() || author.isEmpty()) {
            showSnackbar("Please fill in the title, content, and author.")
            return
        }
        viewModelScope.launch {
            repository.submitTestimony(
                Testimony(
                    title = title,
                    author = author,
                    content = text,
                    category = newTestimonyCategory,
                    isApproved = false // Pending approval by pastor mode
                )
            )
            newTestimonyTitle = ""
            newTestimonyContent = ""
            newTestimonyAuthor = ""
            showSnackbar("Testimony submitted! It will appear after Pastor review.")
        }
    }

    fun approveTestimonyAsAdmin(id: Int) {
        viewModelScope.launch {
            repository.approveTestimony(id, true)
            showSnackbar("Testimony verified and published to church wall!")
        }
    }

    fun deleteTestimonyAsAdmin(id: Int) {
        viewModelScope.launch {
            repository.deleteTestimony(id)
            showSnackbar("Testimony removed.")
        }
    }

    // --- Sermon Video / Live Stream Controls ---
    fun selectSermonVideo(sermon: SermonMedia) {
        selectedSermon = sermon
        currentTab = JfcTab.MEDIA
        stopAudioPlayback() // Switch off audio song
    }

    fun startLiveStream() {
        liveStreamingEnabled = true
        selectedSermon = null // Enforce live screen
        stopAudioPlayback()

        // Spin up active simulated real-time comment chat!
        liveChatJob?.cancel()
        liveChatJob = viewModelScope.launch {
            val names = listOf("Sister Reena", "Brother Manoj", "Prabhu Kumar", "Pastor Joy", "Esther Davg", "Sister Shilpa", "Anil Kumar")
            val comments = listOf(
                "Amen! High praise to King of Kings!",
                "Watching live with my family. Touch us Lord.",
                "Thank you Jesus Followers Ministries! 🎵",
                "Powerful healing fire descending!",
                "Sending prayer request for my daughter's exam.",
                "Sabbath blessings to everyone at JFC!",
                "Amazing sound and worship leads."
            )
            while (liveStreamingEnabled) {
                delay(3000 + Random().nextInt(5000).toLong())
                val name = names[Random().nextInt(names.size)]
                val comment = comments[Random().nextInt(comments.size)]
                liveChatComments.add(LiveChatComment(name, comment, Random().nextInt(4)))
                if (liveChatComments.size > 20) {
                    liveChatComments.removeAt(0)
                }
            }
        }
    }

    fun stopLiveStream() {
        liveStreamingEnabled = false
        liveChatJob?.cancel()
    }

    // --- Upcoming Events Registration & Timer Countdown ---
    fun registerForEvent(eventId: String) {
        if (registeredEventsList.contains(eventId)) {
            registeredEventsList.remove(eventId)
            showSnackbar("Event booking cancelled.")
        } else {
            registeredEventsList.add(eventId)
            showSnackbar("Registered! Open JFC Connect pass to enter.")
        }
    }

    private fun startEventCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            while (true) {
                // Next grand JFC Revival Night scheduled relative to current time 2026-05-26
                val eventDateStr = "2026-05-31 18:00:00"
                try {
                    val eventDate = sdf.parse(eventDateStr)
                    val now = Date()
                    val diff = eventDate.time - now.time
                    if (diff > 0) {
                        val days = diff / (24 * 60 * 60 * 1000)
                        val hours = (diff / (60 * 60 * 1000)) % 24
                        val minutes = (diff / (60 * 1000)) % 60
                        val seconds = (diff / 1000) % 60
                        countdownText = String.format("%02d d : %02d h : %02d m : %02d s", days, hours, minutes, seconds)
                    } else {
                        countdownText = "Worship Night is Live Now!"
                    }
                } catch (e: Exception) {
                    countdownText = "Upcoming Soon!"
                }
                delay(1000)
            }
        }
    }

    // --- Razorpay / UPI simulated Donation Gateway Trigger ---
    fun triggerDonationFlow(amountStr: String, type: String) {
        val cleanAmount = amountStr.replace("[^\\d]".toRegex(), "")
        if (cleanAmount.isEmpty() || cleanAmount.toInt() <= 0) {
            showSnackbar("Please enter a valid donation value.")
            return
        }
        selectedDonationType = type
        viewModelScope.launch {
            showSnackbar("Redirecting safely to $type payments...")
            delay(1500)
            showDonationSuccessDialog = true
            showSnackbar("₹$cleanAmount donation processed securely! God bless your generous heart. 🙏")
        }
    }

    // --- AI Bible Assistant Interaction Flow ---
    fun sendAiChatQuery() {
        val prompt = aiChatInput.trim()
        if (prompt.isEmpty()) return
        
        aiChatHistory.add(ChatMessage(prompt, isUser = true, senderName = "Believer"))
        aiChatInput = ""
        isAiGenerating = true

        viewModelScope.launch {
            val response = GeminiClient.askBibleAssistant(prompt)
            aiChatHistory.add(ChatMessage(response, isUser = false, senderName = "AI Bible Assistant"))
            isAiGenerating = false
        }
    }

    fun clearAiChat() {
        aiChatHistory.clear()
        aiChatHistory.add(ChatMessage(
            text = "Welcome to the JFC AI Bible Assistant. Ask me anything, like \"Give me verses for anxiety\" or \"Explain faith in Kannada.\"",
            isUser = false,
            senderName = "AI Bible Assistant"
        ))
    }

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
        liveChatJob?.cancel()
        countdownJob?.cancel()
    }
}

class JfcViewModelFactory(
    private val application: Application,
    private val repository: JfcRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JfcViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JfcViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
