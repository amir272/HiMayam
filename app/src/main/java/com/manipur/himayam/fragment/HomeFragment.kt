package com.manipur.himayam.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.manipur.himayam.R
import com.manipur.himayam.dto.Item
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.manipur.himayam.adapter.HomePageAdapter
import com.manipur.himayam.misc.DisplayImage.Companion.getDrawableFromPath
import com.manipur.himayam.misc.NavigateToFragment.Companion.navigateToFragmentWithBundle
import com.manipur.himayam.misc.SharedViewModel

class HomeFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var homePageAdapter: HomePageAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        homePageAdapter = HomePageAdapter(
            emptyList()
        ) { item ->
            val fullContentFragment = FragmentFullContent()
            val bundle = Bundle()
            bundle.putString("content", item.text)
            fullContentFragment.arguments = bundle
            Log.e("HomeFragment", "onItemClicked: ${fullContentFragment.requireArguments()["content"]}")
            navigateToFragmentWithBundle(parentFragmentManager, fullContentFragment, R.id.mainFragmentContainer, bundle)
        }
        recyclerView.adapter = homePageAdapter
        val text1 = """
    <body><p>The Indian Council of Medical Research (ICMR) has distanced itself from the Covaxin&nbsp;<a href="https://link.springer.com/article/10.1007/s40264-024-01432-6" data-mce-href="https://link.springer.com/article/10.1007/s40264-024-01432-6">safety study</a>&nbsp;done by a group of researchers at the Banaras Hindu University (BHU).</p><img src="https://cdn.thewire.in/wp-content/uploads/2024/05/18150154/screenshot-2-Covaxin.jpeg" data-mce-src="https://cdn.thewire.in/wp-content/uploads/2024/05/18150154/screenshot-2-Covaxin.jpeg"></p></body>
        """
        val text2 = """
            <body><h3>This is the most important day of my life</h3><p><br data-mce-bogus="1"></p><p>Why bro why?</p><p>This is really awesome</p></body>
        """
        val text3 = """
            <body id="tinymce" class="mce-content-body " data-id="tiny-react_45717717411716115677352" aria-label="Rich Text Area. Press ALT-0 for help." contenteditable="true" spellcheck="false"><p>তূংি্েিগররো��� েসোমসিদরোেলি্িাৈাৌ৮বোপরপল পিোপে্িহো&nbsp;</p></body>
        """
        val text4 = if (sharedViewModel.htmlTextForPost.value != null) {
            sharedViewModel.htmlTextForPost.value
        } else {
            "<body><p>There is no text to display</p></body>"
        }
        val item1 = Item(1, text1, getDrawable(requireContext(), R.drawable.chinese_athlete))
        val item2 = Item(2, text2, getDrawable(requireContext(),R.drawable.wallp))
        val item3 = Item(3, text3, getDrawable(requireContext(), R.drawable.wallp))
        val item4 = Item(4, text4.toString(), getDrawable(requireContext(), R.drawable.stock_image))
        val item5 = Item(5, text4.toString(),
            sharedViewModel.itemSaved.value?.image
        )
        val items = listOf(item1, item2, item3, item4, item5)
        Log.d("HomeFragment", "onViewCreated: ${sharedViewModel.itemSaved.value?.image}")

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        homePageAdapter.updateData(items)
    }
}