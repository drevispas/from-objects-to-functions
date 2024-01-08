import org.example.domain.ListName
import org.example.domain.TodoItem
import org.example.service.Todo
import org.example.domain.TodoList
import org.example.domain.User
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

class SeeATodoListAT {

    @Test
    fun `List owners can see their lists`() {
        val user="brad"
        val listName="shopping"
        val foodToBuy=listOf("carrots","apples","milk")
        startApplication(user,listName,foodToBuy)
        val list=getTodoList(user,listName)
        expectThat(list.listName.name).isEqualTo(listName)
        expectThat(list.items.map(TodoItem::description)).isEqualTo(foodToBuy)
    }
}

fun startApplication(
    user:String, listName:String, items:List<String>
) {
    val todoList = TodoList(ListName(listName), items.map(::TodoItem))
    val lists = mapOf(User(user) to listOf(todoList))
    val server = Todo(lists).asServer(Jetty(9091)).start()
}

fun getTodoList(user:String,listName: String): TodoList {
    val client=JettyClient()
    val request=Request(Method.GET, "http://localhost:9091/todo/$user/$listName")
    val response = client(request)
    return if (response.status== Status.OK) parseResponse(response.bodyString())
    else fail(response.toMessage())
}

// API 응답인 HTML을 TodoList로 되돌린다.
private fun parseResponse(html: String): TodoList {
    val nameRegex = "<h2>.*<".toRegex()
    val listName = ListName(extractListName(nameRegex, html))
    val itemsRegex = "<td>.*?<".toRegex()
    val items = itemsRegex.findAll(html)
        .map { TodoItem(extractItemDesc(it)) }.toList()
    return TodoList(listName,items)
}
private fun extractListName(nameRegex: Regex, html: String): String =
    nameRegex.find(html)?.value
        ?.substringAfter("<h2>")
        ?.dropLast(1)
        .orEmpty()
private fun extractItemDesc(matchResult: MatchResult): String =
    matchResult.value.substringAfter("<td>").dropLast(1)
