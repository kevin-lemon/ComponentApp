package kevin.lemon.componentapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import kevin.lemon.common.base.BaseActivity
import kevin.lemon.order.OrderMainActivity
import kevin.lemon.personal.PersonalMainActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun order(view: View) {
        startActivity(Intent(this,OrderMainActivity::class.java))
    }

    fun personal(view: View) {
        startActivity(Intent(this,PersonalMainActivity::class.java))
    }
}
