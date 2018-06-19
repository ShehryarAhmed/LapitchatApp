
'use strict'
// const functions = require('firebase-functions');
// const admin = require('firebase-admin');

// admin.initializeApp(functions.config().firebase);

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.Bismillah = functions.database.ref('/notifications/{user_id}')
    .onWrite((data, context) => {
    const createdData = context.params.user_id; // data that was created
    console.log(createdData);
});
        
    
