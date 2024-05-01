package com.yenaly.circularrevealswitch.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yenaly.circularrevealswitch.Prefs.getPref
import com.yenaly.circularrevealswitch.Prefs.savePref
import com.yenaly.circularrevealswitch.demo.R
import com.yenaly.circularrevealswitch.demo.databinding.FragmentHomeBinding
import com.yenaly.circularrevealswitch.ext.setThemeSwitcher
import com.yenaly.circularrevealswitch.isAppearanceLightStatusBars

class HomeFragment : Fragment() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Log.d(TAG, "onCreateView")
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.defaultLayout.setThemeSwitcher(R.style.Theme_CircularRevealSwitch) {
            activity?.window?.isAppearanceLightStatusBars = false
            it.context.savePref("theme", "default")
        }
        binding.redLayout.setThemeSwitcher(R.style.Theme_CircularRevealSwitch_Red) {
            activity?.window?.isAppearanceLightStatusBars = false
            it.context.savePref("theme", "red")
        }
        binding.greenLayout.setThemeSwitcher(R.style.Theme_CircularRevealSwitch_Green) {
            activity?.window?.isAppearanceLightStatusBars = false
            it.context.savePref("theme", "green")
        }
        binding.blueLayout.setThemeSwitcher(R.style.Theme_CircularRevealSwitch_Blue) {
            activity?.window?.isAppearanceLightStatusBars = false
            it.context.savePref("theme", "blue")
        }
        binding.yellowLayout.setThemeSwitcher(R.style.Theme_CircularRevealSwitch_Yellow) {
            activity?.window?.isAppearanceLightStatusBars = true
            it.context.savePref("theme", "yellow")
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        binding.defaultRadioButton.isChecked =
            requireContext().getPref("theme", "default") == "default"
        binding.redRadioButton.isChecked =
            requireContext().getPref("theme", "default") == "red"
        binding.greenRadioButton.isChecked =
            requireContext().getPref("theme", "default") == "green"
        binding.blueRadioButton.isChecked =
            requireContext().getPref("theme", "default") == "blue"
        binding.yellowRadioButton.isChecked =
            requireContext().getPref("theme", "default") == "yellow"
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}