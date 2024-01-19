import org.example.domain.ListName
import org.example.domain.TodoItem
import org.example.domain.TodoList
import org.example.domain.User
import org.example.service.Todo
import org.http4k.client.JettyClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * Way of chapter 2
 */
class SeeATodoListAT {

    @Test
    fun `List owners can see their lists`() {
        val userName = "brad"
        val listName = "shopping"
        val foodToBuy = listOf("carrots", "apples", "milk")
        startApplication(userName, listName, foodToBuy)
        val list = getTodoList(userName, listName)
        expectThat(list.listName.name).isEqualTo(listName)
        expectThat(list.items.map(TodoItem::description)).isEqualTo(foodToBuy)
    }
}

fun startApplication(
    userName: String, listName: String, itemNames: List<String>
) {
    val todoList = TodoList(ListName(listName), itemNames.map(::TodoItem))
    val userMap = mapOf(User(userName) to listOf(todoList))
    val server = Todo(userMap).asServer(Jetty(9091)).start()
}

fun getTodoList(user: String, listName: String): TodoList {
    val client = JettyClient()
    val request = Request(Method.GET, "http://localhost:9091/todo/$user/$listName")
    val response = client(request)
    return if (response.status == Status.OK) parseResponse(response.bodyString())
    else fail(response.toMessage())
}

// API 응답인 HTML을 TodoList로 되돌린다.
private fun parseResponse(html: String): TodoList {
    val nameRegex = "<h2>.*<".toRegex()
    val listName = ListName(extractListName(nameRegex, html))
    val itemsRegex = "<td>.*?<".toRegex()
    val items = itemsRegex.findAll(html)
        .map { TodoItem(extractItemDesc(it)) }.toList()
    return TodoList(listName, items)
}

private fun extractListName(nameRegex: Regex, html: String): String =
    nameRegex.find(html)?.value
        ?.substringAfter("<h2>")
        ?.dropLast(1)
        .orEmpty()

private fun extractItemDesc(matchResult: MatchResult): String =
    matchResult.value.substringAfter("<td>").dropLast(1)

/**
 * Way of chapter 3.1
 */

private fun createTodo(userName: String, listName: String, itemNames: List<String>) =
    Todo(mapOf(User(userName) to listOf(TodoList(ListName(listName), itemNames.map(::TodoItem)))))

fun startApplication(todo: Todo) = todo.asServer(Jetty(9091)).start()
interface ScenarioActor {
    val name: String
}

class UserTodoListItems(override val name: String) : ScenarioActor {

    fun canGetItemsOfATodoList(userName: String, listName:String, itemNames: List<String>) {
        val expected = TodoList(ListName(listName), itemNames.map(::TodoItem))
        val actual = getTodoList(userName, listName)
        expectThat(actual).isEqualTo(expected)
    }
}

class TodoItemsTest {

    @Test
    fun `User gets the items of a todo list`() {
        val todo = createTodo("brad", "shopping", listOf("carrot","milk"))
        startApplication(todo)
        val brad = UserTodoListItems("brad")
        brad.canGetItemsOfATodoList("brad", "shopping", listOf("carrot","milk"))
    }
}
