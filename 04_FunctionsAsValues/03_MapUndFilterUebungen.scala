object MapUndFilterUebungenUebungen {
  def main(args: Array[String]): Unit = {
    // Aufgabe 3.1
    case class Mitarbeiter(name: String, abteilung: String, gehalt: Int)

    val mitarbeiter = List(
      Mitarbeiter("Max Mustermann", "IT", 50000),
      Mitarbeiter("Erika Musterfrau", "Marketing", 45000),
      Mitarbeiter("Klaus Klein", "IT", 55000),
      Mitarbeiter("Julia Gross", "HR", 40000)
    )

    val itMitarbeiter = mitarbeiter
      .filter(m => m.abteilung == "IT" && m.gehalt >= 50000)
      .map(m => m.name.split(" ")(0).toUpperCase)

    println("Aufgabe 3.1 - IT Mitarbeiter mit hohem Gehalt:")
    println(itMitarbeiter)

    // Aufgabe 3.2
    val kurse = List(
      "Programmierung in Scala",
      "Datenbanken",
      "Webentwicklung mit JavaScript",
      "Algorithmen und Datenstrukturen"
    )

    val datenKurse = kurse
      .filter(_.contains("Daten"))
      .map(_.replace(" ", ""))

    println("\nAufgabe 3.2 - Kurse mit 'Daten', ohne Leerzeichen:")
    println(datenKurse)

    println("\nAufgabe 3.2 - Alphabetisch sortiert:")
    println(datenKurse.sorted)

    println("\nAufgabe 3.2 - Umgekehrt alphabetisch sortiert:")
    println(datenKurse.sorted.reverse)
  }
}