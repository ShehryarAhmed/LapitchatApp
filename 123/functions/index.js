'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.BismillAH = functions.database.ref('/notifications/{user_id}/{notification_id}')
    .onWrite((change, context) => {
    const userID = context.params.user_id; // data that was created
    const notifiID = context.params.notification_id; // data that was created

    console.log("User Id is ",userID);

    if(!change.after.val()){
    return console.log('A Notification has been deleted from database : ',notifiID);
    }
    
    const fromUser = admin.database().ref(`/notifications/${userID}/${notifiID}`).once('value');

    return fromUser.then(fromUserResutl =>{

        const from_user_id = fromUserResutl.val().from;
        console.log(" You have a new Notification from",from_user_id);

        const userQuery = admin.database().ref(`User/${from_user_id}/name`).once('value');
        const deviceToken = admin.database().ref(`/User/${userID}/device_token`).once('value');

        return Promise.all([userQuery, deviceToken]).then(result =>{
            const userName = result[0].val();
            const token_id = result[1].val();
           
            const payload = {
                notification:{
                    title: "Friend Request",
                    body: `${userName} has sent you friend request`,
                    icon: "default",
                    clickAction: "com.logic.tech.chatapp_TARGET_NOTIFICATION"
                },
                data : {
                    from_profile_id : from_user_id    
                }
            };
        
        return admin.messaging().sendToDevice(token_id,payload).then(response => {
                console.log('this was the notification feature',response);
            });
        });
    });
    });
        
    
  
    
