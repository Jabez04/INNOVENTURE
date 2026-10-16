package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.InventoryScreen
import com.example.ui.screens.KhataScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.SphereAiChatView
import com.example.ui.screens.SphereAiHeroView
import com.example.ui.screens.SphereAiHubView
import com.example.ui.screens.UpiReconciliationScreen
import com.example.ui.theme.CosmicPurpleBright
import com.example.ui.theme.CosmicPurpleDeep
import com.example.ui.theme.CosmicPurplePrimary
import com.example.ui.theme.GlassBorderPurple
import com.example.ui.theme.GlowPillBorder
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonMintDark
import com.example.ui.theme.NeonMintGlow
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianGlass
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.StreetLedgerTheme
import com.example.ui.theme.TextLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.StreetLedgerViewModel
import com.example.util.AppLanguage
import com.example.util.StreetLedgerStrings

class MainActivity : ComponentActivity() {

  private val viewModel: StreetLedgerViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      StreetLedgerTheme {
        SphereMasterApp(viewModel = viewModel)
      }
    }
  }
}

/**
 * Main application container implementing the exact SphereAi / ConvoCraft
 * visual theme, bottom floating bar with glowing 3D orb, and seamless navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SphereMasterApp(viewModel: StreetLedgerViewModel) {
  val language by viewModel.language.collectAsStateWithLifecycle()
  val ledgerTab by viewModel.currentTab.collectAsStateWithLifecycle()

  // App Level Navigation:
  // 0: SphereAi Hub ("Hey, Jordan!"), 1: SphereAi Chat ("ConvoCraft"), 2: Ledger Suite, 3: Hero Onboarding
  var mainAppMode by remember { mutableIntStateOf(0) }
  var activeChatTitle by remember { mutableStateOf("ConvoCraft: UX audit") }

  // Back Navigation Handling
  BackHandler(enabled = mainAppMode != 0) {
    mainAppMode = 0
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBackground)
  ) {
    when (mainAppMode) {
      3 -> {
        // Exact Center Phone: Hero "Elevate your thinking"
        SphereAiHeroView(
          onGetStarted = { mainAppMode = 0 },
          onSignIn = { mainAppMode = 0 }
        )
      }

      1 -> {
        // Exact Right Phone: Chat "ConvoCraft: UX audit"
        SphereAiChatView(
          chatTitle = activeChatTitle,
          onBack = { mainAppMode = 0 }
        )
      }

      else -> {
        // Standard App Shell for Hub (Mode 0) and Ledger Suite (Mode 2)
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          containerColor = ObsidianBackground,
          topBar = {
            CenterAlignedTopAppBar(
              title = {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.clickable { mainAppMode = 3 } // Tap logo to see Hero screen
                ) {
                  Box(
                    modifier = Modifier
                      .size(20.dp)
                      .clip(CircleShape)
                      .background(NeonMint),
                    contentAlignment = Alignment.Center
                  ) {
                    Box(
                      modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(NeonMintDark)
                    )
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = if (mainAppMode == 0) "SphereAi" else StreetLedgerStrings.get("app_title", language),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite
                  )
                }
              },
              navigationIcon = {
                // Quick Switcher Button between AI Assistant Hub and Business Ledger
                Surface(
                  modifier = Modifier
                    .padding(start = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                      mainAppMode = if (mainAppMode == 0) 2 else 0
                    }
                    .testTag("btn_mode_switch"),
                  shape = RoundedCornerShape(20.dp),
                  color = if (mainAppMode == 2) Color(0x3300F298) else Color(0x338B5CF6),
                  border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (mainAppMode == 2) NeonMint else CosmicPurpleBright
                  )
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = if (mainAppMode == 2) Icons.Default.Payments else Icons.Default.AutoAwesome,
                      contentDescription = null,
                      tint = if (mainAppMode == 2) NeonMint else CosmicPurpleBright,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = if (mainAppMode == 2) "Ledger" else "AI Hub",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = if (mainAppMode == 2) NeonMint else CosmicPurpleBright
                    )
                  }
                }
              },
              actions = {
                // One-tap Language toggle pill
                Surface(
                  modifier = Modifier
                    .padding(end = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { viewModel.toggleLanguage() }
                    .testTag("btn_language_toggle"),
                  shape = RoundedCornerShape(20.dp),
                  color = ObsidianGlass,
                  border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderPurple)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.Language,
                      contentDescription = "Language",
                      tint = NeonMint,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = if (language == AppLanguage.ENGLISH) "தமிழ்" else "ENG",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = TextWhite
                    )
                  }
                }
              },
              colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = ObsidianBackground
              )
            )
          }
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            if (mainAppMode == 0) {
              // Left Phone: SphereAi Hub with Services & 4-card grid
              SphereAiHubView(
                onOpenChat = { title ->
                  activeChatTitle = title
                  mainAppMode = 1
                },
                onOpenLedger = { mainAppMode = 2 }
              )
            } else {
              // Mode 2: Street Ledger Business Tabs in exact SphereAi theme
              when (ledgerTab) {
                0 -> DashboardScreen(viewModel = viewModel)
                1 -> KhataScreen(viewModel = viewModel)
                2 -> UpiReconciliationScreen(viewModel = viewModel)
                3 -> InventoryScreen(viewModel = viewModel)
                4 -> ReportsScreen(viewModel = viewModel)
              }
            }

            // Floating Bottom Bar (Matching the exact bottom capsule from the image)
            Box(
              modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
            ) {
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(64.dp),
                shape = RoundedCornerShape(32.dp),
                color = Color(0xDD120B24),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, GlowPillBorder)
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  if (mainAppMode == 0) {
                    // Hub Bottom Navigation: "New Chat" Capsule Button
                    Box(
                      modifier = Modifier
                        .padding(start = 8.dp)
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                          Brush.horizontalGradient(
                            listOf(Color(0x553B1E6D), Color(0x665B21B6))
                          )
                        )
                        .border(1.dp, GlassBorderPurple, RoundedCornerShape(22.dp))
                        .clickable {
                          activeChatTitle = "New Conversation"
                          mainAppMode = 1
                        }
                        .padding(horizontal = 24.dp),
                      contentAlignment = Alignment.Center
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                          imageVector = Icons.Default.ChatBubble,
                          contentDescription = null,
                          tint = TextWhite,
                          modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                          text = "New Chat",
                          fontSize = 14.sp,
                          fontWeight = FontWeight.Bold,
                          color = TextWhite
                        )
                      }
                    }

                    // Onboarding / Hero preview button pill
                    Box(
                      modifier = Modifier
                        .height(38.dp)
                        .clip(RoundedCornerShape(19.dp))
                        .background(ObsidianGlass)
                        .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(19.dp))
                        .clickable { mainAppMode = 3 }
                        .padding(horizontal = 12.dp),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = "Hero View",
                        fontSize = 11.sp,
                        color = TextLavender
                      )
                    }

                    // Iconic 3D Glowing Iridescent Orb Button (from image bottom right!)
                    Box(
                      modifier = Modifier
                        .padding(end = 4.dp)
                        .size(50.dp)
                        .clip(CircleShape)
                        .clickable {
                          activeChatTitle = "ConvoCraft: UX audit"
                          mainAppMode = 1
                        },
                      contentAlignment = Alignment.Center
                    ) {
                      Image(
                        painter = painterResource(id = R.drawable.sphere_glowing_orb_1792144307725),
                        contentDescription = "Sphere Orb",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                      )
                    }
                  } else {
                    // Ledger Suite Bottom Tabs: [Today] [Khata] [UPI] [Stock] [Reports] + Glowing Orb
                    val ledgerTabs = listOf(
                      Triple(0, StreetLedgerStrings.get("nav_dashboard", language), Icons.Default.Payments),
                      Triple(1, StreetLedgerStrings.get("nav_khata", language), Icons.Default.MenuBook),
                      Triple(2, StreetLedgerStrings.get("nav_upi", language), Icons.Default.QrCode2),
                      Triple(3, StreetLedgerStrings.get("nav_stock", language), Icons.Default.Inventory2),
                      Triple(4, StreetLedgerStrings.get("nav_reports", language), Icons.Default.Insights)
                    )

                    ledgerTabs.forEach { (index, label, icon) ->
                      val isSelected = ledgerTab == index
                      Box(
                        modifier = Modifier
                          .weight(1f)
                          .height(48.dp)
                          .clip(RoundedCornerShape(14.dp))
                          .background(if (isSelected) NeonMintGlow else Color.Transparent)
                          .clickable { viewModel.selectTab(index) },
                        contentAlignment = Alignment.Center
                      ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                          Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) NeonMint else TextLavender,
                            modifier = Modifier.size(18.dp)
                          )
                          Text(
                            text = label,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) NeonMint else TextMuted,
                            maxLines = 1
                          )
                        }
                      }
                    }

                    // Orb to jump straight back to AI Assistant
                    Box(
                      modifier = Modifier
                        .padding(end = 4.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable { mainAppMode = 0 },
                      contentAlignment = Alignment.Center
                    ) {
                      Image(
                        painter = painterResource(id = R.drawable.sphere_glowing_orb_1792144307725),
                        contentDescription = "AI Assistant",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
