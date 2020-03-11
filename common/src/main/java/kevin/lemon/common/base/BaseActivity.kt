package kevin.lemon.common.base

import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * Created by wxk on 2020/3/11.
 */
abstract class BaseActivity :ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}