# 📚 Projekt: Funktionale Programmierung mit Scala

---

## 🎯 Lernziele

- Ich kann den Unterschied zwischen Anforderungen der imperativen und deklarativen Programmierung erklären.
- Ich kann Anforderungen aus der imperativen Programmierung in Anforderungen der deklarativen Programmierung übertragen.
- Ich kann Anforderungen als Funktionen erkennen und formulieren.
- Ich kann aus Beschreibungen das **WAS** ableiten, ohne das **WIE** zu beschreiben.

---

## 🧠 Grundidee

In der funktionalen Programmierung steht nicht das *Wie*, sondern das *Was* im Vordergrund.

**Beispiel:**  
> Ich möchte ein Programm, das 5 % Rabatt vom Totalbetrag berechnet.  
→ Dies beschreibt **was** gewünscht ist – nicht **wie** es umgesetzt wird.

---

## 🧩 Aufgabenbeschreibung

### Aufgabe 1 – Reise planen

**Anforderung:**  
- Benutzer gibt Destinationen für eine Reise durch Europa ein.
- Eine bestehende Route soll bearbeitbar sein (z. B. Zwischenstopp hinzufügen oder ändern).

**Mögliche Funktionen:**
- `addDestination(route: List[String], destination: String): List[String]`
- `updateDestination(route: List[String], index: Int, newDestination: String): List[String]`

---

### Aufgabe 2 – Wörter mit Punkten bewerten

**Anforderung:**  
- Wörter sollen eingegeben werden.
- Jeder Buchstabe ergibt einen Punkt, außer "a".
- Wörterliste soll nach Punktzahl sortiert werden.

**Mögliche Funktionen:**
- `calculatePoints(word: String): Int`
- `sortWordsByPoints(words: List[String]): List[(String, Int)]`

---

### Aufgabe 3 – Autorennen

**Anforderung:**  
- Gesamtzeit aller Runden (ohne Warm-up) berechnen.
- Durchschnittszeit berechnen.

**Mögliche Funktionen:**
- `calculateTotalTime(times: List[Double]): Double`
- `calculateAverageTime(times: List[Double]): Double`

---

## ✅ Aufgabenübersicht

| Aufgabe                         | Status |
|----------------------------------|--------|
| Anforderungen analysieren        | ⬜      |
| Funktionssignaturen formulieren | ⬜       |
| Scala-Implementierung            | ⬜      |
| Ergebnisse testen                | ⬜      |

---

## 📌 Hinweis

Bei der Umsetzung wird auf funktionale Konzepte geachtet:
- Keine Seiteneffekte
- Reine Funktionen
- Listenoperationen wie `map`, `filter`, `fold`, `sortBy` etc.
- Immutable Datenstrukturen

---

**Name:** Lorena Jil Vennemann  
**Klasse:** Ap22b  
**Datum:** 12. Mai 2025  