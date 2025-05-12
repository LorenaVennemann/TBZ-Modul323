# Java Übungsprojekt – Imperativ, Deklarativ & Funktional

## 📘 Aufgabe 1 – Imperativ vs. Deklarativ

### Ziel:
Wir erweitern eine bestehende Funktion zur Berechnung eines Scores, bei dem alle Zeichen eines Wortes gezählt werden – **außer** dem Buchstaben `a`.

### Anforderungen:
- Implementiere die Erweiterung im **imperativen Stil**
- Implementiere die Erweiterung im **deklarativen Stil**
- Erwartete Ausgaben:
  - `calculateScore("imperative") == 9`
  - `calculateScore("no") == 2`
  - `wordScore("declarative") == 9`
  - `wordScore("yes") == 3`

---

## 🛒 Aufgabe 2 – Umsetzung einer ShoppingCart Funktion

### Ziel:
Eine einfache Warenkorb-Logik mit Rabattregel.

### Anforderungen – Schritt 1 (Imperativ):
- Implementiere eine Klasse `ShoppingCart`
- Nutze eine interne Liste zur Speicherung der Elemente
- Verwende eine `bookAdded`-Variable für den Rabatt
- Rabatt: 5%, wenn ein Buch enthalten ist – sonst 0%
- Elemente können hinzugefügt, entfernt und gelesen werden
- Hinweis: Was passiert, wenn ein Buch entfernt wird?

### Anforderungen – Schritt 2 (Funktional):
- Implementiere eine Funktion `getDiscountPercentage`
- Übergabe der Warenkorb-Liste als Parameter
- Funktion soll rein sein (keine Seiteneffekte)

---

## 💰 Aufgabe 3 – TipCalculator

### Ziel:
Berechnung des Trinkgelds in Abhängigkeit der Gruppengröße – als **pure Funktion**.

### Anforderungen:
- 0% Trinkgeld bei keiner Person
- 10% Trinkgeld bei Gruppen von 1–5 Personen
- 20% Trinkgeld bei Gruppen ab 6 Personen
- Umwandlung der vorhandenen Klasse in eine reine Funktion:
  - Kein interner Zustand
  - Rückgabewert nur basierend auf Parameter

---
# Java Übungsprojekt – Imperativ, Deklarativ & Funktional  
**Name:** Lorena Jil Vennemann 
**Klasse:** Ap22b 
**Datum:** 12. Mai 2025
