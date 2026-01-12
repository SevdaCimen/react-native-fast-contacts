package com.fastcontacts

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = FastContactsModule.NAME)
class FastContactsModule(reactContext: ReactApplicationContext) :
  NativeFastContactsSpec(reactContext) {

  override fun getName(): String = NAME

  override fun getContacts(promise: Promise) {
    val permission = ContextCompat.checkSelfPermission(
      reactApplicationContext,
      Manifest.permission.READ_CONTACTS
    )

    if (permission != PackageManager.PERMISSION_GRANTED) {
      promise.resolve(Arguments.createArray())
      return
    }

    val contentResolver: ContentResolver = reactApplicationContext.contentResolver
    val result = Arguments.createArray()

    val cursor = contentResolver.query(
      ContactsContract.Contacts.CONTENT_URI,
      arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
      ),
      null,
      null,
      null
    )

    cursor?.use {
      val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
      val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
      val photoIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

      while (it.moveToNext()) {
        val id = it.getString(idIndex)
        val name = it.getString(nameIndex) ?: ""
        val thumbnail = it.getString(photoIndex) ?: ""

        val map = Arguments.createMap()
        map.putString("name", name)
        map.putString("thumbnail", thumbnail)

        // Phone Number
        val phoneCursor = contentResolver.query(
          ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
          arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
          "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
          arrayOf(id),
          null
        )
        val number = if (phoneCursor?.moveToFirst() == true) phoneCursor.getString(0) else ""
        phoneCursor?.close()
        map.putString("number", number)

        // Email
        val emailCursor = contentResolver.query(
          ContactsContract.CommonDataKinds.Email.CONTENT_URI,
          arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
          "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
          arrayOf(id),
          null
        )
        val email = if (emailCursor?.moveToFirst() == true) emailCursor.getString(0) else ""
        emailCursor?.close()
        map.putString("email", email)

        result.pushMap(map)
      }
    }

    promise.resolve(result)
  }

  companion object {
    const val NAME = "FastContacts"
  }
}