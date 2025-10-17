# AccountingLedger

A command-line Java application designed to manage and track financial transactions. Users can log in with a passcode to access a personalized ledger file, add deposits and payments, view all transactions, and generate various types of financial reports.

-----

## 📑 Table of Contents

  * [Features](https://www.google.com/search?q=%23features)
  * [Technologies](https://www.google.com/search?q=%23technologies)
  * [Setup & Installation](https://www.google.com/search?q=%23setup--installation)
      * [Prerequisites](https://www.google.com/search?q=%23prerequisites)
      * [Building the Project](https://www.google.com/search?q=%23building-the-project)
      * [Running the Application](https://www.google.com/search?q=%23running-the-application)
  * [Usage](https://www.google.com/search?q=%23usage)
      * [1. Initial Access](https://www.google.com/search?q=%231-initial-access)
      * [2. Main Menu](https://www.google.com/search?q=%232-main-menu)
      * [3. Ledger File Format](https://www.google.com/search?q=%233-ledger-file-format)

-----

## ✨ Features

### 🔐 Secure Login
- Users authenticate using a **passcode**, which is mapped to a specific ledger file (e.g., `transactions_JL.csv`).
- This ensures that only the correct ledger is loaded for each individual user.

### ➕ Add Transactions
- Users can add new transactions through the main menu:
  - **(D) Add Deposit:** Records an incoming transaction (positive amount).
  - **(P) Add Payment:** Records an outgoing transaction (negative amount).
- Users can specify a custom date or enter **`N`** to auto-fill the current date and time.

### 📋 View Ledger
View and filter transactions using the ledger menu:
- **(A)** Show **all** transactions.
- **(D)** Show **only deposits**.
- **(P)** Show **only payments**.
- **(R)** Access the **Reports Menu** for advanced reporting.
- **(H)** Return to the **Main Menu**.

### 📊 Reports Generation
Generate detailed financial reports using several filtering options:

#### Default Reports
- **(1)** Month to Date
- **(2)** Previous Month
- **(3)** Year to Date
- **(4)** Previous Year

#### Advanced Filtering
- **(5)** Search by Vendor: Filter transactions by a specific vendor name (e.g., “Amazon”).
- **(6)** Custom Search: Define your own filters:
  - Date range
  - Time range
  - Description keywords
  - Vendor
  - Amount range (min/max)

- **(7)** Return to Ledger Menu

### 💾 Data Persistence
- Transactions are stored in a **pipe-delimited CSV** file (e.g., `transactions_JL.csv`).
- Files are **loaded at login** and **saved upon transaction addition** or **report generation**.
- Ensures data is preserved between sessions.

### 📝 Report Exporting
- Every generated report can be saved as a **new CSV file**, automatically named with:
  - A timestamp
  - The type of report  
  (e.g., `yearToDate_20251015_1430_Transactions_JL.csv`)

---

-----

## 🛠️ Technologies

  * **Language:** Java 17
  * **Build Tool:** Apache Maven

-----

## 🚀 Setup & Installation

### Prerequisites

  * Java Development Kit (JDK) 17 or higher
  * Apache Maven

### Building the Project

1.  **Clone the Repository:**
    ```bash
    git clone [your-repository-url]
    cd AccountingLedger
    ```
2.  **Build with Maven:**
    Use Maven to compile the Java code and package it into an executable JAR file.
    ```bash
    mvn clean package
    ```

### Running the Application

1.  **Execute the JAR file:**
    ```bash
    java -jar target/AccountingLedger-1.0-SNAPSHOT.jar
    ```

-----

## 💡 Usage

### 1\. Initial Access

Upon launching, the application will prompt you to select a ledger file by entering a **passcode**.

| Passcode | Ledger File |
| :---: | :---: |
| `12345` | `transactions_JL.csv` |
| `23456` | `transactions_AM.csv` |
| `34567` | `transactions_JG.csv` |
| `45678` | `transactions_AZ.csv` |
| `56789` | `transactions_AB.csv` |
| `67890` | `transactions_HS.csv` |
| `78901` | `transactions_AA.csv` |

*Example:* Enter `12345` to load `transactions_JL.csv`.

### 2\. Main Menu

After a successful login, the main menu is displayed:

```
Please select an option from the following menu:
(D) Add deposit
(P) Add payment
(L) View ledger
(I) More info
(X) Exit program
```

### 3\. Ledger File Format

The ledger files (e.g., `transactions_JL.csv`) are pipe-delimited (`|`) and use the following header:

| Column | Description | Format |
| :--- | :--- | :--- |
| **date** | The date of the transaction. | `YYYY-MM-DD` |
| **time** | The time of the transaction. | `HH:MM:SS` |
| **description** | A brief description of the item or service. | String |
| **vendor** | The vendor or source of the transaction. | String |
| **amount** | The monetary value. **Positive** for deposits, **Negative** for payments. | Double (e.g., `1200.00` or `-7.99`) |
