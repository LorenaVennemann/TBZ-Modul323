import scala.annotation.tailrec
import scala.io.StdIn.readLine
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// Datenmodelle (unveränderlich)
sealed trait Category {
  def name: String
  def subcategories: List[Category]
  // Methode, um Unterkategorien zu ersetzen (für Immutabilität)
  def withSubcategories(newSubcategories: List[Category]): Category
}

case class SimpleCategory(name: String, subcategories: List[Category] = Nil) extends Category {
  def withSubcategories(newSubcategories: List[Category]): Category = 
    copy(subcategories = newSubcategories)
}

// Aufgaben-Item mit ID, Titel, Kategorie, Fälligkeitsdatum und Erledigt-Status
case class TodoItem(
  id: Int,
  title: String,
  category: Category,
  deadline: LocalDate,
  done: Boolean = false
)

object TodoApp extends App {
  // Typalias für bessere Lesbarkeit
  type TodoList = List[TodoItem]
  type CategoryList = List[Category]

  // Zentrale Ein-/Ausgabefunktionen für Konsole
  def printLine(s: String): Unit = println(s)
  def readInput(prompt: String): String = {
    print(prompt)           // Eingabeaufforderung anzeigen
    readLine()              // Zeile vom Benutzer lesen
  }

  // Reine Funktionen zur Bearbeitung von Kategorien

  // Kategorie hinzufügen,
  // optional auch als Unterkategorie einer vorhandenen
  def addCategory(categories: CategoryList, name: String, parentName: Option[String] = None): CategoryList = 
    parentName match {
      case None => // Oberste Ebene: falls Name noch nicht existiert, hinzufügen
        if (categories.exists(_.name == name)) categories
        else categories :+ SimpleCategory(name)
      case Some(parent) => // Unterkategorie hinzufügen, wenn Eltern-Kategorie gefunden wird
        categories.map {
          case c: SimpleCategory if c.name == parent =>
            c.withSubcategories(
              if (c.subcategories.exists(_.name == name)) c.subcategories 
              else c.subcategories :+ SimpleCategory(name)
            )
          case c => c // andere Kategorien bleiben unverändert
        }
    }

  // Kategorie (inkl. Unterkategorien) löschen
  def deleteCategory(categories: CategoryList, name: String): CategoryList = 
    categories.filterNot(_.name == name).map { c =>
      c.withSubcategories(deleteCategory(c.subcategories, name)) // rekursiv Unterkategorien prüfen
    }

  // Rekursive Suche nach Kategorie anhand des Namens
  def findCategory(categories: CategoryList, name: String): Option[Category] = {
    def find(cats: List[Category]): Option[Category] = cats match {
      case Nil => None             // Nichts mehr da? None zurück
      case head :: tail => 
        if (head.name == name) Some(head)                      // Treffer
        else find(head.subcategories) match {                   // sonst in Unterkategorien suchen
          case Some(found) => Some(found)
          case None => find(tail)                                // sonst in restlichen Kategorien suchen
        }
    }
    find(categories)
  }

  // Zusätzliche Funktion zum Hinzufügen von Unter-Unterkategorien (rekursiv)
  def addSubcategoryRec(categories: CategoryList, parentName: String, subcatName: String): CategoryList = {
    categories.map {
      case c if c.name == parentName =>
        // Wenn Eltern gefunden, Unterkategorie anhängen, wenn nicht vorhanden
        val updatedSubs = 
          if (c.subcategories.exists(_.name == subcatName)) c.subcategories
          else c.subcategories :+ SimpleCategory(subcatName)
        c.withSubcategories(updatedSubs)
      case c =>
        // Rekursiv auch in Unterkategorien suchen und hinzufügen
        c.withSubcategories(addSubcategoryRec(c.subcategories, parentName, subcatName))
    }
  }

  // Reine Funktionen zur Bearbeitung von Aufgaben (Todos)

  // Neue Aufgabe hinzufügen mit automatischer ID-Vergabe
  def addTodo(todos: TodoList, title: String, category: Category, deadline: LocalDate): TodoList = {
    val newId = if (todos.isEmpty) 1 else todos.map(_.id).max + 1  // größte ID + 1
    todos :+ TodoItem(newId, title, category, deadline)
  }

  // Aufgabe aktualisieren anhand der ID, mit Update-Funktion
  def updateTodo(todos: TodoList, id: Int, updateFn: TodoItem => TodoItem): TodoList = 
    todos.map(todo => if (todo.id == id) updateFn(todo) else todo)

  // Aufgabe löschen anhand der ID
  def deleteTodo(todos: TodoList, id: Int): TodoList = 
    todos.filterNot(_.id == id)

  // Filterfunktionen mit Higher-Order-Functions

  // Aufgaben filtern nach Kategorie (inkl. Unterkategorien)
  def filterTodosByCategory(todos: TodoList, categoryName: String): TodoList =
    todos.filter(todo => categoryNameMatch(todo.category, categoryName))

  // Aufgaben filtern nach Fälligkeitsdatum bis inkl. deadline
  def filterTodosByDeadline(todos: TodoList, deadline: LocalDate): TodoList =
    todos.filter(todo => todo.deadline.isBefore(deadline) || todo.deadline.isEqual(deadline))

  // Hilfsfunktion: Prüft ob Kategorie oder Unterkategorie mit Namen übereinstimmt
  def categoryNameMatch(cat: Category, name: String): Boolean =
    cat.name == name || cat.subcategories.exists(sc => categoryNameMatch(sc, name))

  // Hilfsfunktion: Datum aus String parsen, Format: yyyy-MM-dd
  def parseDate(input: String): Option[LocalDate] = {
    val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    try Some(LocalDate.parse(input, fmt))
    catch {
      case _: DateTimeParseException => None // falls falsch formatiert
    }
  }

  // Ausgabeformat für eine Aufgabe
  def showTodo(todo: TodoItem): String = 
    s"ID: ${todo.id} | ${todo.title} | Kategorie: ${todo.category.name} | Deadline: ${todo.deadline} | Erledigt: ${if (todo.done) "Ja" else "Nein"}"

  // Ausgabe einer Kategorie mit eingerückten Unterkategorien
  def showCategory(cat: Category, indent: String = ""): String = {
    val subcats = if (cat.subcategories.isEmpty) "" 
                 else cat.subcategories.map(sc => showCategory(sc, indent + "  ")).mkString("\n")
    s"$indent- ${cat.name}" + (if (subcats.isEmpty) "" else "\n" + subcats)
  }

  // Funktionen für die Benutzer-Eingabe und Menüführung

  @tailrec
  def mainMenu(todos: TodoList, categories: CategoryList): Unit = {
    printLine(
      """
        |--- TODO LIST APP ---
        |1) Aufgaben anlegen
        |2) Aufgaben anzeigen
        |3) Aufgabe bearbeiten
        |4) Aufgabe löschen
        |5) Kategorien anzeigen
        |6) Kategorie anlegen
        |7) Kategorie löschen
        |8) Unterkategorie anlegen  
        |9) Nach Aufgaben filtern
        |10) Beenden
        |---------------------
      """.stripMargin)
    val choice = readInput("Wähle Option (1-10): ").trim

    choice match {
      case "1" =>
        val title = readInput("Titel der Aufgabe: ").trim
        if (categories.isEmpty) {
          printLine("Keine Kategorien vorhanden, bitte zuerst Kategorien anlegen.")
          mainMenu(todos, categories)
        } else {
          printLine("Kategorien vorhanden:")
          categories.foreach(cat => printLine(showCategory(cat)))
          val catName = readInput("Kategorie wählen: ").trim
          findCategory(categories, catName) match {
            case None => printLine("Kategorie nicht gefunden."); mainMenu(todos, categories)
            case Some(cat) =>
              val deadlineStr = readInput("Deadline (yyyy-MM-dd): ")
              parseDate(deadlineStr) match {
                case None => printLine("Ungültiges Datum."); mainMenu(todos, categories)
                case Some(date) =>
                  val newTodos = addTodo(todos, title, cat, date)
                  printLine("Aufgabe angelegt."); mainMenu(newTodos, categories)
              }
          }
        }

      case "2" =>
        if (todos.isEmpty) printLine("Keine Aufgaben vorhanden.")
        else todos.foreach(t => printLine(showTodo(t)))
        mainMenu(todos, categories)

      case "3" =>
        val idStr = readInput("ID der zu ändernden Aufgabe: ")
        idStr.toIntOption match {
          case None => printLine("Ungültige ID."); mainMenu(todos, categories)
          case Some(id) =>
            todos.find(_.id == id) match {
              case None => printLine("Aufgabe nicht gefunden."); mainMenu(todos, categories)
              case Some(todo) =>
                val newTitle = readInput(s"Neuer Titel (${todo.title}), Enter=kein Wechsel: ").trim
                val newDoneStr = readInput(s"Erledigt? (ja/nein, aktuell: ${if(todo.done) "ja" else "nein"}): ").trim.toLowerCase
                val changedDone = newDoneStr match {
                  case "ja" => true
                  case "nein" => false
                  case _ => todo.done
                }
                // Option zum Ändern der Kategorie
                val changeCat = readInput("Kategorie ändern? (ja/nein): ").trim.toLowerCase
                val newCategory = if (changeCat == "ja") {
                  printLine("Verfügbare Kategorien:")
                  categories.foreach(c => printLine(showCategory(c)))
                  val newCatName = readInput("Neue Kategorie: ").trim
                  findCategory(categories, newCatName) match {
                    case None => printLine("Kategorie nicht gefunden. Behalte alte Kategorie."); todo.category
                    case Some(cat) => cat
                  }
                } else todo.category

                val newDeadlineStr = readInput(s"Neue Deadline (yyyy-MM-dd), aktuell ${todo.deadline}, Enter=kein Wechsel: ").trim
                val newDeadline = if (newDeadlineStr.isEmpty) todo.deadline else parseDate(newDeadlineStr).getOrElse {
                  printLine("Ungültiges Datum, behalte altes."); todo.deadline
                }

                val updatedTodos = updateTodo(todos, id, _ => todo.copy(
                  title = if (newTitle.isEmpty) todo.title else newTitle,
                  done = changedDone,
                  category = newCategory,
                  deadline = newDeadline
                ))
                printLine("Aufgabe aktualisiert.")
                mainMenu(updatedTodos, categories)
            }
        }

      case "4" =>
        val idStr = readInput("ID der zu löschenden Aufgabe: ")
        idStr.toIntOption match {
          case None => printLine("Ungültige ID."); mainMenu(todos, categories)
          case Some(id) =>
            if (todos.exists(_.id == id)) {
              val filtered = deleteTodo(todos, id)
              printLine("Aufgabe gelöscht."); mainMenu(filtered, categories)
            } else {
              printLine("Aufgabe nicht gefunden."); mainMenu(todos, categories)
            }
        }

      case "5" =>
        if (categories.isEmpty) printLine("Keine Kategorien vorhanden.")
        else categories.foreach(cat => printLine(showCategory(cat)))
        mainMenu(todos, categories)

      case "6" =>
        val name = readInput("Name der neuen Kategorie: ").trim
        if (name.isEmpty) {
          printLine("Name kann nicht leer sein.")
          mainMenu(todos, categories)
        } else {
          val parent = readInput("Unterkategorie zu (Enter leer für oberste Ebene): ").trim match {
            case "" => None
            case p => Some(p)
          }
          val newCats = addCategory(categories, name, parent)
          printLine("Kategorie angelegt.")
          mainMenu(todos, newCats)
        }

      case "7" =>
        val name = readInput("Name der zu löschenden Kategorie: ").trim
        if (categories.exists(_.name == name)) {
          val newCats = deleteCategory(categories, name)
          // Auch Aufgaben löschen, die in dieser Kategorie oder Unterkategorien sind
          val keepTodos = todos.filterNot(t => categoryNameMatch(t.category, name))
          printLine("Kategorie und zugehörige Aufgaben gelöscht.")
          mainMenu(keepTodos, newCats)
        } else {
          printLine("Kategorie nicht gefunden.")
          mainMenu(todos, categories)
        }

      case "8" =>
        // Neu: Unterkategorie hinzufügen
        if (categories.isEmpty) {
          printLine("Keine Kategorien vorhanden, bitte zuerst Kategorien anlegen.")
          mainMenu(todos, categories)
        } else {
          printLine("Verfügbare Kategorien:")
          categories.foreach(cat => printLine(showCategory(cat)))
          val parentName = readInput("Bitte Name der Kategorie eingeben, unter der eine Unterkategorie angelegt werden soll: ").trim
          findCategory(categories, parentName) match {
            case None =>
              printLine("Kategorie nicht gefunden.")
              mainMenu(todos, categories)
            case Some(_) =>
              val subcatName = readInput("Name der neuen Unterkategorie: ").trim
              if (subcatName.isEmpty) {
                printLine("Name der Unterkategorie darf nicht leer sein.")
                mainMenu(todos, categories)
              } else {
                val newCats = addSubcategoryRec(categories, parentName, subcatName)
                printLine(s"Unterkategorie '$subcatName' unter '$parentName' angelegt.")
                mainMenu(todos, newCats)
              }
          }
        }

      case "9" =>
        printLine(
          """
            |Filteroptionen:
            |1) Nach Kategorie filtern
            |2) Nach Deadline filtern
            |3) Zurück zum Menü
          """.stripMargin)
        readInput("Option: ") match {
          case "1" =>
            val cname = readInput("Kategorie Name: ").trim
            val filtered = filterTodosByCategory(todos, cname)
            if (filtered.isEmpty) printLine("Keine Aufgaben gefunden.")
            else filtered.foreach(t => printLine(showTodo(t)))
            mainMenu(todos, categories)
          case "2" =>
            val dlStr = readInput("Deadline (yyyy-MM-dd): ").trim
            parseDate(dlStr) match {
              case None => printLine("Ungültiges Datum."); mainMenu(todos, categories)
              case Some(dl) =>
                val filtered = filterTodosByDeadline(todos, dl)
                if(filtered.isEmpty) printLine("Keine Aufgaben gefunden.")
                else filtered.foreach(t => printLine(showTodo(t)))
                mainMenu(todos, categories)
            }
          case _ =>
            mainMenu(todos, categories)
        }

      case "10" =>
        printLine("Programm beendet. Auf Wiedersehen!")

      case _ =>
        printLine("Ungültige Option.")
        mainMenu(todos, categories)
    }
  }

  // Anfangsdaten mit Kategorien und leeren Aufgaben
  val initialCategories: CategoryList = List(
    SimpleCategory("Privat", List(SimpleCategory("Familie"), SimpleCategory("Freunde"))),
    SimpleCategory("Arbeit", List(SimpleCategory("Projekt A"), SimpleCategory("Projekt B")))
  )
  val initialTodos: TodoList = Nil

  // Programmstart mit Hauptmenü
  mainMenu(initialTodos, initialCategories)
}