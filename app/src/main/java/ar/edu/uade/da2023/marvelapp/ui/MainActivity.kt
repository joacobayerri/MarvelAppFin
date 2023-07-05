package ar.edu.uade.da2023.marvelapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.da2023.marvelapp.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var rvCharacters: RecyclerView
    private lateinit var adapter: CharactersAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var etSearch: EditText

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private val SEARCH_DELAY = 300L // 300 milisegundos
    private var searchTimer: Timer? = null

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)

        progressBar = findViewById(R.id.progressBar)

        etSearch = findViewById(R.id.etSearch)


        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se utiliza en este caso
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se utiliza en este caso
            }

            override fun afterTextChanged(s: Editable?) {
                searchTimer?.cancel()

                searchTimer = Timer()
                searchTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        val nameStartsWith = s.toString()

                        runOnUiThread {
                            progressBar.visibility = if (nameStartsWith.isNotEmpty()) View.VISIBLE else View.GONE
                            rvCharacters.visibility = View.GONE
                        }

                        viewModel.updateNameStartsWith(nameStartsWith)
                        viewModel.onStart()
                    }
                }, SEARCH_DELAY)
            }
        })

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()



        bindView()
        bindViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView)
        } else {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    private fun bindView() {
        rvCharacters = findViewById(R.id.rvCharacters)
        rvCharacters.layoutManager = LinearLayoutManager(this)

        adapter = CharactersAdapter { position ->
            val character = adapter.getItem(position)
            val intent = Intent(this, CharacterDetailActivity::class.java)
            intent.putExtra("characterId", character.id)
            intent.putExtra("characterName", character.name)
            intent.putExtra("characterImage", character.thumbnail?.let { "${it.path}.${it.extension}" })
            startActivity(intent)
        }
        rvCharacters.adapter = adapter

        navigationView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.action_favorites -> {

                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.action_logout -> {

                    firebaseAuth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun bindViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.characters.observe(this) { characters ->
            runOnUiThread {
                progressBar.visibility = View.GONE
                rvCharacters.visibility = if (characters.isNotEmpty()) View.VISIBLE else View.GONE

                if (characters.isNotEmpty()) {
                    adapter.updateList(characters.toMutableList())
                } else {
                    Log.d("_TAG", "No hay resultados")
                }
            }
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            // Usuario no logueado
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


}