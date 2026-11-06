package com.smart.shecancf

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.VpnService
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private var isConnected = false
    private lateinit var statusText: TextView
    private lateinit var actionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#0B0F19"))
            setPadding(64, 64, 64, 64)
        }

        val title = TextView(this).apply {
            text = "ShecanCF Smart"
            textSize = 24f
            setTextColor(Color.parseColor("#06B6D4"))
            gravity = Gravity.CENTER
        }

        statusText = TextView(this).apply {
            text = "وضعیت: قطع | آماده اتصال به شکن + کلادفلر (162.159.135.42)"
            textSize = 14f
            setTextColor(Color.parseColor("#9CA3AF"))
            gravity = Gravity.CENTER
            setPadding(0, 32, 0, 64)
        }

        actionButton = Button(this).apply {
            text = "اتصال هوشمند (شکن + کلادفلر)"
            textSize = 16f
            setBackgroundColor(Color.parseColor("#10B981"))
            setTextColor(Color.BLACK)
            setOnClickListener { toggleVpn() }
        }

        root.addView(title)
        root.addView(statusText)
        root.addView(actionButton)
        setContentView(root)
    }

    private fun toggleVpn() {
        if (!isConnected) {
            val intent = VpnService.prepare(this)
            if (intent != null) {
                startActivityForResult(intent, 100)
            } else {
                onActivityResult(100, RESULT_OK, null)
            }
        } else {
            startService(Intent(this, SmartDnsVpnService::class.java).setAction("STOP"))
            isConnected = false
            statusText.text = "وضعیت: قطع شده"
            actionButton.text = "اتصال هوشمند (شکن + کلادفلر)"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            startService(Intent(this, SmartDnsVpnService::class.java))
            isConnected = true
            statusText.text = "متصل شد! شکن (178.22.122.100) + کلادفلر (162.159.135.42)"
            actionButton.text = "قطع اتصال"
        }
    }
}