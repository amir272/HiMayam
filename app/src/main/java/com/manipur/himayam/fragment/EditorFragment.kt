package com.manipur.himayam.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.manipur.himayam.R
import com.manipur.himayam.misc.DisplayImage
import com.manipur.himayam.misc.SharedViewModel
import jp.wasabeef.richeditor.RichEditor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class EditorFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var mEditor: RichEditor
    private lateinit var mPreview: TextView
    private lateinit var editingOptions: HorizontalScrollView
    private lateinit var actionBold: View
    private lateinit var actionItalic: View
    private lateinit var actionUnderline: View
    private lateinit var actionInsertBullets: View
    private lateinit var actionAddImage: ImageButton
    private var isActionBold = false
    private var isActionItalic = false
    private var isActionUnderline = false
    private var isActionInsertBullets = false
    private var isEditingOptionsVisible = false

    companion object {
        private const val REQUEST_CODE_SELECT_IMAGE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        editingOptions = view.findViewById(R.id.editingOptions)
        actionBold = view.findViewById(R.id.action_bold)
        actionItalic = view.findViewById(R.id.action_italic)
        actionUnderline = view.findViewById(R.id.action_underline)
        actionInsertBullets = view.findViewById(R.id.action_insert_bullets)
        actionAddImage = view.findViewById(R.id.action_add_image)

        mEditor = view.findViewById(R.id.editor)
        mEditor.setEditorHeight(200)
        mEditor.setEditorFontSize(22)
        mEditor.setEditorFontColor(1)
        mEditor.setPlaceholder("Insert text here...")
        mEditor.setPadding(10, 10, 10, 10)

        // Add more customization and listeners as needed
        mPreview = view.findViewById(R.id.preview)
        mEditor.setOnTextChangeListener { text ->
            if (!isEditingOptionsVisible) {
                editingOptions.visibility = View.VISIBLE
                isEditingOptionsVisible = true
            }
            mPreview.text = text
            sharedViewModel.htmlTextForPost.value = text
        }

        actionBold.setOnClickListener {
            isActionBold = !isActionBold
            setBackGround(actionBold, isActionBold)
            mEditor.setBold()
        }
        actionItalic.setOnClickListener {
            isActionItalic = !isActionItalic
            setBackGround(actionItalic, isActionItalic)
            mEditor.setItalic()
        }
        actionUnderline.setOnClickListener {
            isActionUnderline = !isActionUnderline
            setBackGround(actionUnderline, isActionUnderline)
            mEditor.setUnderline()
        }
        actionInsertBullets.setOnClickListener {
            isActionInsertBullets = !isActionInsertBullets
            mEditor.setBullets()
        }

        actionAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                val imagePath = saveImageToLocal(it)
                // Display the image address
                mPreview.text = "Image saved at: $imagePath"
                if (imagePath != null) {
                    sharedViewModel.itemSaved.value?.image = DisplayImage.getDrawableFromPath(requireContext(), imagePath)
                }

            }
        }
    }

    private fun saveImageToLocal(uri: Uri): String? {
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().filesDir, "selected_image.jpg")
        try {
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return file.absolutePath
    }

    private fun setBackGround(view: View, isAction: Boolean) {
        if (isAction) {
            view.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_border)
        } else {
            view.background = null
        }
    }
}