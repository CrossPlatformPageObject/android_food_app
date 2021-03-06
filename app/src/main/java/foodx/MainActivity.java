package foodx;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import foodx.foodoo.R;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

  private static final int FOOD_LIST = 0;
  private static final int FOOD_DETAILS = 1;
  private static final int CART = 2;
  private static final int ADDRESS = 3;
  private static final int PAYMENT_PREFS = 4;
  private static final int CHECKOUT = 5;
  private static final int PURCHASE_SUMMARY = 6;
  private static final int CREDIT = 7;
  private ListView foodList;
  private ArrayList<FoodItem> foodItems;
  private ArrayAdapter<FoodItem> foodAdapter;
  private int screenIndex;
  private FoodItem selectedFoodItem;
  private ArrayList<FoodItem> cartItems;
  private ListView lvCartItems;
  private ArrayAdapter<FoodItem> cartAdapter;
  private double total;
  private String name;
  private String adddress;
  private int screenToShow;


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

    cartItems = new ArrayList<FoodItem>();
    foodItems = readFoodItemsFromJson();
    initializeViews();
    showScreen(FOOD_LIST);
  }

  private ArrayList<FoodItem> readFoodItemsFromJson() {
    String foodJson = loadJsonFromAssets();
    ArrayList<FoodItem> foods = new ArrayList<FoodItem>();
    try {
      JSONObject foodJsonObj = new JSONObject(foodJson);
      JSONArray foodJsonArr = foodJsonObj.getJSONArray("items");
      for (int i = 0; i < foodJsonArr.length(); i++) {
        JSONObject food = (JSONObject) foodJsonArr.get(i);
        FoodItem item = new FoodItem(food.getString("name"), Double.parseDouble(food.getString("price")));
        foods.add(item);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return foods;
  }

  private void initializeViews() {
    foodList = (ListView) findViewById(R.id.lvFoodItems);
    foodAdapter = new FoodItemAdapter<FoodItem>(this, R.layout.food_item, foodItems);
    foodList.setAdapter(foodAdapter);
    foodList.setOnItemClickListener(this);
    foodAdapter.notifyDataSetChanged();

    lvCartItems = (ListView) findViewById(R.id.lvCartItems);
    cartAdapter = new FoodItemAdapter<FoodItem>(this, R.layout.food_item, cartItems);
    lvCartItems.setAdapter(cartAdapter);
    lvCartItems.setOnItemClickListener(this);
    cartAdapter.notifyDataSetChanged();

    findViewById(R.id.btnAddToCart).setOnClickListener(this);
    findViewById(R.id.btnCheckout).setOnClickListener(this);
    findViewById(R.id.btnProceed).setOnClickListener(this);
    findViewById(R.id.btnPaymentPreference).setOnClickListener(this);
    findViewById(R.id.btnSubmitCreditCard).setOnClickListener(this);
    ((RadioGroup) findViewById(R.id.rgPayment)).setOnCheckedChangeListener(this);
  }

  private void showScreen(int screenIndex) {
    findViewById(R.id.vgFoodList).setVisibility(View.GONE);
    findViewById(R.id.vgFoodItemDetails).setVisibility(View.GONE);
    findViewById(R.id.vgCart).setVisibility(View.GONE);
    findViewById(R.id.vgAddress).setVisibility(View.GONE);
    findViewById(R.id.vgPayment).setVisibility(View.GONE);
    findViewById(R.id.vgCreditCard).setVisibility(View.GONE);
    findViewById(R.id.vgPurchaseSummary).setVisibility(View.GONE);

    this.screenIndex = screenIndex;
    switch (screenIndex) {
      case FOOD_LIST:
        findViewById(R.id.vgFoodList).setVisibility(View.VISIBLE);
        break;

      case FOOD_DETAILS:
        findViewById(R.id.vgFoodItemDetails).setVisibility(View.VISIBLE);
        break;

      case CART:
        cartItems.add(new FoodItem("Total", getTotal()));
        findViewById(R.id.vgCart).setVisibility(View.VISIBLE);
        cartAdapter.notifyDataSetChanged();
        break;

      case ADDRESS:
        findViewById(R.id.vgAddress).setVisibility(View.VISIBLE);
        break;

      case PAYMENT_PREFS:
        findViewById(R.id.vgPayment).setVisibility(View.VISIBLE);
        break;

      case CREDIT:
        findViewById(R.id.vgCreditCard).setVisibility(View.VISIBLE);
        break;

      case PURCHASE_SUMMARY:
        findViewById(R.id.vgPurchaseSummary).setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      if (screenIndex == FOOD_LIST) {
        super.onBackPressed();
      } else {
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
    } else {
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

  private void fillFoodDetails(FoodItem food) {
    ((TextView) findViewById(R.id.tvFoodTitle)).setText(food.getFoodName());
    ((TextView) findViewById(R.id.tvFoodPriceInDouble)).setText("" + food.getFoodPrice());
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnAddToCart:
        cartItems.add(selectedFoodItem);
        break;
      case R.id.btnCheckout:

        showScreen(ADDRESS);
        break;
      case R.id.btnProceed:
        showScreen(PAYMENT_PREFS);
        break;
      case R.id.btnSubmitCreditCard:
        showScreen(PURCHASE_SUMMARY);
        break;
      case R.id.btnPaymentPreference:
        showScreen(screenToShow);
        break;
    }
  }

  public String loadJsonFromAssets() {
    String json = null;
    try {
      InputStream is = getAssets().open("items.json");
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      json = new String(buffer, "UTF-8");
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    return json;
  }

  public double getTotal() {
    double total = 0.0d;
    for (int i = 0; i < cartItems.size(); i++) {
      total += cartItems.get(i).getFoodPrice();
    }
    return total;
  }

  @Override
  public void onCheckedChanged(RadioGroup group, int checkedId) {
    switch (checkedId) {
      case R.id.radio_cash:
        screenToShow = PURCHASE_SUMMARY;
        break;

      case R.id.radio_credit:
        screenToShow = CREDIT;
        break;
    }
  }


  public class FoodItemAdapter<F> extends ArrayAdapter<FoodItem> {
    private final Context context;
    private final ArrayList<FoodItem> data;
    private final int layoutResourceId;

    public FoodItemAdapter(Context context, int layoutResourceId, ArrayList<FoodItem> data) {
      super(context, layoutResourceId, data);
      this.context = context;
      this.data = data;
      this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View row = convertView;
      ViewHolder holder = null;

      if (row == null) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.food_item, parent, false);

        holder = new ViewHolder();
        holder.textView1 = (TextView) row.findViewById(R.id.tvFoodName);
        holder.textView2 = (TextView) row.findViewById(R.id.tvFoodPrice);

        row.setTag(holder);
      } else {
        holder = (ViewHolder) row.getTag();
      }

      FoodItem person = data.get(position);

      holder.textView1.setText(person.getFoodName());
      holder.textView2.setText(person.getFoodPrice() + "");

      return row;
    }

    class ViewHolder {
      TextView textView1;
      TextView textView2;
    }
  }
}
