package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.HomeBooking
import com.example.data.local.PrayerRequest
import com.example.data.local.Testimony
import com.example.data.local.UserProfile
import com.example.data.model.BibleBookInfo
import com.example.data.model.BibleVerse
import com.example.data.model.BibleVerseHelper
import com.example.data.model.ChurchMediaData
import com.example.data.model.SermonMedia
import com.example.data.model.WorshipSong
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.JfcTab
import com.example.ui.viewmodel.JfcViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// -------------------------------------------------------------------------
// Helper: Draw Programmatic Decorative Cross
// -------------------------------------------------------------------------
@Composable
fun JfcCrossIcon(
    modifier: Modifier = Modifier,
    glowColor: Color = GoldGlow,
    crossColor: Color = GoldGlow
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        // Soft background circular gold glow
        drawCircle(
            color = glowColor.copy(alpha = 0.15f),
            radius = width * 1.1f,
            center = Offset(width / 2, height / 2)
        )
        
        drawCircle(
            color = glowColor.copy(alpha = 0.08f),
            radius = width * 1.8f,
            center = Offset(width / 2, height / 2)
        )

        // Draw Cross Base (Vertical pillar)
        drawRoundRect(
            color = crossColor,
            topLeft = Offset(width * 0.42f, 0f),
            size = Size(width * 0.16f, height),
            cornerRadius = CornerRadius(width * 0.05f)
        )

        // Draw Cross Beam (Horizontal)
        drawRoundRect(
            color = crossColor,
            topLeft = Offset(0f, height * 0.28f),
            size = Size(width, height * 0.16f),
            cornerRadius = CornerRadius(width * 0.05f)
        )
    }
}

// -------------------------------------------------------------------------
// Helper: Draw Programmatic Dynamic Crown Logo matching uploaded assets
// -------------------------------------------------------------------------
@Composable
fun JfcCrownLogo(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF380C1E), Color(0xFF0D0308))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(1.5.dp, GoldGlow, RoundedCornerShape(16.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Elegant Crown & Cross Combined Draw
            Box(
                modifier = Modifier.size(54.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    
                    // Purple velvet backing cushion
                    drawCircle(
                        color = Color(0xFF8B008B).copy(alpha = 0.8f),
                        radius = w * 0.35f,
                        center = Offset(w / 2, h * 0.55f)
                    )
                    
                    // Draw gold base band of crown
                    drawRoundRect(
                        color = GoldGlow,
                        topLeft = Offset(w * 0.2f, h * 0.72f),
                        size = Size(w * 0.6f, h * 0.12f),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )
                    
                    // Draw crown spikes (3 main peaks)
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.2f, h * 0.72f)
                        lineTo(w * 0.1f, h * 0.40f)
                        lineTo(w * 0.35f, h * 0.55f)
                        lineTo(w * 0.5f, h * 0.25f) // Center peak
                        lineTo(w * 0.65f, h * 0.55f)
                        lineTo(w * 0.9f, h * 0.40f)
                        lineTo(w * 0.8f, h * 0.72f)
                        close()
                    }
                    drawPath(path, brush = Brush.verticalGradient(listOf(GoldGlow, AmberGlow)))
                    
                    // Draw peak circles (jewels)
                    drawCircle(color = Color.White, radius = 3.dp.toPx(), center = Offset(w * 0.1f, h * 0.40f))
                    drawCircle(color = Color.White, radius = 4.dp.toPx(), center = Offset(w * 0.5f, h * 0.25f))
                    drawCircle(color = Color.White, radius = 3.dp.toPx(), center = Offset(w * 0.9f, h * 0.40f))
                    
                    // Draw Cross on top of center peak
                    drawRect(color = GoldGlow, topLeft = Offset(w * 0.47f, h * 0.05f), size = Size(w * 0.06f, h * 0.20f))
                    drawRect(color = GoldGlow, topLeft = Offset(w * 0.40f, h * 0.10f), size = Size(w * 0.20f, h * 0.06f))
                }
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = "JESUS FOLLOWERS",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "MINISTRIES.",
                color = GoldGlow,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = GoldGlow.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Follow Me, Mark 1:17",
                color = AppTextLightGray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

// -------------------------------------------------------------------------
// Helper: Draw Custom Mock QR Code Resource
// -------------------------------------------------------------------------
@Composable
fun JfcMockQrCode(
    modifier: Modifier = Modifier,
    contentTag: String = "JFC"
) {
    Canvas(modifier = modifier.background(Color.White, RoundedCornerShape(8.dp))) {
        val width = size.width
        val height = size.height
        val margin = 8.dp.toPx()
        val matrixSize = 8
        val cellW = (width - margin * 2) / matrixSize
        val cellH = (height - margin * 2) / matrixSize

        // Background bounds outline
        drawRect(
            color = Color.Black,
            topLeft = Offset(margin, margin),
            size = Size(width - margin * 2, height - margin * 2),
            style = androidx.compose.ui.graphics.drawscope.Stroke(2f)
        )

        // Position finder patterns (Top-left, Top-right, Bottom-left)
        val finders = listOf(
            Offset(margin, margin),
            Offset(margin + cellW * 5, margin),
            Offset(margin, margin + cellH * 5)
        )

        finders.forEach { pos ->
            drawRect(
                color = Color.Black,
                topLeft = pos,
                size = Size(cellW * 3, cellH * 3)
            )
            drawRect(
                color = Color.White,
                topLeft = Offset(pos.x + cellW, pos.y + cellH),
                size = Size(cellW, cellH)
            )
        }

        // Programmatic grid fills based on tag name checksum
        val seed = contentTag.hashCode()
        val random = java.util.Random(seed.toLong())
        for (i in 0 until matrixSize) {
            for (j in 0 until matrixSize) {
                // Skip positions inside check pillars
                if ((i < 3 && j < 3) || (i >= 5 && j < 3) || (i < 3 && j >= 5)) continue
                
                if (random.nextBoolean()) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(margin + i * cellW, margin + j * cellH),
                        size = Size(cellW + 0.5f, cellH + 0.5f)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 1. Splash Screen Overlay
// -------------------------------------------------------------------------
@Composable
fun JfcSplashScreen(
    onSplashComplete: () -> Unit
) {
    var animateStart by remember { mutableStateOf(false) }
    val scaleAnim by animateTransition(animateStart, 0.4f, 1f, 1200)
    val alphaAnim by animateTransition(animateStart, 0f, 1f, 1200)

    LaunchedEffect(Unit) {
        animateStart = true
        delay(2200) // Beautiful cinematic brief delay
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal)
            .themeRadialBackground(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            JfcCrossIcon(
                modifier = Modifier
                    .size(110.dp)
                    .animateContentSize()
                    .graphicsLayer(scaleX = scaleAnim, scaleY = scaleAnim, alpha = alphaAnim)
            )
            
            Spacer(modifier = Modifier.height(28.dp))
            
            Text(
                text = "JFC",
                color = GoldGlow,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 6.sp
                ),
                modifier = Modifier.graphicsLayer(alpha = alphaAnim)
            )
            
            Text(
                text = "JESUS FOLLOWERS CHURCH",
                color = AppTextWhite,
                fontSize = 13.sp,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                ),
                modifier = Modifier.graphicsLayer(alpha = alphaAnim)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Mark 1:17 Quote Box
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, GoldGlow.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "“Follow Me” — Mark 1:17",
                    color = AmberGlow,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Simple composable float animator
@Composable
fun animateTransition(target: Boolean, from: Float, to: Float, duration: Int): State<Float> {
    return animateFloatAsState(
        targetValue = if (target) to else from,
        animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing)
    )
}

// Simple modifier extension for dark background highlights (Cinematic Dual-Tone church light)
fun Modifier.themeRadialBackground(): Modifier = this.drawBehind {
    // 1. Soft gold light shining from top-right
    val goldBrush = Brush.radialGradient(
        colors = listOf(
            GoldGlow.copy(alpha = 0.12f),
            Color.Transparent
        ),
        center = Offset(size.width * 0.9f, -size.height * 0.05f),
        radius = size.width * 1.2f
    )
    drawRect(brush = goldBrush)

    // 2. Translucent sapphire-blue haze reflecting from bottom-left
    val blueBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFF1E3A8A).copy(alpha = 0.08f),
            Color.Transparent
        ),
        center = Offset(size.width * 0.1f, size.height * 0.95f),
        radius = size.width * 1.0f
    )
    drawRect(brush = blueBrush)
}

// -------------------------------------------------------------------------
// 2. Login Screen
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JfcLoginScreen(
    viewModel: JfcViewModel
) {
    var loginMethod by remember { mutableStateOf("Phone") } // Phone or Email/Google
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var showOtpField by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal)
            .themeRadialBackground(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .testTag("login_card"),
            colors = CardDefaults.cardColors(containerColor = DeepSlateSurface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                JfcCrownLogo(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp))
                Spacer(modifier = Modifier.height(18.dp))
                
                Text(
                    text = "Welcome to JFC Connect",
                    color = GoldGlow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Growing Together in Jesus",
                    color = AppTextLightGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                // Toggle tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray.copy(alpha = 0.08f))
                        .padding(4.dp)
                ) {
                    Button(
                        onClick = { loginMethod = "Phone" },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (loginMethod == "Phone") GoldGlow else Color.Transparent,
                            contentColor = if (loginMethod == "Phone") Color.Black else AppTextLightGray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Text("Mobile OTP", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    
                    Button(
                        onClick = { loginMethod = "Google" },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (loginMethod == "Google") GoldGlow else Color.Transparent,
                            contentColor = if (loginMethod == "Google") Color.Black else AppTextLightGray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Text("Google Sign-In", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (loginMethod == "Phone") {
                    if (!showOtpField) {
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { if (it.length <= 10) phoneNumber = it },
                            label = { Text("Your Mobile Number", color = AppTextLightGray) },
                            placeholder = { Text("9876543210") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "", tint = GoldGlow) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = AppTextWhite,
                                unfocusedTextColor = AppTextWhite,
                                focusedBorderColor = GoldGlow,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("phone_input")
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(
                            onClick = {
                                if (phoneNumber.length < 10) {
                                    viewModel.showSnackbar("Please enter a valid 10-digit mobile number.")
                                } else {
                                    showOtpField = true
                                    viewModel.showSnackbar("Mock OTP SMS sent to +91 $phoneNumber: '7777'")
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("send_otp_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldGlow, contentColor = Color.Black),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Request OTP Securely", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Text(
                            text = "OTP Code sent to +91 $phoneNumber",
                            color = AppTextLightGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = otpCode,
                            onValueChange = { if (it.length <= 4) otpCode = it },
                            label = { Text("Enter 4-Digit OTP Indicator", color = AppTextLightGray) },
                            placeholder = { Text("7777") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "", tint = AmberGlow) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = AppTextWhite,
                                unfocusedTextColor = AppTextWhite,
                                focusedBorderColor = GoldGlow,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("otp_input")
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(
                            onClick = {
                                if (otpCode == "7777" || otpCode.trim() == "1234" || otpCode.trim().length == 4) {
                                    viewModel.performLogin(phoneNumber)
                                } else {
                                    viewModel.showSnackbar("Invalid code! Try standard code: '7777'")
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("login_submit_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldGlow, contentColor = Color.Black),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Verify & Enter Connect Dashboard", fontWeight = FontWeight.Bold)
                        }

                        TextButton(
                            onClick = { showOtpField = false },
                            colors = ButtonDefaults.textButtonColors(contentColor = AppTextLightGray)
                        ) {
                            Text("Back to phone number")
                        }
                    }
                } else {
                    // Google Mode representation
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Gray.copy(alpha = 0.06f))
                            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .clickable { viewModel.performGoogleSignIn() }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Google Play Icon",
                                tint = GoldGlow,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Continue with Google Identity",
                                color = AppTextWhite,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Sign in safely. Your details are secured locally inside Room databases.",
                        color = AppTextLightGray,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(
                    onClick = { viewModel.isUserLoggedIn = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = GoldGlow)
                ) {
                    Text("Skip / Enter as Guest Believer", textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)
                }
            } // end of Column
        } // end of Card
    } // end of Box
}

// -------------------------------------------------------------------------
// Main Host Scaffold & Tabs Switching Layout
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JfcMainScaffold(
    viewModel: JfcViewModel
) {
    val currentTab = viewModel.currentTab
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    var showProfileDialog by remember { mutableStateOf(false) }
    var showDonateDialog by remember { mutableStateOf(false) }
    var showAiAssistantDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        JfcCrossIcon(modifier = Modifier.size(24.dp))
                        Column {
                            Text(
                                "JFC Connect",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldGlow,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                "Jesus Followers Church",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(0.6f),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                },
                actions = {
                    // Quick togglers to simulate pastor admin side vs normal believer user side!
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(if (viewModel.isPastorMode) AmberGlow.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f))
                            .clickable {
                                viewModel.isPastorMode = !viewModel.isPastorMode
                                viewModel.showSnackbar(
                                    if (viewModel.isPastorMode) "Pastor Mode Activated! You can now answer prayers, schedule home bookings, and approve testimonies."
                                    else "Believer Mode Activated!"
                                )
                            }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (viewModel.isPastorMode) Icons.Default.SupervisorAccount else Icons.Default.Person,
                            contentDescription = "Pastor Mode Indicator",
                            tint = if (viewModel.isPastorMode) AmberGlow else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (viewModel.isPastorMode) "Pastor/Admin" else "Believer",
                            color = if (viewModel.isPastorMode) AmberGlow else Color.White.copy(alpha = 0.6f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { showProfileDialog = true },
                        modifier = Modifier.testTag("profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User profile logo",
                            tint = GoldGlow,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepCharcoal,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DeepCharcoal,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                val tabs = listOf(
                    Triple(JfcTab.HOME, "Home", Icons.Default.Home),
                    Triple(JfcTab.BIBLE, "Bible", Icons.Default.Book),
                    Triple(JfcTab.SONGS, "Songs", Icons.Default.MusicNote),
                    Triple(JfcTab.MEDIA, "Media", Icons.Default.PlayArrow),
                    Triple(JfcTab.EVENTS, "Events", Icons.Default.Event),
                    Triple(JfcTab.PRAYER, "Prayer", Icons.Default.FavoriteBorder)
                )

                tabs.forEach { (tab, label, icon) ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = "$label tab",
                                tint = if (isSelected) Color.Black else Color.White.copy(alpha = 0.6f)
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) GoldGlow else Color.White.copy(alpha = 0.6f)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = GoldGlow,
                            selectedTextColor = GoldGlow
                        ),
                        modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                    )
                }
            }
        },
        floatingActionButton = {
            // Quick-floating key launcher: AI Bible assistant!
            Box(modifier = Modifier.padding(bottom = 12.dp)) {
                FloatingActionButton(
                    onClick = {
                        viewModel.clearAiChat()
                        showAiAssistantDialog = true
                    },
                    containerColor = GoldGlow,
                    contentColor = Color.Black,
                    shape = CircleShape,
                    modifier = Modifier.testTag("ai_assistant_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Bible Assistant Floating launcher",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        // Direct Tab Swapping viewports
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepCharcoal)
                .padding(innerPadding)
        ) {
            when (currentTab) {
                JfcTab.HOME -> JfcHomeScreen(viewModel, { showAiAssistantDialog = true }, { showDonateDialog = true })
                JfcTab.BIBLE -> JfcBibleScreen(viewModel)
                JfcTab.SONGS -> JfcSongBookScreen(viewModel)
                JfcTab.MEDIA -> JfcMediaScreen(viewModel)
                JfcTab.EVENTS -> JfcEventsScreen(viewModel)
                JfcTab.PRAYER -> JfcPrayerScreen(viewModel)
            }

            // Snackbar Alerts Overlay
            viewModel.snackbarMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AmberGlow)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = "", tint = Color.Black)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = msg,
                            color = Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // --- DIALOG MODALS SUBSCRIPTION SYSTEMS ---
    if (showProfileDialog) {
        UserProfileDialog(
            userProfile = userProfile ?: UserProfile(),
            viewModel = viewModel,
            onDismiss = { showProfileDialog = false }
        )
    }

    if (showDonateDialog) {
        DonationDialog(
            viewModel = viewModel,
            onDismiss = { showDonateDialog = false }
        )
    }

    if (showAiAssistantDialog) {
        AiAssistantDialog(
            viewModel = viewModel,
            onDismiss = { showAiAssistantDialog = false }
        )
    }
}

// -------------------------------------------------------------------------
// 3. User Profile Popup Dialogue Component
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileDialog(
    userProfile: UserProfile,
    viewModel: JfcViewModel,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(userProfile.name) }
    var phone by remember { mutableStateOf(userProfile.phone) }
    var familyDetails by remember { mutableStateOf(userProfile.familyDetails) }
    var prayerInterests by remember { mutableStateOf(userProfile.prayerInterests) }
    var baptismStatus by remember { mutableStateOf(userProfile.baptismStatus) }
    var ministryGroup by remember { mutableStateOf(userProfile.ministryGroup) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("profile_dialog_card"),
            colors = CardDefaults.cardColors(containerColor = DeepSlateSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "My JFC Member Account",
                        color = GoldGlow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close dialog", tint = Color.White)
                    }
                }

                Divider(color = Color.White.copy(0.1f))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Believer Full Name", color = Color.Gray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldGlow,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Contact Number", color = Color.Gray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldGlow,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = familyDetails,
                    onValueChange = { familyDetails = it },
                    label = { Text("Family details", color = Color.Gray) },
                    placeholder = { Text("e.g. Spouse + 2 kids") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldGlow,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = prayerInterests,
                    onValueChange = { prayerInterests = it },
                    label = { Text("Prayer interests", color = Color.Gray) },
                    placeholder = { Text("e.g. Healing, youth revival") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldGlow,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Baptism Check Radio Dropdown option
                Column {
                    Text("Baptism Status", color = Color.LightGray, fontSize = 12.sp)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = baptismStatus == "Yes",
                                onClick = { baptismStatus = "Yes" },
                                colors = RadioButtonDefaults.colors(selectedColor = GoldGlow)
                            )
                            Text("Yes (Baptized)", color = Color.White, fontSize = 13.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = baptismStatus == "No",
                                onClick = { baptismStatus = "No" },
                                colors = RadioButtonDefaults.colors(selectedColor = GoldGlow)
                            )
                            Text("No", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }

                // Ministry Group dropdown outline
                OutlinedTextField(
                    value = ministryGroup,
                    onValueChange = { ministryGroup = it },
                    label = { Text("Ministry Group Involvement", color = Color.Gray) },
                    placeholder = { Text("e.g. Youth, Choir, Outreach") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldGlow,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.handleLogout()
                            onDismiss()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        border = BorderStroke(1.dp, Color.Red.copy(0.4f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Log Out Account", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            viewModel.updateProfile(name, phone, familyDetails, prayerInterests, baptismStatus, ministryGroup)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldGlow, contentColor = Color.Black),
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Text("Save Details", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. HOME SCREEN CORE: Slides Carousel + Highlights
// -------------------------------------------------------------------------
@Composable
fun JfcHomeScreen(
    viewModel: JfcViewModel,
    onAiOpen: () -> Unit,
    onDonateOpen: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var currentCarouselIndex by remember { mutableStateOf(0) }
    
    // Auto increment visual slide in top banner carousel every 6 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentCarouselIndex = (currentCarouselIndex + 1) % 5
        }
    }

    val slides = listOf(
        Triple("Daily scripture promise", "“Follow Me, and I will make you fishers of men.”", "Matthew 1:17"),
        Triple("Grand Friday Revival Meeting", "Come expect miracles, healing and raw deliverance anointing.", "May 29 • 6:30 PM"),
        Triple("New Sunday Sermon Uploaded", "Watch Pastor Vincent explain \"Power of Faith in Storms\".", "Sermons (Media tab)"),
        Triple("JFC Global Holy Prayer Night", "A collective gathering across cities.", "Prayer Group Alert"),
        Triple("Worship Songbook Upgrades", "Over 200+ lyrics and karaoke tabs now live offline.", "Worship (Songs tab)")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_scroll"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Welcoming & JFC Ministry Overview
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = DeepSlateSurface),
                border = BorderStroke(1.5.dp, GoldGlow.copy(0.4f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        JfcCrossIcon(modifier = Modifier.size(36.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "JESUS FOLLOWERS MINISTRIES",
                                color = GoldGlow,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Follow Me • Mark 1:17",
                                color = AppTextLightGray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Text(
                        text = "Our calling and vision is to reach the unreached millions in India with the Good News of Jesus Christ and to touch hearts with His divine love. (Mark 16:15)",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "“Love one another. As I have loved you, so you must love one another.” (John 13:34)",
                        color = AppTextLightGray,
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Key media items list
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .background(Color.Black.copy(0.2f), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text("What We Create & Share:", color = GoldGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        val points = listOf(
                            "Bible Documentaries & History Videos",
                            "Kannada Devotional Jesus Songs",
                            "WhatsApp-style Stories & Short Reels",
                            "Animated Bible Teachings & Facts"
                        )
                        points.forEach { point ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = GoldGlow, modifier = Modifier.size(12.dp))
                                Text(point, color = AppTextLightGray, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // 1. CAROUSEL
        item {
            val (slideHeader, slideBody, slideFooter) = slides[currentCarouselIndex]
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF0C0E12), Color(0xFF1F1B15)),
                            start = Offset(0f, 0f),
                            end = Offset(500f, 500f)
                        )
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(24.dp))
                    .clickable {
                        // Swap tabs based on click
                        if (currentCarouselIndex == 1) {
                            viewModel.currentTab = JfcTab.EVENTS
                        } else if (currentCarouselIndex == 2) {
                            viewModel.currentTab = JfcTab.MEDIA
                        } else if (currentCarouselIndex == 4) {
                            viewModel.currentTab = JfcTab.SONGS
                        }
                    }
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = slideHeader.uppercase(),
                            color = GoldGlow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        HorizontalPagerIndicator(size = 5, activeIndex = currentCarouselIndex)
                    }

                    Text(
                        text = slideBody,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Label, contentDescription = "", tint = AmberGlow, modifier = Modifier.size(14.dp))
                        Text(
                            text = slideFooter,
                            color = Color.LightGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 2. MAIN FAST NAVIGATION TILES
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("DISCOVER JFC MODULES", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    HomeTile(
                        title = "Worship Book",
                        description = "Songs & Lyrics",
                        icon = Icons.Default.MusicNote,
                        tint = GoldGlow,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.currentTab = JfcTab.SONGS }
                    )
                    HomeTile(
                        title = "Sermon Hub",
                        description = "Media Center",
                        icon = Icons.Default.PlayArrow,
                        tint = AmberGlow,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.currentTab = JfcTab.MEDIA }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    HomeTile(
                        title = "Holy Bible",
                        description = "Kannada-English",
                        icon = Icons.Default.Book,
                        tint = GoldGlow,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.currentTab = JfcTab.BIBLE }
                    )
                    HomeTile(
                        title = "Request Prayer",
                        description = "Need Pastor?",
                        icon = Icons.Default.Favorite,
                        tint = Color.Red,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.currentTab = JfcTab.PRAYER }
                    )
                }
            }
        }

        // 3. VERSE OF THE DAY
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("verse_of_day_card")
                    .clip(RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C0E12)),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            // Soft church spotlight from top-right
                            drawCircle(
                                color = GoldGlow.copy(alpha = 0.16f),
                                radius = size.width * 0.55f,
                                center = Offset(size.width * 1.05f, -size.height * 0.1f)
                            )
                            // Ambient sapphire glow from bottom-left
                            drawCircle(
                                color = Color(0xFF1E3A8A).copy(alpha = 0.08f),
                                radius = size.width * 0.45f,
                                center = Offset(size.width * -0.05f, size.height * 1.1f)
                            )
                        }
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(18.dp))
                                Text("VERSE OF THE DAY", color = GoldGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            }
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString("Matthew 1:17 - \"Follow Me, and I will make you fishers of men.\" JFC Connect"))
                                        viewModel.showSnackbar("Verse copied securely!")
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy verse", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                                }
                                
                                IconButton(
                                    onClick = {
                                        viewModel.showSnackbar("Inviting text prepared to send to friends!")
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = "Share verse", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "“ಯೇಸು ಅವರಿಗೆ—ನನ್ನ ಹಿಂದೆ ಬನ್ನಿರಿ, ನಿಮ್ಮನ್ನು ಮನುಷ್ಯರನ್ನು ಹಿಡಿಯುವ ಜಾಲಗಾರರನ್ನಾಗಿ ಮಾಡುವೆನು ಅಂದನು.”",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 24.sp,
                            fontStyle = FontStyle.Normal
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "“Jesus said to them, 'Follow me, and I will make you fishers of men.'”",
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ಮತ್ತಾಯ (Matthew) 1:17",
                                color = AmberGlow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Daily Spiritual Bread",
                                color = Color.White.copy(0.4f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // 4. AI & SUPPORT TILES
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // AI Assistant Card button
                Box(
                    modifier = Modifier
                        .weight(1.3f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF2C1B4D), DeepSlateCard)
                            )
                        )
                        .border(1.dp, GoldGlow.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .clickable { onAiOpen() }
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(16.dp))
                            Text("AI Bible Expert", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Text("Instant theological answers on fear, peace and healing.", color = Color.LightGray, fontSize = 11.sp, lineHeight = 15.sp)
                    }
                }

                // Donation Support
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DeepSlateCard)
                        .border(1.dp, GoldGlow.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .clickable { onDonateOpen() }
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.FavoriteBorder, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(16.dp))
                            Text("Donate", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Text("Support JFC Davanagere offline operations.", color = Color.LightGray, fontSize = 11.sp, lineHeight = 15.sp)
                    }
                }
            }
        }

        // UPCOMING EVENT BANNER (SOPHISTICATED DARK NIGHT LIFE)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF0F1115), Color(0xFF07080A))
                        )
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
                    .clickable { viewModel.currentTab = JfcTab.EVENTS }
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "UPCOMING EVENT",
                            color = GoldGlow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "Youth Revival Night",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Sunday • 6:00 PM • JFC Church Hall",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(GoldGlow)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "REGISTER",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 5. SOCIAL MEDIA CORNER (YOUTUBE, FACEBOOK, INSTAGRAM, GOOGLE MAPS)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DeepSlateSurface),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "CONNECT WITH JFC GLOBAL & LOCAL",
                        color = GoldGlow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SocialLink(
                            hostName = "JFC YouTube",
                            url = "https://www.youtube.com/c/JesusFollowersMinistriesDavanagere",
                            icon = Icons.Default.VideoLibrary,
                            modifier = Modifier.weight(1f)
                        )
                        SocialLink(
                            hostName = "JFC Instagram",
                            url = "https://www.instagram.com/jesusfollowersministries",
                            icon = Icons.Default.PhotoCamera,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SocialLink(
                            hostName = "JFC Facebook",
                            url = "https://www.facebook.com/jfc.dvg/",
                            icon = Icons.Default.ThumbUp,
                            modifier = Modifier.weight(1f)
                        )
                        SocialLink(
                            hostName = "JFC Google Map",
                            url = "https://share.google/zwOso3RWcpAtBsLF4",
                            icon = Icons.Default.LocationOn,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Click above to engage with our official platforms or navigate directly to the church campus.",
                        fontSize = 10.sp,
                        color = Color.White.copy(0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun HomeTile(
    title: String,
    description: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(DeepSlateCard)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.04f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = "", tint = tint, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(description, color = Color.Gray, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun HorizontalPagerIndicator(size: Int, activeIndex: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 0 until size) {
            Box(
                modifier = Modifier
                    .size(if (i == activeIndex) 14.dp else 6.dp, 6.dp)
                    .clip(CircleShape)
                    .background(if (i == activeIndex) GoldGlow else Color.Gray.copy(alpha = 0.5f))
            )
        }
    }
}

@Composable
fun SocialLink(
    hostName: String,
    url: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray.copy(0.08f))
            .clickable {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Redirecting safely to: $url", Toast.LENGTH_SHORT).show()
                }
            }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(hostName, color = AppTextWhite, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

// -------------------------------------------------------------------------
// 5. DIGITAL BIBLE TAB (Full English & Kannada Explorer)
// -------------------------------------------------------------------------
@Composable
fun JfcBibleScreen(
    viewModel: JfcViewModel
) {
    val query = viewModel.bibleSearchQuery
    val currentLang = viewModel.selectedBibleLanguage
    val bookmarks by viewModel.bookmarkedVerses.collectAsStateWithLifecycle()
    
    // Explorer states
    val currentBookName = viewModel.selectedExploreBook
    val currentChapter = viewModel.selectedExploreChapter
    val showExplorer = viewModel.showBibleExplorer
    
    var showBookSelectionDialog by remember { mutableStateOf(false) }

    // Locate current book information
    val currentBookInfo = remember(currentBookName) {
        BibleVerseHelper.bibleBooks.firstOrNull { it.nameEnglish == currentBookName } 
            ?: BibleVerseHelper.bibleBooks.first { it.nameEnglish == "John" }
    }

    // Get active verses
    val activeVerses = remember(currentBookName, currentChapter) {
        BibleVerseHelper.getVersesForBookChapter(currentBookName, currentChapter)
    }

    val initialVerses = ChurchMediaData.bibleVerses
    
    // Simple offline matching for verse finder
    val filteredVerses = remember(query) {
        if (query.trim().isEmpty()) {
            initialVerses
        } else {
            initialVerses.filter {
                it.book.lowercase().contains(query.lowercase()) ||
                it.textEnglish.lowercase().contains(query.lowercase()) ||
                it.textKannada.lowercase().contains(query.lowercase())
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
            border = BorderStroke(1.dp, Color.LightGray.copy(0.3f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                
                // Interactive Mode Selector: Study Bible vs Verse Finder
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray.copy(alpha = 0.08f))
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1.5f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (showExplorer) GoldGlow else Color.Transparent)
                            .clickable { viewModel.showBibleExplorer = true }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Browse Full Bible (ಸಂಪೂರ್ಣ ಗ್ರಂಥ)",
                            color = if (showExplorer) Color.White else AppTextLightGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (!showExplorer) GoldGlow else Color.Transparent)
                            .clickable { viewModel.showBibleExplorer = false }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Quick Search (ಶೋಧಕ)",
                            color = if (!showExplorer) Color.White else AppTextLightGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Translation controls "Kannada", "English", "Both"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TRANSLATION / ಭಾಷೆ:",
                        color = AmberGlow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Gray.copy(alpha = 0.06f))
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val languages = listOf("Kannada", "English", "Both")
                        languages.forEach { l ->
                            val isSel = currentLang == l
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSel) AmberGlow else Color.Transparent)
                                    .clickable { viewModel.selectedBibleLanguage = l }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (l == "Both") "Both (ಎರಡೂ)" else if (l == "Kannada") "ಕನ್ನಡ" else "English",
                                    color = if (isSel) Color.White else AppTextLightGray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Main bible layout depending on chosen mode
        if (showExplorer) {
            // BOOK & CHAPTER NAVIGATOR BAR
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
                border = BorderStroke(1.dp, Color.LightGray.copy(0.3f))
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Book Picker trigger
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray.copy(alpha = 0.06f))
                            .clickable { showBookSelectionDialog = true }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Book, contentDescription = "", tint = GoldGlow)
                            Column {
                                Text(
                                    text = "SELECT BIBLE BOOK (ಗ್ರಂಥವನ್ನು ಆರಿಸಿರಿ)",
                                    color = AppTextLightGray,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${currentBookInfo.nameKannada} (${currentBookInfo.nameEnglish})",
                                    color = AppTextWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(currentBookInfo.testament, color = AmberGlow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "", tint = AppTextLightGray)
                        }
                    }

                    // Chapter horizontal picker slider representation
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "CHAPTER / ಅಧ್ಯಾಯ: $currentChapter of ${currentBookInfo.maxChapters}",
                            color = AppTextLightGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (ch in 1..currentBookInfo.maxChapters) {
                                val isSelected = currentChapter == ch
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) GoldGlow else Color.Gray.copy(alpha = 0.08f))
                                        .clickable { viewModel.selectedExploreChapter = ch }
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$ch",
                                        color = if (isSelected) Color.White else AppTextWhite,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Book Selection Pop-up Dialog
            if (showBookSelectionDialog) {
                Dialog(onDismissRequest = { showBookSelectionDialog = false }) {
                    Card(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f),
                        colors = CardDefaults.cardColors(containerColor = DeepSlateSurface),
                        border = BorderStroke(1.dp, Color.LightGray.copy(0.3f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("ALL BIBLE BOOKS (66 ಗ್ರಂಥಗಳು)", color = GoldGlow, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                IconButton(onClick = { showBookSelectionDialog = false }) {
                                    Icon(Icons.Default.Close, contentDescription = "", tint = AppTextLightGray)
                                }
                            }
                            
                            Divider(color = Color.LightGray.copy(0.3f), modifier = Modifier.padding(vertical = 8.dp))

                            // Divided list by Old and New testaments
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                item {
                                    Text("OLD TESTAMENT (ಹಳೆಯ ಒಡಂಬಡಿಕೆ)", color = AmberGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 6.dp))
                                }
                                items(BibleVerseHelper.bibleBooks.filter { it.testament == "Old Testament" }) { bk ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.selectedExploreBook = bk.nameEnglish
                                                viewModel.selectedExploreChapter = 1
                                                showBookSelectionDialog = false
                                            }
                                            .padding(vertical = 10.dp, horizontal = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("${bk.nameKannada} (${bk.nameEnglish})", color = AppTextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                        Text("${bk.maxChapters} chapters", color = AppTextLightGray, fontSize = 11.sp)
                                    }
                                    Divider(color = Color.LightGray.copy(0.12f))
                                }

                                item {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text("NEW TESTAMENT (ಹೊಸ ಒಡಂಬಡಿಕೆ)", color = AmberGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 6.dp))
                                }
                                items(BibleVerseHelper.bibleBooks.filter { it.testament == "New Testament" }) { bk ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.selectedExploreBook = bk.nameEnglish
                                                viewModel.selectedExploreChapter = 1
                                                showBookSelectionDialog = false
                                            }
                                            .padding(vertical = 10.dp, horizontal = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("${bk.nameKannada} (${bk.nameEnglish})", color = AppTextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                        Text("${bk.maxChapters} chapters", color = AppTextLightGray, fontSize = 11.sp)
                                    }
                                    Divider(color = Color.LightGray.copy(0.12f))
                                }
                            }
                        }
                    }
                }
            }

            // Display Explorer Verses
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "${currentBookInfo.nameKannada} - Chapter ${currentChapter} (${currentBookInfo.nameEnglish} ${currentChapter})",
                        color = AmberGlow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                items(activeVerses) { verse ->
                    val referenceStr = "${verse.book} ${verse.chapter}:${verse.verse}"
                    val isFav = viewModel.isVerseBookmarked(referenceStr)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
                        border = BorderStroke(1.dp, if (isFav) GoldGlow.copy(0.4f) else Color.LightGray.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Verse ${verse.verse}",
                                    color = AmberGlow,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(
                                    onClick = { viewModel.toggleVerseBookmark(verse) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isFav) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Bookmark button",
                                        tint = if (isFav) GoldGlow else Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Show Kannada translation
                            if (currentLang == "Kannada" || currentLang == "Both") {
                                Text(
                                    text = verse.textKannada,
                                    color = AppTextWhite,
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            if (currentLang == "Both" && (currentLang == "Kannada" || currentLang == "Both")) {
                                Spacer(modifier = Modifier.height(6.dp))
                            }

                            // Show English translation
                            if (currentLang == "English" || currentLang == "Both") {
                                Text(
                                    text = verse.textEnglish,
                                    color = AppTextLightGray,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }
                    }
                }

                // Separator before bookmark
                if (bookmarks.isNotEmpty()) {
                    item {
                        Column(modifier = Modifier.padding(vertical = 12.dp)) {
                            Divider(color = Color.LightGray.copy(0.3f), modifier = Modifier.padding(bottom = 12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Bookmark, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(16.dp))
                                Text("MY SAVED OFFLINE VERSES (${bookmarks.size})", color = GoldGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    items(bookmarks) { b ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
                            border = BorderStroke(1.dp, GoldGlow.copy(0.2f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(b.verseRef, color = AmberGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    IconButton(
                                        onClick = { viewModel.toggleVerseBookmark(BibleVerse(b.verseRef.split(" ")[0], 0, 0, b.contentEnglish, b.contentEnglish)) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete bookmark", tint = Color.Gray, modifier = Modifier.size(14.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(b.contentKannada, color = AppTextWhite, fontSize = 13.sp, lineHeight = 18.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(b.contentEnglish, color = AppTextLightGray, fontSize = 12.sp, fontStyle = FontStyle.Italic)
                            }
                        }
                    }
                }
            }
        } else {
            // VERSE QUICK FINDER / SEARCH MODE
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
                border = BorderStroke(1.dp, Color.LightGray.copy(0.3f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("BIBLE TRANSLATIONS SEARCH", color = GoldGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    // Text search field
                    OutlinedTextField(
                        value = query,
                        onValueChange = { viewModel.bibleSearchQuery = it },
                        placeholder = { Text("Search Matthew, John, or keywords...", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "", tint = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = AppTextWhite,
                            unfocusedTextColor = AppTextWhite,
                            focusedBorderColor = GoldGlow,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("bible_search")
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredVerses.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Book, contentDescription = "", tint = Color.Gray, modifier = Modifier.size(54.dp))
                            Text("No matching verses found.", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                } else {
                    items(filteredVerses) { verse ->
                        val referenceStr = "${verse.book} ${verse.chapter}:${verse.verse}"
                        val isFav = viewModel.isVerseBookmarked(referenceStr)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
                            border = BorderStroke(1.dp, if (isFav) GoldGlow.copy(0.4f) else Color.LightGray.copy(alpha = 0.2f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = referenceStr,
                                        color = AmberGlow,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(
                                        onClick = { viewModel.toggleVerseBookmark(verse) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isFav) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                            contentDescription = "Bookmark button",
                                            tint = if (isFav) GoldGlow else Color.Gray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Show Kannada description
                                if (currentLang == "Kannada" || currentLang == "Both") {
                                    Text(
                                        text = verse.textKannada,
                                        color = AppTextWhite,
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (currentLang == "Both") {
                                    Spacer(modifier = Modifier.height(10.dp))
                                }

                                // Show English description
                                if (currentLang == "English" || currentLang == "Both") {
                                    Text(
                                        text = verse.textEnglish,
                                        color = AppTextLightGray,
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            }
                        }
                    }
                }

                // Saved verses Bookmarked list
                if (bookmarks.isNotEmpty()) {
                    item {
                        Column(modifier = Modifier.padding(vertical = 12.dp)) {
                            Divider(color = Color.LightGray.copy(0.3f), modifier = Modifier.padding(bottom = 12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Bookmark, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(16.dp))
                                Text("MY SAVED OFFLINE VERSES (${bookmarks.size})", color = GoldGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    items(bookmarks) { b ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
                            border = BorderStroke(1.dp, GoldGlow.copy(0.2f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(b.verseRef, color = AmberGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    IconButton(
                                        onClick = { viewModel.toggleVerseBookmark(BibleVerse(b.verseRef.split(" ")[0], 0, 0, b.contentEnglish, b.contentEnglish)) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete bookmark", tint = Color.Gray, modifier = Modifier.size(14.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(b.contentKannada, color = AppTextWhite, fontSize = 13.sp, lineHeight = 18.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(b.contentEnglish, color = AppTextLightGray, fontSize = 12.sp, fontStyle = FontStyle.Italic)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 6. JFC WORSHIP SONGBOOK TAB
// -------------------------------------------------------------------------
@Composable
fun UserSongDetailsView(
    song: com.example.data.local.UserSong,
    viewModel: JfcViewModel,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(DeepCharcoal)) {
        // Toolbar
        Row(
            modifier = Modifier.fillMaxWidth().background(DeepSlateSurface).padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Return back", tint = Color.White)
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(song.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(song.chordsKeys, color = GoldGlow, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }

            if (viewModel.isPastorMode) {
                IconButton(onClick = { 
                    viewModel.deleteUserSongAsAdmin(song.id)
                    onBack()
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete from admin side",
                        tint = Color.Red
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(36.dp))
            }
        }

        // Body Info Content Scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Contributor Metadata Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
                border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GoldGlow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "", tint = Color.Black, modifier = Modifier.size(18.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("POSTED BY ADMIN / CREATOR", color = GoldGlow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(song.authorName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Phone / Contact: ${song.authorPhone}", color = AppTextLightGray, fontSize = 11.sp)
                    }
                }
            }

            // Kannada Lyrics Card
            if (song.lyricsKannada.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DeepSlateSurface),
                    border = BorderStroke(1.dp, Color.White.copy(0.06f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(14.dp))
                            Text("ಕನ್ನಡ ಆವೃತ್ತಿ (Kannada Lyrics)", color = GoldGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = song.lyricsKannada,
                            color = Color.White,
                            fontSize = 15.sp,
                            lineHeight = 26.sp,
                            fontStyle = FontStyle.Normal
                        )
                    }
                }
            }

            // English Lyrics Card
            if (song.lyricsEnglish.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DeepSlateSurface),
                    border = BorderStroke(1.dp, Color.White.copy(0.06f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MenuBook, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(14.dp))
                            Text("ENGLISH LYRICS & TRANSLATION", color = GoldGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = song.lyricsEnglish,
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JfcSongBookScreen(
    viewModel: JfcViewModel
) {
    val query = viewModel.songSearchQuery
    val selCat = viewModel.songCategoryFilter
    val selectedSong = viewModel.selectedSong
    val selectedUserSong = viewModel.selectedUserSong
    val favoriteSongs by viewModel.favoriteSongsList.collectAsStateWithLifecycle()
    val userSongs by viewModel.allUserSongs.collectAsStateWithLifecycle()

    val categories = listOf("All", "Favorites Only", "Worship", "Holy Spirit", "Youth", "Prayer", "Christmas", "Easter")
    val initialSongs = ChurchMediaData.songs

    // Simple lyrics filters for mock songs
    val filteredSongs = remember(query, selCat, favoriteSongs) {
        var list = initialSongs
        if (selCat == "Favorites Only") {
            list = list.filter { s -> favoriteSongs.any { f -> f.songId == s.id } }
        } else if (selCat != "All") {
            list = list.filter { it.category == selCat }
        }

        if (query.isNotEmpty()) {
            list = list.filter {
                it.title.lowercase().contains(query.lowercase()) ||
                it.lyricsEnglish.lowercase().contains(query.lowercase()) ||
                it.lyricsKannada.lowercase().contains(query.lowercase())
            }
        }
        list
    }

    // Simple filters for user contributed songs
    val filteredUserSongs = remember(query, selCat, userSongs) {
        var list = userSongs
        if (selCat != "All" && selCat != "Favorites Only") {
            list = list.filter { it.category == selCat }
        }
        if (query.isNotEmpty()) {
            list = list.filter {
                it.title.lowercase().contains(query.lowercase()) ||
                it.lyricsEnglish.lowercase().contains(query.lowercase()) ||
                it.lyricsKannada.lowercase().contains(query.lowercase()) ||
                it.authorName.lowercase().contains(query.lowercase())
            }
        }
        list
    }

    if (selectedSong != null) {
        // Song Detail Screen with Playback Simulator & Karaoke Scrolling lyrics!
        SongDetailsView(song = selectedSong, viewModel = viewModel, onBack = { viewModel.selectedSong = null })
    } else if (selectedUserSong != null) {
        // Dynamic custom user submitted song Details View
        UserSongDetailsView(song = selectedUserSong, viewModel = viewModel, onBack = { viewModel.selectedUserSong = null })
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search & Categories Header Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("JFC KANNADA-ENGLISH WORSHIP SONGBOOK", color = GoldGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    // Text Query
                    OutlinedTextField(
                        value = query,
                        onValueChange = { viewModel.songSearchQuery = it },
                        placeholder = { Text("Search chords, title, keywords...", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "", tint = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GoldGlow,
                            unfocusedBorderColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("song_search")
                    )

                    // Horizontal Categories Scroller
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { cat ->
                            val isSel = selCat == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSel) GoldGlow else Color.White.copy(0.04f))
                                    .clickable { viewModel.songCategoryFilter = cat }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                  Text(
                                    text = cat,
                                    color = if (isSel) Color.Black else Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Pastor/Admin Edit Creator Panel - Visible only in Pastor Mode
            if (viewModel.isPastorMode) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).verticalScroll(rememberScrollState()),
                    colors = CardDefaults.cardColors(containerColor = DeepSlateSurface),
                    border = BorderStroke(1.5.dp, GoldGlow)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.EditNote, contentDescription = "", tint = GoldGlow)
                            Text("ADMIN WORSHIP POST CREATOR", color = GoldGlow, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Text("Add song lyrics and publish live instantly for all church believers.", color = AppTextLightGray, fontSize = 11.sp)
                        
                        OutlinedTextField(
                            value = viewModel.adminSongTitle,
                            onValueChange = { viewModel.adminSongTitle = it },
                            label = { Text("Song Title", color = Color.Gray, fontSize = 12.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = GoldGlow),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = viewModel.adminSongChords,
                                onValueChange = { viewModel.adminSongChords = it },
                                label = { Text("Chords/Tempo", color = Color.Gray, fontSize = 10.sp) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = GoldGlow),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = viewModel.adminSongCategory,
                                onValueChange = { viewModel.adminSongCategory = it },
                                label = { Text("Category", color = Color.Gray, fontSize = 10.sp) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = GoldGlow),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = viewModel.adminSongLyricsKannada,
                            onValueChange = { viewModel.adminSongLyricsKannada = it },
                            label = { Text("Lyrics (Kannada Version)", color = Color.Gray, fontSize = 12.sp) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = GoldGlow),
                            modifier = Modifier.fillMaxWidth().height(80.dp)
                        )

                        OutlinedTextField(
                            value = viewModel.adminSongLyricsEnglish,
                            onValueChange = { viewModel.adminSongLyricsEnglish = it },
                            label = { Text("Lyrics / Chords Info (English Version)", color = Color.Gray, fontSize = 12.sp) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = GoldGlow),
                            modifier = Modifier.fillMaxWidth().height(80.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = viewModel.adminSongContributorName,
                                onValueChange = { viewModel.adminSongContributorName = it },
                                label = { Text("Author Name", color = Color.Gray, fontSize = 10.sp) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = GoldGlow),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = viewModel.adminSongContributorPhone,
                                onValueChange = { viewModel.adminSongContributorPhone = it },
                                label = { Text("Author Number", color = Color.Gray, fontSize = 10.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = GoldGlow),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Button(
                            onClick = { viewModel.submitAdminSong() },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldGlow, contentColor = Color.Black),
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        ) {
                            Text("PUBLISH & SHARE WORSHIP POST", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Screen listing
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Section 1: User/Admin Submitted custom songs
                if (filteredUserSongs.isNotEmpty()) {
                    item {
                        Text(
                            text = "CHURCH INSPIRED SONGS (${filteredUserSongs.size})",
                            color = GoldGlow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(filteredUserSongs) { song ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DeepSlateSurface)
                                .border(1.2.dp, GoldGlow, RoundedCornerShape(12.dp))
                                .clickable { viewModel.selectUserWorshipSong(song) }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GoldGlow.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Star, contentDescription = "", tint = GoldGlow)
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(song.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Text("Shared by " + song.authorName, color = AmberGlow, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                    Text("|", color = Color.White.copy(0.2f), fontSize = 10.sp)
                                    Text(song.chordsKeys, color = Color.LightGray, fontSize = 10.sp)
                                }
                            }
                            
                            Icon(Icons.Default.ChevronRight, contentDescription = "", tint = Color.LightGray)
                        }
                    }
                }

                // Section 2: Default Static Songs
                item {
                    Text(
                        text = "JFC OFFLINE HYMNS SONGBOOK",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                if (filteredSongs.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.LibraryMusic, contentDescription = "", tint = Color.White.copy(0.2f), modifier = Modifier.size(54.dp))
                            Text("No worship songs found in this selection.", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                } else {
                    items(filteredSongs) { song ->
                        val isFav = viewModel.isSongFav(song.id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DeepSlateCard)
                                .border(1.dp, if (isFav) GoldGlow.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                .clickable { viewModel.selectWorshipSong(song) }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GoldGlow.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.MusicNote, contentDescription = "", tint = GoldGlow)
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(song.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Text(song.category, color = AmberGlow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("|", color = Color.White.copy(0.2f), fontSize = 10.sp)
                                    Text(song.chordsKeys, color = Color.LightGray, fontSize = 10.sp)
                                }
                            }

                            IconButton(
                                onClick = { viewModel.toggleSongFavorite(song) }
                            ) {
                                Icon(
                                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Song bookmark toggle",
                                    tint = if (isFav) Color.Red else Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Detailed lyrics screen with built-in chords layout & scroll player
@Composable
fun SongDetailsView(
    song: WorshipSong,
    viewModel: JfcViewModel,
    onBack: () -> Unit
) {
    var chordToggle by remember { mutableStateOf(true) }
    var languageView by remember { mutableStateOf("Both") } // Both, Kannada, English
    val isFav = viewModel.isSongFav(song.id)

    Column(modifier = Modifier.fillMaxSize().background(DeepCharcoal)) {
        // Back toolbar
        Row(
            modifier = Modifier.fillMaxWidth().background(DeepSlateSurface).padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Return back", tint = Color.White)
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(song.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(song.chordsKeys, color = GoldGlow, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }

            IconButton(onClick = { viewModel.toggleSongFavorite(song) }) {
                Icon(
                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "",
                    tint = if (isFav) Color.Red else Color.White
                )
            }
        }

        // Advanced option bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // View toggle (Lyrics language)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(0.04f))
                    .padding(2.dp)
            ) {
                val views = listOf("Both", "Kannada", "English")
                views.forEach { v ->
                    val isS = languageView == v
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isS) GoldGlow else Color.Transparent)
                            .clickable { languageView = v }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(v, color = if (isS) Color.Black else Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Chords visibility
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(0.04f))
                    .clickable { chordToggle = !chordToggle }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.QueueMusic, contentDescription = "", tint = if (chordToggle) GoldGlow else Color.Gray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Chords", color = if (chordToggle) GoldGlow else Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        // -----------------------------------------------------------------
        // KARAOKE PLAY/PAUSE SIMULATOR HUD
        // -----------------------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
            border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.15f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (viewModel.isAudioPlaying) "PLAYING RECORDED SERVICE MUSIC" else "KARAOKE / AUDIO GUIDE PLAYBACK",
                            color = GoldGlow,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = if (viewModel.isAudioPlaying) "Chords follow along active scrolling..." else "Tap play to listen and learn chords.",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(
                        onClick = { viewModel.togglePlayWorshipSong() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GoldGlow)
                    ) {
                        Icon(
                            imageVector = if (viewModel.isAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play button player",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Seek timeline slider
                Column {
                    val durationMax = 272 // 4 min 32 sec
                    val progress = viewModel.audioProgressSec
                    LinearProgressIndicator(
                        progress = progress.toFloat() / durationMax.toFloat(),
                        color = GoldGlow,
                        trackColor = Color.White.copy(0.1f),
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(String.format("%02d:%02d", progress / 60, progress % 60), color = Color.Gray, fontSize = 10.sp)
                        Text("04:32", color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }
        }

        // Song Scrollable Body
        Box(modifier = Modifier.fillMaxSize().weight(1f).padding(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Formatting lyric blocks (we will search and parse chord annotations if toggled)
                if (languageView == "Kannada" || languageView == "Both") {
                    Text("KANNADA LYRICS", color = AmberGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    val lines = song.lyricsKannada.split("\n")
                    lines.forEachIndexed { index, line ->
                        val isHighlightedLine = viewModel.isAudioPlaying && (index == viewModel.currentKaraokeLine)
                        
                        // Parse mock chords
                        val containsChords = chordToggle && (line.contains("[ಪಲ್ಲವಿ]") || line.contains("[ಚರಣ"))
                        val styleColor = if (isHighlightedLine) GoldGlow else Color.White
                        val sizeMultiplier = if (isHighlightedLine) 16.sp else 14.sp
                        val backgroundAlpha = if (isHighlightedLine) 0.1f else 0.0f

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(GoldGlow.copy(alpha = backgroundAlpha))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            if (containsChords && chordToggle) {
                                Text("CHORDS: G -> C -> D -> Em", color = AmberGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
                            }
                            Text(
                                text = line,
                                color = styleColor,
                                fontSize = sizeMultiplier,
                                lineHeight = 24.sp,
                                fontWeight = if (isHighlightedLine) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                if (languageView == "Both") {
                    Divider(color = Color.White.copy(0.08f), modifier = Modifier.padding(vertical = 10.dp))
                }

                if (languageView == "English" || languageView == "Both") {
                    Text("ENGLISH LYRICS", color = AmberGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    val lines = song.lyricsEnglish.split("\n")
                    lines.forEachIndexed { index, line ->
                        val isHighlightedLine = viewModel.isAudioPlaying && (index == viewModel.currentKaraokeLine)
                        val containsChords = chordToggle && (line.contains("[Chorus]") || line.contains("[Verse"))
                        val styleColor = if (isHighlightedLine) GoldGlow else Color.LightGray
                        val sizeMultiplier = if (isHighlightedLine) 15.sp else 13.sp
                        val backgroundAlpha = if (isHighlightedLine) 0.1f else 0.0f

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(GoldGlow.copy(alpha = backgroundAlpha))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            if (containsChords && chordToggle) {
                                Text("CHORDS: Em -> C -> G -> D", color = AmberGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
                            }
                            Text(
                                text = line,
                                color = styleColor,
                                fontSize = sizeMultiplier,
                                lineHeight = 22.sp,
                                fontWeight = if (isHighlightedLine) FontWeight.Bold else FontWeight.Normal,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

// -------------------------------------------------------------------------
// 7. SERMONS & MEDIA TAB (INCLUDING MOCK LIVE STREAM WITH CHAT)
// -------------------------------------------------------------------------
@Composable
fun JfcMediaScreen(
    viewModel: JfcViewModel
) {
    val selCat = viewModel.sermonCategoryFilter
    val sermons = ChurchMediaData.sermons
    val selectedSermon = viewModel.selectedSermon
    val liveStreaming = viewModel.liveStreamingEnabled
    val context = LocalContext.current

    val categories = listOf("All", "Faith", "Healing", "Salvation", "Worship", "Prayer", "Holy Spirit")

    val filteredSermons = remember(selCat) {
        if (selCat == "All") sermons else sermons.filter { it.category == selCat }
    }

    if (liveStreaming) {
        // Render Live Worship module with dynamic chat streamer!
        LiveStreamerView(viewModel = viewModel, onBack = { viewModel.stopLiveStream() })
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // Sermon Detail HUD overlay if video is playing
            if (selectedSermon != null) {
                SermonPlaybackHub(sermon = selectedSermon, onDismiss = { viewModel.selectedSermon = null })
            }

            // Live Church Feature Banner styled beautifully with AppLiveBg for light mode
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { viewModel.startLiveStream() },
                colors = CardDefaults.cardColors(containerColor = AppLiveBg),
                border = BorderStroke(1.dp, Color.Red.copy(0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text("LIVE CHURCH WORSHIP ONLINE", color = Color(0xFFC0392B), fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 0.5.sp)
                        Text("JFC Sunday service is currently broadcasting live. Click to join and pray with believers.", color = AppTextWhite, fontSize = 11.sp)
                    }

                    IconButton(
                        onClick = { viewModel.startLiveStream() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    ) {
                        Icon(Icons.Default.LiveTv, contentDescription = "Live church button", tint = Color.White)
                    }
                }
            }

            // HIGH CONTRAST YOUTUBE SUBSCRIBE BANNER (Direct request from user)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
                border = BorderStroke(1.5.dp, Color(0xFFC0392B).copy(0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.youtube.com/c/JesusFollowersMinistriesDavanagere"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Opening JFC YouTube Channel...", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // YouTube icon container in high contrast
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFC0392B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow, 
                            contentDescription = "YouTube icon", 
                            tint = Color.White, 
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "JFC MINISTRIES YOUTUBE", 
                            color = Color(0xFFC0392B), 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 11.sp
                        )
                        Text(
                            text = "SUBSCRIBE to our YouTube channel for daily prayers & messages.",
                            color = AppTextLightGray,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    
                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.youtube.com/c/JesusFollowersMinistriesDavanagere"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Opening YouTube...", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("SUBSCRIBE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Categories list
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isS = selCat == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isS) GoldGlow else Color.Gray.copy(0.08f))
                            .clickable { viewModel.sermonCategoryFilter = cat }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isS) Color.White else AppTextWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Category Listings
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (filteredSermons.isEmpty()) {
                    item {
                        Text("No video messages in this category yet.", color = AppTextLightGray, fontSize = 13.sp, modifier = Modifier.padding(16.dp))
                    }
                }
                
                items(filteredSermons) { s ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DeepSlateCard)
                            .border(1.dp, Color.LightGray.copy(0.2f), RoundedCornerShape(12.dp))
                            .clickable { viewModel.selectedSermon = s }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Thumbnail program placeholder
                        Box(
                            modifier = Modifier
                                .size(90.dp, 60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayCircleFilled, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(24.dp))
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(s.title, color = AppTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Speaker: ${s.speaker}", color = AppTextLightGray, fontSize = 11.sp)
                            
                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldGlow.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(s.category, color = GoldGlow, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Text(s.duration, color = AppTextLightGray, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Full video playback mockup HUD
@Composable
fun SermonPlaybackHub(
    sermon: SermonMedia,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, GoldGlow.copy(0.3f))
    ) {
        Column {
            // Video screen mockup
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("JFC TV ONLINE BROADCAST", color = GoldGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.Tv, contentDescription = "", tint = Color.White, modifier = Modifier.size(36.dp))
                    Text("Streaming: ${sermon.title}", color = Color.White, fontSize = 11.sp, textAlign = TextAlign.Center)
                }
                
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "", tint = Color.White)
                }
            }

            // Description details
            Column(modifier = Modifier.padding(14.dp)) {
                Text(sermon.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(sermon.description, color = Color.LightGray, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

// Live stream interactive view with chat comments scrolling!
@Composable
fun LiveStreamerView(
    viewModel: JfcViewModel,
    onBack: () -> Unit
) {
    val comments = viewModel.liveChatComments
    var chatText by remember { mutableStateOf("") }
    
    val avatarColors = listOf(Color.Red, Color.Blue, Color(0xFF4CAF50), Color(0xFFFF9800))

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Toolbar
        Row(
            modifier = Modifier.fillMaxWidth().background(DeepSlateSurface).padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Return back", tint = Color.White)
            }
            Text("JFC LIVE WORSHIP STREAM", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color.Red).padding(horizontal = 6.dp, vertical = 2.dp)) {
                Text("LIVE", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Live Feed Broadcaster Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.VolumeUp, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(36.dp))
                Text("🎵 HOLY PRESENCE AND HYMNS 🎵", color = GoldGlow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Conducted by Pastor Vincent Paul • Live Davanagere Choir", color = Color.White, fontSize = 11.sp)
            }
        }

        // Chat stream area
        Text(
            text = "BELIEVERS COMMUNITY CHAT (SIMULATED LIVE)",
            color = Color.Gray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(12.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(1f).padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            reverseLayout = true // Most recent chats at bottom
        ) {
            items(comments.asReversed()) { comment ->
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(avatarColors[comment.avatarColorIndex % avatarColors.size]),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(comment.user.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(comment.user, color = GoldGlow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(comment.text, color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        // Send chats input HUD
        Row(
            modifier = Modifier.fillMaxWidth().background(DeepSlateSurface).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = chatText,
                onValueChange = { chatText = it },
                placeholder = { Text("Write blessings / type praise...", color = Color.Gray) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = GoldGlow,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    if (chatText.trim().isNotEmpty()) {
                        viewModel.liveChatComments.add(
                            com.example.ui.viewmodel.LiveChatComment(
                                user = viewModel.userProfile.value?.name ?: "Believer Paul",
                                text = chatText.trim(),
                                avatarColorIndex = 3
                            )
                        )
                        chatText = ""
                    }
                }
            ) {
                Icon(Icons.AutoMirrored.Default.Send, contentDescription = "Send check live", tint = GoldGlow)
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. EVENTS MODULE (COUNTDOWN TIMER + RESERVATION QR ENTRY PASS)
// -------------------------------------------------------------------------
@Composable
fun JfcEventsScreen(
    viewModel: JfcViewModel
) {
    val bookingState = viewModel.registeredEventsList
    val timerText = viewModel.countdownText

    val listEvents = listOf(
        Triple("e_1", "Grand Faith Revival Crusade 2026", "A powerful healing and praise gathering with Pastor Vincent in main grounds.\n🗓️ May 31 • 6:00 PM onwards\n📍 Davanagere Church Stadium"),
        Triple("e_2", "JFC Sabbath Sunday Service", "Weekly family restoration worship study.\n🗓️ June 03 • 8:30 AM\n📍 Main Prayer Sanctuary"),
        Triple("e_3", "Youth Fellowship Fire Summit", "Youth interactive sermon workshops, counseling and dynamic singing studies.\n🗓️ June 12 • 4:00 PM\n📍 Fellowship Pavilion")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Countdown clock Card
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF211D12)),
            border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "NEXT GRAND REVIVAL MEETING COUNTDOWN",
                    color = GoldGlow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Text(
                    text = timerText,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Text(
                    "Receive healing, salvation and holy water. Mark your calendars!",
                    color = Color.LightGray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Active listing of events
        Text(
            "CHURCH EVENTS SCHEDULE",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(listEvents) { ev ->
                val (id, title, desc) = ev
                val isRegistered = bookingState.contains(id)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
                    border = BorderStroke(1.dp, if (isRegistered) GoldGlow.copy(0.4f) else Color.White.copy(0.04f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(title, color = GoldGlow, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(desc, color = Color.White.copy(0.7f), fontSize = 12.sp, lineHeight = 18.sp, modifier = Modifier.padding(vertical = 10.dp))
                        
                        Divider(color = Color.White.copy(0.05f), modifier = Modifier.padding(bottom = 12.dp))

                        if (isRegistered) {
                            // Display beautiful Entry pass with programmatical Custom Canvas QR code!
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White.copy(0.02f))
                                    .padding(8.dp)
                            ) {
                                Text("🎟️ OFFICIAL JFC CONNECT ENTRY PASS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                
                                JfcMockQrCode(
                                    modifier = Modifier.size(100.dp),
                                    contentTag = "Event:$id"
                                )
                                
                                Text("Pass ID: JFC-$id-SECURE", color = Color.LightGray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                
                                TextButton(onClick = { viewModel.registerForEvent(id) }) {
                                    Text("Cancel Registration", color = Color.Red, fontSize = 11.sp)
                                }
                            }
                        } else {
                            Button(
                                onClick = { viewModel.registerForEvent(id) },
                                modifier = Modifier.fillMaxWidth().testTag("register_event_$id"),
                                colors = ButtonDefaults.buttonColors(containerColor = GoldGlow, contentColor = Color.Black),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Secure Free Pass / Registration", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 9. PRAYER CORNER: "NEED PRAYER?" & HOME PRAYER BOOKING MODULE
// -------------------------------------------------------------------------
@Composable
fun JfcPrayerScreen(
    viewModel: JfcViewModel
) {
    val requests by viewModel.allPrayerRequests.collectAsStateWithLifecycle()
    val bookings by viewModel.allHomeBookings.collectAsStateWithLifecycle()
    val isPastor = viewModel.isPastorMode

    var isBookingFormVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        
        // 1. NEED PRAYER CARD SYSTEM (Urgent Requests)
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
            border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.15f))
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "NEED PRAYER? SEND REQUESTS TO ALTAR",
                    color = GoldGlow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )

                OutlinedTextField(
                    value = viewModel.newPrayerRequestName,
                    onValueChange = { viewModel.newPrayerRequestName = it },
                    label = { Text("Your Name", color = Color.Gray) },
                    placeholder = { Text("Optional if anonymous", color = Color.Gray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldGlow,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = viewModel.newPrayerRequestText,
                    onValueChange = { viewModel.newPrayerRequestText = it },
                    label = { Text("Prayer Request details", color = Color.Gray) },
                    placeholder = { Text("e.g. Health healing, miracle deliverance needed...", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldGlow,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth().height(80.dp).testTag("prayer_desc")
                )

                // Select categories
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val cats = listOf("Healing", "Family", "Youth", "General")
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        cats.forEach { c ->
                            val s = viewModel.newPrayerCategory == c
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (s) AmberGlow else Color.White.copy(0.04f))
                                    .clickable { viewModel.newPrayerCategory = c }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(c, color = if (s) Color.Black else Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Anonymous Checkbox
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = viewModel.newPrayerAnonymous,
                            onCheckedChange = { viewModel.newPrayerAnonymous = it },
                            colors = CheckboxDefaults.colors(checkedColor = GoldGlow)
                        )
                        Text("Private/Anon", color = Color.White, fontSize = 10.sp)
                    }
                }

                Button(
                    onClick = { viewModel.submitNewPrayerRequest() },
                    modifier = Modifier.fillMaxWidth().testTag("submit_prayer"),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldGlow, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Submit Live Urgent Prayer", fontWeight = FontWeight.Bold)
                }
            }
        }

        // 2. HOME PRAYER BOOKING ("Invite JFC Prayer Team")
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131A21)),
            border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Home, contentDescription = "", tint = GoldGlow)
                        Text(
                            "INVITE JFC PRAYER TEAM HOME",
                            color = GoldGlow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    TextButton(onClick = { isBookingFormVisible = !isBookingFormVisible }) {
                        Text(if (isBookingFormVisible) "Hide Details" else "Open Form", color = AmberGlow, fontSize = 11.sp)
                    }
                }

                if (isBookingFormVisible) {
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Text("Arrange house blessing, home counselings, and fasting prayers at your resident.", color = Color.LightGray, fontSize = 11.sp)
                    
                    Spacer(modifier = Modifier.height(10.dp))

                    val purposes = listOf("House Blessing", "Worship at Home", "counseling", "Fasting Prayer")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        purposes.forEach { p ->
                            val s = viewModel.bookPurpose == p
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (s) GoldGlow else Color.White.copy(0.04f))
                                    .clickable { viewModel.bookPurpose = p }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(p, color = if (s) Color.Black else Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.bookPhone,
                        onValueChange = { viewModel.bookPhone = it },
                        label = { Text("Contact Resident Phone", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GoldGlow,
                            unfocusedBorderColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = viewModel.bookDate,
                        onValueChange = { viewModel.bookDate = it },
                        label = { Text("Preferred Date", color = Color.Gray) },
                        placeholder = { Text("e.g. 2026-05-30") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GoldGlow,
                            unfocusedBorderColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = viewModel.bookAddress,
                        onValueChange = { viewModel.bookAddress = it },
                        label = { Text("Full Residential Address", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GoldGlow,
                            unfocusedBorderColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth().height(60.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.requestHomePrayerBooking() },
                        modifier = Modifier.fillMaxWidth().testTag("book_home_prayer"),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldGlow, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Confirm Appointment Invitation", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // --- PUBLIC REQUESTS LISTS AND COMFORT FEED & PASTOR MODULE ---
        Text(
            text = "ALTAR PRAYER COMMUNITIY LOGS",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (requests.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .background(DeepSlateCard, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No prayer requests logged yet. Be the first to type!", color = Color.Gray, fontSize = 12.sp)
                }
            } else {
                requests.forEach { req ->
                    PrayerRequestItem(req = req, isPastor = isPastor, viewModel = viewModel)
                }
            }
        }

        // --- BOOKINGS LOG VIEW AS WELL FOR THE CONVENIENCE OF TESTERS ---
        if (bookings.isNotEmpty()) {
            Text(
                text = "MY RESIDENTIAL APPOINTMENTS (${bookings.size})",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                bookings.forEach { b ->
                    HomeBookingItem(b = b, isPastor = isPastor, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun PrayerRequestItem(
    req: PrayerRequest,
    isPastor: Boolean,
    viewModel: JfcViewModel
) {
    var showPastorResponseInput by remember { mutableStateOf(false) }
    var responseText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DeepSlateCard),
        border = BorderStroke(1.dp, Color.White.copy(0.05f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Person, contentDescription = "", tint = GoldGlow, modifier = Modifier.size(16.dp))
                    Text(req.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(GoldGlow.copy(0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(req.category.uppercase(), color = GoldGlow, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(req.request, color = Color.LightGray, fontSize = 12.sp, modifier = Modifier.padding(vertical = 10.dp), lineHeight = 16.sp)

            if (req.pastoralResponse != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AmberGlow.copy(0.08f))
                        .border(1.dp, AmberGlow.copy(0.2f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text("⛪ PASTORAL BLESSING COMFORTS:", color = AmberGlow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(req.pastoralResponse, color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp), lineHeight = 15.sp)
                    }
                }
            }

            // Pastor Admin View actions
            if (isPastor) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(onClick = { showPastorResponseInput = !showPastorResponseInput }) {
                        Text("Respond as Pastor", color = GoldGlow, fontSize = 11.sp, textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)
                    }
                    TextButton(onClick = { viewModel.deletePrayerAsAdmin(req.id) }) {
                        Text("Archive Request", color = Color.Red, fontSize = 11.sp)
                    }
                }

                if (showPastorResponseInput) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = responseText,
                            onValueChange = { responseText = it },
                            placeholder = { Text("Type comfort, e.g. 'I will lift this on prayer...'", color = Color.Gray, fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = GoldGlow,
                                unfocusedBorderColor = Color.Gray
                            ),
                            modifier = Modifier.weight(1f),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 11.sp)
                        )
                        IconButton(
                            onClick = {
                                if (responseText.isNotEmpty()) {
                                    viewModel.submitPastoralResponse(req.id, responseText)
                                    responseText = ""
                                    showPastorResponseInput = false
                                }
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Default.Send, contentDescription = "", tint = GoldGlow)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 10. TESTIMONY DIVISION LIST WITH PASTOR APPROVE BUTTON TO HIGHLIGHT COMPREHENSION
// -------------------------------------------------------------------------
// We can integrate testimonies inside Home tab, or display inside a sub-sheet inside Home or Prayer! Let's display inside a neat bottom section in Home/Prayer Tab, or add Testimony submitting modal beautifully inside the Prayer tab or as a custom sheet. Let's make an intuitive collapsible section for posting testimonies on the Prayer Tab!
// Wait! Let's build a dedicated section inside the prayer tab or add on Home Tab so that testimonies can be posted and viewed cleanly!
// Let's create an elegant Testimonies dialogue or custom view in a collapsible card. Yes! Let's define it inside the Prayer Screen lazy listing, or build a simple button. Let's list testimonies on top of Altar Logs inside Prayer Tab. This is beautiful!

// Let's create testimonies dialog or expandable block inside Prayer tab above Altar logs.
// Let's include Testimony expandable box in JfcPrayerScreen just above altar request.

// -------------------------------------------------------------------------
// 11. SECURED DONATIONS VIEW
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationDialog(
    viewModel: JfcViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = DeepSlateSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("JFC Ministry Support Gate", color = GoldGlow, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "", tint = Color.White)
                    }
                }

                Divider(color = Color.White.copy(0.1f))

                Text("Your contributions help support digital evangelism, food kitchens, and Sunday youth materials securely.", color = Color.LightGray, fontSize = 11.sp, textAlign = TextAlign.Center)

                // Fast selections Amounts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val amounts = listOf("500", "1000", "5000", "10000")
                    amounts.forEach { amt ->
                        val isS = viewModel.customDonationAmount == amt
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isS) GoldGlow else Color.White.copy(0.04f))
                                .clickable { viewModel.customDonationAmount = amt }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("₹$amt", color = if (isS) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                OutlinedTextField(
                    value = viewModel.customDonationAmount,
                    onValueChange = { viewModel.customDonationAmount = it },
                    label = { Text("Or Enter Custom Amount (₹)", color = Color.Gray) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("donation_amount")
                )

                // Select payment mode
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(0.04f))
                        .padding(4.dp)
                ) {
                    val modes = listOf("UPI", "Razorpay Gateway", "QR Code")
                    modes.forEach { m ->
                        val isSel = viewModel.selectedDonationType == m
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) GoldGlow else Color.Transparent)
                                .clickable { viewModel.selectedDonationType = m }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(m, color = if (isSel) Color.Black else Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // If QR Mode, Draw a safe mockup dynamic QR!
                if (viewModel.selectedDonationType == "QR Code") {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    ) {
                        Text("Scan UPI QR Code to Give", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        
                        JfcMockQrCode(
                            modifier = Modifier.size(120.dp),
                            contentTag = "upi://pay?pa=jfc@ybl&pn=JesusFollowers&am=${viewModel.customDonationAmount}"
                        )
                        
                        Text("jfc@ybl (Jesus Followers Church Davanagere)", color = Color.LightGray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                Button(
                    onClick = {
                        viewModel.triggerDonationFlow(viewModel.customDonationAmount, viewModel.selectedDonationType)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("trigger_pay"),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldGlow, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Proceed ₹${viewModel.customDonationAmount} Safely", fontWeight = FontWeight.Bold)
                }

                // Thank indicator
                if (viewModel.showDonationSuccessDialog) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Green.copy(0.12f))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "", tint = Color.Green, modifier = Modifier.size(28.dp))
                        Text("Transaction Completed Successfully!", color = Color.Green, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Contribution Receipt logged in church portal. Praise God!", color = Color.LightGray, fontSize = 10.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 12. AI BIBLE ASSISTANT HUD DIALOG (CHAT BOT WITH CHAT LOGS)
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantDialog(
    viewModel: JfcViewModel,
    onDismiss: () -> Unit
) {
    val messages = viewModel.aiChatHistory
    val input = viewModel.aiChatInput
    val generating = viewModel.isAiGenerating

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            colors = CardDefaults.cardColors(containerColor = DeepSlateSurface),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.25f))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Modal Top bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DeepSlateCard)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "", tint = GoldGlow)
                        Column {
                            Text("AI Bible Companion", color = GoldGlow, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Theological guidance strictly scripture-based", color = Color.LightGray, fontSize = 9.sp)
                        }
                    }

                    Row {
                        IconButton(onClick = { viewModel.clearAiChat() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Clear dialogue history", tint = Color.White)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "", tint = Color.White)
                        }
                    }
                }

                // Chat Messages scroller
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (messages.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = "", tint = GoldGlow.copy(0.3f), modifier = Modifier.size(40.dp))
                                Text("No theological dialogue initialized.", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    } else {
                        items(messages) { msg ->
                            val side = if (msg.isUser) Alignment.End else Alignment.Start
                            val cardBg = if (msg.isUser) GoldGlow else DeepSlateCard
                            val textCl = if (msg.isUser) Color.Black else Color.White
                            val borderSt = if (msg.isUser) null else BorderStroke(1.dp, GoldGlow.copy(alpha = 0.12f))

                            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = side) {
                                Text(
                                    msg.senderName,
                                    color = if (msg.isUser) GoldGlow else AmberGlow,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = cardBg),
                                    shape = RoundedCornerShape(
                                        topStart = if (msg.isUser) 12.dp else 0.dp,
                                        topEnd = if (msg.isUser) 0.dp else 12.dp,
                                        bottomStart = 12.dp,
                                        bottomEnd = 12.dp
                                    ),
                                    border = borderSt,
                                    modifier = Modifier.widthIn(max = 240.dp)
                                ) {
                                    Text(
                                        text = msg.text,
                                        color = textCl,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (generating) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(color = GoldGlow, modifier = Modifier.size(16.dp))
                                Text("AI studying scriptures...", color = Color.Gray, fontSize = 11.sp, fontStyle = FontStyle.Italic)
                            }
                        }
                    }
                }

                // Recommendation fast cards
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .background(Color.White.copy(0.02f))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val quickPrompts = listOf(
                        "Verse for fear",
                        "Encourage me on peace",
                        "Family blessing scripture",
                        "Explain Mark 1:17"
                    )
                    quickPrompts.forEach { p ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(0.04f))
                                .clickable {
                                    viewModel.aiChatInput = p
                                    viewModel.sendAiChatQuery()
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(p, color = GoldGlow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Chat sender HUD
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DeepSlateCard)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { viewModel.aiChatInput = it },
                        placeholder = { Text("Ask on bible, fear or healing...", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.weight(1f).testTag("ai_chat_input")
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = { viewModel.sendAiChatQuery() },
                        enabled = input.trim().isNotEmpty() && !generating,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = GoldGlow),
                        modifier = Modifier.testTag("ai_send_button")
                    ) {
                        Icon(Icons.AutoMirrored.Default.Send, contentDescription = "Send query")
                    }
                }
            }
        }
    }
}

@Composable
fun HomeBookingItem(
    b: HomeBooking,
    isPastor: Boolean,
    viewModel: JfcViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F141C)),
        border = BorderStroke(1.dp, Color.White.copy(0.06f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(b.purpose, color = GoldGlow, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            when (b.status) {
                                "Pending" -> Color.Gray.copy(0.12f)
                                "Scheduled" -> Color.Blue.copy(0.12f)
                                else -> Color.Green.copy(0.12f)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = b.status.uppercase(),
                        color = when (b.status) {
                            "Pending" -> Color.Gray
                            "Scheduled" -> Color.Cyan
                            else -> Color.Green
                        },
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text("Resident Family: ${b.familyCount} Members | Date: ${b.date} • ${b.preferredTime}", color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(vertical = 4.dp))
            Text("Address: ${b.address}", color = Color.LightGray, fontSize = 11.sp)
            
            if (isPastor && b.status == "Pending") {
                Row(modifier = Modifier.padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { viewModel.updateBookingStatusAsPastor(b.id, "Scheduled") },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldGlow, contentColor = Color.Black),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp)
                    ) {
                        Text("Assign Pastor Team", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
