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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.InventoryItemEntity
import com.example.ui.theme.CosmicPurpleBright
import com.example.ui.theme.ExpensePink
import com.example.ui.theme.GlassBorderPurple
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonMintDark
import com.example.ui.theme.NeonMintGlow
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardElevated
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.StreetLedgerViewModel
import com.example.util.AppLanguage
import com.example.util.Formatters
import com.example.util.StreetLedgerStrings

@Composable
fun InventoryScreen(
    viewModel: StreetLedgerViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val inventoryItems by viewModel.inventoryItems.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }

    val lowStockItems = inventoryItems.filter { it.quantity <= it.minThreshold }
    val totalRestockCost = lowStockItems.sumOf { item ->
        val deficit = maxOf(0.0, (item.minThreshold * 2) - item.quantity)
        deficit * item.unitCost
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
                .testTag("inventory_screen")
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Title
            Text(
                text = StreetLedgerStrings.get("inventory_title", language),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Restock Cost Estimate Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(
                            if (lowStockItems.isNotEmpty()) Color(0x66FF5252) else Color(0x6600F298),
                            Color(0x228B5CF6)
                        )
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (lowStockItems.isNotEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = ExpensePink,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = if (lowStockItems.isNotEmpty())
                                    "${lowStockItems.size} ${StreetLedgerStrings.get("low_stock_warning", language)}"
                                else
                                    "All Stock Levels Healthy ✓",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (lowStockItems.isNotEmpty()) ExpensePink else NeonMint
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${StreetLedgerStrings.get("restock_cost_estimate", language)}: ${Formatters.formatRupee(totalRestockCost)}",
                            fontSize = 12.sp,
                            color = TextLavender
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (lowStockItems.isNotEmpty()) Color(0x33FF5252) else NeonMintGlow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = if (lowStockItems.isNotEmpty()) ExpensePink else NeonMint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Inventory Items List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(inventoryItems, key = { it.id }) { item ->
                    InventoryItemCard(
                        item = item,
                        language = language,
                        onAdjust = { delta -> viewModel.adjustStock(item, delta) },
                        onDelete = { viewModel.deleteInventoryItem(item) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        // Add Item FAB
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 90.dp)
                .testTag("fab_add_inventory"),
            containerColor = NeonMint,
            contentColor = NeonMintDark
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }

    // Add Item Dialog
    if (showAddDialog) {
        var nameEnInput by remember { mutableStateOf("") }
        var nameTaInput by remember { mutableStateOf("") }
        var quantityInput by remember { mutableStateOf("") }
        var unitInput by remember { mutableStateOf("kg") }
        var thresholdInput by remember { mutableStateOf("") }
        var costInput by remember { mutableStateOf("") }
        var supplierInput by remember { mutableStateOf("") }

        AlertDialog(
            containerColor = ObsidianCardElevated,
            titleContentColor = TextWhite,
            textContentColor = TextLavender,
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = StreetLedgerStrings.get("add_item", language),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nameEnInput,
                        onValueChange = { nameEnInput = it },
                        label = { Text(StreetLedgerStrings.get("item_name_en", language), color = TextLavender) },
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
                        value = nameTaInput,
                        onValueChange = { nameTaInput = it },
                        label = { Text(StreetLedgerStrings.get("item_name_ta", language), color = TextLavender) },
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
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = quantityInput,
                            onValueChange = { quantityInput = it },
                            label = { Text(StreetLedgerStrings.get("quantity", language), color = TextLavender) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ObsidianSurface,
                                unfocusedContainerColor = ObsidianSurface,
                                focusedBorderColor = CosmicPurpleBright,
                                unfocusedBorderColor = Color(0x338B5CF6),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = unitInput,
                            onValueChange = { unitInput = it },
                            label = { Text(StreetLedgerStrings.get("unit", language), color = TextLavender) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ObsidianSurface,
                                unfocusedContainerColor = ObsidianSurface,
                                focusedBorderColor = CosmicPurpleBright,
                                unfocusedBorderColor = Color(0x338B5CF6),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = thresholdInput,
                            onValueChange = { thresholdInput = it },
                            label = { Text(StreetLedgerStrings.get("min_threshold", language), color = TextLavender) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ObsidianSurface,
                                unfocusedContainerColor = ObsidianSurface,
                                focusedBorderColor = CosmicPurpleBright,
                                unfocusedBorderColor = Color(0x338B5CF6),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = costInput,
                            onValueChange = { costInput = it },
                            label = { Text(StreetLedgerStrings.get("unit_cost", language), color = TextLavender) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ObsidianSurface,
                                unfocusedContainerColor = ObsidianSurface,
                                focusedBorderColor = CosmicPurpleBright,
                                unfocusedBorderColor = Color(0x338B5CF6),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = supplierInput,
                        onValueChange = { supplierInput = it },
                        label = { Text(StreetLedgerStrings.get("supplier", language), color = TextLavender) },
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
                        val qty = quantityInput.toDoubleOrNull() ?: 1.0
                        val thresh = thresholdInput.toDoubleOrNull() ?: 2.0
                        val cost = costInput.toDoubleOrNull() ?: 0.0
                        viewModel.addInventoryItem(
                            nameEn = if (nameEnInput.isNotBlank()) nameEnInput else nameTaInput,
                            nameTa = if (nameTaInput.isNotBlank()) nameTaInput else nameEnInput,
                            quantity = qty,
                            unit = unitInput,
                            threshold = thresh,
                            cost = cost,
                            supplier = supplierInput
                        )
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
}

@Composable
fun InventoryItemCard(
    item: InventoryItemEntity,
    language: AppLanguage,
    onAdjust: (Double) -> Unit,
    onDelete: () -> Unit
) {
    val isLowStock = item.quantity <= item.minThreshold
    val displayName = if (language == AppLanguage.TAMIL && item.nameTa.isNotBlank()) item.nameTa else item.nameEn

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("inventory_item_${item.id}"),
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
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = displayName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        if (isLowStock) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0x33FF5252)
                            ) {
                                Text(
                                    text = "LOW",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ExpensePink,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "Cost: ${Formatters.formatRupee(item.unitCost)} / ${item.unit} • Alert at < ${Formatters.formatPlainAmount(item.minThreshold)} ${item.unit}",
                        fontSize = 11.sp,
                        color = TextLavender
                    )
                    if (item.supplierName.isNotBlank()) {
                        Text(
                            text = "Supplier: ${item.supplierName}",
                            fontSize = 11.sp,
                            color = CosmicPurpleBright
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${Formatters.formatPlainAmount(item.quantity)} ${item.unit}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isLowStock) ExpensePink else NeonMint
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Stock Adjust Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ObsidianCardElevated)
                        .border(1.dp, GlassBorderPurple, RoundedCornerShape(10.dp))
                        .clickable { onAdjust(-1.0) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("-1", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ObsidianCardElevated)
                        .border(1.dp, GlassBorderPurple, RoundedCornerShape(10.dp))
                        .clickable { onAdjust(1.0) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+1", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeonMintGlow)
                        .border(1.dp, NeonMint, RoundedCornerShape(10.dp))
                        .clickable { onAdjust(5.0) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+5", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonMint)
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
