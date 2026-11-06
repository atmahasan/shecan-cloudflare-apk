package com.smart.shecancf

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor

class SmartDnsVpnService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP") {
            stopVpn()
            return START_NOT_STICKY
        }
        startVpn()
        return START_STICKY
    }

    private fun startVpn() {
        if (vpnInterface != null) return
        val builder = Builder()
        builder.setSession("ShecanCF Smart Tunnel")
        builder.addAddress("10.111.222.1", 24)

        // Primary Shecan DNS + Cloudflare Clean Anycast IP
        builder.addDnsServer("178.22.122.100")
        builder.addDnsServer("162.159.135.42")

        // Route only DNS endpoints to keep full native connection speed
        builder.addRoute("178.22.122.100", 32)
        builder.addRoute("162.159.135.42", 32)
        builder.setMtu(1280)

        vpnInterface = builder.establish()
    }

    private fun stopVpn() {
        vpnInterface?.close()
        vpnInterface = null
        stopSelf()
    }

    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }
}