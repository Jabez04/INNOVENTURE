package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BusinessSummaryCard
import com.example.ui.components.TactileNumpad
import com.example.ui.components.TransactionItemRow
import com.example.ui.theme.CosmicPurpleBright
import com.example.ui.theme.CosmicPurpleDeep
import com.example.ui.theme.CosmicPurplePrimary
import com.example.ui.theme.GlassBorderPurple
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonMintDark
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianGlass
import com.example.ui.theme.TextLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.StreetLedgerViewModel
import com.example.util.AppLanguage
import com.example.util.StreetLedgerStrings

@Composable
fun DashboardScreen(
    viewModel: StreetLedgerViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val todaySummary by viewModel.todaySummary.collectAsStateWithLifecycle()
    val todayTransactions by viewModel.todayTransactions.collectAsStateWithLifecycle()
    val numpadAmount by viewModel.numpadAmount.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val noteInput by viewModel.noteInput.collectAsStateWithLifecycle()

    val categories = listOf(
        "General" to if (language == AppLanguage.TAMIL) "பொதுவானது" else "General",
        "Tea/Snacks" to if (language == AppLanguage.TAMIL) "டீ / சிற்றுண்டி" else "Tea/Snacks",
        "Raw Materials" to if (language == AppLanguage.TAMIL) "சரக்கு பொருட்கள்" else "Raw Materials",
        "Gas & Fuel" to if (language == AppLanguage.TAMIL) "எரிவாயு / சிலிண்டர்" else "Gas & Fuel",
        "Transport" to if (language == AppLanguage.TAMIL) "வண்டி வாடகை" else "Transport",
        "Stall Fee" to if (language == AppLanguage.TAMIL) "கடை கட்டணம்" else "Stall Fee",
        "Personal" to if (language == AppLanguage.TAMIL) "வீட்டு செலவு" else "Personal"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(horizontal = 14.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcome Greeting
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = StreetLedgerStrings.get("welcome_greeting", language),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = StreetLedgerStrings.get("tagline", language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = CosmicPurpleBright
                )
            }
        }

        // Financial Summary Card ("Your Business Today")
        item {
            BusinessSummaryCard(
                summary = todaySummary,
                language = language
            )
        }

        // Quick Category Picker
        item {
            Column {
                Text(
                    text = StreetLedgerStrings.get("category", language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextLavender,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { (key, label) ->
                        val isSelected = selectedCategory == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) NeonMint else ObsidianGlass)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonMint else GlassBorderPurple,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { viewModel.selectCategory(key) }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                                .testTag("cat_$key")
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) NeonMintDark else TextWhite
                            )
                        }
                    }
                }
            }
        }

        // Big Tactile Numpad
        item {
            TactileNumpad(
                amount = numpadAmount,
                language = language,
                onDigit = { viewModel.onNumpadDigit(it) },
                onIncrement = { viewModel.onNumpadAddIncrement(it) },
                onBackspace = { viewModel.onNumpadBackspace() },
                onClear = { viewModel.onNumpadClear() },
                onCashIn = { viewModel.recordCashIncome() },
                onUpiIn = { viewModel.recordUpiIncome() },
                onExpenseOut = { viewModel.recordExpense() }
            )
        }

        // Optional Note Input
        item {
            OutlinedTextField(
                value = noteInput,
                onValueChange = { viewModel.updateNoteInput(it) },
                placeholder = {
                    Text(
                        text = StreetLedgerStrings.get("note_optional", language),
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("note_input_field"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ObsidianCard,
                    unfocusedContainerColor = ObsidianCard,
                    focusedBorderColor = CosmicPurpleBright,
                    unfocusedBorderColor = Color(0x338B5CF6),
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                singleLine = true
            )
        }

        // Today's Activity Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = StreetLedgerStrings.get("today_activity", language),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "${todayTransactions.size} entries",
                    fontSize = 12.sp,
                    color = TextLavender
                )
            }
        }

        if (todayTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(GlassBorderPurple, Color(0x158B5CF6))
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = StreetLedgerStrings.get("no_transactions_yet", language),
                            fontSize = 13.sp,
                            color = TextLavender,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(todayTransactions, key = { it.id }) { tx ->
                TransactionItemRow(
                    transaction = tx,
                    language = language,
                    onDelete = { viewModel.deleteTransaction(tx) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
