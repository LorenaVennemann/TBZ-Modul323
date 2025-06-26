import scala.annotation.tailrec
import scala.io.StdIn.readLine
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// Data models (immutable)
sealed trait Category {
  def name: String
  def subcategories: List[Category]
  def withSubcategories(newSubcategories: List[Category]): Category
}

case class SimpleCategory(name: String, subcategories: List[Category] = Nil) extends Category {
  def withSubcategories(newSubcategories: List[Category]): Category = 
    copy(subcategories = newSubcategories)
}

case class TodoItem(
  id: Int,
  title: String,
  category: Category,
  deadline: LocalDate,
  done: Boolean = false
)

object TodoApp extends App {
  type TodoList = List[TodoItem]
  type CategoryList = List[Category]

  // Central IO functions
  def printLine(s: String): Unit = println(s)
  def readInput(prompt: String): String = {
    print(prompt)
    readLine()
  }

  // Pure functions for category manipulation
  def addCategory(categories: CategoryList, name: String, parentName: Option[String] = None): CategoryList = 
    parentName match {
      case None =>
        if (categories.exists(_.name == name)) categories
        else categories :+ SimpleCategory(name)
      case Some(parent) =>
        categories.map {
          case c: SimpleCategory if c.name == parent =>
            c.withSubcategories(
              if (c.subcategories.exists(_.name == name)) c.subcategories 
              else c.subcategories :+ SimpleCategory(name)
            )
          case c => c
        }
    }

  def deleteCategory(categories: CategoryList, name: String): CategoryList = 
    categories.filterNot(_.name == name).map { c =>
      c.withSubcategories(deleteCategory(c.subcategories, name))
    }

  def findCategory(categories: CategoryList, name: String): Option[Category] = {
    def find(cats: List[Category]): Option[Category] = cats match {
      case Nil => None
      case head :: tail => 
        if (head.name == name) Some(head)
        else find(head.subcategories) match {
          case Some(found) => Some(found)
          case None => find(tail)
        }
    }
    find(categories)
  }

  // Pure functions for todo manipulation
  def addTodo(todos: TodoList, title: String, category: Category, deadline: LocalDate): TodoList = {
    val newId = if (todos.isEmpty) 1 else todos.map(_.id).max + 1
    todos :+ TodoItem(newId, title, category, deadline)
  }

  def updateTodo(todos: TodoList, id: Int, updateFn: TodoItem => TodoItem): TodoList = 
    todos.map(todo => if (todo.id == id) updateFn(todo) else todo)

  def deleteTodo(todos: TodoList, id: Int): TodoList = 
    todos.filterNot(_.id == id)

  // Filtering with HOFs
  def filterTodosByCategory(todos: TodoList, categoryName: String): TodoList =
    todos.filter(todo => categoryNameMatch(todo.category, categoryName))

  def filterTodosByDeadline(todos: TodoList, deadline: LocalDate): TodoList =
    todos.filter(todo => todo.deadline.isBefore(deadline) || todo.deadline.isEqual(deadline))

  // Helper functions
  def categoryNameMatch(cat: Category, name: String): Boolean =
    cat.name == name || cat.subcategories.exists(sc => categoryNameMatch(sc, name))

  def parseDate(input: String): Option[LocalDate] = {
    val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    try Some(LocalDate.parse(input, fmt))
    catch {
      case _: DateTimeParseException => None
    }
  }

  def showTodo(todo: TodoItem): String = 
    s"ID: ${todo.id} | ${todo.title} | Kategorie: ${todo.category.name} | Deadline: ${todo.deadline} | Erledigt: ${if (todo.done) "Ja" else "Nein"}"

  def showCategory(cat: Category, indent: String = ""): String = {
    val subcats = if (cat.subcategories.isEmpty) "" 
                 else cat.subcategories.map(sc => showCategory(sc, indent + "  ")).mkString("\n")
    s"$indent- ${cat.name}" + (if (subcats.isEmpty) "" else "\n" + subcats)
  }

  // Funktionen zur Userinteraktion (rein in der IO-Schicht)

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
        |8) Nach Aufgaben filtern
        |9) Beenden
        |---------------------
      """.stripMargin)
    val choice = readInput("Wähle Option (1-9): ").trim

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
                // Für Kategorieänderung Option anbieten
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
          // Auch Tasks löschen, die diese Kategorie bzw. Unterkategorien haben
          val keepTodos = todos.filterNot(t => categoryNameMatch(t.category, name))
          printLine("Kategorie und zugehörige Aufgaben gelöscht.")
          mainMenu(keepTodos, newCats)
        } else {
          printLine("Kategorie nicht gefunden.")
          mainMenu(todos, categories)
        }

      case "8" =>
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

      case "9" =>
        printLine("Programm beendet. Auf Wiedersehen!")

      case _ =>
        printLine("Ungültige Option.")
        mainMenu(todos, categories)
    }
  }

  val initialCategories: CategoryList = List(
    SimpleCategory("Privat", List(SimpleCategory("Familie"), SimpleCategory("Freunde"))),
    SimpleCategory("Arbeit", List(SimpleCategory("Projekt A"), SimpleCategory("Projekt B")))
  )
  val initialTodos: TodoList = Nil

  mainMenu(initialTodos, initialCategories)
}

