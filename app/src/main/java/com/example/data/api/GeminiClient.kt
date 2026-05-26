package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    suspend fun askBibleAssistant(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "No valid GEMINI_API_KEY found, entering local fallback mode.")
            return@withContext getLocalFallbackResponse(prompt)
        }

        val systemInstruction = "You are an encouraging and wise AI Bible Assistant for JFC (Jesus Followers Church) Connect app. Answer the user spiritually with matching English and Kannada references where possible. Keep it compact, professional, deeply respectful, and focused completely on gospel comfort and prayer."

        val jsonRequest = JSONObject().apply {
            val contentsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            }
            put("contents", contentsArray)
            
            // Add system instruction
            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", systemInstruction)
                    })
                })
            })

            // Add standard configuration
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.7)
                put("maxOutputTokens", 800)
            })
        }

        val requestBody = jsonRequest.toString().toRequestBody("application/json".toMediaType())
        val url = "$BASE_URL?key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .header("Content-Type", "application/json")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e(TAG, "API failed with code ${response.code}: $errBody")
                    return@withContext "I'm having a small connection difficulty with the cloud assistant, but remember: \"Do not be afraid, for I am with you.\" Let me share this local scripture: \n\n${getLocalFallbackResponse(prompt)}"
                }
                
                val responseBody = response.body?.string() ?: return@withContext "Received an empty response from scripture assistant."
                val responseJson = JSONObject(responseBody)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "Amen. Keep praying.")
                    }
                }
                return@withContext "Let your heart be comforted. No answer could be parsed, but God always hears your whispers."
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during call", e)
            return@withContext "I am offline right now, but God's word remains: \n\n${getLocalFallbackResponse(prompt)}"
        }
    }

    private fun getLocalFallbackResponse(prompt: String): String {
        val query = prompt.lowercase()
        return when {
            query.contains("fear") || query.contains("afraid") || query.contains("ಹೆದರಿಕೆ") -> {
                "📖 Isaiah 41:10 (ಯೆಶಾಯ 41:10):\n\"Fear not, for I am with you; be not dismayed, for I am your God; I will strengthen you, I will help you.\"\n\n🙏 Prayer Note: Ask Jesus to pour His perfect love into your heart, as perfect love casts out all fear."
            }
            query.contains("peace") || query.contains("anxiety") || query.contains("ಚಿಂತೆ") || query.contains("ಶಾಂತಿ") -> {
                "📖 Philippians 4:6-7:\n\"Do not be anxious about anything, but in everything by prayer and supplication with thanksgiving let your requests be made known to God. And the peace of God, which surpasses all understanding, will guard your hearts.\"\n\n🙏 Prayer Note: Close your eyes and receive the comforting presence of the Holy Spirit right now."
            }
            query.contains("love") || query.contains("grace") || query.contains("ಪ್ರೀತಿ") -> {
                "📖 John 3:16 (ಯೋಹಾನ 3:16):\n\"For God so loved the world that He gave His only begotten Son, that whoever believes in Him should not perish but have everlasting life.\"\n\n🙏 Prayer Note: Bask in the eternal, unconditional love of Christ today."
            }
            query.contains("sickness") || query.contains("heal") || query.contains("disease") || query.contains("ಸ್ವಸ್ಥತೆ") -> {
                "📖 Isaiah 53:5:\n\"But He was wounded for our transgressions, He was bruised for our iniquities; the chastisement for our peace was upon Him, and by His stripes we are healed.\"\n\n🙏 Prayer Note: Father, stretch out Your healing hand over Your child and restore them completely in Jesus' name."
            }
            query.contains("strength") || query.contains("weak") || query.contains("ಬಲ") -> {
                "📖 Philippians 4:13 (ಫಿಲಿಪ್ಪಿ 4:13):\n\"I can do all things through Christ who strengthens me.\"\n\n🙏 Prayer Note: Rest in His strength when your own power is exhausted."
            }
            else -> {
                "📖 Matthew 11:28:\n\"Come to Me, all you who labor and are heavy laden, and I will give you rest.\"\n\n🙏 Prayer Note: Bring all your burdens to the altar. Jesus is waiting to refresh your soul and lead your path."
            }
        }
    }
}
