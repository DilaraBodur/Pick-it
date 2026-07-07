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

<p align="left">
<img width="280" alt="Startseite" src="https://github.com/user-attachments/assets/d38ef953-eb57-41e2-ba97-6eb691e005ba" />
<img width="280" alt="Ladenseite" src="https://github.com/user-attachments/assets/99ed62a3-2eb3-4c91-9602-98ca20453d0c" />
<img width="280" alt="Inventar_Seite" src="https://github.com/user-attachments/assets/0cbd65a5-2a22-4733-beb9-48f5e7c78421" />
</p>

<p align="left">
<img width="320" alt="Rangliste" src="https://github.com/user-attachments/assets/55d8cfb3-2fda-41f8-9c2c-24d4161d909d" />
<img width="320" alt="Einstellungen" src="https://github.com/user-attachments/assets/2e4d9d0e-7739-4a8e-808e-323b3aca3b3a" />
<img width="320" alt="Einstellungen2" src="https://github.com/user-attachments/assets/cfabc82a-9789-4aba-b2cc-d318b74ca273" />
</p>

<p align="left">
<img width="320" alt="Profil_Seite" src="https://github.com/user-attachments/assets/3a5f1569-9304-4e3e-adaf-2e372232fdc3" />
<img width="320" alt="Profil_bearbeiten" src="https://github.com/user-attachments/assets/9bb872cf-b3dd-4aa5-b07a-2608bcc14ec5" />
<img width="320" alt="Spiel_Seite" src="https://github.com/user-attachments/assets/39966778-e890-4551-a20f-b0e9543de119" />
</p>

---

## ✨ Features

- ✅ Realtime-Multiplayer (1–4 Spieler) via Firebase
- ✅ Symbolpakete (je 6 Symbole pro Thema)
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
- [ ] Saisonale Symbolpakete (z.B.: Halloween)
- [ ] **Abomodell: Werbefreiheit (monatlich)**
- [ ] **Abomodell: +1 Joker dauerhaft**

---

## 👩‍💻 Entwicklerin

**Dilara Bodur**  
📍 Deutschland  
🔗 GitHub: [github.com/DilaraBodur](https://github.com/DilaraBodur)  
🔗 LinkedIn: [linkedin.com/in/dilara-bodur](https://www.linkedin.com/in/dilara-bodur19)
