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
import com.digiview.workwell.databinding.FragmentConfirmationStartRoutineBinding;

public class ConfirmationStartRoutineFragment extends Fragment {

    private ConfirmationStartRoutineViewModel mViewModel;
    private FragmentConfirmationStartRoutineBinding confirmationStartRoutineBinding;

    public static ConfirmationStartRoutineFragment newInstance() {
        return new ConfirmationStartRoutineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        confirmationStartRoutineBinding = FragmentConfirmationStartRoutineBinding.inflate(inflater, container, false);
        return confirmationStartRoutineBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(ConfirmationStartRoutineViewModel.class);
        // TODO: Use the ViewModel
        confirmationStartRoutineBinding
                .confirmationNoButton
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewModel.setConfirmationState(RoutineConstants.CONFIRMATION_STATE.REJECTED);
                            }
                        }
                );

        confirmationStartRoutineBinding
                .confirmationYesButton
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewModel.setConfirmationState(RoutineConstants.CONFIRMATION_STATE.ACCEPTED);
                            }
                        }
                );
    }

}