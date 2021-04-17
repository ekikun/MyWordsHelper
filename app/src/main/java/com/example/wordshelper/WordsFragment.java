package com.example.wordshelper;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.PrimaryKey;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordsFragment newInstance(String param1, String param2) {
        WordsFragment fragment = new WordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private MyViewModel viewModel;

    private RecyclerView recyclerView;

    private LinearLayoutManager manager;

    private MyAdpater adpater_r, adpater_c;

    int oldSize = 1;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity(),new ViewModelProvider.NewInstanceFactory()).get(MyViewModel.class);
        viewModel.init(getActivity());
        viewModel.setIsRecyclerview(true);
        recyclerView = getActivity().findViewById(R.id.fraRecycler);
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adpater_r = new MyAdpater(R.layout.word_item_update, new ArrayList<Word>(),viewModel);
        adpater_c = new MyAdpater(R.layout.card_word_item, new ArrayList<Word>(),viewModel);
        viewModel.getList().observe(getActivity(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if(viewModel.getIsRecyclerview().getValue()){
                    MyAdpater adpater = adpater_r;
                    List<Word> wordList = viewModel.getList().getValue();
                    if(wordList.size()==oldSize){
                        return;
                    }
                    adpater.setList(wordList);
                    changeView_R(adpater,wordList);
                    oldSize = wordList.size();
                }else{
                    MyAdpater adpater = adpater_c;
                    List<Word> wordList = viewModel.getList().getValue();
                    if(wordList.size()==oldSize){
                        return;
                    }
                    adpater.setList(wordList);
                    changeView_C(adpater,wordList);
                    oldSize = wordList.size();
                }
            }
        });
        viewModel.getIsRecyclerview().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                MyAdpater adpater;
                List<Word> wordList = viewModel.getList().getValue();
                if(viewModel.getIsRecyclerview().getValue()){
                    adpater = adpater_r;
                    adpater.setList(wordList);
                    changeView_R(adpater, wordList);
                }else{
                    adpater = adpater_c;
                    adpater.setList(wordList);
                    changeView_C(adpater,wordList);
                }

            }
        });
        Switch sWtich = requireActivity().findViewById(R.id.card_recycler);
        sWtich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    viewModel.setIsRecyclerview(false);
                }else {
                    viewModel.setIsRecyclerview(true);
                }
            }
        });
        FloatingActionButton button = getActivity().findViewById(R.id.to_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(button);
                controller.navigate(R.id.action_wordsFragment_to_addFragment);
            }
        });
        Button button2 = requireActivity().findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteAll();
            }
        });
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
        return inflater.inflate(R.layout.fragment_words, container, false);
    }

    void changeView_R(MyAdpater adpater, List<Word> wordList){
        adpater.addChildClickViewIds(R.id.Layout1);
        adpater.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.Layout1) {
                    System.out.println("被点击");
                    Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q="+wordList.get(position).getEnglish());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adpater);
    }

    void changeView_C(MyAdpater adpater, List<Word> wordList){
        adpater.addChildClickViewIds(R.id.Layout2);
        adpater.setOnItemChildClickListener((adapter, view, position) -> {
            if(view.getId()==R.id.Layout2){
                Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q="+wordList.get(position).getEnglish());
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(uri);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adpater);
    }
}