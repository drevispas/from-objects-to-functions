package org.example

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val app = routes(
        "/todo/{user}/{list}" bind Method.GET to ::showList
    )
    app.asServer(Jetty(9090)).start()
}

fun showList(req: Request): Response {
    val user = req.path("user")
    val list = req.path("list")
    val html = """
        <html>
            <head>
                <meta charset="utf-8">
            </head>
            <body>
                <h1>절대</h1>
                <p>Here is the list <b>$list</b> of user <b>$user</b></p>
            </body>
        </html>
    """.trimIndent()
    return Response(Status.OK).body(html)
}
