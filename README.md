# 🎱 Ball Wars - Multiplayer Billiards Arena

A fast-paced, physics-based local network (LAN) multiplayer game developed in Java. Instead of hitting colored balls with a cue ball, you are the numbered balls, and your goal is to hunt down the white ball and force it into the pocket!

---

## 🕹️ Game Rules
1. **Player 1 (Yellow)** and **Player 2 (Blue)** take turns shooting.
2. Adjust your angle and power carefully to hit the **White Ball**.
3. The first player to knock the White Ball into the black pocket wins the match!
4. Watch out for realistic cushion physics—the balls can spin and bounce off walls unpredictably!

---

## 🎮 Controls
*   **Up / Down Arrow Keys**: Change shooting angle.
*   **Left / Right Arrow Keys**: Adjust shooting power.
*   **SPACEBAR** or **"FIRE" Button**: Launch your ball and pass the turn.

---

## 🌐 Network & Connection Guide (Multiplayer)

The game uses **Server-Authoritative Synchronization** so both screens remain perfectly matched pixel by pixel.

### Option A: Local Network (LAN)
1. Both computers must be connected to the same Wi-Fi or Router.
2. **Player 1 (Host)**: Enter your username, leave the port as `5555`, and click **Create Room (Host)**.
3. **Player 2 (Client)**: Enter your username, type `server` in the IP address box, and click **Join Room (Client)**.

### Option B: Internet (Play with Friends Globally via UPnP)
1. **Player 1 (Host)**: Ensure **"Enable UPnP"** is checked. Click **Create Room (Host)**. The game will automatically forward port `5555` on your router.
2. Share your Public/External IP address with your friend.
3. **Player 2 (Client)**: Paste your friend's Public IP address into the IP box and click **Join Room (Client)**.

---

## 🚀 How to Build & Run

### Prerequisites
*   **Java SE 1.8 (JDK 8)** or higher.

### Folder Structure
Ensure your game assets are placed in the correct location:
```text
C:/Users/YUSUF/Desktop/java/BallWars/res/
├── table_prototype.png
```

### Compile & Run from Terminal
```bash
# Navigate to source folder
cd BallWars/src

# Compile everything
javac com/yusuf/ballwars/Main.java

# Run the game
java com.yusuf.ballwars.Main
```

---

## 📜 License
This project is licensed under the **MIT License**. See the text below:

```text
Copyright (c) 2026 Yusuf

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
