<h1 align="center">
  <img width="400" src="/docs/envoyapp.png" alt="EnvoyApp Logo"/>
  <br>
  <br>
</h1>

[![Stable Version](https://poser.pugx.org/kartik-v/yii2-mpdf/v/stable)](https://packagist.org/packages/kartik-v/yii2-mpdf)
[![Unstable Version](https://poser.pugx.org/kartik-v/yii2-mpdf/v/unstable)](https://packagist.org/packages/kartik-v/yii2-mpdf)
[![License](https://poser.pugx.org/kartik-v/yii2-mpdf/license)](https://packagist.org/packages/kartik-v/yii2-mpdf)
[![Total Downloads](https://poser.pugx.org/kartik-v/yii2-mpdf/downloads)](https://packagist.org/packages/kartik-v/yii2-mpdf)
[![Monthly Downloads](https://poser.pugx.org/kartik-v/yii2-mpdf/d/monthly)](https://packagist.org/packages/kartik-v/yii2-mpdf)
[![Daily Downloads](https://poser.pugx.org/kartik-v/yii2-mpdf/d/daily)](https://packagist.org/packages/kartik-v/yii2-mpdf)

A complete messaging application with Kotlin using [Firebase Authentication](https://firebase.google.com/docs/auth?hl=pt-br) for creating users, [Firebase Storage](https://firebase.google.com/docs/storage) for storing and accessing images, [Firebase Realtime Database](https://firebase.google.com/docs/database) for storing data and updating live on devices. EnvoyApp uses [hdodenhof/CircleImageView](https://github.com/hdodenhof/CircleImageView) to render images with rounded edges, [square/picasso](https://github.com/square/picasso) to load the content of images given a URL and load into an ImageView, and [lisawray/groupie](https://github.com/lisawray/groupie) to automatically configure RecyclerView adapters.
<br>
<br>
<br>
<br>

## SIGN UP/ SIGN IN

You can create a new account from the Sign Up screen by adding a profile picture, status (can be up to 24 characters), username, email (valid emails only) and password. On the Sign In screen, it is possible to log in to an existing account, informing the email and password. If you fill in any incorrect data or leave it blank, the application will validate and inform the fields that are incorrectly filled.

**User registration is performed with [FirebaseAuth](https://firebase.google.com/docs/auth?hl=pt-br), using the following code:**

```js
        FirebaseAuth.getInstance()
          .createUserWithEmailAndPassword(email,password)
          .addOnCompleteListener {
              if(!it.isSuccessful) return@addOnCompleteListener
              uploadProfilePictureToFirebaseStore()
          };
```
**And user login is performed using the following code:**
```js
        FirebaseAuth.getInstance()
          .signInWithEmailAndPassword(email,password)
          .addOnCompleteListener {
              if(!it.isSuccessful) return@addOnCompleteListener
              val intent = Intent(this, LatestMessagesActivity::class.java)
              startActivity(intent)
          };
```

<img width="100%" src="/docs/80c4551a6e468dbd6a48836c5f98a9d7.gif" alt="Sign In and Sign Up"/>

**You can use some already created accounts for test purpose:**
<div align="center">
  <table>
    <thead>
      <tr>
        <td>
          <strong>Email</strong>
        </td>
        <td>
          <strong>Password</strong>
        </td>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>
          girl@gmail.com
        </td>
        <td>
          12345678
        </td>
      </tr>
      <tr>
        <td>
          men@gmail.com
        </td>
        <td>
          12345678
        </td>
      </tr>
      <tr>
        <td>
          kid@gmail.com
        </td>
        <td>
          12345678
        </td>
      </tr>
      <tr>
        <td>
          otherkid@gmail.com
        </td>
        <td>
          12345678
        </td>
      </tr>
      <tr>
        <td>
          gothicgirl@gmail.com
        </td>
        <td>
          12345678
        </td>
      </tr>
      <tr>
        <td>
          gothicmen@gmail.com
        </td>
        <td>
          12345678
        </td>
      </tr>
    </tbody>
  </table>
</div>
<br>
<br>
<br>

## Latests messages/New message

The Last Messages screen contains all the chats you have interacted with. By clicking on the fab button, it loads the New Message screen with all registered users, their profile picture, username and status. Clicking on any of the users will open the chat log, and when sending a message, that message will instantly appear on the Latest Messages screen.

<img width="100%" src="/docs/c3b63592251390f4646a99ac065c7616.gif" alt="Latest messages and new messages"/>

***The instant update of the data on the screen is performed with the following code:***

```kt
    val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
    ref.addChildEventListener(object: ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
            latestMessageMap[snapshot.key!!] = chatMessage
            refreshRecyclerViewMessages()
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
            latestMessageMap[snapshot.key!!] = chatMessage
            refreshRecyclerViewMessages()
        }

        override fun onChildRemoved(snapshot: DataSnapshot) { }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

        override fun onCancelled(error: DatabaseError) { }
    })
```
<br>
<br>
<br>

## Chat log

The Chat Log contains all messages from the user logged in with the user selected in Latest Messages/New Message. When sending a message, the RecyclerView that contains the messages is instantly updated for both the sender and the recipient, including an item in its adapter: ChatToItem if it is an incoming message or a ChatFromItem if it is a sent message. After the RecyclerView is incremented, it slides the screen to the last chat message.

<img width="100%" src="/docs/e5fceacd1931835cb1d3e49aa6b577fe.gif" alt="Latest messages and new messages"/>

***Instant refresh is provided by FirebaseDatabase with following code:***

```kt
      val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
      ref.addChildEventListener(object: ChildEventListener {
          override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
              val chatMessage = snapshot.getValue(ChatMessage::class.java)
              if(chatMessage != null){
                  if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                      val currentUser = LatestMessagesActivity.currentUser
                      adapter.add(ChatToItem(chatMessage.text, currentUser!!))
                  }else{
                      adapter.add(ChatFromItem(chatMessage.text,user!!))
                  }
              }

          }

          override fun onCancelled(error: DatabaseError) {}

          override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

          override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

          override fun onChildRemoved(snapshot: DataSnapshot) {}
      })
```

<br>
<br>
<br>

## User profile

The User Profile screen receives ``android:theme="@style/ProfileDialog"`` in the ``AndroidManifest.xml`` file, which in turn inherits from ``Theme.AppCompat.Dialog`` to open only part of the screen and behave like a custom dialog. The User Profile screen displays the logged in user's profile picture, username and status. It also has a logout button that will log the user out and go to the Sign Up screen. When clicking the X button or outside the User Profile screen, this screen will close and return to the Latest Messages screen.

<img width="100%" src="/docs/a488d1799fe7f09a478fb915f602fe8b.gif" alt="Latest messages and new messages"/>

***To get the logged in user, the following code is being used:***

```kt
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) { }
        })
```

***To log out, the following code is being used:***

```kt
        logout_button_profile.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
```
