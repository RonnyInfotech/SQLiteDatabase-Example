package com.example.employeedetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class EmployeeAdapter extends ArrayAdapter<Employee>
{
    Context mCtx;
    int layoutRes;
    List<Employee> employeeList;
    SQLiteDatabase mDatabase;

    public EmployeeAdapter(Context mCtx, int layoutRes, List<Employee> employeeList, SQLiteDatabase mDatabase){
        super(mCtx, layoutRes, employeeList);

        this.mCtx = mCtx;
        this.layoutRes = layoutRes;
        this.employeeList = employeeList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(layoutRes,null);

        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_age = view.findViewById(R.id.tv_age);
        TextView tv_designation = view.findViewById(R.id.tv_designation);
        TextView tv_department = view.findViewById(R.id.tv_department);
        TextView tv_salary = view.findViewById(R.id.tv_salary);

        final Employee employee = employeeList.get(position);

        tv_name.setText(employee.getName());
        tv_age.setText(employee.getAge());
        tv_designation.setText(employee.getDesignation());
        tv_department.setText(employee.getDepartment());
        tv_salary.setText(String.valueOf(employee.getSalary()));

        view.findViewById(R.id.Btn_Delete_Student).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deleteEmployee(employee);
            }
        });


        view.findViewById(R.id.Btn_Edit_Student).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                upDateEmployee(employee);
            }
        });

        return view;
    }

    private void upDateEmployee(final Employee employee)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater =  LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_employee,null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText Edt_Name = view.findViewById(R.id.Edt_Name);
        final EditText Edt_Age = view.findViewById(R.id.Edt_Age);
        final EditText Edt_Designation = view.findViewById(R.id.Edt_Designation);
        final EditText Edt_Department = view.findViewById(R.id.Edt_Department);
        final EditText Edt_Salary = view.findViewById(R.id.Edt_Salary);

        Edt_Name.setText(employee.getName());
        Edt_Age.setText(employee.getAge());
        Edt_Designation.setText(employee.getDesignation());
        Edt_Department.setText(employee.getDepartment());
        Edt_Salary.setText(String.valueOf(employee.getSalary()));


        view.findViewById(R.id.Btn_Update).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
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

                String sql = "UPDATE student SET name = ?, age = ?, designation = ?, department = ?, salary = ? WHERE id = ?";
                mDatabase.execSQL(sql,new String[]{Name, Age, Desg, Dept, Salary, String.valueOf(employee.getId())});
                Toast.makeText(mCtx, "Employee Updated", Toast.LENGTH_SHORT).show();
                loadEmployeesFromDatabaseAgain();
                alertDialog.dismiss();

            }
        });

    }

    private void deleteEmployee(final Employee employee)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String sql = "DELETE FROM student WHERE id = ?";
                mDatabase.execSQL(sql, new Integer[]{employee.getId()});
                loadEmployeesFromDatabaseAgain();
            }
        });

        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadEmployeesFromDatabaseAgain()
    {
        String sql = "SELECT * FROM student";

        Cursor cursor = mDatabase.rawQuery(sql,null);

        if (cursor.moveToFirst())
        {
            employeeList.clear();
            do{
                employeeList.add(new Employee(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(5)
                ));
            }while (cursor.moveToNext());

            cursor.close();
            Collections.reverse(employeeList);
            notifyDataSetChanged();
        }
    }
}
