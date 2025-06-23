import java.time.LocalTime

object TupelExercises extends App {
  // Aufgabe 1: Wetter Funktion mit Tupel
  def getWetter(): (String, LocalTime, Double) = {
    ("sonnig", LocalTime.now(), 25.5)
  }
  
  val aktuellesWetter = getWetter()
  println(s"Wetter: ${aktuellesWetter._1}, Zeit: ${aktuellesWetter._2}, Temperatur: ${aktuellesWetter._3}°C")

  // Aufgabe 2: Liste von Wetterdaten
  val wetterDaten = List(
    ("Zürich", "sonnig", 22.5),
    ("Bern", "bewölkt", 18.0),
    ("Basel", "sonnig", 24.0),
    ("Genf", "regnerisch", 17.5)
  )

  def warmeStaedte(daten: List[(String, String, Double)]): List[String] = {
    daten.filter(_._3 > 20).map(_._1)
  }

  println("Städte über 20°C: " + warmeStaedte(wetterDaten))

  // Zusatzaufgabe: Trending Funktion
  def trending(rates: List[BigDecimal]): Boolean = {
    rates
      .zip(rates.drop(1))  
      .forall { case (prev, curr) => curr > prev }       
}

  // Test der Trending Funktion
  println(trending(List(BigDecimal(1), BigDecimal(4), BigDecimal(3), BigDecimal(8))))  // false
  println(trending(List(BigDecimal(1), BigDecimal(2), BigDecimal(3), BigDecimal(8))))  // true
}