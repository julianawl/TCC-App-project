package com.julianawl.testemoov.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.julianawl.testemoov.*
import com.julianawl.testemoov.databinding.ActivityMainBinding
import com.julianawl.framework.model.SetModel
import com.julianawl.framework.model.SceneModel
import com.julianawl.framework.model.SetList
import com.julianawl.testemoov.data.ModelPreferencesManager
import com.julianawl.testemoov.ui.activity.adapter.CompositionsAdapter
import com.julianawl.testemoov.ui.dialog.NewCompositionDialog

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var newCompositionDialog: NewCompositionDialog? = null
    private val adapter by lazy { CompositionsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onCreateClickListener()
        getAllCompositions()
    }

    private fun onCreateClickListener() {
        binding.mainNewCompFab.setOnClickListener {
            newCompositionDialog = NewCompositionDialog()
            newCompositionDialog?.show(supportFragmentManager, getString(R.string.new_composition_dialog_tag))
            newCompositionDialog?.newCompositionListener(object :
                NewCompositionDialog.NewCompositionListener {
                override fun onClickCreateComposition(width: Float, height: Float, name: String) {
                    val id = setCompositionId()

                    createNewComposition(width, height, name, id)

                    val intent = Intent(this@MainActivity, CompositionActivity::class.java)
                    intent.putExtra(WIDTH_KEY, width)
                    intent.putExtra(HEIGHT_KEY, height)
                    intent.putExtra(NAME_KEY, name)
                    intent.putExtra(PREFERENCES_KEY, PREFERENCES_NAME)
                    intent.putExtra(ID_KEY, id)
                    startActivity(intent)
                }
            })
        }
    }

    private fun setCompositionId(): Int {
        return if (ModelPreferencesManager.get<SetList>(PREFERENCES_NAME) == null) {
            1
        } else {
            ModelPreferencesManager.get<SetList>(PREFERENCES_NAME)?.setList?.last()?.id!! + 1
        }
    }

    private fun createNewComposition(width: Float, height: Float, name: String, id: Int){
        if (id != 1){
            val composition = ModelPreferencesManager.get<SetList>(PREFERENCES_NAME)
            val setList = composition?.setList
            setList?.add(
                SetModel(
                    id,
                    name,
                    width,
                    height,
                    mutableListOf(SceneModel(1, null))
                )
            )
            composition?.setList = setList
            ModelPreferencesManager.put(composition, PREFERENCES_NAME)
        } else {
            ModelPreferencesManager.put(
                SetList(
                    setList = mutableListOf(
                        SetModel(
                            id,
                            name,
                            width,
                            height,
                            mutableListOf(SceneModel(1, null))
                        )
                    )
                ), PREFERENCES_NAME
            )
        }
    }
    private fun getAllCompositions() {
        binding.mainCompositionsRv.adapter = adapter
        binding.mainCompositionsRv.layoutManager = LinearLayoutManager(this)
        val compositions = ModelPreferencesManager.get<SetList>(PREFERENCES_NAME)

        if(compositions != null){
            adapter.update(compositions.setList as List<SetModel>)
            adapter.onCompositionClickListener = {
                val intent = Intent(this@MainActivity, CompositionActivity::class.java)
                intent.putExtra(WIDTH_KEY, it.stageWidth)
                intent.putExtra(HEIGHT_KEY, it.stageHeight)
                intent.putExtra(NAME_KEY, it.name)
                intent.putExtra(PREFERENCES_KEY, PREFERENCES_NAME)
                intent.putExtra(ID_KEY, it.id)
                startActivity(intent)
            }
        }
    }
}