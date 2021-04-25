package com.example.wordshelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
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


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu,menu);
        InputMethodManager inm =(InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        inm.showSoftInput(searchView,0);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.trim();
                filterList.removeObservers(requireActivity());
                if(newText.matches("[\u4e00-\u9fa5]+")){
                    filterList = viewModel.queryChinese(newText);
                }else if(newText.matches("[a-zA-Z]+")){
                    filterList = viewModel.queryEnglish(newText);
                }else {
                    filterList = viewModel.getList();
                }
                filterList.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    int tmp = -1;
                    @Override
                    public void onChanged(List<Word> wordList) {
                        if(tmp==wordList.size()){
                            return;
                        }
                        Log.d("看这里2","执行了这部分的监听");
                        if(viewModel.getIsRecyclerview().getValue()){
                            MyAdpater adpater = adpater_r;
                            adpater.setList(wordList);
                            changeView_R(adpater,wordList);
                        }else{
                            MyAdpater adpater = adpater_c;
                            adpater.setList(wordList);
                            changeView_C(adpater,wordList);
                        }
                        tmp = wordList.size();
                    }
                });
                inm.hideSoftInputFromWindow(getView().getWindowToken(),0);
                return true;
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.changeItem:
                viewModel.setIsRecyclerview(!viewModel.getIsRecyclerview().getValue());
                break;
            case R.id.deleteItem:
                AlertDialog.Builder builder =  new AlertDialog.Builder(requireActivity());
                builder.setTitle("清空数据");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteAll();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
            default:
                break;
        }
        return true;
    }

    private MyViewModel viewModel;

    private RecyclerView recyclerView;

    private LinearLayoutManager manager;

    private MyAdpater adpater_r, adpater_c;

    private List<Word> allWords;

    WindowManager wm;

    int height;

    MyTTs ts;


    /* 除非整个fragment被销毁，不如oldSize不会重新初始化，所以应该在回调方法中初始化
        这样每次回调oldSize都被重置，因为我们设置oldSize的目的是避免多次刷新，这是针对
        当前这个wordFragment页面来做的,切换回来应该不受影响
     */


    private LiveData<List<Word>> filterList;

    int oldSize;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oldSize = -1;
        recyclerView = getActivity().findViewById(R.id.fraRecycler);
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        filterList = viewModel.getList();
        filterList.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                allWords = words;
                if(viewModel.getIsRecyclerview().getValue()){
                    MyAdpater adpater = adpater_r;
                    List<Word> wordList = viewModel.getList().getValue();
                    if(wordList.size()==oldSize){
                        return;
                    }
                    Log.d("看这里1","执行了这部分的监听");
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
        viewModel.getIsRecyclerview().observe(requireActivity(), new Observer<Boolean>() {
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
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.START|ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Word word = allWords.get(viewHolder.getAdapterPosition());
                viewModel.delete(word);
                Snackbar.make(requireActivity().findViewById(R.id.wordsFragment),"删除了该词汇",
                        Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         viewModel.insert(word);
                    }
                }).show();
            }
        }).attachToRecyclerView(recyclerView);

        FloatingActionButton button = getActivity().findViewById(R.id.to_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(button);
                controller.navigate(R.id.action_wordsFragment_to_addFragment);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(requireActivity(),new ViewModelProvider.NewInstanceFactory()).get(MyViewModel.class);
        viewModel.init(getActivity());
        adpater_r = new MyAdpater(R.layout.word_item_update, viewModel.getList().getValue(),viewModel);
        adpater_c = new MyAdpater(R.layout.card_word_item, new ArrayList<Word>(),viewModel);

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
                   showBottomSheetDialog(wordList,position);
                }
            }
        });
        recyclerView.setAdapter(adpater);
    }


    void changeView_C(MyAdpater adpater, List<Word> wordList){
        adpater.addChildClickViewIds(R.id.Layout2);
        adpater.setOnItemChildClickListener((adapter, view, position) -> {
            if(view.getId()==R.id.Layout2){
                showBottomSheetDialog(wordList,position);
            }
        });
        recyclerView.setAdapter(adpater);
    }

    @SuppressLint("SetTextI18n")
    void showBottomSheetDialog(List<Word>wordList, int position){
        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog,null);
        TextView textWord = view.findViewById(R.id.wordText);
        TextView textUc = view.findViewById(R.id.userChinese);
        TextView textAc = view.findViewById(R.id.apiChinese);
        textUc.setText(wordList.get(position).getChinese());
        textAc.setText("abbr. （计算机）应用程序 (application)\n" +
                "n. (App) （美、德、巴）阿普（人名）");
        textWord.setText(wordList.get(position).getEnglish());
        Button speak = view.findViewById(R.id.button);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String english = textWord.getText().toString().trim();
                speakEnglish(english);
            }
        });
        textWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String english = textWord.getText().toString().trim();
                speakEnglish(english);
            }
        });
        DisplayMetrics metrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity(),R.style.MybottomSheetDialog);
        dialog.setCanceledOnTouchOutside(true);
        view.setMinimumHeight((int)(0.4*height));
        dialog.setContentView(view);
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
        dialog.show();
    }

    String getTranslation(String english){
        return "ok";
    }

    void speakEnglish(String english){
        ts = new MyTTs(requireActivity(),english);
    }

    class MyTTs implements TextToSpeech.OnInitListener{
        private TextToSpeech mSpeech;
        private Context mContext;
        private String text;
        @Override
        public void onInit(int status) {
            mSpeech.setPitch(0.5f);
            mSpeech.setSpeechRate(1.0f);
            mSpeech.speak(text,TextToSpeech.QUEUE_ADD,null);
        }

        MyTTs(Context mContext, String text){
            this.mContext = mContext;
            this.text = text;
            this.mSpeech = new TextToSpeech(mContext,this);
        }

        void stopTTs(){
            mSpeech.stop();
            mSpeech.shutdown();
            mSpeech = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(ts!=null){
            ts.stopTTs();
        }
    }
}