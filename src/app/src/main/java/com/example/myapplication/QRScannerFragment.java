package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import kotlin.annotation.Target;

/**
 * This fragment allows the user to scan a QR code that links to an event.
 */
public class QRScannerFragment extends Fragment {
    View view;
    Button scanBtn;


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
        scanBtn = view.findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(this::onClickScan);

    }
    public void onClickScan(View view) {
        ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
                result -> {
                    if (result.getContents() == null) { // TODO handle other qr code texts (not in our database path format)
                        Toast.makeText(requireContext(), "Unable to get event details", Toast.LENGTH_LONG).show();
                    } else {
                        // pass event path to view event details fragment to populate fragment
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ViewEventDetailsFragment viewEventDetailsFragment = new ViewEventDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("eventPath", result.getContents());
                        viewEventDetailsFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_container, viewEventDetailsFragment);
                        fragmentTransaction.commit();

                    }
                });
        barcodeLauncher.launch(new ScanOptions().setDesiredBarcodeFormats(ScanOptions.QR_CODE).setPrompt("Scan Event QR code")); // launch QR scanner

    }
}