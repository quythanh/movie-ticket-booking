package com.example.movie_ticket_booking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Common.IReloadOnDestroy;
import com.example.movie_ticket_booking.Components.ProductAdapter;
import com.example.movie_ticket_booking.Controllers.ProductController;
import com.example.movie_ticket_booking.Models.Product;
import com.example.movie_ticket_booking.R;

import java.util.ArrayList;

import lombok.Getter;

public class ManageProducts extends Fragment implements IReloadOnDestroy {
    private static ManageProducts _instance = null;
    @Getter private static final MutableLiveData<Product> selectedProduct = new MutableLiveData<>();

    private ImageView mBtnAdd, mBtnBack;
    private GridView mGridProducts;

    private ProductController _controller;
    private ProductAdapter productAdapter;

    private ManageProducts() {
        super(R.layout.frag_admin_manage_products);
    }

    public static ManageProducts getInstance() {
        if (_instance == null)
            _instance = new ManageProducts();
        return _instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        bindingViews(view);
        initData();
        reload(); // Load view data
        setupViews();
        return view;
    }

    private void initData() {
        _controller = ProductController.getInstance();
        productAdapter = new ProductAdapter(new ArrayList<>());
    }

    private void bindingViews(View view) {
        mBtnAdd = view.findViewById(R.id.btn_add);
        mBtnBack = view.findViewById(R.id.btn_back);
        mGridProducts = view.findViewById(R.id.grid_products);
    }

    private void setupViews() {
        mGridProducts.setAdapter(productAdapter);
        mBtnAdd.setOnClickListener(_v -> {
            AddProduct dialog = new AddProduct();
            dialog.show(getChildFragmentManager(), "Dialog");
        });
        mBtnBack.setOnClickListener(_view -> getParentFragmentManager().popBackStack());
        mGridProducts.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedProduct.setValue(productAdapter.getItem(i));
            EditProduct dialog = new EditProduct();
            dialog.show(getChildFragmentManager(), "Dialog");
        });
    }

    @Override
    public void reload() {
        _controller.getAll().observe(getViewLifecycleOwner(), _prods -> {
            productAdapter.setList(_prods);
        });
    }
}
