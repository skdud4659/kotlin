package com.example.clone_instagram

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide


class PostFragment:Fragment() {
  var imageUri: Uri? = null
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_post, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    val selectedImageView = view.findViewById<ImageView>(R.id.write_image)
    val glide = Glide.with(activity as BoardActivity)
    
    // 유저가 선택한 이미지를 구분하기 위해.
    val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      imageUri = it.data!!.data
      glide.load(imageUri).into(selectedImageView)
    }
    imagePickerLauncher.launch(Intent(Intent.ACTION_PICK).apply {
      this.type = MediaStore.Images.Media.CONTENT_TYPE
    })
  }
}