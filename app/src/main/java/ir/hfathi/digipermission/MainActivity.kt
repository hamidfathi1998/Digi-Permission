package ir.hfathi.digipermission

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import ir.hfathi.permission.DigiPermission

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val digiPermission = DigiPermission.init(this)
        findViewById<Button>(R.id.btnCamera).setOnClickListener {
            digiPermission
                .permissions(CAMERA)
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(
                            this,
                            "All permissions are granted $grantedList",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "These permissions are denied: $deniedList",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
        findViewById<Button>(R.id.btnContact).setOnClickListener {
            digiPermission
                .permissions(READ_CONTACTS)
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(
                            this,
                            "All permissions are granted $grantedList",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "These permissions are denied: $deniedList",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
        findViewById<Button>(R.id.btnReadCallPhone).setOnClickListener {
            digiPermission
                .permissions(CALL_PHONE)
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(
                            this,
                            "All permissions are granted $grantedList",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "These permissions are denied: $deniedList",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}