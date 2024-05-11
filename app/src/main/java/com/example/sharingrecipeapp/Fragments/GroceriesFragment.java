

package com.example.sharingrecipeapp.Fragments;

import static android.widget.Toast.makeText;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sharingrecipeapp.Activities.BottomNavigationCustomActivity;
import com.example.sharingrecipeapp.Adapters.Home.IClickOnItemRecipe;
import com.example.sharingrecipeapp.Adapters.ListInAdapter;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.IClickOnItemSavedRecipe;
import com.example.sharingrecipeapp.Adapters.NguyenLieu.ReGroAdapter;
import com.example.sharingrecipeapp.Classes.ListIngredient;
import com.example.sharingrecipeapp.Classes.ReGro;
import com.example.sharingrecipeapp.Classes.Recipes;
import com.example.sharingrecipeapp.databinding.FragmentGroceriesBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroceriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroceriesFragment extends Fragment {


    public GroceriesFragment() {
        // Required empty public constructor
    }

    public static GroceriesFragment newInstance(String param1, String param2) {
        GroceriesFragment fragment = new GroceriesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    FirebaseFirestore db;
    FirebaseAuth auth;
    BottomNavigationCustomActivity bottomNavigationCustomActivity;
    String userID;
    ListView ingListView;
    ArrayList<ListIngredient> arrayListIng;
    ListInAdapter adapter;
    ImageView dot, plus;
    RecyclerView recyclerView_re ;
    ArrayList<ReGro> arrayRecipe;
    ReGroAdapter regroadapter;
    private FragmentGroceriesBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroceriesBinding.inflate(inflater,container,false);

        bottomNavigationCustomActivity = (BottomNavigationCustomActivity) getActivity();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userID = auth.getUid();
        ingListView = binding.listGroceries;
        recyclerView_re = binding.recyRecipeGroceries;
        dot = binding.btn;
        plus = binding.plus;
        AnhXa();




        //khi nhan 3 cham se hien ra thong bao de xac dinh xoa het tat ca
        // Xoa khi nhan day 3 cham
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveAll();
            }
        });

        //nhan plus thi add them ngieu lieu
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNew();
            }
        });

        ingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UpdateDelete(position);

            }
        });




        return binding.getRoot();
    }

    private void AnhXa(){
        displaySavedRecipes();

        arrayListIng =new ArrayList<>();
        arrayListIng.add(new ListIngredient("avocado","4", R.drawable.avocado, false));
        arrayListIng.add(new ListIngredient("baguette","2", R.drawable.baguette, false));
        arrayListIng.add(new ListIngredient("broccoli", "5", R.drawable.broccoli, false));
        arrayListIng.add(new ListIngredient("cabbage","3", R.drawable.cabbage, false));
        arrayListIng.add(new ListIngredient("carrot","2", R.drawable.carrot, false));
        arrayListIng.add(new ListIngredient("cheese","4", R.drawable.cheese, false));
        arrayListIng.add(new ListIngredient("corn","3", R.drawable.corn, false));
        arrayListIng.add(new ListIngredient("crab","4", R.drawable.crab,false));
        arrayListIng.add(new ListIngredient("eggs","4", R.drawable.eggs, false));
        arrayListIng.add(new ListIngredient("fish", "2", R.drawable.fish,false));
        arrayListIng.add(new ListIngredient("milk", "3", R.drawable.milk, false));

        adapter = new ListInAdapter(binding.getRoot().getContext(), R.layout.list_ingredients, arrayListIng);
        ingListView.setAdapter(adapter);

    }

    private void displaySavedRecipes() {
        arrayRecipe = new ArrayList<>();
        //arrayRecipe.add(new ReGro("MucNhoiThit","Cut","https://firebasestorage.googleapis.com/v0/b/fantafood-3ea80.appspot.com/o/Recipes%2Fmuc_nhoi_TB.jpg?alt=media&token=f0949f1b-611d-4cd0-918b-907a2bda317b"));

        regroadapter = new ReGroAdapter();
        regroadapter.setData(arrayRecipe, new IClickOnItemSavedRecipe() {
            @Override
            public void onClickItemSavedRecipe(String id) {
                onClickGoToDetailFood(id);
            }

        });
        recyclerView_re.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView_re.setAdapter(regroadapter);

        db.collection("SaveRecipes").whereArrayContains("idUsers",userID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){

                        List<String> tenRecipes = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                            tenRecipes.add(documentSnapshot.get("Recipes").toString());
                        }
                        for (String i : tenRecipes){
                            db.collection("Recipes").document(i).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        String image = documentSnapshot.getString("image");
                                        String name = documentSnapshot.getString("name");

                                        ReGro recipes = new ReGro(i,name,image);
                                        arrayRecipe.add(recipes);
                                        regroadapter.notifyDataSetChanged();
                                    });
                        }
                    }
                });
    }

    private void onClickGoToDetailFood(String id) {
        bottomNavigationCustomActivity.gotoFoodDetail(id);
    }

    private void RemoveAll (){
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(binding.getRoot().getContext());
        alertDialog.setTitle("Thông báo");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Bạn có muốn xóa hết nguyên liệu không?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                arrayListIng.clear();
                adapter.notifyDataSetInvalidated();

            }
        });

        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }


    private void AddNew(){
        Dialog dialog = new Dialog(binding.getRoot().getContext());
        // phai set title truoc  setContent
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_list);
        dialog.setCanceledOnTouchOutside(true);
        EditText edtName= (EditText) dialog.findViewById(R.id.edtName);
        EditText edtQuan= (EditText) dialog.findViewById(R.id.edtQuan);
        EditText edtDv= (EditText) dialog.findViewById(R.id.edtdv);
        Button btnCancel =(Button) dialog.findViewById(R.id.cancel);
        Button btnConfirm = (Button) dialog.findViewById(R.id.confirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= edtName.getText().toString().trim();
                String quan= edtQuan.getText().toString().trim();
                String dv = edtDv.getText().toString().trim();
                String quandv= quan+" "+dv;
                arrayListIng.add(new ListIngredient(name, quandv));
                adapter.notifyDataSetInvalidated();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void UpdateDelete(int position){
        Dialog dialog = new Dialog(binding.getRoot().getContext());
        // phai set title truoc  setContent
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_delete);
        dialog.setCanceledOnTouchOutside(true);
        EditText edtQuan = (EditText) dialog.findViewById(R.id.edtQuanSua);
        EditText edtDv = (EditText) dialog.findViewById(R.id.edtdvSua);
        View cancel = (View) dialog.findViewById(R.id.cancelview);
        Button delete = (Button) dialog.findViewById(R.id.delete);
        Button update = (Button) dialog.findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quan= edtQuan.getText().toString().trim();
                String dv = edtDv.getText().toString().trim();
                String quandv= quan+ " " +dv;
                arrayListIng.get(position).setQuantityIn(quandv);
                adapter.notifyDataSetInvalidated();
                dialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayListIng.remove(position);
                adapter.notifyDataSetInvalidated();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }






}