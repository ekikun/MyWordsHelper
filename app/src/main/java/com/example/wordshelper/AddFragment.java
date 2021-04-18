package com.example.wordshelper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    private EditText editC, editE;

    private MaterialButton button;

    private MyViewModel viewModel;

    private  InputMethodManager inm;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new MyViewModel();
        viewModel.init(requireActivity());
        editC = requireActivity().findViewById(R.id.editChinese);
        editE = requireActivity().findViewById(R.id.editEnglish);
        button = requireActivity().findViewById(R.id.btnadd);
        button.setEnabled(false);
        inm =(InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inm.showSoftInput(editE,0);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String chinese = editC.getText().toString().trim();
                String english = editE.getText().toString().trim();
                if(!chinese.isEmpty()&&!english.isEmpty()){
                    button.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editE.addTextChangedListener(textWatcher);
        editC.addTextChangedListener(textWatcher);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chinese = editC.getText().toString().trim();
                String english = editE.getText().toString().trim();
                Word word = new Word();
                word.setEnglish(english);
                word.setChinese(chinese);
                word.setInv_chinese(true);
                viewModel.insert(word);
                viewModel.setIsRecyclerview(true);
                NavController controller = Navigation.findNavController(button);
                controller.navigateUp();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        inm.hideSoftInputFromWindow(getView().getWindowToken(),0);
    }
}