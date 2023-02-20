package ir.hfathi.digipermission

import android.Manifest.permission.*
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import ir.hfathi.digipermission.databinding.ActivityMainBinding
import ir.hfathi.permission.DigiPermission

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnThreePermission.setOnClickListener {
            DigiPermission.initialize(this)
                .addPermissions(ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN)
                .permissionResult { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(
                            this,
                            "Permissions Are Granted \n$grantedList",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Permissions Are Denied: \n$deniedList",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
        binding.btnInternet.setOnClickListener {
            DigiPermission.initialize(this)
                .addPermissions(INTERNET)
                .permissionResult { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(
                            this,
                            "Permission is Granted \n$grantedList",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Permissions is Denied: \n$deniedList",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}