# Change log

## unreleased

- Added: Control loggable level per class (#20)
- Added: Auto-detect tag names on iOS, Android and JVM platforms (#21)

## 0.5

- Support WebAssembly (WasmJS) platform.
- Add LogSink for iOS (`IosLogSink`).
- Add LogSink for JS (`js.ConsoleLogSink`).
- Add LogSink for WasmJS (`wasmjs.ConsoleLogSink`).
- Add `buildLogPrinter` DSL for configuring log printers.
- Add sample apps for supported platforms.

## Older releases

- 0.4 Migrate to the Apache 2.0 license.
- 0.3 MemoryRingSink prints log entries lazily.
- 0.2 Improve log layouts for Println and MemoryRing sinks.
- 0.1 Initial release
