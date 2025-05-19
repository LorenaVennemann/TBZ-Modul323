// Aufgabe 1 – Reise planen
object ReisePlaner {
  def addDestination(route: List[String], destination: String): List[String] =
    route :+ destination

  def updateDestination(route: List[String], oldDest: String, newDest: String): List[String] =
    route.map(dest => if (dest == oldDest) newDest else dest)
}

// Aufgabe 2 – Wörter mit Punkten bewerten
object WortBewertung {
  def scoreWord(word: String): Int =
    word.count(_ != 'a')

  def filterAndScoreWords(words: List[String]): List[(String, Int)] =
    words.map(w => (w, scoreWord(w)))

  def sortWordsByPoints(words: List[(String, Int)]): List[(String, Int)] =
    words.sortBy(-_._2)
}

// Aufgabe 3 – Autorennen
object AutoRennen {
  def berechneGesamtzeit(zeiten: List[Double]): Double =
    zeiten.drop(1).sum

  def berechneDurchschnitt(zeiten: List[Double]): Double = {
    val runden = zeiten.drop(1)
    if (runden.nonEmpty) runden.sum / runden.size else 0.0
  }

  def getRaceSummary(autoDaten: Map[String, List[Double]]): Map[String, (Double, Double)] =
    autoDaten.map { case (auto, zeiten) =>
      val total = berechneGesamtzeit(zeiten)
      val avg = berechneDurchschnitt(zeiten)
      (auto, (total, avg))
    }
}

@main def runExamples(): Unit =
  println("=== Aufgabe 1 – Reise Planer ===")
  val route = List("Zürich", "Wien")
  val newRoute = ReisePlaner.addDestination(route, "Paris")
  val updatedRoute = ReisePlaner.updateDestination(newRoute, "Wien", "Berlin")
  println(s"Route: $updatedRoute")

  println("\n=== Aufgabe 2 – Wort Bewertung ===")
  val words = List("apfel", "banane", "kiwi", "traube")
  val scored = WortBewertung.filterAndScoreWords(words)
  val sorted = WortBewertung.sortWordsByPoints(scored)
  println(s"Bewertet: $scored")
  println(s"Sortiert: $sorted")

  println("\n=== Aufgabe 3 – Autorennen ===")
  val zeitenProAuto = Map(
    "CarA" -> List(120.0, 115.5, 118.3, 119.0),
    "CarB" -> List(123.0, 122.5, 121.0, 120.5)
  )
  val resultate = AutoRennen.getRaceSummary(zeitenProAuto)
  resultate.foreach { case (car, (gesamt, durchschnitt)) =>
    println(s"$car - Total: $gesamt s, Durchschnitt: $durchschnitt s")
  }
