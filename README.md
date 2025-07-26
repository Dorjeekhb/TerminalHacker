# 💻 TERMINAL HACKER

> 🎯 *A retro-style CTF hacking simulation game, still in early development.*

> ⚠️ **NOTE:** This game is currently only available in **Spanish** and is in a **very early stage** of development (alpha). English version will come later.

---

## 🇬🇧 ENGLISH (WIP version)

### 🎮 What is Terminal Hacker?

Terminal Hacker is a retro-inspired Android game that simulates hacking nodes using real-world CTF logic. Each node is a small hacking challenge with flags (`FLAG{...}`), hidden files, decryption, brute-force and puzzles inspired by Hack The Box and other platforms.

> ⚠️ This version is **not yet localized in English**. Gameplay and all text are in Spanish. English will be supported in the future.

---

### 🧹 Features

* Simulated terminal UI (green-on-black)
* Type real commands like `ls`, `cat`, `decrypt`, `submit`
* Every node contains a puzzle and a README
* Progress is persistent (✔️ or ❌)
* Vibration and glitch effects
* Difficulty levels and estimated time
* Lore narrative per node

---

### 💡 Planned Features

* Full English translation
* More nodes with steganography, cryptanalysis, metadata leaks...
* Online scoreboard
* Story mode with AI and dystopian narrative
* Upload/download payloads (simulated)
* Custom puzzle editor

---

### ⚙️ Technologies

* Kotlin / Android SDK
* SharedPreferences for persistent node state
* Terminal-like TextView / EditText interface
* Modular file-based challenge system

---

## 🇪🇸 ESPAÑOL

### 🎮 ¿Qué es Terminal Hacker?

Terminal Hacker es un juego de simulación hacker estilo retro donde deberás superar nodos como si fueran retos tipo CTF (Capture The Flag), inspirados en Hack The Box y otras plataformas. Cada nodo contiene archivos, pistas, un README con instrucciones y una FLAG oculta.

> ⚠️ Este juego está **muy verde aún**. Todo el contenido está **solo en español** y se encuentra en una **fase muy temprana de desarrollo**.

---

### 🦚 Características actuales

* Interfaz estilo terminal verde retro (tipo consola)
* Comandos reales: `ls`, `cat`, `decrypt`, `submit`
* Puzzles por nodo con flags ocultas
* README explicativo por reto
* Vibración y efectos visuales glitch
* Dificultad y tiempo estimado por nodo
* Progreso persistente (✔️ o ❌)
* Lore narrativo desbloqueable

---

### 🚀 Ideas futuras

* Traducción completa al inglés
* Más nodos: metadata, cifrado, ingeniería inversa...
* Modo historia cyberpunk
* Ranking global online
* Editor de retos personalizado
* Integración IA narrativa estilo NetRunner

---

## 🧠 Nodos actuales

| ID            | Nombre        | Dificultad | Tags                    |
| ------------- | ------------- | ---------- | ----------------------- |
| node-001      | GhostLog      | Fácil      | logs, archivos ocultos  |
| node-047      | EncryptedEcho | Medio      | cifrado, decrypt        |
| node-admin    | AdminTrap     | Medio      | fuerza bruta, passwords |
| node-vault    | TheVault      | Difícil    | base64, análisis        |
| node-metadata | MetaLeak      | Fácil      | metadata, imágenes      |
| node-stegano  | StegaSnoop    | Medio      | esteganografía, imagen  |
| node-ransom   | RansomAI      | Difícil    | IA, binarios, seguridad |

---

## 📂 Estructura del Proyecto

```
TerminalHacker/
├── MainActivity.kt
├── MapActivity.kt
├── NodeSelectionActivity.kt
├── SharedPreferencesHelper.kt
├── res/
│   └── layout/
│       ├── activity_main.xml
│       ├── activity_map.xml
│       └── activity_node_selection.xml
└── README.md
```

---

## 👤 Desarrollador

**Dorjee** – Estudiante de Desarrollo de Videojuegos en la UCM. Apasionado del hacking ético.

---

## 🛡️ Disclaimer

> Terminal Hacker is an educational project. All challenges and flags are fictional. No real systems are affected or exploited.

