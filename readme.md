# Pick-It – Dein Multiplayer-Spiel

> Drehe Symbole, gewinne Runden und dominiere das Spielfeld mit deinen Lieblings-Symbolen!  
> Pick-It ist ein strategisches, schnelles Multiplayer-Spiel mit Symbolkarten und Emojis für 1–4 Spieler:innen.

---

## 🎯 Was ist Pick-It?

Pick-It ist ein Echtzeit-Multiplayer-Spiel, bei dem du mit 5 Symbolen pro Runde dein Feld drehst, gezielt Symbole festhältst und versuchst, bestimmte Kombinationen zu erfüllen – z. B. Vier Gleiche, Full House oder 5 Verschiedene.  
Du kannst deine Lieblings-Symbole in Paketen kaufen und gegen Freund:innen oder zufällige Gegner:innen antreten.

### Für wen?

Für alle, die Spaß an Strategie und Glück haben.  
Ideal für Zwischendurch, aber mit Langzeitmotivation durch Symbolauswahl und Rangliste.

---

## 🧑‍🎨 Design

> Screenshots folgen …

---

## ✨ Features

- ✅ Realtime-Multiplayer (1–4 Spieler) via Firebase
- ✅ Symbolpakete (je 6 Emojis pro Thema)
- ✅ Standardpaket ist kostenlos
- ✅ Alle weiteren Pakete kosten echtes Geld (In-App-Kauf)
- ✅ Kombinationslogik mit Punktesystem (z. B. Vier Gleiche, Full House)
- ✅ Joker-System für knifflige Runden
- ✅ Platzwahl wie bei einem echten Spieletisch
- ✅ Countdown-Spielstart, sobald Plätze belegt sind
- ✅ Spielerprofile & Rangliste
- ✅ Komplett auf Landscape optimiert

---

## 💡 Symbolpakete & In-App-Käufe

- 🆓 **Standardpaket**: kostenlos und sofort verfügbar
- 💸 **Weitere Pakete**:
  - Themen: Tier, Obst, Flaggen, Emoji, Ball, Mix usw.
  - Preis: kostenpflichtig per In-App-Kauf
  - Inhalt: 6 Symbole pro Paket
  - Nach dem Kauf dauerhaft freigeschaltet

---

## ⚙️ Technischer Aufbau

### 🔧 Projektstruktur

- **MVVM-Architektur**
- **Repositories** zur Trennung von Logik & Daten
- **ViewModels** für Spiel, Lobby, Symbolshop
- **Jetpack Compose** für UI

### 💾 Datenspeicherung

- **Firebase Authentication**: Anonym, Google, Facebook
- **Firestore**:
  - Spielerstatus, Lobby, Spielverlauf
- **Firebase Hosting**:
  - Eigenes API-Backend: `symbols.json`
- **In-App-Käufe**:
  - Google Play Billing

### 🌐 API Calls

| API              | Zweck                         |
|------------------|-------------------------------|
| Firebase Auth    | Spieler-Login                 |
| Firestore        | Realtime-Spielstatus          |
| Eigene JSON-API  | Laden der Symbolpakete        |

### 🔌 3rd-Party Libraries

- **Retrofit** (API-Zugriff)
- **Moshi** (JSON-Parsing)
- **Koin** (Dependency Injection)

---

## 📈 Ausblick

- [ ] Avatarwahl & Profilgestaltung
- [ ] Belohnte Werbung für Zusatzjoker
- [ ] Tägliche Belohnungen (Free-Spin)
- [ ] Soundeffekte & Animationen
- [ ] Eventpakete (z. B. Halloween, Winter)

---

## 👩‍💻 Entwicklerin

**Dilara Öztas**  
📍 Deutschland  
🔗 GitHub: [github.com/dilaraoeztas](https://github.com/dilaraoeztas)  
🔗 LinkedIn: [linkedin.com/in/dilara-bodur19](https://www.linkedin.com/in/dilara-bodur19)
