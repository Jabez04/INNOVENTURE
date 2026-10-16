package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CosmicPurpleBright
import com.example.ui.theme.CosmicPurpleDeep
import com.example.ui.theme.CosmicPurpleGlow
import com.example.ui.theme.CosmicPurplePrimary
import com.example.ui.theme.GlassBorderPurple
import com.example.ui.theme.GlowPillBorder
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonMintDark
import com.example.ui.theme.NeonMintGlow
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardElevated
import com.example.ui.theme.ObsidianGlass
import com.example.ui.theme.TextLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

/**
 * 1. SphereAi Hero / Onboarding Screen (Center phone in image.png)
 */
@Composable
fun SphereAiHeroView(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .testTag("sphere_hero_view")
    ) {
        // Celestial Hero Background
        Image(
            painter = painterResource(id = R.drawable.sphere_cosmic_hero_1792144291634),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark gradient vignette
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x99080511),
                            ObsidianBackground
                        ),
                        startY = 300f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: SphereAi Logo + 3 dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(NeonMint),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(NeonMintDark)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SphereAi",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                // 3 Dots indicator
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(NeonMint))
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0x6600F298)))
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0x3300F298)))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Center Content: Badge + Massive Title + Subtitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                // Pill badge: Your Assistant 2.0
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0x332E1D52),
                    modifier = Modifier.border(1.dp, GlassBorderPurple, RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = CosmicPurpleBright,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Your Assistant 2.0",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextLavender
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Massive Headline: "Elevate your thinking"
                Text(
                    text = "Elevate your\nthinking",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    textAlign = TextAlign.Center,
                    lineHeight = 44.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Subtitle: "Discover endless ways our AI can enhance your business thinking"
                Text(
                    text = "Discover endless ways our AI can enhance your business thinking",
                    fontSize = 14.sp,
                    color = TextLavender,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Glowing Violet Capsule: "Get Started"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0x403B1E6D), Color(0x605B21B6), Color(0x403B1E6D))
                            )
                        )
                        .border(1.5.dp, GlowPillBorder, RoundedCornerShape(28.dp))
                        .clickable { onGetStarted() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Secondary Button: "Sign in"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(Color(0x33100A1F))
                        .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(26.dp))
                        .clickable { onSignIn() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sign in",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextLavender
                    )
                }
            }
        }
    }
}

/**
 * 2. SphereAi Assistant Hub (Left phone in image.png)
 */
@Composable
fun SphereAiHubView(
    onOpenChat: (String) -> Unit,
    onOpenLedger: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedService by remember { mutableStateOf("AI Chat") }

    val services = listOf("AI Chat", "BG Remover", "PDF Scaner", "Ledger")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(horizontal = 16.dp)
            .testTag("sphere_hub_view"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Header: "Hey, Jordan!" / "How can I assist you today?" + Grid Icon in mint rounded square
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hey, Jordan!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "How can I assist you today?",
                        fontSize = 14.sp,
                        color = TextLavender
                    )
                }

                // Mint-accented grid button
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(NeonMintGlow)
                        .border(1.5.dp, NeonMint, RoundedCornerShape(14.dp))
                        .clickable { onOpenLedger() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Menu",
                        tint = NeonMint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Services Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Services",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "View All",
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Service Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(services) { service ->
                        val isSelected = selectedService == service
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) NeonMint else ObsidianGlass)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonMint else GlassBorderPurple,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    selectedService = service
                                    if (service == "Ledger") onOpenLedger()
                                }
                                .padding(horizontal = 18.dp, vertical = 9.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = service,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) NeonMintDark else TextWhite
                            )
                        }
                    }
                }
            }
        }

        // Recent Chats Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Chats",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "View All",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // TalkTech Nexus Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenChat("TalkTech Nexus") },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(GlassBorderPurple, Color(0x108B5CF6))))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(CosmicPurpleDeep),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(TextWhite)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "TalkTech Nexus: Where Ideas Spark and Conversations Ignite",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite,
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 4 Grid Feature Cards: Generating Visuals, Document Creation, Meeting Scheduler, Note Taking
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SphereHubCard(
                        title = "Generating Visuals",
                        subtitle = "Create custom images, bring your ideas to life",
                        icon = Icons.Default.Image,
                        onClick = { onOpenChat("Generating Visuals") },
                        modifier = Modifier.weight(1f)
                    )
                    SphereHubCard(
                        title = "Document Creation",
                        subtitle = "Draft documents with all the essentials",
                        icon = Icons.Default.Description,
                        onClick = { onOpenChat("Document Creation") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SphereHubCard(
                        title = "Meeting Scheduler",
                        subtitle = "Organize your schedule with meeting planning",
                        icon = Icons.Default.CalendarMonth,
                        onClick = { onOpenChat("Meeting Scheduler") },
                        modifier = Modifier.weight(1f)
                    )
                    SphereHubCard(
                        title = "Note Taking",
                        subtitle = "Jot down important thoughts and ideas",
                        icon = Icons.Default.EditNote,
                        onClick = { onOpenChat("Note Taking") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Bottom Space for floating bar
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun SphereHubCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(Color(0x558B5CF6), Color(0x158B5CF6))
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon + Arrow Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0x3300F298)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = NeonMint,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.NorthEast,
                    contentDescription = null,
                    tint = TextLavender,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Title & Subtitle
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextLavender,
                    lineHeight = 14.sp,
                    maxLines = 2
                )
            }
        }
    }
}

/**
 * 3. SphereAi / ConvoCraft Chat View (Right phone in image.png)
 */
data class ChatMessage(
    val isUser: Boolean,
    val text: String,
    val hasImages: Boolean = false
)

@Composable
fun SphereAiChatView(
    chatTitle: String = "ConvoCraft: UX audit",
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                isUser = true,
                text = "Hi! I'm working on a new web design project and want to add some creative elements to make the site more engaging. Can you give me some tips?"
            ),
            ChatMessage(
                isUser = false,
                text = "Hi there! Using images strategically can really elevate your design. To create a more immersive experience, use illustrated or lightly textured backgrounds that subtly enhance certain sections. For instance, a soft gradient or abstract shapes.",
                hasImages = true
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .testTag("sphere_chat_view")
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ObsidianGlass)
                        .border(1.dp, GlassBorderPurple, CircleShape)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Title + Subtitle
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = chatTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Free Plan",
                        fontSize = 11.sp,
                        color = TextLavender
                    )
                }

                // Hamburger menu with mint background
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeonMintGlow)
                        .border(1.dp, NeonMint, RoundedCornerShape(12.dp))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = NeonMint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Chat Messages List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Date separator: "Today"
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = ObsidianGlass,
                            modifier = Modifier.border(1.dp, Color(0x338B5CF6), RoundedCornerShape(14.dp))
                        ) {
                            Text(
                                text = "Today",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextLavender,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                items(messages) { msg ->
                    ChatBubbleRow(message = msg)
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        // Floating Bottom Capsule Input Bar with Glowing Orb
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(30.dp),
                color = Color(0xCC18102E),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, GlowPillBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Attachment Icon
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "Attach",
                            tint = TextLavender,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Input Text Field
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text("Type your question", color = TextMuted, fontSize = 14.sp)
                        },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        singleLine = true
                    )

                    // Glowing 3D Iridescent Orb Send Button
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (inputText.isNotBlank()) {
                                    messages.add(ChatMessage(isUser = true, text = inputText))
                                    val prompt = inputText
                                    inputText = ""
                                    // Simulated SphereAi intelligent response
                                    messages.add(
                                        ChatMessage(
                                            isUser = false,
                                            text = "SphereAi Analysis: For \"$prompt\", optimal execution involves balancing high-contrast typography with cohesive obsidian surfaces. Let me know if you would like me to draft detailed specifications!"
                                        )
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sphere_glowing_orb_1792144307725),
                            contentDescription = "Send",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleRow(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!message.isUser) {
            // SphereAi Green Avatar
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(NeonMint),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(NeonMintDark)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = if (message.isUser) 18.dp else 4.dp,
                    bottomEnd = if (message.isUser) 4.dp else 18.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.isUser) Color(0xFF261944) else ObsidianCardElevated
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(GlassBorderPurple, Color(0x108B5CF6))
                    )
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = message.text,
                        fontSize = 14.sp,
                        color = TextWhite,
                        lineHeight = 20.sp
                    )

                    // Optional Visual Attachments in AI message
                    if (message.hasImages) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(90.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(CosmicPurpleDeep, CosmicPurplePrimary)
                                        )
                                    )
                                    .border(1.dp, GlassBorderPurple, RoundedCornerShape(12.dp))
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(90.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF1E1038), Color(0xFF3B1E6D))
                                        )
                                    )
                                    .border(1.dp, GlassBorderPurple, RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            }

            // Action row below AI response: Thumbs up, Thumbs down, Refresh, Copy
            if (!message.isUser) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    Icon(Icons.Default.ThumbUp, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    Icon(Icons.Default.ThumbDown, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                }
            }
        }

        if (message.isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // User Avatar
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "J",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}
