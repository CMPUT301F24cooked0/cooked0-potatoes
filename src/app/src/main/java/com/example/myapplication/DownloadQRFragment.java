package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * This fragment is used to notify the user that the QR code has been generated and stored in the database.
 * It also provides the option to download the QR code and continue to the view events page.
 */
public class DownloadQRFragment extends Fragment {
    View view;
    QRCode eventQRCode;
    Button downloadBtn;
    Button continueBtn;
    ImageView qrCodeImage;

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
        downloadBtn = view.findViewById(R.id.downloadButton);
        continueBtn = view.findViewById(R.id.continueButton);
        qrCodeImage = view.findViewById(R.id.qrCodeImage);
        // Notify the user that the QR code has been generated
        Toast.makeText(requireContext(), "QR Code Generated and Stored", Toast.LENGTH_SHORT).show(); // TODO change style of pop up message
        // eventQRCode = (QRCode) getArguments().getSerializable("qrCode"); // TODO get qr code from bundle once navigation is completed
//        try { // TODO set qr image once navigation complete
//            qrCodeImage.setImageBitmap(eventQRCode.getImage());
//        } catch (WriterException e) {
//            throw new RuntimeException(e);
//        }
        downloadBtn.setOnClickListener(this::onClickDownload);
        continueBtn.setOnClickListener(this::onClickContinue);

    }
    public void onClickDownload (View view) {
        // TODO download QR code


    }
    public void onClickContinue (View view) {
//        Fragment fragment = new FacilityViewEventsFragment();
//        FragmentManager fragmentManager = getParentFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit(); // TODO continue to view events page when navigation complete
    }

}