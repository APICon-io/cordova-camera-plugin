# cordova-camera-plugin
Cordova Camera Plugin


USAGE:-

//CordovaCustomPluginFrontCamera Test
function CordovaCustomPluginFront() {
  let promise = new Promise((resolve, reject) => {
    cordova.exec(
      (success) => {
        console.log("MyPlugin " + success);
      },
      (error) => {
        alert("error " + JSON.stringify(error));
        console.log(error);
      },
      "CordovaCustomPlugin",
      "Camera",
      [1]
    );
  });
  
  
  
  //CordovaCustomPluginBack Test
function CordovaCustomPluginBack() {
  let promise = new Promise((resolve, reject) => {
    cordova.exec(
      (success) => {
        console.log("MyPlugin " + success);
      },
      (error) => {
        alert("error" + JSON.stringify(error));
        console.log(error);
      },
      "CordovaCustomPlugin",
      "Camera",
      [0]
    );
  });
