package br.com.pratofeito

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class PratoFeitoWebsocketApplication

fun main(args: Array<String>) {
  runApplication<PratoFeitoWebsocketApplication>(*args)
}