package com.example.data.model

data class WorshipSong(
    val id: String,
    val title: String,
    val chordsKeys: String, // e.g., "G Major"
    val lyricsKannada: String,
    val lyricsEnglish: String,
    val category: String, // Worship, Holy Spirit, Prayer, Youth, Christmas, Easter
    val audioDuration: String = "4:32"
)

data class SermonMedia(
    val id: String,
    val title: String,
    val speaker: String = "Pastor Vincent Paul",
    val videoUrl: String, // Mock or real YouTube
    val category: String, // Faith, Healing, Salvation, Worship, Prayer, Holy Spirit
    val duration: String,
    val isLive: Boolean = false,
    val description: String = ""
)

data class BibleVerse(
    val book: String,
    val chapter: Int,
    val verse: Int,
    val textKannada: String,
    val textEnglish: String,
    val category: String = "Faith"
)

object ChurchMediaData {

    val songs = listOf(
        WorshipSong(
            id = "song_1",
            title = "ಹೊಸ ಹಾಡು ಹಾಡುತ್ತಾ (Hosa Haadu Haadutha)",
            chordsKeys = "KEY: G Major | Tempo: 92 BPM",
            category = "Worship",
            lyricsKannada = """
                [ಪಲ್ಲವಿ]
                ಹೊಸ ಹಾಡು ಹಾಡುತ್ತಾ ಯೇಸುವ ಕೊಂಡಾಡುವೆ
                ಸ್ತುತಿ ಯಾಗ ಅರ್ಪಿಸಿ ನಾ ಆರಾಧಿಸುವೆ || ಹೊಸ ||
                
                [ಅನುಪಲ್ಲವಿ]
                ಹಲ್ಲೇಲೂಯಾ ಹಲ್ಲೇಲೂಯಾ ಯೇಸುವಿಗೆ
                ಹಲ್ಲೇಲೂಯಾ ಹಲ್ಲೇಲೂಯಾ ಒಡೆಯನಿಗೆ || ಹಲ್ಲೇಲೂಯಾ ||
                
                [ಚರಣ ೧]
                ಪಾಪದ ಕೂಪದಿ ನಾನಿರುವಾಗ 
                ಪ್ರೀತಿಯ ಹಸ್ತವ ನೀ ಚಾಚಿದೆ
                ನನ್ನನ್ನು ಮೇಲೆತ್ತಿ ಬಂಡೆಯ ಮೇಲಿರಿಸಿದೆ
                ನನ್ನಯ ಹೆಜ್ಜೆಯ ಸ್ಥಿರಪಡಿಸಿದೆ || ಹೊಸ ||
                
                [ಚರಣ ೨]
                ಸಂಕಟ ಸಮಯದಲ್ಲಿ ಮೊರೆಯ ರಾಲಿಸಲು
                ನನ್ನಯ ಸಂಗಡ ನೀನಿದ್ದೀಯೆ
                ಕಣ್ಣೀರ ಒರೆಸಿ ಸಂತೈಸಿದೆ
                ಜಯದ ಹಾದಿಗೆ ನಡೆಸುತ್ತೀಯೆ || ಹೊಸ ||
            """.trimIndent(),
            lyricsEnglish = """
                [Chorus]
                Hosa haadu haadutha Yesuva kondaaduve
                Stuthi yaaga arpisi naa aaraadhisuve || Hosa ||

                [Bridge]
                Hallelujah Hallelujah Yesuvige
                Hallelujah Hallelujah Odeyanige || Hallelujah ||

                [Verse 1]
                Paapada koopadi naaniruvaaga
                Preethiya hasthava nee chaachide
                Nannannu meleththi bandeya meliriside
                Nannyya Hejjeya Sthirapadiside || Hosa ||

                [Verse 2]
                Sankata samayadi moreya raalisalu
                Nannaya sangada neeniddiye
                Kanneeru oresi santhaiside
                Jayada haadige nadesuththiye || Hosa ||
            """.trimIndent()
        ),
        WorshipSong(
            id = "song_2",
            title = "ನನ್ನ ಯೇಸುವೇ ನಿನ್ನ ಆರಾಧಿಸುವೆ (Nanna Yesuve)",
            chordsKeys = "KEY: C Major | Tempo: 78 BPM",
            category = "Prayer",
            lyricsKannada = """
                [ಪಲ್ಲವಿ]
                ನನ್ನ ಯೇಸುವೇ ನಿನ್ನ ಆರಾಧಿಸುವೆ
                ನನ್ನ ಪ್ರಾಣವೇ ನಿನ್ನನೆ ಕೊಂಡಾಡುವೆ || ನನ್ನ ||
                
                [ಚರಣ ೧]
                ನಿನಗಿಂತ ಶ್ರೇಷ್ಠರು ಯಾರೂ ಇಲ್ಲಾ 
                ನಿನಗಿಂತ ಪ್ರೀತಿಯು ಬೇರೆ ಇಲ್ಲಾ
                ನಿನ್ನಲ್ಲೇ ನನ್ನ ಜೀವ ಅಡಗಿದೆ ಯೇಸಯ್ಯ 
                ನಿನ್ನ ಆರಾಧನೆ ಕೊನೆಯವರೆಗೂ ಮಾಡುವೆ || ನನ್ನ ||
            """.trimIndent(),
            lyricsEnglish = """
                [Chorus]
                Nanna Yesuve ninna aaraadhisuve
                Nanna praanave ninnane kondaaduve || Nanna ||

                [Verse 1]
                Ningintha shreshtharu yaaroo illa
                Ningintha preethiyu bere illa
                Ninnallle nanna jeeva adagide Yesayya
                Ninna aaraadhane koneyavaregu maaduve || Nanna ||
            """.trimIndent()
        ),
        WorshipSong(
            id = "song_3",
            title = "ಪರಿಶುದ್ಧಾತ್ಮನೇ ಬನ್ನಿ (Parishudhathmane Banni)",
            chordsKeys = "KEY: D Minor | Tempo: 68 BPM",
            category = "Holy Spirit",
            lyricsKannada = """
                [ಪಲ್ಲವಿ]
                ಪರಿಶುದ್ಧಾತ್ಮನೇ ಬನ್ನಿ ನಮ್ಮಯ ಮಧ್ಯದಲ್ಲಿ 
                ಅಭಿಷೇಕ ಮಾಡಿರಿ ನಮ್ಮ ಮನಗಳಲ್ಲಿ || ಪರಿಶುದ್ಧಾತ್ಮನೇ ||
                
                [ಚರಣ ೧]
                ಬಲವಾದ ಗಾಳಿಯಂತೆ ಬೀಸಿ ಬನ್ನಿ 
                ಅಗ್ನಿಯ ಜ್ವಾಲೆಯಂತೆ ಇಳಿದು ಬನ್ನಿ
                ಹೊಸ ಭಾಷೆ ನೀಡಿ ಅರಳಿಸಿರಿ
                ನಮ್ಮಯ ಪ್ರಾಣವ ಉಜ್ಜೀವಿಸು || ಪರಿಶುದ್ಧಾತ್ಮನೇ ||
            """.trimIndent(),
            lyricsEnglish = """
                [Chorus]
                Parishudhathmane banni nammaya madhyadalli
                Abhisheka maadiri namma managalalli || Parishudhathmane ||

                [Verse 1]
                Balavaada gaaliyanthe beesi banni
                Agniya jwaleyanthe ilidu banni
                Hosa bhaashe needi aralisiri
                Nammaya praanava ujjeevisu || Parishudhathmane ||
            """.trimIndent()
        ),
        WorshipSong(
            id = "song_4",
            title = "ಯೇಸು ಕ್ರಿಸ್ತನೇ ನನ್ನ ಕುರುಬನು (Jesus is My Shepherd)",
            chordsKeys = "KEY: F Major | Tempo: 85 BPM",
            category = "Youth",
            lyricsKannada = """
                [ಪಲ್ಲವಿ]
                ಯೇಸು ಕ್ರಿಸ್ತನೇ ನನ್ನ ಕುರುಬನು 
                ಯಾವ ಕೊರತೆಯೂ ನನಗಿಲ್ಲಾ || ಯೇಸು ||
                
                [ಚರಣ ೧]
                ಹಸಿರು ಹುಲ್ಲುಗಾವಲಿಗೆ ನಡೆಸುವನು 
                ಶಾಂತ ಜಲದ ಬಳಿಗೆ ಮುನ್ನಡೆಸುವನು
                ಆತ್ಮವ ಸ್ಥಿರಪಡಿಸಿ ನೀತಿ ಹಾದಿಯಲ್ಲಿ 
                ಹೆಸರಿನ ನಿಮಿತ್ತ ನಡೆಸುತಾನೆ || ಯೇಸು ||
            """.trimIndent(),
            lyricsEnglish = """
                [Chorus]
                Yesu Christane nanna kurubanu
                Yaava koratheyu nannagilla || Yesu ||

                [Verse 1]
                Hasiru hullugaavalige nadesuvanu
                Shantha jalada balige munnadesuvanu
                Aathmava sthirapadisi neethi haadiyalli
                Hesarina nimiththa nadesuthaane || Yesu ||
            """.trimIndent()
        ),
        WorshipSong(
            id = "song_5",
            title = "ನನ್ನ ಹೃದಯವೇ ಯೇಸುವ ಕೊಂಡಾಡು (Bless the Lord O My Soul)",
            chordsKeys = "KEY: E Major | Tempo: 105 BPM",
            category = "Worship",
            lyricsKannada = """
                [ಪಲ್ಲವಿ]
                ನನ್ನ ಹೃದಯವೇ ಯೇಸುವ ಕೊಂಡಾಡು
                ನನ್ನ ಒಳಗೆ ಇರುವುದೆಲ್ಲಾ ಆತನ ನಾಮ ಕೀರ್ತಿಸು || ನನ್ನ ||
                
                [ಚರಣ]
                ಆತನು ಮಾಡಿದ ಉಪಕಾರಗಳ ಮರೆಯಬೇಡ
                ನಿನ್ನ ಪಾಪಗಳನ್ನೆಲ್ಲಾ ಕ್ಷಮಿಸಿದಾತನು
                ನಿನ್ನ ರೋಗಗಳನ್ನೆಲ್ಲಾ ಗುಣಪಡಿಸಿದಾತನು || ನನ್ನ ||
            """.trimIndent(),
            lyricsEnglish = """
                [Chorus]
                Nanna hrudayave Yesuva kondaadu
                Nanna olage iruvudella aathana naama keerthisu || Nanna ||

                [Verse]
                Aathanu maadida upakaaragala mareyabeda
                Ninna paapagalannella kshamisidhaathanu
                Ninna rogagalannella gunapadisidhaathanu || Nanna ||
            """.trimIndent()
        )
    )

    val sermons = listOf(
        SermonMedia(
            id = "s_1",
            title = "ನಂಬಿಕೆಯ ಶಕ್ತಿ (The Power of Faith in Storms)",
            speaker = "Pastor Vincent Paul",
            category = "Faith",
            duration = "45:20",
            videoUrl = "https://www.youtube.com/watch?v=mock_faith",
            description = "Learn how to hold onto Jesus even when the storm is rising. Inspired by Mark 4:35-41."
        ),
        SermonMedia(
            id = "s_2",
            title = "ದೇವರ ಅದ್ಭುತ ಸ್ವಸ್ಥತೆ (God's Divine Healing Miracle)",
            speaker = "Pastor Vincent Paul",
            category = "Healing",
            duration = "52:15",
            videoUrl = "https://www.youtube.com/watch?v=mock_healing",
            description = "A powerful sermon exploring scripture promises of physical and mental restoration through Christ."
        ),
        SermonMedia(
            id = "s_3",
            title = "ರಕ್ಷಣೆ ಮತ್ತು ವಿಮೋಚನೆ (Salvation and Deliverance)",
            speaker = "Pastor Vincent Paul",
            category = "Salvation",
            duration = "38:40",
            videoUrl = "https://www.youtube.com/watch?v=mock_salvation",
            description = "Jesus calls every broken heart to receive free redemption and a life of total freedom."
        ),
        SermonMedia(
            id = "s_4",
            title = "ಪರಿಶುದ್ಧಾತ್ಮನ ಅಭಿಷೇಕ (Anointing of the Holy Spirit)",
            speaker = "Pastor Vincent Paul",
            category = "Holy Spirit",
            duration = "1:05:10",
            videoUrl = "https://www.youtube.com/watch?v=mock_anointing",
            isLive = true,
            description = "We are streaming live today. Join our congregation as we wait on the Holy Spirit's touch."
        ),
        SermonMedia(
            id = "s_5",
            title = "ಪ್ರಾರ್ಥನಾ ಜೀವಿತದ ರಹಸ್ಯ (Secrets of a Dynamic Prayer Life)",
            speaker = "Pastor Vincent Paul",
            category = "Prayer",
            duration = "42:10",
            videoUrl = "https://www.youtube.com/watch?v=mock_prayer",
            description = "Deepen your secret place with God and develop unwavering resilience in your prayer journey."
        )
    )

    val bibleVerses = listOf(
        BibleVerse(
            book = "ಮತ್ತಾಯ (Matthew)", chapter = 1, verse = 17,
            textKannada = "ಯೇಸು ಅವರಿಗೆ—ನನ್ನ ಹಿಂದೆ ಬನ್ನಿರಿ, ನಿಮ್ಮನ್ನು ಮನುಷ್ಯರನ್ನು ಹಿಡಿಯುವ ಜಾಲಗಾರರನ್ನಾಗಿ ಮಾಡುವೆನು ಅಂದನು.",
            textEnglish = "Jesus said to them, 'Follow me, and I will make you fishers of men.'"
        ),
        BibleVerse(
            book = "ಯೋಹಾನ (John)", chapter = 3, verse = 16,
            textKannada = "ದೇವರು ಪ್ರಪಂಚದ ಮೇಲೆ ಎಷ್ಟೋ ಪ್ರೀತಿ ಇಟ್ಟು ತನ್ನ ಒಬ್ಬನೇ ಮಗನನ್ನು ಕೊಟ್ಟನು; ಆತನಲ್ಲಿ ನಂಬಿಕೆಯಿಡುವ ಯಾವನೂ ನಾಶವಾಗದೆ ನಿತ್ಯಜೀವವನ್ನು ಪಡೆಯುವನು.",
            textEnglish = "For God so loved the world that He gave His only begotten Son, that whoever believes in Him should not perish but have everlasting life."
        ),
        BibleVerse(
            book = "ಯೆಶಾಯ (Isaiah)", chapter = 41, verse = 10,
            textKannada = "ಹೆದರಬೇಡ, ನಾನೇ ನಿನ್ನ ಸಂಗಡ ಇದ್ದೇನೆ; ಧೃತಿಗೆಡಬೇಡ, ನಾನೇ ನಿನ್ನ ದೇವರು; ನಿನ್ನನ್ನು ಉಜ್ಜೀವಿಸಿ ನಿನಗೆ ಸಹಾಯ ಮಾಡುವೆನು.",
            textEnglish = "Fear not, for I am with you; be not dismayed, for I am your God; I will strengthen you, I will help you."
        ),
        BibleVerse(
            book = "ಕೀರ್ತನೆಗಳು (Psalms)", chapter = 23, verse = 1,
            textKannada = "ಯೆಹೋವನೇ ನನ್ನ ಕುರುಬನು, ನನಗೆ ಕೊರತೆಯೇನೂ ಇರದು.",
            textEnglish = "The Lord is my shepherd; I shall not want."
        ),
        BibleVerse(
            book = "ಫಿಲಿಪ್ಪಿ (Philippians)", chapter = 4, verse = 13,
            textKannada = "ನನ್ನನ್ನು ಬಲಪಡಿಸುವ ಕ್ರಿಸ್ತನಲ್ಲಿ ನಾನು ಎಲ್ಲವನ್ನೂ ಮಾಡಲು ಶಕ್ತನಾಗಿದ್ದೇನೆ.",
            textEnglish = "I can do all things through Christ who strengthens me."
        ),
        BibleVerse(
            book = "ರೋಮಾಪುರದವರಿಗೆ (Romans)", chapter = 8, verse = 28,
            textKannada = "ದೇವರನ್ನು ಪ್ರೀತಿಸುವವರಿಗೆ, ಅಂದರೆ ಆತನ ಸಂಕಲ್ಪದಂತೆ ಕರೆಯಲ್ಪಟ್ಟವರಿಗೆ ಎಲ್ಲವೂ ಒಳಿತಿಗಾಗಿಯೇ ಸಂಭವಿಸುತ್ತದೆ ಎಂದು ನಾವು ಬಲ್ಲೆವು.",
            textEnglish = "And we know that all things work together for good to those who love God, to those who are called according to His purpose."
        ),
        BibleVerse(
            book = "ಜ್ಞಾನೋಕ್ತಿಗಳು (Proverbs)", chapter = 3, verse = 5,
            textKannada = "ನಿನ್ನ ಸ್ವಂತ ತಿಳುವಳಿಕೆಯನ್ನು ನಂಬದೆ ಪೂರ್ಣಹೃದಯದಿಂದ ಯೆಹೋವನಲ್ಲಿ ಭರವಸೆಯಿಡು.",
            textEnglish = "Trust in the Lord with all your heart, and lean not on your own understanding."
        ),
        BibleVerse(
            book = "ಯೆರೆಮೀಯ (Jeremiah)", chapter = 29, verse = 11,
            textKannada = "ನಿಮ್ಮ ಕ್ಷೇಮಕ್ಕಾಗಿ ಮತ್ತು ನಿರೀಕ್ಷಿತವಾದ ಭವಿಷ್ಯತ್ತನ್ನು ಕೊಡುವುದಕ್ಕಾಗಿಯೇ ನಾನು ಯೋಚಿಸುತ್ತೇನೆ ಎಂದು ಯೆಹೋವನು ಘೋಷಿಸುತ್ತಾನೆ.",
            textEnglish = "For I know the thoughts that I think toward you, says the Lord, thoughts of peace and not of evil, to give you a future and a hope."
        )
    )
}
