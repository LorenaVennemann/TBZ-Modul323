import scala.annotation.tailrec
import java.time.LocalDate

sealed trait Category {
  def name: String
  def subcategories: List[Category]
}

case class SimpleCategory(name: String, subcategories: List[Category] = Nil) extends Category

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