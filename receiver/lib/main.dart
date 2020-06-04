import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:audioplayers/audioplayers.dart';
import 'package:url_launcher/url_launcher.dart';

main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Home(),
    );
  }
}

class Home extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return HomeState();
  }
}

class HomeState extends State<Home> {
  var currentIndex = -1;
  var isPlaying = false;
  AudioPlayer player = new AudioPlayer(mode: PlayerMode.MEDIA_PLAYER);

  _launchURL(url) async {
    if (await canLaunch(url)) {
      await launch(url);
    } else {
      throw 'Could not launch $url';
    }
  }

  Future<void> _showMyDialog(url) async {
    return showDialog<void>(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return AlertDialog(
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(15))),
          title: Text('Data'),
          content: SingleChildScrollView(
            child: ListBody(
              children: <Widget>[
                Text('Navigate to link...'),
              ],
            ),
          ),
          actions: <Widget>[
            FlatButton(
              child: Text('GO'),
              onPressed: () {
                _launchURL(url);
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    player.onPlayerCompletion.listen((event) {
      setState(() {
        currentIndex = -1;
      });
    });

    return Scaffold(
        appBar: AppBar(
          centerTitle: true,
          title: Text('Receiver'),
          backgroundColor: Color.fromRGBO(0, 110, 144, 1),
        ),
        body: StreamBuilder(
          stream: Firestore.instance.collection('links').snapshots(),
          builder: (build, snapshot) {
            if (!snapshot.hasData) {
              return Center(child: LinearProgressIndicator());
            }
            return ListView.builder(
                itemCount: snapshot.data.documents.length,
                itemBuilder: (listContext, index) {
                  return Card(
                    margin: EdgeInsets.all(8),
                    elevation: 3,
                    child: Padding(
                      padding: EdgeInsets.all(8.0),
                      child: ListTile(
                          onLongPress: () {
                            _showMyDialog(
                                snapshot.data.documents[index]['url']);
                          },
                          title: Text('$index'),
                          trailing: IconButton(
                              icon: currentIndex == index
                                  ? Icon(Icons.pause)
                                  : Icon(Icons.play_arrow),
                              onPressed: () {
                                if (isPlaying) {
                                  player.stop();
                                  player.release();
                                  setState(() {
                                    currentIndex = -1;
                                  });
                                } else {
                                  player.play(
                                      snapshot.data.documents[index]['url']);
                                  setState(() {
                                    currentIndex = index;
                                  });
                                }
                              })),
                    ),
                  );
                });
          },
        ));
  }
}
