package kevin.lemon.personal

import android.content.Intent
import android.os.Bundle
import android.view.View
import kevin.lemon.common.base.BaseActivity

class PersonalMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_activity_main)
    }

    fun order(view: View) {
        val classPath = "kevin.lemon.order.OrderMainActivity"
        val clazz = Class.forName(classPath)
        startActivity(Intent(this,clazz))
    }
}
