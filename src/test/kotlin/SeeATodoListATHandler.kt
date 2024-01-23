import org.example.domain.ListName
import org.example.domain.TodoItem
import org.example.domain.TodoList
import org.example.domain.User
import org.example.service.TodoHandler
import org.http4k.client.JettyClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.junit.jupiter.api.Test

/**
 * Way of chapter 3.1
 */

fun startTodoFacade(lists: Map<User, List<TodoList>>): TodoFacade {
    val port = 8081
    val server = TodoHandler(lists).asServer(Jetty(port)).start()
    val client = ClientFilters.SetBaseUriFrom(Uri.of("http://localhost:$port/")).then(JettyClient())
    return TodoFacade(client, server)
}

class TodoHandlerItemsTest {

    @Test
    fun `User gets the items of a todo list`() {
        val userName = "brad"
        val listName = "shopping"
        val shoppingItems = listOf("carrot", "milk")
        val lists = createLists(userName, listName, shoppingItems)

        val brad = TodoListActor(userName)
        val app = startTodoFacade(lists)
        app.runScenario(
            brad.canGetItemsOfATodoList(listName, shoppingItems)
        )
    }

    private fun createLists(userName: String, listName: String, itemNames: List<String>) =
        mapOf(User(userName) to listOf(TodoList(ListName(listName), itemNames.map(::TodoItem))))
}
