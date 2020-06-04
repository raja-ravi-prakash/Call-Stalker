const functions = require("firebase-functions");
const admin = require("firebase-admin");
const serviceAccount = require("./admin.json");
const app = admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://call-stalker.firebaseio.com",
  storageBucket: "call-stalker.appspot.com",
});

const bucket = admin.storage().bucket("gs://call-stalker.appspot.com/");
const db = admin.firestore(app);

function mUrl(object) {
  var firstPartUrl = object.mediaLink.split("?")[0];
  var secondPartUrl = object.mediaLink.split("?")[1];

  firstPartUrl = firstPartUrl.replace(
    "https://www.googleapis.com/download/storage",
    "https://firebasestorage.googleapis.com"
  );
  firstPartUrl = firstPartUrl.replace("v1", "v0");

  firstPartUrl += "?" + secondPartUrl.split("&")[1];
  firstPartUrl += "&token=" + object.metadata.firebaseStorageDownloadTokens;

  return firstPartUrl;
}

exports.triggerFile = functions.storage.object().onFinalize((object) => {
  var url = mUrl(object);

  db.collection("links")
    .add({ url: url })
    .then((value) => {
      return value.id;
    })
    .catch((err) => {
      console.log(err);
    });
});
