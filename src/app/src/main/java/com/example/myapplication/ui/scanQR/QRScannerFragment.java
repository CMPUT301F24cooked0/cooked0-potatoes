package com.example.myapplication.ui.scanQR;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;



/**
 * This fragment allows the user to scan a QR code that links to an event.
 */
public class QRScannerFragment extends Fragment {
    View view;
    private Button scanBtn;
    private DatabaseManager databaseManager;
    private ScanQRViewModel scanQRViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);
        return view;
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

}