package com.example.demo.controllers

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import javax.annotation.PreDestroy
import kotlin.concurrent.withLock

@RestController
class ChatController(
    private val chatService: ChatService
) {

    @GetMapping("/api/chat/listen")
    fun listen(): SseEmitter {
        val listener = chatService.newListener()
        return listener.sseEmitter
    }

    @PostMapping("/api/chat/send")
    fun send(@RequestBody message: String) {
        chatService.sendMessage(message)
    }
}

@Service
class ChatService {

    private val listeners = mutableListOf<ChatListener>()
    private var currentId = 0L
    private val lock = ReentrantLock()

    val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1).also {
        it.scheduleAtFixedRate({ keepAlive() }, 2, 2, TimeUnit.SECONDS)
    }

    @PreDestroy
    private fun preDestroy() {
        logger.info("shutting down")
        scheduler.shutdown()
    }

    fun newListener(): ChatListener = lock.withLock {
        logger.info("newListener")
        val sseEmitter = SseEmitter(TimeUnit.MINUTES.toMillis(5))
        val listener = ChatListener(sseEmitter)
        listeners.add(listener)
        listener.sseEmitter.onCompletion {
            removeListener(listener)
        }
        listener
    }

    fun sendMessage(msg: String) = lock.withLock {
        logger.info("sendMessage")
        val id = currentId++
        sendEventToAll(Event.Message(id, msg))
    }

    private fun removeListener(listener: ChatListener) = lock.withLock {
        logger.info("remove")
        listeners.remove(listener)
    }

    private fun keepAlive() = lock.withLock {
        logger.info("keepAlive, sending to {} listeners", listeners.count())
        sendEventToAll(Event.KeepAlive(Instant.now().epochSecond))
    }

    private fun sendEventToAll(event: Event) {
        listeners.forEach {
            try {
                event.writeTo(it.sseEmitter)
            } catch (ex: Exception) {
                logger.info("Exception while sending event - {}", ex.message)
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ChatService::class.java)
    }

}

sealed interface Event {

    fun writeTo(emitter: SseEmitter)

    class Message(val id: Long, val msg: String) : Event {

        private val event = SseEmitter.event()
            .id(id.toString())
            .name("message")
            .data(this)

        override fun writeTo(emitter: SseEmitter) {
            emitter.send(
                event
            )
        }
    }

    class KeepAlive(val timestamp: Long) : Event {

        private val event = SseEmitter.event()
            .comment(timestamp.toString())

        override fun writeTo(emitter: SseEmitter) {
            emitter.send(
                event
            )
        }
    }
}

class ChatListener(
    val sseEmitter: SseEmitter
)
