package com.github.wakingrufus.eloleague.udp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.github.wakingrufus.eloleague.data.LeagueData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import mu.KLogging
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface


class UdpClient {
    companion object : KLogging()

    var stop = false
    val objectMapper = ObjectMapper()
            .registerModule(ParameterNamesModule())
            .registerModule(KotlinModule())
    fun discoverUdp() {
        val socket: DatagramSocket = DatagramSocket(8888, InetAddress.getByName("0.0.0.0")).apply {
            broadcast = true
        }
        sendDiscoveryRequest(socket)
        //Wait for a response
        launch(CommonPool) {
            while (!stop) {
                handleResponse(socket) { address ->
                    println(address.address)
                }
            }
        }
        //Close the port!
        socket.close()
    }

    fun sendDiscoveryRequest(socket: DatagramSocket) {

        val sendData = discoveryPacket.toByteArray()

        //Try the 255.255.255.255 first
        try {
            val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName("255.255.255.255"), 8888)
            socket.send(sendPacket)
            println(javaClass.name + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)")
        } catch (e: Exception) {
        }


        // Broadcast the message over all the network interfaces
        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val networkInterface = interfaces.nextElement()

            if (networkInterface.isLoopback || !networkInterface.isUp) {
                continue // Don't want to broadcast to the loopback interface
            }

            for (interfaceAddress in networkInterface.interfaceAddresses) {
                val broadcast = interfaceAddress.broadcast ?: continue

                // Send the broadcast package!
                try {
                    val sendPacket = DatagramPacket(sendData, sendData.size, broadcast, 8888)
                    socket.send(sendPacket)
                } catch (e: Exception) {
                }

                println(javaClass.name + ">>> Request packet sent to: " + broadcast.hostAddress + "; Interface: " + networkInterface.displayName)
            }
        }

        println(javaClass.name + ">>> Done looping over all network interfaces. Now waiting for a reply!")
    }

    fun sendLeague(leagueData: LeagueData, socket: DatagramSocket) {

        val sendData = objectMapper.writeValueAsBytes(leagueData)

        //Try the 255.255.255.255 first
        try {
            val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName("255.255.255.255"), 8888)
            socket.send(sendPacket)
            println(javaClass.name + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)")
        } catch (e: Exception) {
        }


        // Broadcast the message over all the network interfaces
        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val networkInterface = interfaces.nextElement()

            if (networkInterface.isLoopback || !networkInterface.isUp) {
                continue // Don't want to broadcast to the loopback interface
            }

            for (interfaceAddress in networkInterface.interfaceAddresses) {
                val broadcast = interfaceAddress.broadcast ?: continue

                // Send the broadcast package!
                try {
                    val sendPacket = DatagramPacket(sendData, sendData.size, broadcast, 8888)
                    socket.send(sendPacket)
                } catch (e: Exception) {
                }

                println(javaClass.name + ">>> Request packet sent to: " + broadcast.hostAddress + "; Interface: " + networkInterface.displayName)
            }
        }

        println(javaClass.name + ">>> Done looping over all network interfaces. Now waiting for a reply!")
    }

    fun handleResponse(socket: DatagramSocket, op: (InetAddress) -> Unit) {
        //Wait for a response
        val recvBuf = ByteArray(15000)
        val receivePacket = DatagramPacket(recvBuf, recvBuf.size)
        socket.receive(receivePacket)

        //We have a response
        println(javaClass.name + ">>> Broadcast response from server: " + receivePacket.address.hostAddress)

        //Check if the message is correct
        val message = String(receivePacket.data).trim { it <= ' ' }
        if (message == discoveryResponsePacket) {
            //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
            op(receivePacket.address)
        }

    }
}