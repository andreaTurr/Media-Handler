package it.unimib.exercise.andrea.mediahandler.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.unimib.exercise.andrea.mediahandler.R;

/**
 * create an instance of this fragment.
 */
public class FragmentLogin extends Fragment {

    public FragmentLogin() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonRegister = view.findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(view1 -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_fragment_login_to_activityMain);
        });
        Button buttonForgot = view.findViewById(R.id.button_forgot_password);
        buttonForgot.setOnClickListener(view1 -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_fragment_login_to_fragmentLoginAuth);
        });

    }
}