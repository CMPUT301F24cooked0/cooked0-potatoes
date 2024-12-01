package com.example.myapplication.ui.facility;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.Event;
import com.example.myapplication.QRCode;
import com.example.myapplication.R;
import com.google.zxing.WriterException;


/**
 * This fragment is used to notify the user that the QR code has been generated and stored in the database.
 * It also provides the option to download the QR code and continue to the view events page.
 */
public class DownloadQRFragment extends Fragment {
    View view;
    Event event;
    QRCode eventQRCode;
    Button downloadBtn;
    Button continueBtn;
    ImageView qrCodeImage;
    FacilityViewModel facilityViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qr_download, container, false);
        return view;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        facilityViewModel = new ViewModelProvider(requireActivity()).get(FacilityViewModel.class);
        event = facilityViewModel.getEventToManage();
        eventQRCode = event.getQrCode();
        downloadBtn = view.findViewById(R.id.downloadButton);
        continueBtn = view.findViewById(R.id.continueButton);
        qrCodeImage = view.findViewById(R.id.qrCodeImage);

        try {
            qrCodeImage.setImageBitmap(eventQRCode.getImage());
            Toast.makeText(requireContext(), "QR Code Generated and Stored", Toast.LENGTH_SHORT).show(); // Notify the user that the QR code has been generated
        } catch (WriterException e) {
            Toast.makeText(requireContext(), "Error generating QR code", Toast.LENGTH_SHORT).show();
        }
        downloadBtn.setOnClickListener(this::onClickDownload);
        continueBtn.setOnClickListener(this::onClickContinue);

    }
    public void onClickDownload (View view) {
        // TODO download QR code to device


    }
    public void onClickContinue (View view) {
        // Navigate to the view events page
        Fragment fragment = new FacilityViewEventsFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new FacilityViewEventsFragment()).commit();
    }

}