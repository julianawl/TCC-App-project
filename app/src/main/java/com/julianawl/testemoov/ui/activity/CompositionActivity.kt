package com.julianawl.testemoov.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.julianawl.testemoov.*
import com.julianawl.testemoov.databinding.ActivityCompositionBinding
import com.julianawl.testemoov.ui.fragment.CompositionFragment

class CompositionActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {

    private val binding by lazy {
        ActivityCompositionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val libgdxFragment = CompositionFragment()
        val width = intent.getFloatExtra(WIDTH_KEY, 0f)
        val height = intent.getFloatExtra(HEIGHT_KEY, 0f)
        val name = intent.getStringExtra(NAME_KEY)
        val prefs = intent.getStringExtra(PREFERENCES_KEY)
        val compositionId = intent.getIntExtra(ID_KEY, 0)
        libgdxFragment.arguments = bundleOf(
            Pair(WIDTH_KEY, width), Pair(HEIGHT_KEY, height), Pair(NAME_KEY, name), Pair(
                PREFERENCES_KEY, prefs
            ), Pair(ID_KEY, compositionId)
        )
        supportFragmentManager.beginTransaction().add(R.id.composition_view, libgdxFragment)
            .commit()
    }

    override fun exit() {}
}