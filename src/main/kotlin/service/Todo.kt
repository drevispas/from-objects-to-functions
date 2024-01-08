package org.example.service

import org.example.domain.ListName
import org.example.domain.TodoItem
import org.example.domain.TodoList
import org.example.domain.User
import org.example.web.HtmlPage
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes

data class Todo(
    val lists: Map<User, List<TodoList>>
) : HttpHandler {



    val routingHttpHandler = routes(
        "/todo/{user}/{list}" bind Method.GET to ::showList
    )

    private fun showList(request: Request): Response {
        return request.let(::extractListData)
            .let(::fetchListContent)
            .let(::renderHtml)
            .let(::createResponse)
    }

    override fun invoke(request: Request): Response = routingHttpHandler(request)

    // Request -> {user, listName}
    fun extractListData(request: Request): Pair<User, ListName> {
        val user = request.path("user").orEmpty().let(::User)
        val list = request.path("list").orEmpty().let(::ListName)
        return user to list
    }

    // {user, listName} -> TodoList
    fun fetchListContent(listId: Pair<User, ListName>): TodoList {
        return lists[listId.first]?.first { it.listName == listId.second }
            ?: error("List `${listId.second}` not found")
    }

    // TodoList -> HtmlPage
    fun renderHtml(todoList: TodoList): HtmlPage {
        return HtmlPage("""
            <html>
                <head>
                    <meta charset="utf-8">
                </head>
                <body>
                    <h1>절대</h1>
                    <h2>${todoList.listName.name}</h2>
                    <table>
                        <tbody>${rednerItems(todoList.items)}</tbody>                    
                    </table>
                </body>
            </html>
        """.trimIndent()
        )
    }

    fun rednerItems(items: List<TodoItem>): String {
        return items.map {"""
            <tr><td>${it.description}</tr></td>
        """.trimIndent()
        }.joinToString("")
    }

    // HtmlPage -> Response
    fun createResponse(htmlPage: HtmlPage): Response {
        return Response(Status.OK).body(htmlPage.raw)
    }
}
