/// Copyright 2024 Halfbit GmbH, Sergej Shafarenka
import SwiftUI
import Shared

private let TAG = "SampleApp"

struct ContentView: View {
    @State private var showContent = false
    var body: some View {
        VStack {
            Button("Click me!") {
                withAnimation {
                    showContent = !showContent
                }
            }

            Button("Debug") {
                LogKt.d(tag: "\(TAG)Debug") { "debug message" }
            }

            Button("Info") {
                LogKt.i(tag: "\(TAG)Info") { "info message" }
            }

            Button("Warning") {
                LogKt.w(tag: "\(TAG)Warning") { "warning message" }
            }

            Button("Error") {
                LogKt.e(tag: "\(TAG)Error") { "error message" }
            }
            
            if showContent {
                VStack(spacing: 16) {
                    Image(systemName: "swift")
                        .font(.system(size: 200))
                        .foregroundColor(.accentColor)
                    Text("SwiftUI: \(Greeting().greet())")
                }
                .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
