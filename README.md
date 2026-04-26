# Peer-to-Peer File Sharing System

## 📌 Overview

This project implements a simple Peer-to-Peer (P2P) file sharing system using Java socket programming. It allows clients to share and download files directly without relying on a central server for file transfer.

---

## 🎯 Features

* Share files with other peers
* Download files from peers
* Tracker-based peer discovery
* Decentralized file transfer

---

## 🏗️ Project Structure

```
P2P_File_Sharing/
│
├── TrackerServer.java
├── Client.java
```

---

## ⚙️ How It Works

1. Tracker Server stores file locations
2. Clients register files with tracker
3. Clients search for files
4. File transfer happens directly between peers

---

## ▶️ How to Run

### Step 1: Compile

```
javac TrackerServer.java
javac Client.java
```

---

### Step 2: Run Tracker Server

```
java TrackerServer
```

---

### Step 3: Run Client (Uploader)

```
java Client
```

* Enter port: 6001
* Choose "Share File"
* Enter file name

---

### Step 4: Run Client (Downloader)

```
java Client
```

* Enter port: 6002
* Choose "Download File"
* Enter file name

---

## 📂 Output

Downloaded file will be saved as:

```
downloaded_<filename>
```

---

## 🔄 Example Flow

* Client A shares a file
* Client B searches for the file
* Tracker provides peer info
* Client B downloads directly from Client A

---

## 🔑 Key Concept

This project demonstrates **Decentralized Communication**:

* Tracker is only used for discovery
* File transfer is peer-to-peer

---

## ⚠️ Limitations

* No authentication
* No encryption
* Works on local system

---

## 🚀 Future Improvements

* GUI Interface
* Secure file transfer
* Multi-peer downloading

---

## 📌 Author

Sharani Sri

