package com.dicoding.storyapp.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dicoding.storyapp.R
import com.dicoding.storyapp.app.di.ResponseRegisterViewModel
import com.dicoding.storyapp.app.utils.Status
import com.dicoding.storyapp.app.utils.Utils
import com.dicoding.storyapp.data.models.RegisterDataModel
import com.dicoding.storyapp.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModelRegister by viewModels<ResponseRegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // define binding
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSignup.setOnClickListener {
                val name = edtNameRegister.text.toString()
                val email = edtEmailRegister.text.toString()
                val password = edtPwRegister.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    viewModelRegister.responseRegister(RegisterDataModel(name, email, password))
                } else {
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.validate_login),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // cek state for edit text password
            edtPwRegister.isWrong.observe(viewLifecycleOwner) { isChecked ->
                btnSignup.isEnabled = isChecked
            }

            // loading
            viewModelRegister.res.observe(viewLifecycleOwner) {
                Utils.showLoading(progressIndicator, it.status == Status.LOADING)
                Utils.showLoading(overlay, it.status == Status.LOADING)
            }

            // goto login page
            login.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        val registerState = viewModelRegister.state
        lifecycleScope.launch {
            registerState.collect {
                if (it.isNavigate == true) {
                    // set fragment result when sucess to create new account
                    setFragmentResult(
                        Utils.GET_REQ_EMAIL,
                        bundleOf(Utils.GET_EMAIL_KEY to binding.edtEmailRegister.text.toString())
                    )

                    setFragmentResult(
                        Utils.GET_REQ_PASSWORD,
                        bundleOf(Utils.GET_PASSWORD_KEY to binding.edtPwRegister.text.toString())
                    )

                    findNavController().navigateUp()
                    viewModelRegister.navigates()
                }
                it.error?.also { error ->
                    Snackbar.make(
                        requireView(),
                        error,
                        Snackbar.LENGTH_SHORT
                    ).addCallback(
                        object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                viewModelRegister.showErrors()
                            }
                        }
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}