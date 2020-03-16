package kevin.lemon.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import kevin.lemon.common.base.BaseActivity
import kevin.lemon.wrouter_annotation.WRouter

@WRouter("/Order/PersonalMainActivity")
class OrderMainActivity : BaseActivity() {
    @BindView(R2.id.jumpBtn)
    lateinit var btm:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)
        setContentView(R.layout.order_activity_main)
    }

    fun personal(view: View) {
        val classPath = "kevin.lemon.personal.PersonalMainActivity"
        val clazz = Class.forName(classPath)
        startActivity(Intent(this,clazz))
    }
}
