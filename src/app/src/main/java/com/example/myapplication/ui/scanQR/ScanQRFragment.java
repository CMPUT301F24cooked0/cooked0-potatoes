package com.example.myapplication.ui.scanQR;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ScanQrScreenFragmentBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScanQRFragment extends Fragment {

    private ScanQrScreenFragmentBinding binding;
    private Button scanBtn;
    private DatabaseManager databaseManager;
    private ScanQRViewModel scanQRViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScanQRViewModel scanQRViewModel =
                new ViewModelProvider(this).get(ScanQRViewModel.class);

        binding = ScanQrScreenFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scanQRViewModel = new ViewModelProvider(requireActivity()).get(ScanQRViewModel.class);
        scanBtn = view.findViewById(R.id.scan_button);
        databaseManager = new DatabaseManager();
        registerUiListener();
    }

    private void registerUiListener() {
        scanBtn.setOnClickListener(view -> {
            ScanOptions options = new ScanOptions();
            options.setBeepEnabled(false);
            options.setPrompt("Scan QR code");
            scanLauncher.launch(options);
        });
    }

    private ActivityResultLauncher<ScanOptions> scanLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) { // TODO handle other qr code texts (not in our database path format)
                    Toast.makeText(requireContext(), "QR code contents cannot be read", Toast.LENGTH_LONG).show();
                } else {
                    scanQRViewModel.setEventPath(result.getContents());
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    SearchEventFragment searchEventFragment = new SearchEventFragment();
                    fragmentTransaction.replace(R.id.fragment_container, searchEventFragment);
                    fragmentTransaction.commit();
                }
            });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}