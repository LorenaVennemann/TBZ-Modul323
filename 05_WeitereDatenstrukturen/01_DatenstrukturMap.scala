object MapExercises extends App {
  // Übung 1: Werte zuweisen
  val m1: Map[String, String] = Map("key" -> "value")
  println("Übung 1: " + m1)

  // Übung 2: Map aktualisieren
  val m2: Map[String, String] = m1 + ("key2" -> "value2")
  println("Übung 2: " + m2)

  // Übung 3: Map nochmals aktualisieren
  val m3: Map[String, String] = m2 + ("key2" -> "aDifferentValue")
  println("Übung 3: " + m3)

  // Übung 4: Element entfernen
  val m4: Map[String, String] = m3 - "key"
  println("Übung 4: " + m4)

  // Übung 5: Nach key suchen
  val valueFromM3: Option[String] = m3.get("key")
  println("Übung 5: " + valueFromM3)

  // Übung 6: Kein existierender Wert
  val valueFromM4: Option[String] = m4.get("key")
  println("Übung 6: " + valueFromM4)
}