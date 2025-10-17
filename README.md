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

The application provides a comprehensive set of features accessible through a main menu and sub-menus:

  * **Secure Login:** Uses a **passcode** to identify the user and load the correct ledger file (e.g., `transactions_JL.csv`).
  * **Add Transaction:**
      * **(D) Add Deposit:** Records income (positive amount).
      * **(P) Add Payment:** Records expenses (negative amount).
      * Supports entering the current date/time by typing **'N'** for "now".
  * **View Ledger:** Allows displaying all entries or filtering by type.
      * **(A)** Display all transactions.
      * **(D)** Display only deposits.
      * **(P)** Display only payments.
  * **Reports Generation:** A dedicated menu for running filtered reports.
      * **Default Reports:** Generate reports for Month to Date, Previous Month, Year to Date, and Previous Year.
      * **Search by Vendor:** Filter transactions by a specific vendor name.
      * **Custom Search:** Define a custom filter using a combination of date range, time range, description, vendor, and amount range (min/max).
  * **Data Persistence:** Transactions are loaded from and saved back to a pipe-delimited CSV file (e.g., `transactions_JL.csv`).
  * **Report Saving:** Any generated report can be saved to a new CSV file with a timestamp and report type in the filename.

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

| Passcode | Name | Ledger File |
| :---: | :---: | :---: |
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
