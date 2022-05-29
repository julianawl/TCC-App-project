package com.julianawl.testemoov.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.julianawl.testemoov.ModelPreferencesManager
import com.julianawl.testemoov.databinding.ActivityMainBinding
import com.julianawl.testemoov.graphics.model.SetModel
import com.julianawl.testemoov.graphics.model.SceneModel
import com.julianawl.testemoov.graphics.model.SetList
import com.julianawl.testemoov.ui.dialog.NewCompositionDialog
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var newCompositionDialog: NewCompositionDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mainNewCompFab.setOnClickListener {
            newCompositionDialog = NewCompositionDialog()
            newCompositionDialog?.show(supportFragmentManager, "new composition dialog")
            newCompositionDialog?.newCompositionListener(object :
                NewCompositionDialog.NewCompositionListener {
                override fun onClickCreateComposition(width: Float, height: Float, name: String) {
                    val intent = Intent(this@MainActivity, CompositionActivity::class.java)
                    intent.putExtra("width", width)
                    intent.putExtra("height", height)
                    intent.putExtra("name", name)

//                    ModelPreferencesManager.put(
//                        SetModel(
//                            setCompositionId(),
//                            name,
//                            width,
//                            height,
//                            mutableListOf(SceneModel(1, null))
//                        ),
//                        "COMPOSITION_${name}"
//                    )

                    startActivity(intent)
                }
            })
        }
    }

//    private fun setCompositionId(): Int{
//        return if(ModelPreferencesManager.preferences.all.isNullOrEmpty()){
//            1
//        } else {
//            ModelPreferencesManager.get<SetList>("COMPOSITION")?.setList?.last()?.id!! +1
//        }
//    }
}