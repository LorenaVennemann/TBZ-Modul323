/**
 * Hauptanwendung für die Todo-Listen App
 * 
 * Diese Klasse ist der Einstiegspunkt der Anwendung und initialisiert:
 * - Die benötigten Services für Kategorien und Aufgaben
 * - Die Konsolen-basierte Benutzeroberfläche
 * - Eine initiale Hierarchie von Kategorien
 * 
 * Besonderheiten:
 * - Verwendung des App-Traits für einfachen Programmstart
 * - Vordefinierte Kategorie-Hierarchie für Benutzerfreundlichkeit
 * - Trennung von UI und Geschäftslogik durch Services
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