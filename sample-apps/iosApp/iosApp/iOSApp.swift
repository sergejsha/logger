/// Copyright 2024 Halfbit GmbH, Sergej Shafarenka
import SwiftUI
import Shared

@main
struct iOSApp: App {
    
    init() {
        LogKt.initializeLogger { builder in
            builder.registerIosLogSink(logPrinter: LogPrinterCompanion.shared.Default)
        }
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
