/**
 * Service-Klasse für die Verwaltung von Aufgaben (Tasks) in der Todo-Liste
 * 
 * Diese Klasse stellt alle notwendigen Operationen für das Handling von Aufgaben bereit:
 * - Hinzufügen neuer Aufgaben mit automatischer ID-Generierung
 * - Aktualisieren bestehender Aufgaben
 * - Löschen von Aufgaben
 * - Filtern von Aufgaben nach Kategorie oder Fälligkeitsdatum
 * 
 * Alle Methoden sind immutabel implementiert und geben eine neue Liste zurück,
 * anstatt die bestehende zu modifizieren.
 */
package services

import modelle._
import java.time.LocalDate


class AufgabenService {
  type AufgabenListe = List[Aufgabe]

  def aufgabeHinzufuegen(aufgaben: AufgabenListe, titel: String, kategorie: Kategorie, faelligkeitsDatum: LocalDate): AufgabenListe = {
    val neueId = if (aufgaben.isEmpty) 1 else aufgaben.map(_.id).max + 1
    aufgaben :+ Aufgabe(neueId, titel, kategorie, faelligkeitsDatum)
  }

  def aufgabeAktualisieren(aufgaben: AufgabenListe, id: Int, updateFn: Aufgabe => Aufgabe): AufgabenListe = 
    aufgaben.map(aufgabe => if (aufgabe.id == id) updateFn(aufgabe) else aufgabe)

  def aufgabeLoeschen(aufgaben: AufgabenListe, id: Int): AufgabenListe = 
    aufgaben.filterNot(_.id == id)

  def filterNachKategorie(aufgaben: AufgabenListe, kategorieName: String): AufgabenListe =
    aufgaben.filter(aufgabe => kategorieNameStimmt(aufgabe.kategorie, kategorieName))

  def filterNachFaelligkeit(aufgaben: AufgabenListe, datum: LocalDate): AufgabenListe =
    aufgaben.filter(aufgabe => aufgabe.faelligkeitsDatum.isBefore(datum) || aufgabe.faelligkeitsDatum.isEqual(datum))

  private def kategorieNameStimmt(kat: Kategorie, name: String): Boolean =
    kat.name.equalsIgnoreCase(name) || kat.unterkategorien.exists(uk => kategorieNameStimmt(uk, name))
}