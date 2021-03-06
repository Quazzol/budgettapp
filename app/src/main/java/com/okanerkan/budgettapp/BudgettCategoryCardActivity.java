package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.okanerkan.dll.BindingManager;
import com.okanerkan.globals.ExceptionHandler;
import com.okanerkan.globals.KNGlobal;
import com.okanerkan.globals.Message;
import com.okanerkan.sqlite.model.BudgettCategory;
import com.okanerkan.sqlite.model_list.BudgettCategoryList;

public class BudgettCategoryCardActivity extends AppCompatActivity {

    private Button mSaveButton;
    private Button mDeleteButton;
    private RadioGroup mEntryTypeRadio;
    private EditText mBudgettCategoryCode;
    private BudgettCategory mBudgettCategory;
    private BindingManager mBindingManager;

    private static String TAG = "CategoryCard";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_category_card);
        this.SetProperties();
        this.CreateBudgetCategory();
        this.LoadBudgettCategory();
        this.SetCancelButtonText();
        this.CreateBindingManager();
        this.AddEventHandlers();
    }

    private void SetProperties()
    {
        this.mEntryTypeRadio = (RadioGroup) findViewById(R.id.rdgEntryType);
        this.mSaveButton = (Button) findViewById(R.id.btnSave);
        this.mDeleteButton = (Button) findViewById(R.id.btnDelete);
        this.mBudgettCategoryCode = (EditText) findViewById(R.id.budgettCategory);
    }

    private void CreateBudgetCategory()
    {
        this.mBudgettCategory = new BudgettCategory();
    }

    private void LoadBudgettCategory()
    {
        try
        {
            Intent intent = getIntent();
            String id = intent.getStringExtra("BudgettCategoryID");
            this.mBudgettCategory.setID(id);
            this.mBudgettCategory.Load();
        }
        catch (Exception ex)
        {
            ExceptionHandler.HandleException(TAG, ex);
        }
    }

    private void CreateBindingManager()
    {
        try
        {
            this.mBindingManager = new BindingManager(this.mBudgettCategory);
            this.mBindingManager.Add(this.mEntryTypeRadio);
            this.mBindingManager.Add(this.mBudgettCategoryCode);
            this.mBindingManager.Initialize();
        }
        catch (Exception ex)
        {
            ExceptionHandler.HandleException(TAG, ex);
        }
    }

    private void SetCancelButtonText()
    {
        this.mDeleteButton.setText(this.mBudgettCategory.ExistInDB() ? R.string.Delete : R.string.Cancel);
    }

    private void AddEventHandlers()
    {
        this.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveButtonClicked(view);
            }
        });

        this.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnDeleteButtonClicked(view);
            }
        });
    }

    public void OnSaveButtonClicked(View view)
    {
        try
        {
            this.mBudgettCategory.Save();
            this.CreateBudgetCategory();
            this.SetCancelButtonText();
            this.mBindingManager.Rebind(this.mBudgettCategory);
            this.mBindingManager.BindValues();
            Message.Show(R.string.MSGSaved);
        }
        catch (Exception ex)
        {
            ExceptionHandler.HandleException(TAG, ex);
        }
    }

    public void OnDeleteButtonClicked(View view)
    {
        try
        {
            this.mBudgettCategory.Delete();
            this.finish();
        }
        catch (Exception ex)
        {
            ExceptionHandler.HandleException(TAG, ex);
        }
    }

}
