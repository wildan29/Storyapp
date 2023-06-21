package com.dicoding.storyapp.app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dicoding.storyapp.R
import com.dicoding.storyapp.app.di.ResponseLoginViewModel
import com.dicoding.storyapp.app.ui.activity.DashboardActivity
import com.dicoding.storyapp.app.utils.Status
import com.dicoding.storyapp.app.utils.Utils
import com.dicoding.storyapp.data.models.LoginDataModel
import com.dicoding.storyapp.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModelLogin by viewModels<ResponseLoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // define binding
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // signUp
            signUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            // get fragment result
            setFragmentResultListener(Utils.GET_REQ_EMAIL) { _, bundle ->
                val email = bundle.getString(Utils.GET_EMAIL_KEY)
                edtEmailLogin.setText(email)
            }

            setFragmentResultListener(Utils.GET_REQ_PASSWORD) { _, bundle ->
                val password = bundle.getString(Utils.GET_PASSWORD_KEY)
                edtPwLogin.setText(password)
            }

            // login
            btnLogin.setOnClickListener {
                val email = edtEmailLogin.text.toString()
                val password = edtPwLogin.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModelLogin.responseLogin(LoginDataModel(email, password))
                } else {
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.validate_register),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // state edittext
            edtPwLogin.isWrong.observe(viewLifecycleOwner) {
                btnLogin.isEnabled = it
            }

            // loading
            viewModelLogin.res.observe(viewLifecycleOwner) {
                Utils.showLoading(progressIndicator, it.status == Status.LOADING)
                Utils.showLoading(overlay, it.status == Status.LOADING)
            }
        }

        val loginState = viewModelLogin.state
        lifecycleScope.launch {
            loginState.collect {
                if (it.isNavigate == true) {
                    // change to dashboard activity
                    startActivity(Intent(requireActivity(), DashboardActivity::class.java))
                    viewModelLogin.navigates()
                    activity?.finish()
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
                                viewModelLogin.showErrors()
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