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
import services._
import modelle._
import ui.KonsolenUI

object TodoApp extends App {
  val kategorieService = new KategorieService()
  val aufgabenService = new AufgabenService()
  val ui = new KonsolenUI(kategorieService, aufgabenService)

  val initialKategorien = List(
    EinfacheKategorie("Privat", List(
      EinfacheKategorie("Familie"), 
      EinfacheKategorie("Freunde")
    )),
    EinfacheKategorie("Arbeit", List(
      EinfacheKategorie("Projekt A"), 
      EinfacheKategorie("Projekt B")
    ))
  )

  ui.hauptMenu(kategorien = initialKategorien)
}