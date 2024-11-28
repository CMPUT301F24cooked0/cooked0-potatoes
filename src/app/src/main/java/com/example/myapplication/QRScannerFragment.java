package com.example.myapplication;

import android.graphics.Bitmap;
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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.time.Instant;
import java.util.HashMap;


/**
 * This fragment allows the user to scan a QR code that links to an event.
 */
public class QRScannerFragment extends Fragment implements OnSingleEventFetchListener {
    View view;
    Button scanBtn;
    Event eventToView;
    FirebaseFirestore db;
    DocumentReference eventRef;
    HashMap<String, Object> eventData;
    DatabaseManager databaseManager;


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
        this.eventToView = null;

    }
    public void onClickScan(View view) {
        ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
                result -> {
                    if (result.getContents() == null) { // TODO handle other qr code texts (not in our database path format)
                        Toast.makeText(requireContext(), "QR code contents cannot be read", Toast.LENGTH_LONG).show();
                    } else {
                        new DatabaseManager().getSingleEvent(result.getContents(), this); // get event from database
                        if (this.eventToView == null) { // event not found in database or qr code is not correct
                            Toast.makeText(requireContext(), "Event not found", Toast.LENGTH_LONG).show();
                        } else {
                            // navigate to view event details fragment to show event details
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            ViewEventDetailsFragment viewEventDetailsFragment = new ViewEventDetailsFragment();
                            // TODO add event and event path to viewmodel
                            fragmentTransaction.replace(R.id.fragment_container, viewEventDetailsFragment);
                            fragmentTransaction.commit();

                        }

                    }
                });
        barcodeLauncher.launch(new ScanOptions().setDesiredBarcodeFormats(ScanOptions.QR_CODE).setPrompt("Scan Event QR code")); // launch QR scanner

    }
    @Override
    public void onSingleEventFetch(Event event) {
        this.eventToView = event;
    }

//    public Event loadEventDetails(String eventPath) {
//        db = FirebaseFirestore.getInstance();
//        eventRef = db.document(eventPath);
//        eventRef.get().addOnSuccessListener(documentSnapshot -> {
//            // if the document exists, get the data otherwise set it to null
//            if (documentSnapshot.exists()) {
//                    eventData = (HashMap<String, Object>) documentSnapshot.getData();
//            } else {
//                eventData = null;
//            }
//            });
//            if (eventData != null) {
//                // get event data from document
//                Object nameTemp = eventData.get(DatabaseEventFieldNames.name.name());
//                Object dateTemp = eventData.get(DatabaseEventFieldNames.instant.name());
//                Object eventPosterTemp = eventData.get(DatabaseEventFieldNames.eventPoster.name());
//                Object qrCodeTemp = eventData.get(DatabaseEventFieldNames.qrCode.name());
//                QRCode qrCode = (QRCode) qrCodeTemp; // QR Code converted to object to easily check for id later on if it is stored here
//                Object capacityTemp = eventData.get(DatabaseEventFieldNames.capacity.name());
//                // check if all required fields exist and qr code is correct
//                if (nameTemp == null || dateTemp == null || eventPosterTemp == null || qrCodeTemp == null) {
//                    return null;
//                } else {
//                    String eventName = (String) nameTemp;
//                    Instant eventDate = (Instant) dateTemp;
//                    String encodedEventPoster = (String) eventPosterTemp;
//                    Bitmap eventPoster = BitmapConverter.StringToBitmap(encodedEventPoster);
//                    Integer capacity = (Integer) capacityTemp;
//                    try {
//                        return new Event(eventName, eventDate, eventPoster, capacity); // create event object
//                    } catch (Exception e) {
//                        return null;
//                    }
//
//                }
//
//            }
//            return null;
//        }

}