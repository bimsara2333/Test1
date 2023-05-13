package com.example.test1

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.test1.databinding.ActivityBuyerProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.File

class buyerProfile : AppCompatActivity() {

    private lateinit var binding: ActivityBuyerProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
   // private lateinit var storageReference: StorageReference
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get current user id
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        val currUser = auth.currentUser
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        databaseReference.child(auth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //retrieve values from the db and convert them to user data class
                val user = snapshot.getValue(UserModel::class.java)!!

                binding.tvName.text = user.name
                binding.tvEmail.text = user.email
                binding.tvAddress.text = user.address
                binding.tvPhoneNumber.text = user.contactNo
               // getUserProfilePicture()
            }

            override fun onCancelled(error: DatabaseError) {
            }


        })



        binding.btnLogout.setOnClickListener {
            auth.signOut()

            //redirect user to login page
            intent = Intent(applicationContext, Login_signUp::class.java)
            startActivity(intent)

            //toast message
            Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()

        }

        binding.btnEditProfile.setOnClickListener {
            intent = Intent(applicationContext, Buyer_profile_editing::class.java)
            startActivity(intent)
        }

        binding.btnDlt.setOnClickListener {
            currUser?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account Deleted", Toast.LENGTH_SHORT).show()

                        intent = Intent(applicationContext, Login_signUp::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Failed to delete the account", Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }

   /* private fun getUserProfilePicture() {
        //find image named with the current uid
        storageReference = FirebaseStorage.getInstance().reference.child("Users/$uid")

        //create temporary local file to store the retrieved image
        val localFile = File.createTempFile("tempImage", ".jpg")

        //retrieve image and store it to created temp file
        storageReference.getFile(localFile).addOnSuccessListener {

            //covert temp file to bitmap
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            //bind image
            binding.ProfileDp.setImageBitmap(bitmap)


        }.addOnFailureListener{
            //Toast.makeText(activity, "Failed to retrieve image", Toast.LENGTH_SHORT).show()
        }
    } */








}