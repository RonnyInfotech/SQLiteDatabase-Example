package com.example.employeedetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public  static final String DATABASE_NAME = "Bhautik";
    SQLiteDatabase mDatabase;

    private EditText Edt_Name,Edt_Age,Edt_Designation,Edt_Department,Edt_Salary;
    Button Btn_Add,Btn_Show;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);

        Edt_Name = findViewById(R.id.Edt_Name);
        Edt_Age = findViewById(R.id.Edt_Age);
        Edt_Designation = findViewById(R.id.Edt_Designation);
        Edt_Department = findViewById(R.id.Edt_Department);
        Edt_Salary = findViewById(R.id.Edt_Salary);

        findViewById(R.id.Btn_Add).setOnClickListener(this);
        findViewById(R.id.Btn_Show).setOnClickListener(this);

        createTable();
    }
    private void createTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS student (\n" +
                "    id INTEGER PRIMARY KEY,\n" +
                "    name varchar(200) NOT NULL,\n" +
                "    age varchar(200) NOT NULL,\n" +
                "    designation varchar(200) NOT NULL,\n" +
                "    department varchar(200) NOT NULL,\n" +
                "    salary double NOT NULL\n" +
                ");";

        mDatabase.execSQL(sql);
    }

    private void addEmployee()
    {
        String Name = Edt_Name.getText().toString().trim();
        String Age = Edt_Age.getText().toString().trim();
        String Desg = Edt_Designation.getText().toString().trim();
        String Dept = Edt_Department.getText().toString().trim();
        String Salary = Edt_Salary.getText().toString().trim();

        if (Name.isEmpty()) {
            Edt_Name.setError("Please enter a name");
            Edt_Name.requestFocus();
            return ;
        }

        if (Age.isEmpty()) {
            Edt_Name.setError("Please enter a age");
            Edt_Name.requestFocus();
            return ;
        }

        if (Desg.isEmpty()) {
            Edt_Name.setError("Please enter a designation");
            Edt_Name.requestFocus();
            return ;
        }
        if (Dept.isEmpty()) {
            Edt_Name.setError("Please enter a department");
            Edt_Name.requestFocus();
            return ;
        }

        if (Salary.isEmpty()) {
            Edt_Salary.setError("Please enter salary");
            Edt_Salary.requestFocus();
            return ;
        }

        String sql = "INSERT INTO student(name, age, designation, department, salary)"+
                "VALUES (?, ?, ?, ?, ?)";

        mDatabase.execSQL(sql, new String[]{Name, Age, Desg, Dept, Salary});

        Toast.makeText(this, "Employee Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Btn_Add:

                addEmployee();
                
                break;
            case R.id.Btn_Show:

                startActivity(new Intent(this, EmployeeActivity.class));

                break;
        }
    }
}
