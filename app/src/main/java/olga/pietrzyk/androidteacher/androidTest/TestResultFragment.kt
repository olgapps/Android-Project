package olga.pietrzyk.androidteacher.androidTest


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import olga.pietrzyk.androidteacher.R
//import olga.pietrzyk.androidteacher.TestResultFragmentArgs
import olga.pietrzyk.androidteacher.databinding.FragmentTestResultBinding

/**
 * A simple [Fragment] subclass.
 */var result : String=""
class TestResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTestResultBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_test_result, container, false)
        val testResultFragmentArgs by navArgs<TestResultFragmentArgs>()

        binding.finalResultText.text = testResultFragmentArgs.finalResult.toString()

        result =testResultFragmentArgs.finalResult.toString()+"%"
        val lala = "jjd"
        binding.btnTryAgain.setOnClickListener { view:View ->
            Navigation.findNavController(view).navigate(R.id.action_testResultFragment_to_testFragment)
        }

        setHasOptionsMenu(true)
        return binding.root


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.result_menu, menu)
    }


    private fun getShareIntent(): Intent {        val shareIntent = Intent(Intent.ACTION_SEND)


        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT,
                resources.getString(R.string.share_result)+
                result
                )
        return shareIntent
    }

    private fun shareResult(){
        startActivity(getShareIntent())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.resultMenu -> shareResult()
        }
        return super.onOptionsItemSelected(item)
    }
}
