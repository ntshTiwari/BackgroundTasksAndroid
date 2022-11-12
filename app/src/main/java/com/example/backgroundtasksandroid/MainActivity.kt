package com.example.backgroundtasksandroid

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/// we will use coroutines here to do tasks in background execution
/// coroutines have different scopes
/// for eg:
    /// viewModelScope => will automatically be cancelled when the viewModel is closed/cleared
    /// lifeCycleScope => will run the coroutine in the life cycle of the activity/fragment
    ///                   will be cleared when the lifecycle is destroyed
    /// customScope => create a Job() variable, create a scope and attach a job to it and clear the couroutine when the job is done

class MainActivity : AppCompatActivity() {
    var dialog: Dialog? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            showLoader()
            lifecycleScope.launch{
                executeATask()
            }
        }
    }

    /// adding suspend makes the executeATask a coroutine function
    private suspend fun executeATask() {
        /// Calls the specified suspending block with a given coroutine context
        /// withContext moves the task to a new thread, in this case it is the IO thread, not blocking the main thread
        withContext(Dispatchers.IO){
            for(i in 1..100000){
                Log.e("delay : ", "" +i)
            }

            runOnUiThread{
                hideLoader()
                /// Toast is not a background task, it can only run in the UI thread
                /// to get the context of an activity we add @Activity name after this like this@MainActivity
                Toast.makeText(this@MainActivity, "DONE", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoader() {
        dialog = Dialog(this@MainActivity);
        dialog!!.setContentView(R.layout.dialog_loader)
        dialog!!.show()
    }

    private fun hideLoader() {
        dialog!!.dismiss()
        dialog = null
    }
}