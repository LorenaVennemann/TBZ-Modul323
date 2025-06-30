/**
 * Benutzeroberfläche für die Todo-Listen Anwendung
 * 
 * Diese Klasse stellt die Konsolen-basierte Benutzeroberfläche bereit:
 * - Hauptmenü mit allen verfügbaren Optionen
 * - Eingabeaufforderungen und Benutzerinteraktion
 * - Anzeige von Aufgaben und Kategorien
 * - Fehlerbehandlung bei ungültigen Eingaben
 * 
 * Besonderheiten:
 * - Rekursives Menüsystem für Navigation
 * - Formatierte Ausgabe von Aufgaben und Kategorien
 * - Datumseingabe mit Validierung
 * - Unterstützung für Kategoriehierarchie
 */
package ui

import services.{KategorieService, AufgabenService}
import modelle.{Kategorie, Aufgabe}
import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import scala.annotation.tailrec

class KonsolenUI(kategorieService: KategorieService, aufgabenService: AufgabenService) {
  def zeileDrucken(s: String): Unit = println(s)
  
  def eingabeLesen(prompt: String): String = {
    print(prompt)
    scala.io.StdIn.readLine()
  }

  private def datumParsen(eingabe: String): Option[LocalDate] = {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    try Some(LocalDate.parse(eingabe, format))
    catch {
      case _: DateTimeParseException => None
    }
  }

  private def aufgabeAnzeigen(aufgabe: Aufgabe): String = 
    s"ID: ${aufgabe.id} | ${aufgabe.titel} | Kategorie: ${aufgabe.kategorie.name} | " +
    s"Fällig am: ${aufgabe.faelligkeitsDatum} | Erledigt: ${if (aufgabe.erledigt) "Ja" else "Nein"}"

  private def kategorieAnzeigen(kat: Kategorie, einrueckung: String = ""): String = {
    val unterKats = if (kat.unterkategorien.isEmpty) "" 
                   else kat.unterkategorien.map(uk => kategorieAnzeigen(uk, einrueckung + "  ")).mkString("\n")
    s"$einrueckung- ${kat.name}" + (if (unterKats.isEmpty) "" else "\n" + unterKats)
  }

  @tailrec
  final def hauptMenu(aufgaben: List[Aufgabe] = Nil, kategorien: List[Kategorie] = Nil): Unit = {
    zeileDrucken(
      """
        |--- TODO LIST APP ---
        |1) Aufgabe hinzufügen
        |2) Aufgaben anzeigen
        |3) Aufgabe bearbeiten
        |4) Aufgabe löschen
        |5) Kategorien anzeigen
        |6) Kategorie hinzufügen
        |7) Kategorie löschen
        |8) Aufgaben filtern
        |9) Beenden
        |---------------------
      """.stripMargin)
    
    val auswahl = eingabeLesen("Wähle Option (1-9): ").trim

    auswahl match {
      case "1" =>
        val titel = eingabeLesen("Titel der Aufgabe: ").trim
        if (kategorien.isEmpty) {
          zeileDrucken("Keine Kategorien vorhanden, bitte zuerst Kategorien anlegen.")
          hauptMenu(aufgaben, kategorien)
        } else {
          zeileDrucken("Verfügbare Kategorien:")
          kategorien.foreach(kat => zeileDrucken(kategorieAnzeigen(kat)))
          val katName = eingabeLesen("Kategorie wählen: ").trim
          kategorieService.kategorieFindenNachName(kategorien, katName) match {
            case None => 
              zeileDrucken("Kategorie nicht gefunden.")
              hauptMenu(aufgaben, kategorien)
            case Some(kat) =>
              val datumStr = eingabeLesen("Fälligkeitsdatum (yyyy-MM-dd): ")
              datumParsen(datumStr) match {
                case None => 
                  zeileDrucken("Ungültiges Datum.")
                  hauptMenu(aufgaben, kategorien)
                case Some(datum) =>
                  val neueAufgaben = aufgabenService.aufgabeHinzufuegen(aufgaben, titel, kat, datum)
                  zeileDrucken("Aufgabe angelegt.")
                  hauptMenu(neueAufgaben, kategorien)
              }
          }
        }

      case "2" =>
        if (aufgaben.isEmpty) zeileDrucken("Keine Aufgaben vorhanden.")
        else aufgaben.foreach(a => zeileDrucken(aufgabeAnzeigen(a)))
        hauptMenu(aufgaben, kategorien)

      case "3" =>
        val idStr = eingabeLesen("ID der zu ändernden Aufgabe: ")
        idStr.toIntOption match {
          case None => 
            zeileDrucken("Ungültige ID.")
            hauptMenu(aufgaben, kategorien)
          case Some(id) =>
            aufgaben.find(_.id == id) match {
              case None => 
                zeileDrucken("Aufgabe nicht gefunden.")
                hauptMenu(aufgaben, kategorien)
              case Some(aufgabe) =>
                val neuerTitel = eingabeLesen(s"Neuer Titel (${aufgabe.titel}), Enter=keine Änderung: ").trim
                val erledigtStr = eingabeLesen(s"Erledigt? (ja/nein, aktuell: ${if(aufgabe.erledigt) "ja" else "nein"}): ").trim.toLowerCase
                val neuErledigt = erledigtStr match {
                  case "ja" => true
                  case "nein" => false
                  case _ => aufgabe.erledigt
                }

                val neueKategorie = if (eingabeLesen("Kategorie ändern? (ja/nein): ").trim.toLowerCase == "ja") {
                  zeileDrucken("Verfügbare Kategorien:")
                  kategorien.foreach(k => zeileDrucken(kategorieAnzeigen(k)))
                  val neuerKatName = eingabeLesen("Neue Kategorie: ").trim
                  kategorieService.kategorieFindenNachName(kategorien, neuerKatName).getOrElse(aufgabe.kategorie)
                } else aufgabe.kategorie

                val neuesDatumStr = eingabeLesen(s"Neues Fälligkeitsdatum (yyyy-MM-dd), aktuell ${aufgabe.faelligkeitsDatum}, Enter=keine Änderung: ").trim
                val neuesDatum = if (neuesDatumStr.isEmpty) aufgabe.faelligkeitsDatum 
                                else datumParsen(neuesDatumStr).getOrElse(aufgabe.faelligkeitsDatum)

                val aktualisierteAufgaben = aufgabenService.aufgabeAktualisieren(
                  aufgaben, 
                  id,
                  _ => aufgabe.copy(
                    titel = if (neuerTitel.isEmpty) aufgabe.titel else neuerTitel,
                    erledigt = neuErledigt,
                    kategorie = neueKategorie,
                    faelligkeitsDatum = neuesDatum
                  )
                )
                zeileDrucken("Aufgabe aktualisiert.")
                hauptMenu(aktualisierteAufgaben, kategorien)
            }
        }

      case "4" =>
        val idStr = eingabeLesen("ID der zu löschenden Aufgabe: ")
        idStr.toIntOption match {
          case None => 
            zeileDrucken("Ungültige ID.")
            hauptMenu(aufgaben, kategorien)
          case Some(id) =>
            if (aufgaben.exists(_.id == id)) {
              val neueAufgaben = aufgabenService.aufgabeLoeschen(aufgaben, id)
              zeileDrucken("Aufgabe gelöscht.")
              hauptMenu(neueAufgaben, kategorien)
            } else {
              zeileDrucken("Aufgabe nicht gefunden.")
              hauptMenu(aufgaben, kategorien)
            }
        }

      case "5" =>
        if (kategorien.isEmpty) zeileDrucken("Keine Kategorien vorhanden.")
        else kategorien.foreach(kat => zeileDrucken(kategorieAnzeigen(kat)))
        hauptMenu(aufgaben, kategorien)

      case "6" =>
        zeileDrucken(
          """
            |Kategorie hinzufügen:
            |1) Neue Hauptkategorie
            |2) Neue Unterkategorie
            |3) Zurück
          """.stripMargin)
        
        eingabeLesen("Option wählen (1-3): ").trim match {
          case "1" =>
            val name = eingabeLesen("Name der neuen Hauptkategorie: ").trim
            if (name.isEmpty) {
              zeileDrucken("Name darf nicht leer sein.")
              hauptMenu(aufgaben, kategorien)
            } else {
              val neueKategorien = kategorieService.kategorieHinzufuegen(kategorien, name)
              zeileDrucken("Hauptkategorie angelegt.")
              hauptMenu(aufgaben, neueKategorien)
            }
          
          case "2" =>
            if (kategorien.isEmpty) {
              zeileDrucken("Keine Hauptkategorien vorhanden. Bitte erst Hauptkategorie anlegen.")
              hauptMenu(aufgaben, kategorien)
            } else {
              zeileDrucken("Verfügbare Kategorien:")
              kategorien.foreach(kat => zeileDrucken(kategorieAnzeigen(kat)))
              val elternName = eingabeLesen("Name der Hauptkategorie für neue Unterkategorie: ").trim
              val name = eingabeLesen("Name der neuen Unterkategorie: ").trim
              if (name.isEmpty || elternName.isEmpty) {
                zeileDrucken("Name darf nicht leer sein.")
                hauptMenu(aufgaben, kategorien)
              } else {
                val neueKategorien = kategorieService.kategorieHinzufuegen(kategorien, name, Some(elternName))
                if (neueKategorien == kategorien) {
                  zeileDrucken("Hauptkategorie nicht gefunden oder Unterkategorie existiert bereits.")
                } else {
                  zeileDrucken("Unterkategorie angelegt.")
                }
                hauptMenu(aufgaben, neueKategorien)
              }
            }
          
          case _ => hauptMenu(aufgaben, kategorien)
        }

       case "7" =>
      val name = eingabeLesen("Name der zu löschenden Kategorie: ").trim
      if (kategorieService.kategorieFindenNachName(kategorien, name).isDefined) { 
        val neueKategorien = kategorieService.kategorieLoeschen(kategorien, name)
        val neueAufgaben = aufgaben.filterNot(a => a.kategorie.name.equalsIgnoreCase(name))  
        zeileDrucken("Kategorie und zugehörige Aufgaben gelöscht.")
        hauptMenu(neueAufgaben, neueKategorien)
      } else {
        zeileDrucken("Kategorie nicht gefunden.")
        hauptMenu(aufgaben, kategorien)
      }

      case "8" =>
        zeileDrucken(
          """
            |Filteroptionen:
            |1) Nach Kategorie filtern
            |2) Nach Fälligkeit filtern
            |3) Zurück zum Hauptmenü
          """.stripMargin)
        eingabeLesen("Option: ") match {
          case "1" =>
            val katName = eingabeLesen("Kategorie Name: ").trim
            val gefiltert = aufgabenService.filterNachKategorie(aufgaben, katName)
            if (gefiltert.isEmpty) zeileDrucken("Keine Aufgaben gefunden.")
            else gefiltert.foreach(a => zeileDrucken(aufgabeAnzeigen(a)))
            hauptMenu(aufgaben, kategorien)
          
          case "2" =>
            val datumStr = eingabeLesen("Fälligkeitsdatum (yyyy-MM-dd): ").trim
            datumParsen(datumStr) match {
              case None => 
                zeileDrucken("Ungültiges Datum.")
                hauptMenu(aufgaben, kategorien)
              case Some(datum) =>
                val gefiltert = aufgabenService.filterNachFaelligkeit(aufgaben, datum)
                if(gefiltert.isEmpty) zeileDrucken("Keine Aufgaben gefunden.")
                else gefiltert.foreach(a => zeileDrucken(aufgabeAnzeigen(a)))
                hauptMenu(aufgaben, kategorien)
            }
          
          case _ => hauptMenu(aufgaben, kategorien)
        }

      case "9" =>
        zeileDrucken("Programm beendet. Auf Wiedersehen!")

      case _ =>
        zeileDrucken("Ungültige Option.")
        hauptMenu(aufgaben, kategorien)
    }
  }
}