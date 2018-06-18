package com.github.wakingrufus.eloleague.udp

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import mu.KLogging
import org.junit.Test
import java.net.DatagramSocket
import java.net.InetAddress

class UdpServerTest {
    companion object : KLogging()

    @Test
    fun test() {
        val clientSocket: DatagramSocket = DatagramSocket(8888, InetAddress.getByName("0.0.0.0")).apply {
            broadcast = true
        }
        launch(CommonPool) {
            UdpServer().handleUdp(clientSocket).apply {
                logger.info { this.address }
            }
        }
        val client = UdpClient()
        val receive = async(CommonPool) {
            client.handleResponse(clientSocket) { address ->
                logger.info { address.hostAddress }
                logger.info { address.canonicalHostName }
            }
        }
        client.sendDiscoveryRequest(clientSocket)
        runBlocking {
            receive.await()
        }

    }
}