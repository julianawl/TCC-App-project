package com.julianawl.testemoov.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.julianawl.testemoov.R
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
        val width = intent.getFloatExtra("width", 0f)
        val height = intent.getFloatExtra("height", 0f)
        val name = intent.getStringExtra("name")
        libgdxFragment.arguments = bundleOf(Pair("width", width), Pair("height", height), Pair("name", name))
        supportFragmentManager.beginTransaction().add(R.id.composition_view, libgdxFragment)
            .commit()
    }

    override fun exit() {}
}