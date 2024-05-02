package com.wonderfulsigma.flick.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.wonderfulsigma.flick.feature.qrcode.QRCode
import com.wonderfulsigma.flick.R
import com.wonderfulsigma.flick.base.BaseActivity
import com.wonderfulsigma.flick.databinding.ActivityMainBinding
import com.wonderfulsigma.flick.feature.user.viewmodel.UserViewModel
import com.wonderfulsigma.flick.utils.setStatusBarColorBackground
import com.wonderfulsigma.flick.utils.setStatusBarColorWhite
import java.text.DecimalFormat

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()
    val userViewModel: UserViewModel by viewModels()

    private lateinit var navController: NavController

    override fun start() {
        checkUpdate()
        checkPermission()

        userViewModel.getUserInfo()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        navController = navHostFragment.findNavController()

        binding.bnv.setupWithNavController(navController)

        setBottomNavigation()

        userViewModel.myInfo.observe(this) {
            setQRCode()
            if(userViewModel.FCMToken.value == "") {
                getFCMToken(it.id)
            }
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission: Array<String> = arrayOf(
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            // 노티 권한 활성화 체크
            ActivityCompat.requestPermissions(this@MainActivity, permission, 0)
        }
    }

    private fun getFCMToken(uuid: String) {
        var token: String?
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result
            if (token != null) {
                Log.d(TAG, "FCM Token is ${task.result}")
                userViewModel.getFCMToken(task.result.toString(), uuid)
            }
        })
    }

    private fun checkUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        val i = Intent(Intent.ACTION_VIEW)
        val updateUrl = "https://play.google.com/store/apps/details?id=com.wonderfulsigma.flick"

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                AlertDialog.Builder(this)
                    .setTitle("플릭 업데이트")
                    .setMessage("원활한 사용을 위해선 업데이트가 필요해요")
                    .setPositiveButton("확인") { _, _ ->
                        i.data = Uri.parse(updateUrl)
                        startActivity(i)
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun setQRCode() {
        /** QR Code */
        val qrCodeClass = QRCode(userViewModel, this, this, this, layoutInflater)

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(qrCodeClass.bottomSheetView)

        binding.bnv.menu.findItem(R.id.paymentFragment).setOnMenuItemClickListener {
            qrCodeClass.setQRCode()
            bottomSheetDialog.show()
            qrCodeClass.generateQRCode()
            return@setOnMenuItemClickListener false
        }
    }

    private fun setBottomNavigation() {
        /** Bottom Navigation */
        navController.addOnDestinationChangedListener { _, destination, _ ->
            /** Background Color & Status Bar Color */
            if (destination.id == R.id.homeFragment) {
                binding.root.setBackgroundColor(resources.getColor(R.color.activity_background))
                setStatusBarColorBackground(this, this)
            } else {
                binding.root.setBackgroundColor(Color.WHITE)
                setStatusBarColorWhite(this, this)
            }

            if (destination.id == R.id.settingFragment) {
                window.navigationBarColor = ContextCompat.getColor(applicationContext, R.color.activity_background)
            } else {
                window.navigationBarColor = Color.WHITE
            }

            /** Bottom Nav */
            if (destination.id == R.id.homeFragment || destination.id == R.id.allFragment || destination.id == R.id.stockFragment ||
                destination.id == R.id.paymentFragment || destination.id == R.id.eventFragment || destination.id == R.id.homeLoadingFragment
            ) {
                binding.bnv.visibility = View.VISIBLE
            } else {
                binding.bnv.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}
fun Long.toDecimalFormat(): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(this) + "코인"
}
fun String.toDecimalFormat(): String {
    return try{
        val decimalFormat = DecimalFormat("#,###")
        decimalFormat.format(this.toLong())
    } catch (e: Exception){
        ""
    }
}