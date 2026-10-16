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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.KhataPartyEntity
import com.example.ui.theme.CosmicPurpleBright
import com.example.ui.theme.CosmicPurpleDeep
import com.example.ui.theme.CosmicPurplePrimary
import com.example.ui.theme.ExpensePink
import com.example.ui.theme.GlassBorderPurple
import com.example.ui.theme.GlowPillBorder
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonMintDark
import com.example.ui.theme.NeonMintGlow
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardElevated
import com.example.ui.theme.ObsidianGlass
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.StreetLedgerViewModel
import com.example.util.AppLanguage
import com.example.util.Formatters
import com.example.util.SocialSharing
import com.example.util.StreetLedgerStrings

@Composable
fun KhataScreen(
    viewModel: StreetLedgerViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsStateWithLifecycle()
    val customers by viewModel.customers.collectAsStateWithLifecycle()
    val suppliers by viewModel.suppliers.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Customers, 1: Suppliers
    var showAddDialog by remember { mutableStateOf(false) }

    var partyForPayment by remember { mutableStateOf<KhataPartyEntity?>(null) }
    var isPaymentReceived by remember { mutableStateOf(true) }
    var paymentAmountInput by remember { mutableStateOf("") }

    val totalReceivable = customers.sumOf { it.balanceDue }
    val totalPayable = suppliers.sumOf { it.balanceDue }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
                .testTag("khata_screen")
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Khata Title
            Text(
                text = StreetLedgerStrings.get("khata_title", language),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tab Selector
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = ObsidianCard,
                contentColor = NeonMint,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = NeonMint,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, GlassBorderPurple, RoundedCornerShape(14.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = StreetLedgerStrings.get("tab_customers", language),
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) NeonMint else TextLavender
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = StreetLedgerStrings.get("tab_suppliers", language),
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) NeonMint else TextLavender
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Total Balance Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(
                            if (selectedTab == 0) Color(0x6600F298) else Color(0x66FF5252),
                            Color(0x228B5CF6)
                        )
                    )
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (selectedTab == 0)
                            StreetLedgerStrings.get("total_receivables", language)
                        else
                            StreetLedgerStrings.get("total_payables", language),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextLavender
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = Formatters.formatRupee(if (selectedTab == 0) totalReceivable else totalPayable),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (selectedTab == 0) NeonMint else ExpensePink
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val currentList = if (selectedTab == 0) customers else suppliers

            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTab == 0)
                            "No customers added yet.\nTap '+' to add credit customers."
                        else
                            "No suppliers added yet.\nTap '+' to add wholesale suppliers.",
                        textAlign = TextAlign.Center,
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(currentList, key = { it.id }) { party ->
                        KhataPartyCard(
                            party = party,
                            isCustomer = selectedTab == 0,
                            language = language,
                            onReceivePayment = {
                                partyForPayment = party
                                isPaymentReceived = true
                                paymentAmountInput = ""
                            },
                            onGiveCredit = {
                                partyForPayment = party
                                isPaymentReceived = false
                                paymentAmountInput = ""
                            },
                            onSendReminder = {
                                SocialSharing.shareWhatsAppReminder(
                                    context = context,
                                    recipientName = party.name,
                                    phone = party.phone,
                                    amount = party.balanceDue,
                                    shopName = "Street Ledger Vendor",
                                    language = language
                                )
                            },
                            onDelete = { viewModel.deleteKhataParty(party) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 90.dp)
                .testTag("fab_add_khata"),
            containerColor = NeonMint,
            contentColor = NeonMintDark
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }

    // Add Customer/Supplier Dialog
    if (showAddDialog) {
        var nameInput by remember { mutableStateOf("") }
        var phoneInput by remember { mutableStateOf("") }
        var initialBalanceInput by remember { mutableStateOf("") }

        AlertDialog(
            containerColor = ObsidianCardElevated,
            titleContentColor = TextWhite,
            textContentColor = TextLavender,
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = if (selectedTab == 0)
                        StreetLedgerStrings.get("add_customer", language)
                    else
                        StreetLedgerStrings.get("add_supplier", language),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text(StreetLedgerStrings.get("name", language), color = TextLavender) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurface,
                            unfocusedContainerColor = ObsidianSurface,
                            focusedBorderColor = CosmicPurpleBright,
                            unfocusedBorderColor = Color(0x338B5CF6),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text(StreetLedgerStrings.get("phone", language), color = TextLavender) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurface,
                            unfocusedContainerColor = ObsidianSurface,
                            focusedBorderColor = CosmicPurpleBright,
                            unfocusedBorderColor = Color(0x338B5CF6),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = initialBalanceInput,
                        onValueChange = { initialBalanceInput = it },
                        label = { Text(StreetLedgerStrings.get("initial_balance", language), color = TextLavender) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurface,
                            unfocusedContainerColor = ObsidianSurface,
                            focusedBorderColor = CosmicPurpleBright,
                            unfocusedBorderColor = Color(0x338B5CF6),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val initBal = initialBalanceInput.toDoubleOrNull() ?: 0.0
                        if (selectedTab == 0) {
                            viewModel.addCustomer(nameInput, phoneInput, initBal)
                        } else {
                            viewModel.addSupplier(nameInput, phoneInput, initBal)
                        }
                        showAddDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonMint, contentColor = NeonMintDark)
                ) {
                    Text(StreetLedgerStrings.get("save", language), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(StreetLedgerStrings.get("cancel", language), color = TextLavender)
                }
            }
        )
    }

    // Payment / Credit Adjustment Dialog
    partyForPayment?.let { party ->
        AlertDialog(
            containerColor = ObsidianCardElevated,
            titleContentColor = TextWhite,
            textContentColor = TextLavender,
            onDismissRequest = { partyForPayment = null },
            title = {
                Text(
                    text = if (isPaymentReceived)
                        "${StreetLedgerStrings.get("receive_payment", language)}: ${party.name}"
                    else
                        "${StreetLedgerStrings.get("give_credit", language)}: ${party.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Current Balance: ${Formatters.formatRupee(party.balanceDue)}",
                        fontSize = 14.sp,
                        color = TextLavender
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = paymentAmountInput,
                        onValueChange = { paymentAmountInput = it },
                        label = { Text("Amount (₹)", color = TextLavender) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurface,
                            unfocusedContainerColor = ObsidianSurface,
                            focusedBorderColor = CosmicPurpleBright,
                            unfocusedBorderColor = Color(0x338B5CF6),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = paymentAmountInput.toDoubleOrNull() ?: 0.0
                        if (amt > 0) {
                            if (selectedTab == 0) {
                                if (isPaymentReceived) {
                                    viewModel.receiveCustomerPayment(party, amt)
                                } else {
                                    viewModel.giveCustomerCredit(party, amt)
                                }
                            } else {
                                if (isPaymentReceived) {
                                    viewModel.paySupplierDues(party, amt)
                                } else {
                                    viewModel.addSupplierCredit(party, amt)
                                }
                            }
                        }
                        partyForPayment = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPaymentReceived) NeonMint else ExpensePink,
                        contentColor = if (isPaymentReceived) NeonMintDark else TextWhite
                    )
                ) {
                    Text(StreetLedgerStrings.get("save", language), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { partyForPayment = null }) {
                    Text(StreetLedgerStrings.get("cancel", language), color = TextLavender)
                }
            }
        )
    }
}

@Composable
fun KhataPartyCard(
    party: KhataPartyEntity,
    isCustomer: Boolean,
    language: AppLanguage,
    onReceivePayment: () -> Unit,
    onGiveCredit: () -> Unit,
    onSendReminder: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("khata_party_${party.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(GlassBorderPurple, Color(0x108B5CF6))
            )
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isCustomer) NeonMintGlow else Color(0x338B5CF6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCustomer) Icons.Default.Person else Icons.Default.Store,
                            contentDescription = null,
                            tint = if (isCustomer) NeonMint else CosmicPurpleBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = party.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        if (party.phone.isNotBlank()) {
                            Text(
                                text = party.phone,
                                fontSize = 12.sp,
                                color = TextLavender
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = Formatters.formatRupee(party.balanceDue),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isCustomer) NeonMint else ExpensePink
                    )
                    Text(
                        text = if (isCustomer) "You'll Get" else "You'll Pay",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeonMintGlow)
                        .border(1.dp, NeonMint, RoundedCornerShape(10.dp))
                        .clickable { onReceivePayment() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isCustomer) "Jama (Recv)" else "Pay Due",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonMint
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x22FF5252))
                        .border(1.dp, ExpensePink, RoundedCornerShape(10.dp))
                        .clickable { onGiveCredit() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isCustomer) "+ Udhar" else "+ Bill",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ExpensePink
                    )
                }

                if (isCustomer && party.balanceDue > 0) {
                    IconButton(
                        onClick = onSendReminder,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0x3300F298), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "WhatsApp Reminder",
                            tint = NeonMint,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
