package com.example.photo_frame

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
  var imageUri: MutableList<Uri> = mutableListOf()
  private val addPhoto: Button by lazy { findViewById(R.id.add_photo) }
  private val startFrame: Button by lazy { findViewById(R.id.start_frame) }
  
  private val imageViewList: List<ImageView> by lazy {
    mutableListOf<ImageView>().apply {
      add(findViewById(R.id.photo1_1))
      add(findViewById(R.id.photo1_2))
      add(findViewById(R.id.photo1_3))
      add(findViewById(R.id.photo2_1))
      add(findViewById(R.id.photo2_2))
      add(findViewById(R.id.photo2_3))
    }
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    initAddPhotoButton()
    initStartFrame()
  }
  private fun initAddPhotoButton() {
    addPhoto.setOnClickListener {
      when {
        ContextCompat.checkSelfPermission(
          this, android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED -> {
          // 권한이 잘 부여되었을 때 캘러리에서 사진을 선택하는 기능
          navigatePhotos()
        }
        ActivityCompat.shouldShowRequestPermissionRationale(
          this,
          android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) -> {
          // 교육용 팝업 확인 후 권한 팝업을 띄우는 기능
          showPermissionContextPopup()
        }
        else -> {
          ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1000
          )
        }
      }
    }
  }
  private fun navigatePhotos() {
    // SAF로 이미지 가져오기 > ACTION_GET_CONTENT / 일반 사진첩 : ACTION_PICK
    imagePickerLauncher.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
      this.type = "image/*"
    })
  }
  private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    val selectedUri = it.data?.data
    if (selectedUri !== null) {
      if (imageUri.size == 6) {
        Toast.makeText(this, "이미 사진이 꽉 찼습니다.", Toast.LENGTH_SHORT).show()
        return@registerForActivityResult
      }
      imageUri.add(selectedUri)
      imageViewList[imageUri.size - 1].setImageURI(selectedUri)
    }
  }
  private fun showPermissionContextPopup() {
    AlertDialog.Builder(this).setTitle("권한이 필요합니다.")
      .setMessage("전자 액자 앱에서 사진을 불러오기 위해 권한이 필요합니다.")
      .setPositiveButton("동의하기") { _, _ ->
        ActivityCompat.requestPermissions(
          this,
          arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
          1000
        )
      }
      .setNegativeButton("취소하기", null)
      .show()
  }
  private fun initStartFrame() {
    startFrame.setOnClickListener {
      val intent = Intent(this, FrameActivity::class.java)
      imageUri.forEachIndexed {index, uri ->
        intent.putExtra("photo$index", uri.toString())
      }
      intent.putExtra("size", imageUri.size)
      startActivity(intent)
    }
  }
  
  // 권한 결과 확인
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    //requestCode를 권한마다 다르게 줘서 구분.
    when (requestCode) {
      1000 -> {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          // 권한 부여 됨
          navigatePhotos()
        } else {
          Toast.makeText(this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }
}