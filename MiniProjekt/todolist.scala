import scala.annotation.tailrec
import scala.io.StdIn.readLine
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Data models
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
}