# AccountingLedger

A command-line Java application designed to manage and track financial transactions. Users can log in with a passcode to access a personalized ledger file, add deposits and payments, view all transactions, and generate various types of financial reports.

-----

## 📑 Table of Contents

  * [Features](#-features)
  * [Technologies](#%EF%B8%8F-technologies)
  * [Setup & Installation](#-setup--installation)
  * [Project Structure](#-project-structure)
  * [Application Screens](#%EF%B8%8F-application-screens)
      * [Main Menu](#main-menu)
      * [Ledger Menu](#ledger-menu)
      * [Reports Menu](#reports-menu)
      * [Example Report Output (Displaying All Entries)](#example-report-output-displaying-all-entries)
  * [Code Highlight: Dynamic Filtering with Streams](#-code-highlight-dynamic-filtering-with-streams)
  * [Usage](#-usage)

-----

## ✨ Features

The application provides a comprehensive set of features accessible through a main menu and sub-menus:

  * **Secure & Personalized Login:**

      * Users log in by entering a **passcode** to identify their account.
      * The application reads `passcodes.csv` to map the passcode to the user's name and dedicated ledger file (e.g., `transactions_JL.csv`).

  * **Robust Transaction Management:**

      * **Add Deposit (D) / Add Payment (P):** Allows users to record new income or expense transactions.
      * **Input Validation:** Collects and validates date, time, description, vendor, and amount inputs. It supports multiple date and time formats and non-empty strings.
      * **"Now" Option:** Users can enter **'N'** to automatically use the current system date and time for the transaction.
      * **Amount Normalization:** The system automatically adjusts the amount to be **positive for deposits** and **negative for payments**, regardless of the user's input sign.
      * **Confirmation & Insertion:** New transactions are displayed for **user review and confirmation** before being added to the ledger.
      * **Chronological Ordering:** Transactions are inserted into the ledger in **precise chronological order** (date and time).

  * **Data Structure & Persistence:**

      * **Transaction Model:** Data is encapsulated in a `Transaction` object, which stores the date (`LocalDate`), time (`LocalTime`), description, vendor, and amount (`double`).
      * **Pipe-Delimited CSV:** All ledger files use a simple, **pipe-delimited format** (`date|time|description|vendor|amount`).
      * **Dynamic Saving:** New transactions are immediately written to the corresponding CSV file. If a transaction is not the most recent, the entire file is **rewritten** to maintain reverse chronological order in the file.

  * **Ledger Viewing & Reporting:**

      * **Display All (A):** Shows all entries in the ledger, ordered from **newest to earliest**.
      * **Filter by Type (D/P):** Filters the display to show only deposits or only payments.
      * **Default Reports (1-4):** Provides one-click reports for predefined periods: Month to Date, Previous Month, Year to Date, and Previous Year.
      * **Search by Vendor (5):** Runs a report filtered exclusively by a vendor name specified by the user.
      * **Custom Search (6):** Allows fine-grained filtering based on multiple criteria:
          * **Date Range** (start/end).
          * **Time Range** (start/end).
          * **Description/Vendor** (case-insensitive, partial string match).
          * **Amount Range** (minimum and maximum value).
      * **Report Saving:** Any generated report (filtered view) can be optionally saved as a new CSV file, with the filename including a timestamp and the report type.

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

## 📁 Project Structure

This project follows the standard Maven project layout (`src/main/java`) and includes several CSV files at the root level for application data.

```
AccountingLedger/
├── .gitignore
├── pom.xml
├── passcodes.csv                 // Maps passcodes to ledger files and user names
├── transactions_AA.csv           // Example ledger data for user AA
├── transactions_AB.csv           // Example ledger data for user AB
├── transactions_AM.csv           // Example ledger data for user AM
├── transactions_AZ.csv           // Example ledger data for user AZ
├── transactions_HS.csv           // Example ledger data for user HS
├── transactions_JG.csv           // Example ledger data for user JG
├── transactions_JL.csv           // Example ledger data for user JL
└── src/
    └── main/
        └── java/
            └── com/
                └── pluralsight/
                    ├── InputValidation.java  // Methods for robust user input parsing and validation
                    ├── LedgerApp.java        // The main class containing entry point (main method) and menu logic
                    ├── Transaction.java      // The data model class for a single financial transaction
                    └── UtilizedMethods.java  // Contains core application logic, e.g. file loading, report generation
```
-----

## 🖥️ Application Screens

The application is entirely command-line driven, presenting a series of nested menus for navigation.

### Main Menu

This is the first menu displayed after a successful login.

```
================== MAIN MENU ==================
Please select an option from the following menu:
(D) Add deposit
(P) Add payment
(L) View ledger
(I) More info
(X) Exit program
```

### Ledger Menu

Selecting `(L) View ledger` from the main menu opens this submenu, allowing the user to view transactions or run reports.

```
================== LEDGER MENU ==================
Which entries would you like to see?
(A) Display all entries
(D) Display only deposits
(P) Display only payments
(R) Run reports
(H) Back to main menu
```

### Reports Menu

Selecting `(R) Run reports` from the Ledger View Menu displays the available report options, which include default periods and custom searches.

```
================== REPORTS MENU ==================
What report would you like to run?
(1) Month to date
(2) Previous month
(3) Year to date
(4) Previous year
(5) Search by vendor
(6) Custom search
(7) Back to ledger menu
```

### Example Report Output (Displaying All Entries)

The `displayEntries` method generates a tabular, formatted list of transactions. Since the ledger is loaded in reverse chronological order (newest first), the report displays transactions from newest to oldest.

```
Searching....
Date         Time       Description                             Vendor              Amount
+------------+----------+---------------------------------------+--------------------+--------------+
|2024-08-25  |13:10:11  |payment received                       |PayPal              |      500.00|
|2024-08-10  |07:45:10  |coffee                                 |Starbucks           |       -5.45|
|2024-07-26  |16:30:41  |music subscription                     |Spotify             |       -9.99|
|2024-07-11  |09:55:55  |groceries                              |Walmart             |      -78.50|
+------------+----------+---------------------------------------+--------------------+--------------+
Searching complete.
Returning to previous menu....
```

-----

## 🔍 Code Highlight: Dynamic Filtering with Streams

The `runReportCustom` method in `UtilizedMethods.java` demonstrates a clean and efficient way to handle complex, optional filtering using **Java Streams**. This single block of code handles all seven possible filtering parameters (date range, time range, description, vendor, and amount range).

The key is the structure of the `filter` condition: `(parameter == null || condition)`. If the search parameter is null (meaning the user didn't specify a filter for that field), the stream condition is always `true`, and the transactions are passed through. If the parameter is present, the specific condition is applied.

```java
// Snippet from UtilizedMethods.java
public static void runReportCustom(String type, LocalDate startDate, LocalDate endDate, LocalTime startTime,
                                       LocalTime endTime, String description, String vendor,
                                       Double amountMin, Double amountMax) {
        // Filters ledger using the provided parameters
        ArrayList<Transaction> filteredLedger = (ArrayList<Transaction>) openLedger.stream()
                .filter(t -> (startDate == null || !t.getTransactionDate().isBefore(startDate)) &&
                        (endDate == null || !t.getTransactionDate().isAfter(endDate))) // Filters by date range
                .filter(t -> (startTime == null || !t.getTransactionTime().isBefore(startTime)) &&
                        (endTime == null || !t.getTransactionTime().isAfter(endTime))) // Filters by time range
                .filter(t -> (description == null || description.isEmpty() ||
                        t.getDescription().toLowerCase().contains(description)))  // Filters by description
                .filter(t -> (vendor == null || vendor.isEmpty() ||
                        t.getVendor().toLowerCase().contains(vendor))) // Filters by vendor
                .filter(t -> (amountMin == null || t.getAmount() >= amountMin))
                .filter(t -> (amountMax == null || t.getAmount() <= amountMax)) // Filters by amount
                .collect(Collectors.toCollection(ArrayList::new)); // Collects filtered results into ArrayList
        // ... rest of method
}
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


### 2\. Ledger File Format

The ledger files (e.g., `transactions_JL.csv`) are pipe-delimited (`|`) and use the following header:

| Column | Description | Format |
| :--- | :--- | :--- |
| **date** | The date of the transaction. | `YYYY-MM-DD` |
| **time** | The time of the transaction. | `HH:MM:SS` |
| **description** | A brief description of the item or service. | String |
| **vendor** | The vendor or source of the transaction. | String |
| **amount** | The monetary value. **Positive** for deposits, **Negative** for payments. | Double (e.g., `1200.00` or `-7.99`) |
