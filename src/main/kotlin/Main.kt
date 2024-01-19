package org.example

import org.example.domain.ListName
import org.example.domain.TodoItem
import org.example.domain.TodoList
import org.example.domain.User
import org.example.service.Todo
import org.http4k.server.Jetty
import org.http4k.server.asServer

// http4k spike
fun main() {
    val userName = "brad"
    val listName = "shopping"
    val itemNames = listOf("carrots", "apples", "milk")
    val todoList = TodoList(ListName(listName), itemNames.map(::TodoItem))
    val todo = mapOf(User(userName) to listOf(todoList))
    val app = Todo(todo)
    app.asServer(Jetty(9090)).start()
}
