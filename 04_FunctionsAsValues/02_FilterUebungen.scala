object FilterUebungen {
  def main(args: Array[String]): Unit = {
    // Aufgabe 2.1
    val numbers = List(1, 2, 3, 4, 5)
    val evenNumbers = numbers.filter(n => n % 2 == 0)
    println("Aufgabe 2.1 - Gerade Zahlen:")
    println(evenNumbers)

    // Aufgabe 2.2
    val names = List("Alice", "Bob", "Charlie", "Diana")
    val longNames = names.filter(name => name.length > 4)
    println("\nAufgabe 2.2 - Namen länger als 4 Buchstaben:")
    println(longNames)

    // Aufgabe 2.3
    val numberList = List(12, 45, 68, 100)
    val largeNumbers = numberList.filter(_ > 50)
    println("\nAufgabe 2.3 - Zahlen größer als 50:")
    println(largeNumbers)

    // Aufgabe 2.4
    val words = List("Scala", "ist", "fantastisch")
    val sWords = words.filter(_.startsWith("S"))
    println("\nAufgabe 2.4 - Wörter die mit 'S' beginnen:")
    println(sWords)

    // Aufgabe 2.5
    case class Buch(titel: String, autor: String, jahr: Int)
    
    val buecher = List(
      Buch("1984", "George Orwell", 1949),
      Buch("Brave New World", "Aldous Huxley", 1932),
      Buch("Fahrenheit 451", "Ray Bradbury", 1953)
    )

    val oldBooks = buecher.filter(_.jahr < 1950).map(_.titel)
    println("\nAufgabe 2.5 - Bücher vor 1950:")
    println(oldBooks)
  }
}