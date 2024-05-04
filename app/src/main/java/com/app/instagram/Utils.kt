package com.app.instagram

import android.content.Context
import android.text.format.DateFormat
import android.widget.Toast

import java.util.Calendar
import java.util.Locale

// A class that will contain static functions, constants, variables that we will be used in whole application
object Utils {

    // Gender List
    val genderList = listOf("Male", "Female", "Custom", "Prefer not to say")

    /**
     ** A function to show toast

     * @param context the context of activity/fragment where this function will be called
     * @param message the message to be be shown in the toast
     * */

    fun toast(context: Context, message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    /**
     ** A function to get Current timestamp

     * @return Return the current timestamp as long datatype
     * */

    fun getTimeStamp() :Long{

        return System.currentTimeMillis()
    }


    /**
     ** A function to show date

     * @param timestamp the timestamp of type long that we need to format dd/MM/yyyy
     * @return timestamp formatted to date dd/MM/yyyy
     *
     * */


    fun formatTimeStamp(timestamp:Long) : String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp

        return DateFormat.format("dd/MM/yyyy", calendar).toString()
    }
}