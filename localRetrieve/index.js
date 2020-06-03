const admin = require("firebase-admin");
const serviceAccount = require("./admin.json");
const fs = require("fs");
const path = require("path");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://call-stalker.firebaseio.com",
  storageBucket: "call-stalker.appspot.com",
});

const bucket = admin.storage().bucket("gs://call-stalker.appspot.com/");
var filesObject;

function createFile(file) {
  var name = file.name.split("files/")[1];

  if (name.length == 0) return 0;
  var fullPath = path.join(__dirname, "files", name);

  file
    .createReadStream()
    .on("error", function (err) {})
    .on("response", function (response) {
      // Server connected and responded with the specified status and headers.
    })
    .on("end", function () {
      console.log("Done !!!");
    })
    .pipe(fs.createWriteStream(fullPath));
}

function set() {
  fs.mkdirSync("files");
  filesObject.forEach((val) => {
    createFile(val);
  });
}

bucket
  .getFiles({
    directory: "files",
  })
  .then((files) => {
    filesObject = files[0];
    set();
  })
  .catch((err) => {
    console.log(err);
  });
