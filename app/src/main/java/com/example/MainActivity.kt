package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.InventoryScreen
import com.example.ui.screens.KhataScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.UpiReconciliationScreen
import com.example.ui.theme.StreetLedgerTheme
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary
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
        StreetLedgerApp(viewModel = viewModel)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreetLedgerApp(viewModel: StreetLedgerViewModel) {
  val language by viewModel.language.collectAsStateWithLifecycle()
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()

  // Handle system back navigation to return to Dashboard tab
  if (currentTab != 0) {
    BackHandler {
      viewModel.selectTab(0)
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = Color(0xFFF6F9F8),
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = StreetLedgerStrings.get("app_title", language),
              fontSize = 19.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
            Text(
              text = StreetLedgerStrings.get("tagline", language),
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFFB2DFDB)
            )
          }
        },
        actions = {
          // Instant One-Tap Language Toggle Button
          Surface(
            modifier = Modifier
              .padding(end = 12.dp)
              .clip(RoundedCornerShape(20.dp))
              .clickable { viewModel.toggleLanguage() }
              .testTag("btn_language_toggle"),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF004D40)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Language,
                contentDescription = "Language",
                tint = Color(0xFF80CBC4),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (language == AppLanguage.ENGLISH) "தமிழ்" else "ENG",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = TealPrimary
        )
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
      ) {
        val navItems = listOf(
          Triple(0, StreetLedgerStrings.get("nav_dashboard", language), Icons.Default.Payments),
          Triple(1, StreetLedgerStrings.get("nav_khata", language), Icons.Default.MenuBook),
          Triple(2, StreetLedgerStrings.get("nav_upi", language), Icons.Default.QrCode2),
          Triple(3, StreetLedgerStrings.get("nav_stock", language), Icons.Default.Inventory2),
          Triple(4, StreetLedgerStrings.get("nav_reports", language), Icons.Default.Insights)
        )

        navItems.forEach { (index, label, icon) ->
          val isSelected = currentTab == index
          NavigationBarItem(
            selected = isSelected,
            onClick = { viewModel.selectTab(index) },
            icon = {
              Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(22.dp)
              )
            },
            label = {
              Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = TealPrimary,
              selectedTextColor = TealPrimary,
              indicatorColor = Color(0xFFE0F2F1),
              unselectedIconColor = Color.Gray,
              unselectedTextColor = Color.Gray
            ),
            modifier = Modifier.testTag("nav_item_$index")
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        0 -> DashboardScreen(viewModel = viewModel)
        1 -> KhataScreen(viewModel = viewModel)
        2 -> UpiReconciliationScreen(viewModel = viewModel)
        3 -> InventoryScreen(viewModel = viewModel)
        4 -> ReportsScreen(viewModel = viewModel)
      }
    }
  }
}
