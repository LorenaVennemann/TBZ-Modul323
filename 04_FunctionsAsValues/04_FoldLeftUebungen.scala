object FoldLeftUebungen {
  def main(args: Array[String]): Unit = {
    // Aufgabe 4.1
    val numbers = List(1, 2, 3, 4, 5)
    val sum = numbers.foldLeft(0)((acc, num) => acc + num)
    println("Aufgabe 4.1 - Summe aller Zahlen:")
    println(sum)

    // Aufgabe 4.2
    val words = List("Hallo", " ", "Welt", "!")
    val combinedString = words.foldLeft("")((acc, str) => acc + str)
    println("\nAufgabe 4.2 - Kombinierter String:")
    println(combinedString)

    // Aufgabe 4.3
    val points = List((1, 3), (2, 5), (4, 8), (6, 2))
    
    // Berechnung des Schwerpunkts
    val count = points.size
    val (sumX, sumY) = points.foldLeft((0, 0)) { 
      case ((accX, accY), (x, y)) => (accX + x, accY + y) 
    }
    
    val centerPoint = (sumX.toDouble / count, sumY.toDouble / count)
    println("\nAufgabe 4.3 - Schwerpunkt der Punkte:")
    println(centerPoint)
  }
}