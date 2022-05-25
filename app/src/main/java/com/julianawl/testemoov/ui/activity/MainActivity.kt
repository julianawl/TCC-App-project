package com.julianawl.testemoov.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.julianawl.testemoov.databinding.ActivityMainBinding
import com.julianawl.testemoov.ui.dialog.NewCompositionDialog

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val newCompositionDialog by lazy { NewCompositionDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mainNewCompFab.setOnClickListener {
            newCompositionDialog.show(supportFragmentManager, "new composition dialog")
            newCompositionDialog.newCompositionListener(object :
                NewCompositionDialog.NewCompositionListener {
                override fun onClickCreateComposition(width: Float, height: Float) {
                    val intent = Intent(this@MainActivity, CompositionActivity::class.java)
                    intent.putExtra("width", width)
                    intent.putExtra("height", height)

                    startActivity(intent)
                }
            })
        }
    }
}