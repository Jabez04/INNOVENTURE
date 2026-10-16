package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.InventoryItemEntity
import com.example.data.model.KhataPartyEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TransactionEntity::class,
        KhataPartyEntity::class,
        InventoryItemEntity::class,
        SavingsGoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class StreetLedgerDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun khataDao(): KhataDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun savingsGoalDao(): SavingsGoalDao

    companion object {
        @Volatile
        private var INSTANCE: StreetLedgerDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): StreetLedgerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StreetLedgerDatabase::class.java,
                    "street_ledger_db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }

            private suspend fun populateInitialData(database: StreetLedgerDatabase) {
                val now = System.currentTimeMillis()
                val oneHourAgo = now - 3600000L
                val twoHoursAgo = now - 7200000L
                val threeHoursAgo = now - 10800000L

                // Initial transactions for today
                val transDao = database.transactionDao()
                transDao.insertTransaction(
                    TransactionEntity(
                        type = "INCOME",
                        paymentMode = "CASH",
                        amount = 450.0,
                        category = "Tea/Snacks",
                        note = "Morning crowd sales",
                        timestamp = threeHoursAgo,
                        isUpiReconciled = false
                    )
                )
                transDao.insertTransaction(
                    TransactionEntity(
                        type = "INCOME",
                        paymentMode = "UPI",
                        amount = 320.0,
                        category = "Sales",
                        note = "QR Scans - GPay/PhonePe",
                        timestamp = twoHoursAgo,
                        isUpiReconciled = true
                    )
                )
                transDao.insertTransaction(
                    TransactionEntity(
                        type = "INCOME",
                        paymentMode = "UPI",
                        amount = 180.0,
                        category = "Sales",
                        note = "QR Scan - Paytm",
                        timestamp = oneHourAgo,
                        isUpiReconciled = false
                    )
                )
                transDao.insertTransaction(
                    TransactionEntity(
                        type = "EXPENSE",
                        paymentMode = "CASH",
                        amount = 240.0,
                        category = "Raw Materials",
                        note = "Daily milk 5 packets",
                        timestamp = twoHoursAgo,
                        isUpiReconciled = false
                    )
                )

                // Initial Khata (Credit)
                val khataDao = database.khataDao()
                khataDao.insertParty(
                    KhataPartyEntity(
                        name = "Raju (Auto Driver)",
                        phone = "9876543210",
                        partyType = "CUSTOMER",
                        balanceDue = 140.0,
                        lastUpdated = now
                    )
                )
                khataDao.insertParty(
                    KhataPartyEntity(
                        name = "Suresh Master",
                        phone = "9442198765",
                        partyType = "CUSTOMER",
                        balanceDue = 250.0,
                        lastUpdated = now - 86400000L
                    )
                )
                khataDao.insertParty(
                    KhataPartyEntity(
                        name = "Kumar Wholesale Grocers",
                        phone = "9840112233",
                        partyType = "SUPPLIER",
                        balanceDue = 1200.0,
                        lastUpdated = now - 172800000L
                    )
                )

                // Initial Stock Inventory
                val invDao = database.inventoryDao()
                invDao.insertItem(
                    InventoryItemEntity(
                        nameEn = "Fresh Milk",
                        nameTa = "பசும்பால்",
                        quantity = 6.0,
                        unit = "L",
                        minThreshold = 4.0,
                        unitCost = 48.0,
                        supplierName = "Murugan Dairy"
                    )
                )
                invDao.insertItem(
                    InventoryItemEntity(
                        nameEn = "Tea Leaves Powder",
                        nameTa = "தேயிலைத் தூள்",
                        quantity = 2.5,
                        unit = "kg",
                        minThreshold = 1.0,
                        unitCost = 340.0,
                        supplierName = "Ooty Tea Agency"
                    )
                )
                invDao.insertItem(
                    InventoryItemEntity(
                        nameEn = "Sugar",
                        nameTa = "சர்க்கரை",
                        quantity = 8.0,
                        unit = "kg",
                        minThreshold = 3.0,
                        unitCost = 42.0,
                        supplierName = "Kumar Grocers"
                    )
                )
                invDao.insertItem(
                    InventoryItemEntity(
                        nameEn = "Paper Tea Cups",
                        nameTa = "பேப்பர் டீ கப்புகள்",
                        quantity = 80.0,
                        unit = "pcs",
                        minThreshold = 200.0, // Low stock warning!
                        unitCost = 0.85,
                        supplierName = "City Packaging"
                    )
                )
                invDao.insertItem(
                    InventoryItemEntity(
                        nameEn = "Cooking Oil",
                        nameTa = "சமையல் எண்ணெய்",
                        quantity = 2.0,
                        unit = "L",
                        minThreshold = 5.0, // Low stock warning!
                        unitCost = 145.0,
                        supplierName = "Kumar Grocers"
                    )
                )

                // Initial Savings Goals
                val savingsDao = database.savingsGoalDao()
                savingsDao.insertGoal(
                    SavingsGoalEntity(
                        titleEn = "New Stall Canopy & LED Board",
                        titleTa = "புதிய கடை பந்தல் & எல்இடி போர்டு",
                        targetAmount = 8000.0,
                        savedAmount = 3500.0,
                        deadlineDate = "Diwali"
                    )
                )
                savingsDao.insertGoal(
                    SavingsGoalEntity(
                        titleEn = "Emergency Reserve Fund",
                        titleTa = "அவசர கால சேமிப்பு நிதி",
                        targetAmount = 15000.0,
                        savedAmount = 6200.0,
                        deadlineDate = "Year End"
                    )
                )
            }
        }
    }
}
