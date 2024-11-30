package com.example.myapplication.ui.scanQR;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ScanQrScreenFragmentBinding;


public class ScanQRFragment extends Fragment {

    private ScanQrScreenFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScanQRViewModel scanQRViewModel =
                new ViewModelProvider(this).get(ScanQRViewModel.class);

        binding = ScanQrScreenFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // FIXME ideally we wouldnt use this extra fragment but this is a quick fix as the fragments arent being replaced properly/don't cover the screen entirely
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new QRScannerFragment());
        fragmentTransaction.commit();

        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}