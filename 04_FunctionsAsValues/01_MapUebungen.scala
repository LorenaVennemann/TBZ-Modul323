object MapUebungen {
  def main(args: Array[String]): Unit = {
    // Aufgabe 1.1
    val numbers = List(1, 2, 3, 4, 5)
    val doubledNumbers = numbers.map(num => num * 2)
    println(s"Übung 1: $doubledNumbers")

    // Aufgabe 1.2
    val names = List("Alice", "Bob", "Charlie")
    val upperCaseNames = names.map(name => name.toUpperCase)
    println(s"Übung 2: $upperCaseNames")
    
    // Aufgabe 1.3
    val numbers2 = List(12, 45, 68, 100)
    val halvedNumbers = numbers2.map(x => x.toDouble / 2)
    println(s"Übung 3: $halvedNumbers")

    // Aufgabe 1.4
    case class Adresse(strasse: String, hausnummer: Int, postleitzahl: String, stadt: String)
    val adressen = List(
      Adresse("Hauptstrasse", 10, "12345", "Musterstadt"),
      Adresse("Nebenstrasse", 5, "23456", "Beispielburg")
    )
    val formattedAddresses = adressen.map(addr => 
      s"${addr.strasse} ${addr.hausnummer}, ${addr.postleitzahl} ${addr.stadt}"
    )
    println(s"Übung 4: $formattedAddresses")

    // Aufgabe 1.5
    val namen = List("Max Mustermann", "Erika Mustermann")
    val firstNames = namen.map(name => name.split(" ")(0).toUpperCase)
    println(s"Übung 5: $firstNames")
  }
}