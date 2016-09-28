package cn.edu.cdut.lm.mymuiscplayer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.adapter.SearchAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;
import cn.edu.cdut.lm.mymuiscplayer.widget.DividerItemDecoration;

/**
 * Created by LimiaoMaster on 2016/9/26 18:11
 */
public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private SearchView searchView;
    private InputMethodManager inputMethodManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        //toolbar.setPadding(0, 0, 0, 20);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        recyclerView = (RecyclerView) findViewById(R.id.search_content);
        //1
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //2
        adapter = new SearchAdapter(this,this);//注意第二个参数是getcontext()，而不是getAppcontext();
        recyclerView.setAdapter(adapter);
        //3
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputManager();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem menuItem = menu.findItem(R.id.item_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconified(false); //折叠为一个图标true，还是展开false。
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("搜索本地歌曲");
        menuItem.expandActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Mp3Info> list = MediaUtil.searchMp3InfoByKeyword(this,newText);
        if(!newText.trim().equals("")){

            adapter.getListByKeyword(list);
            adapter.notifyDataSetChanged();
        }else{
            list.clear();
            adapter.getListByKeyword(list);
            adapter.notifyDataSetChanged();
        }
        //hideInputManager();
        return false;
    }

    public void hideInputManager() {
        if (searchView != null) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            }
            searchView.clearFocus();
        }
    }
}
