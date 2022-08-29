package com.example.routes

import Customer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.id.toId

fun Route.customerRouting() {
    val client = KMongo.createClient()
    val database = client.getDatabase("ktor_project")
    val collection = database.getCollection<Customer>()

    route("/customer") {
        get {
            val customerList = collection.find().toList()
            if (customerList.isNotEmpty()) {
                call.respond(customerList)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                collection.findOneById(ObjectId(id).toId<String>()) ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )

            call.respond(customer)
        }
        post {
            val customer = call.receive<Customer>()
            collection.insertOne(customer)
            /*customerStorage.add(customer)*/
            call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (collection.deleteOneById(id).wasAcknowledged()){
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}