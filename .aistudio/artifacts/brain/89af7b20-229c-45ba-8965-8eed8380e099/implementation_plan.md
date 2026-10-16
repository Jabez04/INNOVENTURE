# Street Ledger — Daily Income, Expense & Business Management for Indian Street Vendors

Every Rupee. Every Day. A dedicated, offline-first mobile business ledger engineered specifically for Indian street vendors, food stall owners, fruit/vegetable hawkers, and small shopkeepers. Provides frictionless cash and UPI logging with large tactile buttons, automatic profit calculation, UPI reconciliation, customer and supplier credit (Udhar/Khata) management, inventory alerts, savings goals, and seamless one-tap English and Tamil bilingual support.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following decisions were confirmed during interactive requirements clarification and will guide the implementation:

- **Confirmed Framework & Platform**: **Native Android Jetpack Compose** — chosen for instant interactive preview in AI Studio, native Android performance on budget devices, and zero external runtime dependencies.
- **Confirmed Primary Logging Flow**: **Big Numeric Keypad with Instant Cash & UPI Buttons** — designed for single-hand, quick-touch entry while running a busy stall, eliminating cumbersome keyboards and multi-step forms.
- **Confirmed Language Toggle**: **Direct One-Tap Header Toggle (ENG / தமிழ்)** — instantly switches all screens, tabs, categories, prompts, and report terms between English and Tamil with zero app restart.
- **Color Palette & Branding**: Deep Teal primary (`#00695C`), Fresh Green (`#2E7D32`), Crisp light surface (`#F8FAF9`), Warm Crimson (`#C62828`) for debts and expenses.
- **Currency & Local Conventions**: Indian Rupee (`₹`) throughout, Lakhs/Thousands formatting, and familiar local business terminology (Udhar, Khata, Jama, Udhar Vasool, Varavu, Selavu, Kadan).

---

## 1. Overview & Core Concept

### What It Does
Street Ledger replaces physical paper diaries, unorganized register books, and mental math for micro-merchants. Vendors can:
1. **Record Sales in 2 Seconds**: Punch ₹50 -> Tap **Cash** or **UPI** -> Done.
2. **Track Expenses**: Log raw materials (vegetables, milk, flour), cart gas/cylinder, transport, stall municipal fees.
3. **See Real-time Profits ("Your Business Today")**: Cash in hand, digital UPI bank total, total expenses, and actual net profit.
4. **Reconcile UPI Payments**: Verify digital payments recorded against the QR payment app / soundbox notifications to catch missed UPI receipts.
5. **Manage Khata / Debt (Udhar)**: Track credit given to regular customers and credit taken from wholesale suppliers, with 1-tap WhatsApp reminder text generation in English or Tamil.
6. **Inventory & Restock Planning**: Track key items (e.g., Cooking Oil, Tea Powder, Cups, Potatoes) with low-stock warnings and restock cost estimates.
7. **Savings Goals & Daily Insights**: Set targets (e.g., "New Cart Canopy ₹3,000" or "Diwali Festival Fund"), track daily savings allocation, and view peak business hour trends.

### Target Audience & Persona
- **Ramesh (Tea & Snack Stall)**: Handles 300+ ₹10/₹20 tea & samosa transactions daily. Needs instant cash/UPI split without typing names.
- **Murugan (Fruit & Vegetable Hawker)**: Needs Tamil language interface, buys daily wholesale stock on credit, and tracks regular customer udhar.
- **Lakshmi (Flower / Garlands Vendor)**: Needs evening profit calculation to ensure raw flower costs are covered before festival days.

---

## 2. User Experience & Visual Design

### Key User Flows

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       STREET LEDGER USER NAVIGATION                          │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
          ┌───────────────────────────┼───────────────────────────┐
          ▼                           ▼                           ▼
  [1. Daily Dashboard]        [2. Udhar / Khata]         [3. Inventory & Stock]
  - "Your Business Today"     - Customer Credit          - Daily Raw Stock
  - Big Numpad Entry          - Supplier Dues            - Low Stock Warnings
  - Cash vs UPI Split         - WhatsApp Reminder        - Reorder Cost Est.
  - Net Profit Card           - Quick Settle Button      - Supplier Directory
          │                           │                           │
          └───────────────────────────┼───────────────────────────┘
                                      │
          ┌───────────────────────────┴───────────────────────────┐
          ▼                                                       ▼
  [4. UPI Reconciliation]                                 [5. Reports & Savings]
  - Digital Scan Verification                             - Today / Week / Month
  - Soundbox / QR Match                                   - Income vs Expense Bar
  - Mismatch Spotter                                      - Goal Savings Pots
```

### Screen Details

1. **Top Header & Language Switcher**:
   - App branding: "Street Ledger" ("ஸ்ட்ரீட் லெட்ஜர்"), tagline "Every Rupee. Every Day."
   - Instant language pill toggle: `[ENG | தமிழ்]` toggles entire app immediately.
   - Quick date selector (Today, Yesterday, or custom calendar date).

2. **Main Dashboard ("Your Business Today" / "இன்றைய வியாபாரம்")**:
   - **Financial Health Summary Card**:
     - Today's Income (Green ₹), Split: Cash (`₹ Cash`) and Digital (`₹ UPI`).
     - Today's Expenses (Coral/Red ₹).
     - Net Daily Profit (Bold highlight with profit badge).
   - **Fast Entry Pad (Big Button Tactile Numpad)**:
     - Clear digital display for amount.
     - Quick chips: +₹10, +₹20, +₹50, +₹100, +₹500.
     - Direct action buttons: **+ Cash In**, **+ UPI In**, **- Expense Out**.
     - Optional quick category chip (Sales, Chai/Snacks, Raw Goods, Gas/Fuel, Cart Rent, Personal).
   - **Recent Activity Ledger**: Time-stamped list with quick delete or edit.

3. **UPI Reconciliation ("UPI சரிபார்ப்பு")**:
   - Dedicated counter showing Total UPI Logged in app vs actual bank account / QR soundbox total.
   - List of individual UPI payments with tick marks to mark verified.
   - Difference detector ("₹0 Difference — All Balanced!" or "₹40 Missing in Ledger").

4. **Khata & Udhar Tracker ("கடன் / உத்தரி கணக்கு")**:
   - Two distinct tabs: **Customers (Who owes me)** & **Suppliers (Who I owe)**.
   - Customer card with balance, last payment date, phone number.
   - **WhatsApp Reminder** generator button: Formats ready-to-share message in Tamil/English ("வணக்கம், உங்கள் மீதி கடன் ₹150...").
   - Quick "Receive Payment" (Jama) and "Give Credit" (Udhar) sheet.

5. **Inventory & Suppliers ("சரக்கு & சப்ளையர்")**:
   - Stock items with quantity, unit (kg, litres, packets, pieces), reorder threshold.
   - Visual alert badges: `Low Stock! Restock today`.
   - Supplier ledger linked to wholesale purchasing.

6. **Reports & Savings Goals ("அறிக்கைகள் & சேமிப்பு")**:
   - Daily, Weekly, and Monthly income vs expense summary.
   - Peak hours trend (Morning, Afternoon, Evening peak).
   - Savings piggy bank (Target amount, saved amount, daily contribution calculator).

### Visual Identity & Theme
- **Primary Color**: Deep Teal (`#00695C`) — evokes trust, stability, and ledger permanence.
- **Secondary / Success**: Fresh Green (`#2E7D32`) — represents income, positive profit, and financial growth.
- **Expense / Warning**: Crimson Amber (`#D32F2F`) — clearly distinguishes cash outflows and pending debts.
- **Background & Surface**: Clean light cream/mint white (`#F8FAF9` and `#FFFFFF`), high contrast for outdoor sunlight readability.
- **Card Styling**: High elevation, thick touch boundaries, 16dp rounded corners, minimum 48dp touch targets on all elements.

---

## 3. Key Product Decisions & Trade-Offs

| Decision | Chosen Approach | Rationale | Alternatives Considered |
|---|---|---|---|
| **Platform** | Native Android Jetpack Compose | Instant browser preview in AI Studio, native Kotlin performance, smooth 60fps on 2GB RAM phones | Flutter (requires non-native toolchain not installed in this container) |
| **Data Storage** | Android Room Database (SQLite) | 100% offline-first reliability. Street stalls frequently face zero network; local SQLite guarantees zero data loss | Cloud Firestore (would break when vendor is in basement or market without 4G) |
| **Language System** | Reactive String Table (ENG + தமிழ்) | Instant runtime switching via StateFlow without needing system locale change or activity reload | Android resource string qualifiers alone (requires configuration restart) |
| **Input UX** | Tactile In-App Numpad + Quick Denominations | Street vendors often have greasy or wet hands; oversized tactile buttons are significantly faster than native soft keyboard | Native soft keyboard (covers half the screen, requires multiple taps) |
| **Reconciliation** | Manual Tick-to-Match UPI Engine | Allows vendors to audit their daily QR soundbox announcements against recorded sales in under 60 seconds | Auto SMS parsing (requires dangerous runtime SMS permissions restricted by Google Play) |

---

## 4. Technical Architecture & Data Strategy

### System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                             JETPACK COMPOSE UI                              │
│                                                                             │
│  ┌─────────────────────── TopAppBar & Language Toggle ───────────────────┐  │
│  │                     [ Street Ledger | ENG / தமிழ் ]                   │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
│  ┌────────────────────────── BottomNavigationBar ────────────────────────┐  │
│  │   [ Dashboard ]   [ Udhar/Khata ]   [ UPI Reconcile ]   [ Stock/More ]│  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
│  ┌──────────────────────── Active Screen Composables ────────────────────┐  │
│  │  - DashboardScreen (Today's Profit Card + Numpad Entry + Today Ledger)│  │
│  │  - KhataScreen (Customer Udhar + Supplier Khata + WhatsApp Share)     │  │
│  │  - UpiReconciliationScreen (QR Audit + Soundbox Matcher + Checklist)  │  │
│  │  - InventoryScreen (Stock Items + Units + Low Stock Badges + Reorder) │  │
│  │  - ReportsScreen (Daily/Monthly P&L + Savings Goals + Peak Trends)    │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │ StateFlow & Intents
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           STREET LEDGER VIEWMODEL                           │
│  - uiState: DashboardUiState (Income, Expense, Cash, UPI, Profit)           │
│  - currentLanguage: LanguageState (ENGLISH / TAMIL)                         │
│  - numpadInput: String                                                      │
│  - khataUiState: KhataState                                                 │
│  - inventoryUiState: InventoryState                                         │
│  - savingsUiState: SavingsState                                             │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │ Coroutines / Flow
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         STREET LEDGER REPOSITORY                            │
│  - TransactionsRepository (CRUD for Cash/UPI sales & expenses)             │
│  - KhataRepository (Customer & Supplier credit ledgers)                     │
│  - InventoryRepository (Stock tracking & restock alerts)                    │
│  - SavingsGoalRepository (Target savings pots)                              │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │ DAO calls
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        ROOM DATABASE (SQLite Local)                         │
│  - transactions table (id, type, amount, paymentMode, category, timestamp)  │
│  - khata_parties table (id, name, phone, type, balance, lastUpdated)        │
│  - inventory_items table (id, name, quantity, unit, minThreshold, cost)     │
│  - savings_goals table (id, title, targetAmount, currentAmount, deadline)   │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Database Schema Entities

1. **`TransactionEntity`**:
   - `id: Long` (Auto-increment PK)
   - `type: String` ("INCOME" / "EXPENSE")
   - `paymentMode: String` ("CASH" / "UPI")
   - `amount: Double`
   - `category: String` (e.g., "General", "Tea/Snacks", "Raw Materials", "Gas/Fuel", "Transport", "Rent")
   - `note: String?`
   - `dateMillis: Long`
   - `isUpiReconciled: Boolean` (default false)

2. **`KhataPartyEntity`**:
   - `id: Long` (PK)
   - `name: String`
   - `phone: String`
   - `partyType: String` ("CUSTOMER" / "SUPPLIER")
   - `balanceDue: Double` (Positive = they owe me; Negative = I owe them)
   - `lastTransactionDate: Long`

3. **`InventoryItemEntity`**:
   - `id: Long` (PK)
   - `itemNameEn: String`
   - `itemNameTa: String`
   - `currentStock: Double`
   - `unit: String` ("kg", "L", "pcs", "packets")
   - `minimumThreshold: Double`
   - `unitCost: Double`

4. **`SavingsGoalEntity`**:
   - `id: Long` (PK)
   - `titleEn: String`
   - `titleTa: String`
   - `targetAmount: Double`
   - `savedAmount: Double`

---

## 5. Implementation Steps & Milestones

1. **Project Setup & Identity**:
   - Update `metadata.json` (`name: "Street Ledger"`, descriptive summary).
   - Update `strings.xml` with `app_name: "Street Ledger"`.
   - Update `AndroidManifest.xml` with necessary permissions (Vibration for tactile numpad feedback).
   - Create custom vector App Icon combining a ledger book with the Indian Rupee symbol (`₹`).

2. **Data Persistence Layer (Room + Coroutines)**:
   - Define entities: `TransactionEntity`, `KhataPartyEntity`, `InventoryItemEntity`, `SavingsGoalEntity`.
   - Create DAOs: `TransactionDao`, `KhataDao`, `InventoryDao`, `SavingsGoalDao`.
   - Setup `StreetLedgerDatabase` Room database class with initial starter seeds (common vendor items and sample khata).
   - Implement `StreetLedgerRepository` encapsulating all reactive database queries into Kotlin Flows.

3. **Bilingual Localization System**:
   - Create comprehensive `StreetLedgerStrings.kt` defining string keys in English and Tamil (கையேடு, இன்றைய வியாபாரம், ரொக்கம், யுபிஐ, வரவு, செலவு, லாபம், கடன், சரக்கு, சேமிப்பு, முதலியன).
   - Expose active language toggle in ViewModel.

4. **Core UI Components & Design System**:
   - Theme configuration with Deep Teal (`#00695C`), Fresh Green (`#2E7D32`), and Crimson (`#D32F2F`).
   - Tactile `NumpadWidget` with oversized keys, quick increment chips (+10, +20, +50, +100, +500), and vibration feedback.
   - `FinancialSummaryCard` with dynamic profit computation and high-contrast badges.
   - `TransactionListItem` with clear Cash/UPI iconography.

5. **Screen Implementation**:
   - **DashboardScreen**: Header + Business Today card + Quick Numpad + Recent entries list.
   - **UpiReconciliationScreen**: Interactive UPI transaction checklist with soundbox comparison calculator.
   - **KhataScreen**: Customer credit & supplier dues tabs with WhatsApp reminder intent launcher.
   - **InventoryScreen**: Quick stock counter, low stock alert banners, and restock expense calculator.
   - **Reports & SavingsScreen**: Daily/weekly/monthly profit & loss breakdown + savings pot progress.

6. **Verification & Testing**:
   - Compile applet with `compile_applet`.
   - Verify room database compilation and KSP code generation.
   - Test screen navigation, language toggling, transaction insertion, and profit recalculation.
