/**
 * Datenmodell für die Kategorisierung von Aufgaben
 * 
 * Bietet eine flexible Struktur für:
 * - Hierarchische Organisation von Kategorien
 * - Haupt- und Unterkategorien
 * - Unveränderliche (immutable) Datenstruktur
 * 
 * Das Trait Kategorie definiert die Basis-Schnittstelle,
 * während EinfacheKategorie eine konkrete Implementierung bereitstellt.
 * Unterkategorien können beliebig tief verschachtelt werden.
 */
package modelle

sealed trait Kategorie {
  def name: String
  def unterkategorien: List[Kategorie]
  def mitUnterkategorien(neueUnterkategorien: List[Kategorie]): Kategorie
}

case class EinfacheKategorie(
  name: String, 
  unterkategorien: List[Kategorie] = Nil
) extends Kategorie {
  def mitUnterkategorien(neueUnterkategorien: List[Kategorie]): Kategorie = 
    copy(unterkategorien = neueUnterkategorien)
}