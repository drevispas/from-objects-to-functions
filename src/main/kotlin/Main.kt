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
    val user = "brad"
    val listName = "shopping"
    val items = listOf("carrots", "apples", "milk")
    val todoList = TodoList(ListName(listName), items.map(::TodoItem))
    val lists = mapOf(User(user) to listOf(todoList))
    val app = Todo(lists)
    app.asServer(Jetty(9090)).start()
}
