package com.example.gopetext.auth.home.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.gopetext.R
import com.example.gopetext.auth.home.edit.EditProfileActivity
import com.example.gopetext.data.model.User
import com.example.gopetext.data.storage.SessionManager

class ProfileFragment : Fragment(), ProfileContract.View {

    private lateinit var presenter: ProfileContract.Presenter
    private lateinit var sessionManager: SessionManager

    private lateinit var etName: TextView
    private lateinit var etLastName: TextView
    private lateinit var etEmail: TextView
    private lateinit var etAge: TextView
    private lateinit var ivProfileImage: ImageView
    private lateinit var btnEdit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        presenter = ProfilePresenter(this, sessionManager)

        etName = view.findViewById(R.id.edtName)
        etLastName = view.findViewById(R.id.edtLastName)
        etEmail = view.findViewById(R.id.edtEmail)
        etAge = view.findViewById(R.id.edtAge)
        ivProfileImage = view.findViewById(R.id.imgProfile)
        btnEdit = view.findViewById(R.id.btnEdit)

        // Abrir actividad de edición
        btnEdit.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        // ✅ Carga los datos del usuario logueado
        presenter.loadUserData()
    }

    override fun showUserData(user: User) {
        etName.text = user.name
        etLastName.text = user.last_name
        etAge.text = user.age.toString()
        etEmail.text = user.email
        if (!user.profile_image_url.isNullOrBlank()) {
            Glide.with(requireContext()).load(user.profile_image_url).into(ivProfileImage)
        } else {
            ivProfileImage.setImageResource(R.drawable.ic_baseline_person_24)
        }
        Log.d("DEBUG", "Usuario recibido: $user")
    }

    override fun showUpdateSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

