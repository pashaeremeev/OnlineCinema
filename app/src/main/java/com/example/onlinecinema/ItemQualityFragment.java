package com.example.onlinecinema;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecinema.entities.Quality;

import java.util.ArrayList;

public class ItemQualityFragment extends DialogFragment {

    public static final String BUNDLE_KEY =  "qualityDialogKey";
    public static final String REQUEST_KEY =  "qualityRequestKey";
    public static final String LIST_BUNDLE_KEY = "LIST_BUNDLE_KEY";
    public static boolean isExist;

    public static ItemQualityFragment getInstance(ArrayList<Quality> list, int current) {
        ItemQualityFragment itemQualityFragment = new ItemQualityFragment();
        list.get(current).setCurrent(1);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LIST_BUNDLE_KEY, list);
        itemQualityFragment.setArguments(bundle);
        isExist = true;
        return itemQualityFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quality_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.qualityList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        ArrayList<Quality> qualities = (getArguments() != null) ? getArguments().getParcelableArrayList(LIST_BUNDLE_KEY) : new ArrayList<>();

        recyclerView.setAdapter(new MyItemQualityAdapter(
                view.getContext(),
                qualities,
                quality -> clickOnQuality(quality)
        ));
        return view;
    }

    private Void clickOnQuality(Quality quality) {
        Bundle result = new Bundle();
        result.putInt(BUNDLE_KEY, quality.getIndex());
        requireActivity().getSupportFragmentManager().setFragmentResult(REQUEST_KEY, result);
        dismiss();
        isExist = false;
        return null;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        isExist = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        window.setBackgroundDrawable(
                getResources().getDrawable(R.drawable.quality_bg, getContext().getTheme()));
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 55;
        params.y = 100;
        window.setAttributes(params);
    }
}
