# 🧠 Anforderungen deklarativ umsetzen – Funktions-Signaturen

Dieses Dokument enthält Skizzen für drei Aufgabenstellungen. Es geht darum, Anforderungen nicht imperativ (WIE), sondern deklarativ (WAS) zu denken und entsprechende Funktions-Signaturen abzuleiten.

---

## 🧳 Aufgabe 1: Eine Reise planen

**Beschreibung:**  
Der Benutzer soll Destinationen für eine Reise eingeben.  
Es soll möglich sein, bestehende Routen zu ändern (z. B. Zwischenaufenthalte hinzufügen).

### 🔧 Mögliche Funktions-Signaturen:

```scala
addDestination(route: List[String], newDestination: String): List[String]

removeDestination(route: List[String], destination: String): List[String]

updateDestination(route: List[String], oldDestination: String, newDestination: String): List[String]

getRoute(route: List[String]): String
```

---

## 📝 Aufgabe 2: Wörter mit Punkten bewerten

**Beschreibung:**  
Benutzer gibt Wörter ein. Jeder Buchstabe gibt 1 Punkt – außer "a".  
Die Ausgabe ist eine sortierte Liste nach Punktzahl.

### 🔧 Mögliche Funktions-Signaturen:

```scala
calculateWordPoints(word: String): Int

filterAndScoreWords(words: List[String]): List[(String, Int)]

sortWordsByPoints(scoredWords: List[(String, Int)]): List[String]
```

---

## 🏁 Aufgabe 3: Autorennen

**Beschreibung:**  
Die App berechnet die Gesamtzeit aller Runden (ohne Warm-up-Runde).  
Zudem wird die Durchschnittszeit pro Auto berechnet.

### 🔧 Mögliche Funktions-Signaturen:

```scala
excludeWarmUpRound(roundTimes: List[Double]): List[Double]

calculateTotalTime(roundTimes: List[Double]): Double

calculateAverageTime(roundTimes: List[Double]): Double

getRaceSummary(allCars: Map[String, List[Double]]): Map[String, (Double, Double)]
```
