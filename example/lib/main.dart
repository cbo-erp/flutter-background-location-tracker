import 'dart:async';
import 'dart:io';

import 'package:background_location_tracker/background_location_tracker.dart';
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';

@pragma('vm:entry-point')
void backgroundCallback() {
  BackgroundLocationTrackerManager.handleBackgroundUpdated(
    (data) => LocationDao().saveLocation(data),
  );
}

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await BackgroundLocationTrackerManager.initialize(
    backgroundCallback,
    config: const BackgroundLocationTrackerConfig(
      loggingEnabled: true,
      androidConfig: AndroidConfig(
        notificationIcon: 'explore',
        trackingInterval: Duration(minutes: 5),
        distanceFilterMeters: null,
        enableNotificationLocationUpdates: true,
        enableCancelTrackingAction: false,
      ),
      iOSConfig: IOSConfig(
        activityType: ActivityType.FITNESS,
        distanceFilterMeters: null,
        restartAfterKill: true,
      ),
    ),
  );

  runApp(MyApp());
}

@override
class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var isTracking = false;

  Timer? _timer;
  List<String> _locations = [];

  @override
  void initState() {
    super.initState();
    _getTrackingStatus();
    _startLocationsUpdatesStream();
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(
          width: double.infinity,
          child: Column(
            children: [
              Expanded(
                child: Column(
                  children: [
                    MaterialButton(
                      child: const Text('Request location permission'),
                      onPressed: _requestLocationPermission,
                    ),
                    if (Platform.isAndroid) ...[
                      const Text(
                          'Permission on android is only needed starting from sdk 33.'),
                    ],
                    MaterialButton(
                      child: const Text('Request Notification permission'),
                      onPressed: _requestNotificationPermission,
                    ),
                    MaterialButton(
                      child: const Text('Auto Start'),
                      onPressed: () async {
                        await BackgroundLocationTrackerManager.checkAutoStart();
                      },
                    ),
                    MaterialButton(
                      child: const Text('Start Tracking'),
                      onPressed: isTracking
                          ? null
                          : () async {
                              await BackgroundLocationTrackerManager
                                  .startTracking();
                              setState(() => isTracking = true);
                            },
                    ),
                    MaterialButton(
                      child: const Text('Stop Tracking'),
                      onPressed: isTracking
                          ? () async {
                              await LocationDao().clear();
                              await _getLocations();
                              await BackgroundLocationTrackerManager
                                  .stopTracking();
                              setState(() => isTracking = false);
                            }
                          : null,
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 8),
              Container(
                color: Colors.black12,
                height: 2,
              ),
              const Text('Locations'),
              MaterialButton(
                child: const Text('Refresh locations'),
                onPressed: _getLocations,
              ),
              Expanded(
                child: Builder(
                  builder: (context) {
                    if (_locations.isEmpty) {
                      return const Text('No locations saved');
                    }
                    return ListView.builder(
                      itemCount: _locations.length,
                      itemBuilder: (context, index) => Padding(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 16,
                          vertical: 12,
                        ),
                        child: Text(
                          _locations[index],
                        ),
                      ),
                    );
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<void> _getTrackingStatus() async {
    isTracking = await BackgroundLocationTrackerManager.isTracking();
    setState(() {});
  }

  Future<void> _requestLocationPermission() async {
    final result = await Permission.location.request();
    if (result == PermissionStatus.granted) {
      print('GRANTED'); // ignore: avoid_print
    } else {
      print('NOT GRANTED'); // ignore: avoid_print
    }
  }

  Future<void> _requestNotificationPermission() async {
    final result = await Permission.notification.request();
    if (result == PermissionStatus.granted) {
      print('GRANTED'); // ignore: avoid_print
    } else {
      print('NOT GRANTED'); // ignore: avoid_print
    }
  }

  Future<void> _getLocations() async {
    final locations = await LocationDao().getLocations();
    setState(() {
      _locations = locations;
    });
  }

  void _startLocationsUpdatesStream() {
    _timer?.cancel();
    _timer = Timer.periodic(
        const Duration(milliseconds: 250), (timer) => _getLocations());
  }
}

class LocationDao {
  static const _locationsKey = 'background_updated_locations';
  static const _locationSeparator = '-/-/-/';

  static LocationDao? _instance;
  static List<String> locationsMain = [];

  LocationDao._();

  factory LocationDao() => _instance ??= LocationDao._();

  Future<void> saveLocation(BackgroundLocationUpdateData data) async {
    locationsMain.add(
        '${DateTime.now().toIso8601String()}       ${data.lat},${data.lon}');
    print("saved ${locationsMain.length}");
  }

  Future<List<String>> getLocations() async {
    // final prefs = await this.prefs;
    // await prefs.reload();
    // final locationsString = prefs.getString(_locationsKey);
    // if (locationsString == null) return [];
    // return locationsString.split(_locationSeparator);
    return locationsMain;
  }

  // Future<void> clear() async => (await prefs).clear();
  Future<void> clear() async => Future.value();
}
