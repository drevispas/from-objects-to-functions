import org.example.domain.ListName
import org.example.domain.TodoItem
import org.example.domain.TodoList
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.fail

interface TodoActions {

    fun getTodoList(userName: String, listName: String): TodoList?
}

typealias Step = TodoActions.()->Unit

class TodoFacade(val client: HttpHandler, val server: AutoCloseable): TodoActions {

    fun runScenario(vararg steps: Step) {
        server.use {
            // this: TodoActions
            steps.forEach { step -> this.step() }
        }
    }

    // Call server  "/todo/{user}/{list}" using client
    override fun getTodoList(userName: String, listName: String): TodoList {
        val response = client(Request(Method.GET,  "/todo/$userName/$listName"))
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
        return TodoList(listName, items)
    }

    private fun extractListName(nameRegex: Regex, html: String): String =
        nameRegex.find(html)?.value
            ?.substringAfter("<h2>")
            ?.dropLast(1)
            .orEmpty()

    private fun extractItemDesc(matchResult: MatchResult): String =
        matchResult.value.substringAfter("<td>").dropLast(1)
}
