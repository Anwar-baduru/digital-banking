# 🏦 Production-Grade Digital Banking Core Engine

A highly secure, decoupled, and interactive **Core Banking Engine** implemented in **Java (Java 17+)** and backed by a **MySQL Relational Database**. The application transitions from simple volatile in-memory models into a fully enterprise-grade, **ACID-compliant relational disk architecture** utilizing the standard **JDBC API**.

---

## 🌟 Key Engineering Features

### 🧑‍💻 Interactive Multitenant CLI Interfaces
* **Employee Subsystem:** Fully authorized administrative section enabling dynamic regional branch registrations and customer account creations. Includes comprehensive **Regex input sanitization** to block data corruption or special character exploits.
* **Account Holder Subsystem:** Self-service portal enabling secure peer-to-peer wire transfers and instant transaction history queries.

### 🛡️ ACID-Compliant Transaction Architecture
* Leverages **Manual Transaction Bounds (`conn.setAutoCommit(false)`)** inside the transfer funnels.
* Guarantees absolute data state perfection via **Dynamic Cascading Exceptions** and strict **Rollback (`conn.rollback()`)** structures. If a sender's balance updates but a receiver lookup experiences network drops, the entire database transaction instantly rolls back to safeguard client funds.

### ⚡ Algorithmic Sequence Generation & Optimization
* **High-Low Sequential Identifier Engine:** Generates unique account tokens systematically using a dynamic 5-digit prefix tracker mapped against an accelerated random 4-digit suffix block using a fast **`HashSet` time lookup matrix**.
* **Constant-Time Mini-Statement Extraction:** Eliminates expensive collection cloning by shifting processing down to the SQL engine. Leverages time-sorted row filtering indexes (`ORDER BY timestamp DESC LIMIT 10`) to display logs inside a constant **O(1) Time and Space complexity overhead**.

### 🔒 Enterprise Data Privacy & Security
* Uses **`PreparedStatement` parameterized query mappings** to completely defend against **SQL Injection attacks**.
* Leverages **Environment Variables Contexts (`System.getenv`)** to load access credentials locally, keeping the primary public source repositories secure and clean.
* Splits visible transaction descriptions (using clean string names) from internal audit tables containing immutable relational metadata columns (`send_to` and `receive_from`).

---

## 🛠️ System Architecture & Data Schema

The database model decouples structures into a **One-to-Many Relationship** utilizing a multi-table structure to record balance entries cleanly:
```text
[branch_registry] (Composite Unique Key: city, region)
│
[bank_accounts] (Primary Key: account_number)
│
└───► [transaction_ledger] (Foreign Key Link with ON DELETE CASCADE)
```
---

## 🚀 Environment Setup & Run Instructions

### 1. Database Initialization
Open your local **MySQL Workbench** or command line panel and run the database setup container statement:
```sql
CREATE DATABASE banking_db;
```
*(The internal bootstrapper class `DatabaseConfig.java` will automatically script the tables, composite primary indexes, and foreign key boundaries dynamically upon the application's first execution run).*

### 2. Configure Local Credentials Securely
To pass credentials cleanly into the application framework without modifying source code files, configure your local environment variable context variables inside **IntelliJ IDEA**:
1. Open the dropdown selector menu at the top right of the IDE interface and click **Edit Configurations...**.
2. Select or create your active execution instance template under **Application** pointing to your main class (`BankingEngine`).
3. Locate the **Environment variables** field box and inject these three active key flags:
    * `DB_URL` = `jdbc:mysql://localhost:3306/banking_db`
    * `DB_USER` = `root`
    * `DB_PASS` = `YOUR_MYSQL_ROOT_PASSWORD`
4. Click **Apply** and then hit **OK**.

### 3. Run the Core App Engine
Select the **`BankingEngine.java`** file and click the green **Play/Run** icon to access the fully interactive terminal console menu panel!