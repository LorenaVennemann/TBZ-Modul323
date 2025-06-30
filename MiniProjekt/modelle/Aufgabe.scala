/**
 * Datenmodell für eine einzelne Aufgabe in der Todo-Liste
 * 
 * Repräsentiert eine Aufgabe mit:
 * - Eindeutiger ID zur Identifikation
 * - Titel als Beschreibung
 * - Zuordnung zu einer Kategorie
 * - Fälligkeitsdatum
 * - Status (erledigt/nicht erledigt)
 * 
 * Die Klasse ist als unveränderlicher (immutable) Case Class implementiert.
 * Das Companion Object bietet eine Hilfsmethode zur vereinfachten Erstellung.
 */
package modelle

import java.time.LocalDate

case class Aufgabe(
  id: Int,                     
  titel: String,               
  kategorie: Kategorie,        
  faelligkeitsDatum: LocalDate,
  erledigt: Boolean = false    
)

object Aufgabe {
  def neu(
    id: Int, 
    titel: String, 
    kategorie: Kategorie, 
    faelligkeitsDatum: LocalDate
  ): Aufgabe = Aufgabe(id, titel, kategorie, faelligkeitsDatum)
}