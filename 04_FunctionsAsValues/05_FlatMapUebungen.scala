object FlatMapUebungen {
  def main(args: Array[String]): Unit = {
    // Aufgabe 5.1
    val nestedNumbers = List(List(1, 2), List(3, 4), List(5, 6))
    val doubledNumbers = nestedNumbers.flatMap(list => list.map(_ * 2))
    println("Aufgabe 5.1 - Verdoppelte Zahlen:")
    println(doubledNumbers)

    // Aufgabe 5.2
    val people = List(
      ("Max", List("Blau", "Grün")),
      ("Anna", List("Rot")),
      ("Julia", List("Gelb", "Blau", "Grün"))
    )
    val uniqueColors = people.flatMap(_._2).distinct
    println("\nAufgabe 5.2 - Einzigartige Farben:")
    println(uniqueColors)
  }
}