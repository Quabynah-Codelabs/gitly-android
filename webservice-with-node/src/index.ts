// import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import * as app from './app';

admin.initializeApp();

// export const helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Gitly!");
// });

export const gitlyApp = app;