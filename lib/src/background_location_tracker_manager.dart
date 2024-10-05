import 'dart:async';

import 'package:background_location_tracker/background_location_tracker.dart';
import 'package:background_location_tracker/src/channel/background_channel.dart';
import 'package:background_location_tracker/src/channel/foreground_channel.dart';
import 'package:background_location_tracker/src/util/logger.dart';

typedef LocationUpdateCallback = Future<void> Function(
    BackgroundLocationUpdateData data);

class BackgroundLocationTrackerManager {
  static Future<void> initialize(
    Function callback, {
    BackgroundLocationTrackerConfig? config,
  }) {
    final pluginConfig = config ??= const BackgroundLocationTrackerConfig();
    BackgroundLocationTrackerLogger.enableLogging = pluginConfig.loggingEnabled;
    return ForegroundChannel.initialize(callback, config: pluginConfig);
  }

  static Future<bool> isTracking() => ForegroundChannel.isTracking();

  static Future<void> startTracking({AndroidConfig? config}) =>
      ForegroundChannel.startTracking(config: config);

  static Future<void> stopTracking() => ForegroundChannel.stopTracking();

  static Future<dynamic> checkAutoStart() => ForegroundChannel.checkAutoStart();

  static void handleBackgroundUpdated(LocationUpdateCallback callback) =>
      BackgroundChannel.handleBackgroundUpdated(callback);
}
