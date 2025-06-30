/**
 * Service-Klasse für die Verwaltung der Kategorien in der Todo-Liste
 * 
 * Diese Klasse stellt alle Operationen für die Kategorieverwaltung bereit:
 * - Hinzufügen neuer Kategorien (Haupt- und Unterkategorien)
 * - Löschen von Kategorien (inklusive Unterkategorien)
 * - Suchen von Kategorien nach Namen
 * 
 * Besonderheiten:
 * - Unterstützt eine hierarchische Kategoriestruktur
 * - Alle Operationen sind case-insensitive
 * - Verwendet unveränderliche Datenstrukturen (immutable)
 * - Verhindert Duplikate auf gleicher Ebene
 */
package services

import modelle._

class KategorieService {
  type KategorieListe = List[Kategorie]

  def kategorieHinzufuegen(kategorien: KategorieListe, name: String, elternName: Option[String] = None): KategorieListe = 
    elternName match {
      case None =>
        if (kategorien.exists(_.name == name)) kategorien
        else kategorien :+ EinfacheKategorie(name)
      case Some(eltern) =>
        kategorien.map {
          case k: EinfacheKategorie if k.name.equalsIgnoreCase(eltern) =>
            k.mitUnterkategorien(
              if (k.unterkategorien.exists(_.name == name)) k.unterkategorien 
              else k.unterkategorien :+ EinfacheKategorie(name)
            )
          case k => k
        }
    }

  def kategorieLoeschen(kategorien: KategorieListe, name: String): KategorieListe = 
    kategorien.filterNot(_.name.equalsIgnoreCase(name)).map { k =>
      k.mitUnterkategorien(kategorieLoeschen(k.unterkategorien, name))
    }

  def kategorieFindenNachName(kategorien: KategorieListe, name: String): Option[Kategorie] = {
    def suchen(kats: List[Kategorie]): Option[Kategorie] = kats match {
      case Nil => None
      case kopf :: rest => 
        if (kopf.name.equalsIgnoreCase(name)) Some(kopf)
        else suchen(kopf.unterkategorien) match {
          case Some(gefunden) => Some(gefunden)
          case None => suchen(rest)
        }
    }
    suchen(kategorien)
  }
}