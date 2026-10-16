package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "INCOME" or "EXPENSE"
    val paymentMode: String, // "CASH" or "UPI"
    val amount: Double,
    val category: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isUpiReconciled: Boolean = false
)

@Entity(tableName = "khata_parties")
data class KhataPartyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String = "",
    val partyType: String, // "CUSTOMER" (receivable) or "SUPPLIER" (payable)
    val balanceDue: Double, // Positive = amount owed
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "inventory_items")
data class InventoryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nameEn: String,
    val nameTa: String,
    val quantity: Double,
    val unit: String, // "kg", "L", "pcs", "packets"
    val minThreshold: Double,
    val unitCost: Double,
    val supplierName: String = ""
)

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titleEn: String,
    val titleTa: String,
    val targetAmount: Double,
    val savedAmount: Double = 0.0,
    val deadlineDate: String = ""
)
