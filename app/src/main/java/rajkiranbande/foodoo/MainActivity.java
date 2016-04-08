package rajkiranbande.foodoo;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private static final int FOOD_LIST = 0;
    private static final int FOOD_DETAILS = 1;
    private static final int CART = 2;
    private ListView foodList;
    private String[] foodItems = new String[]{"Filter Coffee","Idli","Masala Dosa","Pani Puri","Bhel","Roti Sabzi","Raj Kachori","Subway Sandwich","Kadhi Chaval","Dal Makhani","Plain Dal","Ice cream","Sheera","Brownie","Cold Coffee","Lime Juice","Masala Chai"};
    private ArrayAdapter<String> foodAdapter;
    private int screenIndex;
    private String selectedFoodItem;
    private ArrayList<String> cartItems;
    private ListView lvCartItems;
    private ArrayAdapter<String> cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cartItems = new ArrayList<String>();
        initializeViews();
        showScreen(FOOD_LIST);
    }

    private void initializeViews() {
        foodList = (ListView)findViewById(R.id.lvFoodItems);
        foodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foodItems);
        foodList.setAdapter(foodAdapter);
        foodList.setOnItemClickListener(this);
        foodAdapter.notifyDataSetChanged();

        lvCartItems = (ListView) findViewById(R.id.lvCartItems);
        cartAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cartItems);
        lvCartItems.setAdapter(cartAdapter);
        lvCartItems.setOnItemClickListener(this);
        cartAdapter.notifyDataSetChanged();

        findViewById(R.id.btnAddToCart).setOnClickListener(this);
    }

    private void showScreen(int screenIndex) {
        findViewById(R.id.vgFoodList).setVisibility(View.GONE);
        findViewById(R.id.vgFoodItemDetails).setVisibility(View.GONE);
        findViewById(R.id.vgCart).setVisibility(View.GONE);

        this.screenIndex = screenIndex;
        switch (screenIndex) {
            case FOOD_LIST:
                findViewById(R.id.vgFoodList).setVisibility(View.VISIBLE);
                break;

            case FOOD_DETAILS:
                findViewById(R.id.vgFoodItemDetails).setVisibility(View.VISIBLE);
                break;

            case CART:
                findViewById(R.id.vgCart).setVisibility(View.VISIBLE);
                cartAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(screenIndex == FOOD_LIST) {
                super.onBackPressed();
            } else{
                showScreen(FOOD_LIST);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_food_list) {
            showScreen(FOOD_LIST);
        } else if (id == R.id.nav_cart) {
            showScreen(CART);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showScreen(FOOD_DETAILS);
        selectedFoodItem = foodAdapter.getItem(position);
        fillFoodDetails(selectedFoodItem);
    }

    private void fillFoodDetails(String foodTitle) {
        ((TextView)findViewById(R.id.tvFoodTitle)).setText(foodTitle);
    }

    @Override
    public void onClick(View v) {
        cartItems.add(selectedFoodItem);
    }
}
