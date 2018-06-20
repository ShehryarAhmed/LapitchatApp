// import { Result } from 'range-parser';

// 'use strict'
// const functions = require('firebase-functions');
// const admin = require('firebase-admin');

// admin.initializeApp(functions.config().firebase);

// const functions = require('firebase-functions');
// const admin = require('firebase-admin');
// admin.initializeApp();

// exports.BismillAH = functions.database.ref('/notifications/{user_id}/{notification_id}')
//     .onWrite((data, context) => {
//     const userID = context.params.user_id; // data that was created
//     const notifiID = context.params.notification_id; // data that was created
    
//     console.log(userID);
    
    // console.log(userID);
    // if(!context.data.val()){
    //     return console.log('A Notification has been deleted from database : ',notifiID);
    // }
    
    // const deviceToken = admin.database().ref(`/User/${user_id}/device_token`).once('value');

    // return deviceToken.then(result => {

    //     const token_id = result.val();

    //     const payload = {
    //         notification:{
    //             title: "Friend Request",
    //             body: "You've received a new friend request",
    //             icon: "default"
    //         }
    //     };
    
    //     return admin.messaging().sendToDevice(token_id,payload).then(response => {
    //         console.log('this was the notification feature',response);
    //     });
    
    
    // });



// });
      

'use strict'
// const functions = require('firebase-functions');
// const admin = require('firebase-admin');

// admin.initializeApp(functions.config().firebase);

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
    
    const deviceToken = admin.database().ref(`/User/${userID}/device_token`).once('value');
    console.log("Device Token", deviceToken);
    return deviceToken.then(result => {

        const token_id   = result.val();
        // const token_id = "dnemHACDFW8:APA91bEegVzgy9p2zW-TIIKuYh0s6tX994CmH_e7j6RzgAF-7-1PPUsGuCouK7jLfaCnzB_9qFTN0z8wsDmIHM1lIFMga9IbZRfZwAvHy4689X5MpKFaTJZd9hDyyZhQ3-JciVKM9J9N";
        
        console.log(" Token ID", token_id);

        const payload = {
            notification:{
                title: "Friend Request",
                body: "You've received a new friend request",
                icon: "default"
            }
        };
    
        return admin.messaging().sendToDevice(token_id,payload).then(response => {
            console.log('this was the notification feature',response);
        });
    
    
    });



});
        
    
  
    
