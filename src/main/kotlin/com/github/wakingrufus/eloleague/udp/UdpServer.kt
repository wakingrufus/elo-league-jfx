package com.github.wakingrufus.eloleague.udp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.github.wakingrufus.eloleague.State
import com.github.wakingrufus.eloleague.data.LeagueData
import com.github.wakingrufus.eloleague.league.fromData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import mu.KLogging
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UdpServer {
    companion object : KLogging()
    val objectMapper = ObjectMapper()
            .registerModule(ParameterNamesModule())
            .registerModule(KotlinModule())
    fun run() {
        launch(CommonPool) {
            val socket = DatagramSocket(8888, InetAddress.getByName("0.0.0.0")).apply {
                broadcast = true
            }
            while (true) handleUdp(socket)
        }
    }

    fun handleUdp(socket: DatagramSocket): DatagramPacket {
        logger.info { "${javaClass.name}>>>Ready to receive broadcast packets!" }

        //Receive a packet
        val recvBuf = ByteArray(15000)
        val packet = DatagramPacket(recvBuf, recvBuf.size)
        socket.receive(packet)

        //Packet received
        logger.info { "${javaClass.name}>>>Discovery packet received from: ${packet.address.hostAddress}" }


        //See if the packet holds the right command (message)
        val message = String(packet.data).trim { it <= ' ' }
        logger.info { "${javaClass.name}>>>Packet received; data: $message" }
        if (message == discoveryPacket) {
            val sendData = discoveryResponsePacket.toByteArray()
            //Send a response
            val sendPacket = DatagramPacket(sendData, sendData.size, packet.address, packet.port)
            socket.send(sendPacket)

            logger.info { "${javaClass.name}>>>Sent packet to: ${sendPacket.address.hostAddress}" }
        } else {
            State.updateRemoteLeague(fromData(objectMapper.readValue(message, LeagueData::class.java)))
            logger.info { "invalid packet:$message" }
        }
        return packet
    }
}