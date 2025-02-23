package com.digiview.workwell.ui.routine.execution;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiview.workwell.R;
import com.digiview.workwell.databinding.FragmentReminderBinding;

public class ReminderFragment extends Fragment {

    private ReminderViewModel mViewModel;
    private FragmentReminderBinding reminderBinding;

    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        reminderBinding = FragmentReminderBinding.inflate(inflater, container, false);
        return reminderBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(ReminderViewModel.class);
        // TODO: Use the
        reminderBinding.proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.setReminderState(RoutineConstants.REMINDER_STATE.ACCEPTED);
            }
        });
    }

}