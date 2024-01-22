import org.example.domain.ListName
import org.example.domain.TodoItem
import org.example.domain.TodoList
import strikt.api.expectThat
import strikt.assertions.isEqualTo

interface ScenarioActor {

    val userName: String
}

class TodoListActor(override val userName: String) : ScenarioActor {

    fun canGetItemsOfATodoList(listName:String, itemNames: List<String>): Step = {
        val expected = TodoList(ListName(listName), itemNames.map(::TodoItem))
        val actual = getTodoList(userName, listName)
        expectThat(actual).isEqualTo(expected)
    }
}
